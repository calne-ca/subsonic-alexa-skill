package net.beardbot.subsonic.alexa.subsonic;

import com.amazon.ask.model.interfaces.audioplayer.AudioItemMetadata;
import com.amazon.ask.model.interfaces.display.Image;
import com.amazon.ask.model.interfaces.display.ImageInstance;
import com.amazon.ask.model.interfaces.display.ImageSize;
import info.debatty.java.stringsimilarity.Levenshtein;
import lombok.extern.slf4j.Slf4j;
import net.beardbot.subsonic.alexa.model.PlaybackQueue;
import net.beardbot.subsonic.alexa.model.SubsonicAudioItem;
import net.beardbot.subsonic.alexa.model.SubsonicUserAttributes;
import net.beardbot.subsonic.alexa.utils.EnvironmentConfig;
import net.beardbot.subsonic.alexa.utils.TokenUtil;
import net.beardbot.subsonic.client.Subsonic;
import net.beardbot.subsonic.client.SubsonicPreferences;
import net.beardbot.subsonic.client.api.media.CoverArtParams;
import org.subsonic.restapi.Child;
import org.subsonic.restapi.Playlist;
import org.subsonic.restapi.PlaylistWithSongs;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
public class SubsonicController {
    private static final int COVERT_ART_SIZE = 960;

    private static final SubsonicController INSTANCE = new SubsonicController();

    public static SubsonicController getInstance(){
        return INSTANCE;
    }

    private final Subsonic subsonic;

    public SubsonicController() {
        var preferences = new SubsonicPreferences(
                EnvironmentConfig.subsonicUrl(),
                EnvironmentConfig.subsonicUsername(),
                EnvironmentConfig.subsonicPassword());

        preferences.setStreamBitRate(192);
        preferences.setClientName("Subsonic Alexa Skill");

        subsonic = new Subsonic(preferences);
    }

    public Optional<String> getPlaylistForQuery(String query, SubsonicUserAttributes attributes){
        var playlist = findPlaylist(query);

        if (playlist.isEmpty()){
            return Optional.empty();
        }

        var settings = attributes.getPlaybackSettings();

        var songIds = playlist.get().getEntries().stream()
                .map(Child::getId)
                .collect(Collectors.toList());

        var queue = new PlaybackQueue();
        queue.setLoop(settings.isLoop());
        queue.getQueue().addAll(songIds);

        if (settings.isShuffle()){
            queue.shuffle();
        }

        attributes.setPlaybackQueue(queue);

        return Optional.of(playlist.get().getName());
    }

    public Optional<SubsonicAudioItem> getNextSong(SubsonicUserAttributes attributes){
        var queue = attributes.getPlaybackQueue();
        var nextSongId = queue.nextSongId();

        if (nextSongId.isEmpty()){
            return Optional.empty();
        }

        return Optional.of(createAudioItem(nextSongId.get()));
    }

    public void scrobble(SubsonicUserAttributes attributes, String songToken){
        if (!attributes.getPlaybackSettings().isScrobblingEnabled()){
            log.info("Skipped scrobbling, since it is disabled for current user");
            return;
        }

        var songId = TokenUtil.songIdFromToken(songToken);

        subsonic.annotation().scrobble(songId);
        log.info("Scrobbled current song");
    }

    public void scrobbleNowPlaying(SubsonicUserAttributes attributes, String songToken){
        if (!attributes.getPlaybackSettings().isScrobblingEnabled()){
            log.info("Skipped scrobbling, since it is disabled for current user");
            return;
        }

        var songId = TokenUtil.songIdFromToken(songToken);

        subsonic.annotation().scrobbleNowPlaying(songId);
        log.info("Scrobbled now playing status for current song");
    }

    private Optional<PlaylistWithSongs> findPlaylist(String query){
        var playlists = subsonic.playlists().getPlaylists();

        var matchingPlaylist = findMatchingPlaylist(playlists, query);

        if (matchingPlaylist.isEmpty()){
            return Optional.empty();
        }

        var playlistWithSongs = subsonic.playlists().getPlaylist(matchingPlaylist.get().getId());

        return Optional.of(playlistWithSongs);
    }

    private Optional<Playlist> findMatchingPlaylist(List<Playlist> playlists, String query){
        if (playlists.isEmpty()){
            return Optional.empty();
        }

        var mostSimilarPlaylist = playlists.stream()
                .min(playlistSimilarityDistanceComparator(query))
                .orElseThrow();

        if (!isSimilarEnough(mostSimilarPlaylist, query)){
            return Optional.empty();
        }

        return Optional.of(mostSimilarPlaylist);
    }

    private SubsonicAudioItem createAudioItem(String songId){
        var song = subsonic.browsing().getSong(songId);
        return createAudioItem(song);
    }

    private SubsonicAudioItem createAudioItem(Child song){
        var streamUrl = subsonic.media().streamUrl(song.getId()).toString();
        var metadata = createMetaData(song);

        var audioItem = new SubsonicAudioItem();
        audioItem.setSongId(song.getId());
        audioItem.setStreamUrl(streamUrl);
        audioItem.setMetadata(metadata);

        return audioItem;
    }

    private AudioItemMetadata createMetaData(Child song){
        return AudioItemMetadata.builder()
                .withTitle(song.getTitle())
                .withSubtitle(song.getArtist())
                .withArt(createImageMetadata(song))
                .build();
    }

    private Image createImageMetadata(Child song){
        var coverArtUrl = subsonic.media().getCoverArtUrl(song.getId(), CoverArtParams.create().size(COVERT_ART_SIZE));

        return Image.builder()
                .addSourcesItem(ImageInstance.builder()
                        .withSize(ImageSize.MEDIUM).withHeightPixels(COVERT_ART_SIZE).withWidthPixels(COVERT_ART_SIZE)
                        .withUrl(coverArtUrl.toString())
                        .build())
                .build();
    }

    private boolean isSimilarEnough(Playlist playlist, String query){
        var similarityDistance = calculateSimilarityDistance(query, playlist.getName());

        if (similarityDistance > 3){
            log.info("Most similar playlist '{}' does not match query '{}'. Distance of {} is too high", playlist.getName(), query, similarityDistance);
            return false;
        }

        log.info("Playlist '{}' matches query '{}' with a distance of {}", playlist.getName(), query, similarityDistance);

        return true;
    }

    private Comparator<Playlist> playlistSimilarityDistanceComparator(String query) {
        return Comparator.comparing(p -> calculateSimilarityDistance(p.getName().toLowerCase(), query.toLowerCase()));
    }

    private double calculateSimilarityDistance(String query, String phrase){
        var similarityAlgorithm = new Levenshtein();
        return similarityAlgorithm.distance(query.toLowerCase(), phrase.toLowerCase());
    }
}

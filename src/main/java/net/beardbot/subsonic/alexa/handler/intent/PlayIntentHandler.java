package net.beardbot.subsonic.alexa.handler.intent;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.RequestHandler;
import com.amazon.ask.model.IntentRequest;
import com.amazon.ask.model.Response;
import com.amazon.ask.model.Slot;
import com.amazon.ask.model.interfaces.audioplayer.PlayBehavior;
import net.beardbot.subsonic.alexa.model.SubsonicUserAttributes;
import net.beardbot.subsonic.alexa.subsonic.SubsonicController;
import net.beardbot.subsonic.alexa.utils.Phrases;

import java.util.Map;
import java.util.Optional;

import static com.amazon.ask.request.Predicates.intentName;
import static java.lang.String.format;
import static net.beardbot.subsonic.alexa.utils.Phrases.*;

public class PlayIntentHandler implements RequestHandler {

    private final SubsonicController subsonicController = SubsonicController.getInstance();

    @Override
    public boolean canHandle(HandlerInput input) {
        return input.matches(intentName("PlayIntent")
                .or(intentName("AMAZON.ResumeIntent")));
    }

    @Override
    public Optional<Response> handle(HandlerInput input) {
        var attributes = SubsonicUserAttributes.from(input);
        var query = getQuery(input);

        var playlist = subsonicController.getPlaylistForQuery(query, attributes);

        var response = input.getResponseBuilder();
        response.withShouldEndSession(true);

        if (playlist.isEmpty()){
            response.withSpeech(Phrases.getPhrase(PLAYLIST_NOT_FOUND_PHRASE, query));
            response.withShouldEndSession(false);
            return response.build();
        }

        var nextSong = subsonicController.getNextSong(attributes);

        if (nextSong.isEmpty()){
            response.withSpeech(Phrases.getPhrase(PLAYLIST_EMPTY_PHRASE, playlist.get()));
            return response.build();
        }

        var streamUrl = nextSong.get().getStreamUrl();
        var metadata = nextSong.get().getMetadata();
        var token = nextSong.get().token();

        response.withSpeech(Phrases.getPhrase(PLAYLIST_PLAY_PHRASE, playlist.get()));
        response.addAudioPlayerPlayDirective(PlayBehavior.REPLACE_ALL, 0L, null, token, streamUrl, metadata);

        return response.build();
    }

    private String getQuery(HandlerInput input){
         var slots = getIntentSlots(input);
         var slot = slots.get("query");

         return slot.getValue();
    }

    private Map<String, Slot> getIntentSlots(HandlerInput handlerInput){
        return ((IntentRequest) handlerInput.getRequestEnvelope().getRequest()).getIntent().getSlots();
    }
}

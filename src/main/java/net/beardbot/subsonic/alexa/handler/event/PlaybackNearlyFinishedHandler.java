package net.beardbot.subsonic.alexa.handler.event;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.impl.PlaybackNearlyFinishedRequestHandler;
import com.amazon.ask.model.Response;
import com.amazon.ask.model.interfaces.audioplayer.PlayBehavior;
import com.amazon.ask.model.interfaces.audioplayer.PlaybackNearlyFinishedRequest;
import net.beardbot.subsonic.alexa.model.SubsonicUserAttributes;
import net.beardbot.subsonic.alexa.subsonic.SubsonicController;

import java.util.Optional;

public class PlaybackNearlyFinishedHandler implements PlaybackNearlyFinishedRequestHandler {

    private final SubsonicController subsonicController = SubsonicController.getInstance();

    @Override
    public boolean canHandle(HandlerInput input, PlaybackNearlyFinishedRequest playbackNearlyFinishedRequest) {
        return true;
    }

    @Override
    public Optional<Response> handle(HandlerInput input, PlaybackNearlyFinishedRequest playbackNearlyFinishedRequest) {
        var attributes = SubsonicUserAttributes.from(input);

        var nextSong = subsonicController.getNextSong(attributes);
        var response = input.getResponseBuilder();

        if (nextSong.isPresent()){
            var streamUrl = nextSong.get().getStreamUrl();
            var metadata = nextSong.get().getMetadata();
            var token = nextSong.get().token();
            var previousToken = playbackNearlyFinishedRequest.getToken();

            response.addAudioPlayerPlayDirective(PlayBehavior.ENQUEUE, 0L, previousToken, token, streamUrl, metadata);
        }

        return response.build();
    }
}

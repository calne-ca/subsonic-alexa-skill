package net.beardbot.subsonic.alexa.handler.event;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.impl.PlaybackStartedRequestHandler;
import com.amazon.ask.model.Response;
import com.amazon.ask.model.interfaces.audioplayer.PlaybackStartedRequest;
import net.beardbot.subsonic.alexa.model.SubsonicUserAttributes;
import net.beardbot.subsonic.alexa.subsonic.SubsonicController;

import java.util.Optional;

public class PlaybackStartedHandler implements PlaybackStartedRequestHandler {

    private final SubsonicController subsonicController = SubsonicController.getInstance();

    @Override
    public boolean canHandle(HandlerInput input, PlaybackStartedRequest playbackStartedRequest) {
        return true;
    }

    @Override
    public Optional<Response> handle(HandlerInput input, PlaybackStartedRequest playbackStartedRequest) {
        var attributes = SubsonicUserAttributes.from(input);

        subsonicController.scrobbleNowPlaying(attributes, playbackStartedRequest.getToken());

        return input.getResponseBuilder().build();
    }
}

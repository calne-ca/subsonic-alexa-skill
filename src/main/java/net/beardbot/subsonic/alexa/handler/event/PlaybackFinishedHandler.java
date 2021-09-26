package net.beardbot.subsonic.alexa.handler.event;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.impl.PlaybackFinishedRequestHandler;
import com.amazon.ask.model.Response;
import com.amazon.ask.model.interfaces.audioplayer.PlaybackFinishedRequest;
import net.beardbot.subsonic.alexa.model.SubsonicUserAttributes;
import net.beardbot.subsonic.alexa.subsonic.SubsonicController;

import java.util.Optional;

public class PlaybackFinishedHandler implements PlaybackFinishedRequestHandler {

    private final SubsonicController subsonicController = SubsonicController.getInstance();

    @Override
    public boolean canHandle(HandlerInput input, PlaybackFinishedRequest playbackFinishedRequest) {
        return true;
    }

    @Override
    public Optional<Response> handle(HandlerInput input, PlaybackFinishedRequest playbackFinishedRequest) {
        var attributes = SubsonicUserAttributes.from(input);

        subsonicController.scrobble(attributes, playbackFinishedRequest.getToken());

        return input.getResponseBuilder().build();
    }
}

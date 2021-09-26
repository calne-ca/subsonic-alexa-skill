package net.beardbot.subsonic.alexa.handler.event;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.impl.PlaybackStoppedRequestHandler;
import com.amazon.ask.model.Response;
import com.amazon.ask.model.interfaces.audioplayer.PlaybackStoppedRequest;

import java.util.Optional;

public class PlaybackStoppedHandler implements PlaybackStoppedRequestHandler {

    @Override
    public boolean canHandle(HandlerInput input, PlaybackStoppedRequest playbackStoppedRequest) {
        return true;
    }

    @Override
    public Optional<Response> handle(HandlerInput input, PlaybackStoppedRequest playbackStoppedRequest) {
        return input.getResponseBuilder().build();
    }
}

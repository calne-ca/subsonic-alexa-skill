package net.beardbot.subsonic.alexa.handler.intent;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.model.Response;
import com.amazon.ask.model.SessionEndedRequest;

import java.util.Optional;

public class SessionEndedRequestHandler implements com.amazon.ask.dispatcher.request.handler.impl.SessionEndedRequestHandler {

    @Override
    public boolean canHandle(HandlerInput input, SessionEndedRequest sessionEndedRequest) {
        return true;
    }

    @Override
    public Optional<Response> handle(HandlerInput input, SessionEndedRequest sessionEndedRequest) {
        return input.getResponseBuilder().build();
    }
}

package net.beardbot.subsonic.alexa.handler.event;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.impl.ExceptionEncounteredRequestHandler;
import com.amazon.ask.model.Response;
import com.amazon.ask.model.interfaces.system.ExceptionEncounteredRequest;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;

@Slf4j
public class SystemExceptionHandler implements ExceptionEncounteredRequestHandler {
    @Override
    public boolean canHandle(HandlerInput input, ExceptionEncounteredRequest exceptionEncounteredRequest) {
        return true;
    }

    @Override
    public Optional<Response> handle(HandlerInput input, ExceptionEncounteredRequest exceptionEncounteredRequest) {
        log.error(exceptionEncounteredRequest.getError().getMessage());
        return input.getResponseBuilder().build();
    }
}

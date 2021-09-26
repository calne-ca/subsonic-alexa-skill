package net.beardbot.subsonic.alexa.interceptor;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.model.Response;
import com.amazon.ask.request.interceptor.GenericRequestInterceptor;
import com.amazon.ask.request.interceptor.GenericResponseInterceptor;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;

@Slf4j
public class RequestResponseLoggingInterceptor implements GenericRequestInterceptor<HandlerInput>, GenericResponseInterceptor<HandlerInput, Optional<Response>> {
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    @SneakyThrows
    public void process(HandlerInput handlerInput) {
        var jsonEnvelope = handlerInput.getRequestEnvelopeJson();
        var jsonEnvelopeString = objectMapper.writeValueAsString(jsonEnvelope);
        log.info("Incoming request: {}", jsonEnvelopeString);
    }

    @Override
    @SneakyThrows
    public void process(HandlerInput handlerInput, Optional<Response> response) {
        if (response.isEmpty()){
            log.info("Returning empty response");
            return;
        }

        var responseJson = objectMapper.writeValueAsString(response.get());
        log.info("Outgoing response: {}", responseJson);
    }
}

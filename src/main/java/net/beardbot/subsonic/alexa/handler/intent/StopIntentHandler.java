package net.beardbot.subsonic.alexa.handler.intent;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.RequestHandler;
import com.amazon.ask.model.Response;

import java.util.Optional;

import static com.amazon.ask.request.Predicates.intentName;

public class StopIntentHandler implements RequestHandler {

    @Override
    public boolean canHandle(HandlerInput input) {
        return input.matches(intentName("AMAZON.StopIntent")
                .or(intentName("AMAZON.CancelIntent"))
                .or(intentName("AMAZON.NavigateHomeIntent"))
                .or(intentName("AMAZON.PauseIntent"))
                .or(intentName("AMAZON.NavigateHomeIntent"))
        );
    }

    @Override
    public Optional<Response> handle(HandlerInput input) {
        return input.getResponseBuilder()
                .addAudioPlayerStopDirective()
                .withShouldEndSession(true)
                .build();
    }
}

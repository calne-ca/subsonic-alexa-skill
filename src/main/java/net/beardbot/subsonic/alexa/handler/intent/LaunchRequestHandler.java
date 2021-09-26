package net.beardbot.subsonic.alexa.handler.intent;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.RequestHandler;
import com.amazon.ask.model.LaunchRequest;
import com.amazon.ask.model.Response;
import net.beardbot.subsonic.alexa.utils.Phrases;

import java.util.Optional;

import static com.amazon.ask.request.Predicates.requestType;
import static net.beardbot.subsonic.alexa.utils.Phrases.LAUNCH_PHRASE;

public class LaunchRequestHandler implements RequestHandler {

    @Override
    public boolean canHandle(HandlerInput input) {
        return input.matches(requestType(LaunchRequest.class));
    }

    @Override
    public Optional<Response> handle(HandlerInput input) {
        String speechText = Phrases.getPhrase(LAUNCH_PHRASE);
        return input.getResponseBuilder()
                .withSpeech(speechText)
                .withReprompt(speechText)
                .build();
    }

}

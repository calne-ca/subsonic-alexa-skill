package net.beardbot.subsonic.alexa.handler.intent;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.RequestHandler;
import com.amazon.ask.model.Response;
import net.beardbot.subsonic.alexa.utils.Phrases;

import java.util.Optional;

import static com.amazon.ask.request.Predicates.intentName;
import static net.beardbot.subsonic.alexa.utils.Phrases.HELP_PHRASE;

public class HelpIntentHandler implements RequestHandler {

    @Override
    public boolean canHandle(HandlerInput input) {
        return input.matches(intentName("AMAZON.HelpIntent"));
    }

    @Override
    public Optional<Response> handle(HandlerInput input) {
        String speechText = Phrases.getPhrase(HELP_PHRASE);
        return input.getResponseBuilder()
                .withSpeech(speechText)
                .withSimpleCard("Subsonic", speechText)
                .withReprompt(speechText)
                .build();
    }
}

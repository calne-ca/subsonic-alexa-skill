package net.beardbot.subsonic.alexa.handler.intent;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.RequestHandler;
import com.amazon.ask.model.Response;
import com.amazon.ask.model.interfaces.audioplayer.PlayBehavior;
import net.beardbot.subsonic.alexa.model.SubsonicUserAttributes;
import net.beardbot.subsonic.alexa.subsonic.SubsonicController;

import java.util.Optional;

import static com.amazon.ask.request.Predicates.intentName;

public class NextIntentHandler implements RequestHandler {

    private final SubsonicController subsonicController = SubsonicController.getInstance();

    @Override
    public boolean canHandle(HandlerInput input) {
        return input.matches(intentName("AMAZON.NextIntent"));
    }

    @Override
    public Optional<Response> handle(HandlerInput input) {
        var attributes = SubsonicUserAttributes.from(input);

        var nextSong = subsonicController.getNextSong(attributes);

        var response = input.getResponseBuilder();
        response.withShouldEndSession(true);

        if (nextSong.isPresent()){
            var streamUrl = nextSong.get().getStreamUrl();
            var metadata = nextSong.get().getMetadata();
            var token = nextSong.get().token();

            response.addAudioPlayerPlayDirective(PlayBehavior.REPLACE_ALL, 0L, null, token, streamUrl, metadata);
        }

        return response.build();
    }
}

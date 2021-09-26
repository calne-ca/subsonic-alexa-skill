package net.beardbot.subsonic.alexa.model;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class SubsonicUserAttributes {
    public static final String KEY = "user_attributes";

    @JsonProperty("playback_settings")
    private PlaybackSettings playbackSettings = new PlaybackSettings();

    @JsonProperty("playback_queue")
    private PlaybackQueue playbackQueue = new PlaybackQueue();

    public static SubsonicUserAttributes from(HandlerInput input){
        var attributes = input.getAttributesManager().getPersistentAttributes();
        return (SubsonicUserAttributes) attributes.get(SubsonicUserAttributes.KEY);
    }
}

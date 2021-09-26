package net.beardbot.subsonic.alexa.model;

import lombok.Data;

@Data
public class PlaybackSettings {
    private boolean loop = true;
    private boolean shuffle = true;
    private boolean scrobblingEnabled = true;
}

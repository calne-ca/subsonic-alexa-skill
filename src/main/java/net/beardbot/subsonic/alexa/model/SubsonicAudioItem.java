package net.beardbot.subsonic.alexa.model;

import com.amazon.ask.model.interfaces.audioplayer.AudioItemMetadata;
import lombok.Data;
import net.beardbot.subsonic.alexa.utils.TokenUtil;

@Data
public class SubsonicAudioItem {
    private String songId;
    private String streamUrl;
    private AudioItemMetadata metadata;

    public String token(){
        return TokenUtil.tokenFromSongId(songId);
    }
}

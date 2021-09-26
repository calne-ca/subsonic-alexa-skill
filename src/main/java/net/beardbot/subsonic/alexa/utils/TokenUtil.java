package net.beardbot.subsonic.alexa.utils;

import java.util.regex.Pattern;

public class TokenUtil {

    private static final Pattern TOKEN_PATTERN = Pattern.compile("^subsonic-song-(.+)$");

    public static String tokenFromSongId(String songId){
        return String.format("subsonic-song-%s", songId);
    }

    public static String songIdFromToken(String songToken){
        var matcher = TOKEN_PATTERN.matcher(songToken);
        matcher.find();

        return matcher.group(1);
    }
}

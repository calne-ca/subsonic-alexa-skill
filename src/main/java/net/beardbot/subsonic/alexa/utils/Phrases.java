package net.beardbot.subsonic.alexa.utils;

import lombok.SneakyThrows;

import java.util.Properties;

public class Phrases {

    public static String HELP_PHRASE = "help";
    public static String LAUNCH_PHRASE = "launch";
    public static String PLAYLIST_NOT_FOUND_PHRASE = "play.playlist.notFound";
    public static String PLAYLIST_EMPTY_PHRASE = "play.playlist.empty";
    public static String PLAYLIST_PLAY_PHRASE = "play.playlist.play";

    private static Properties properties;

    public static String getPhrase(String name, Object... args){
        var phrase = getPhraseProperties().getProperty("phrase." + name);
        return String.format(phrase, args);
    }


    private static Properties getPhraseProperties(){
        if (properties == null){
            loadProperties();
        }

        return properties;
    }

    @SneakyThrows
    private static void loadProperties(){
        var locale = EnvironmentConfig.locale();
        var propertiesFilePath = String.format("phrases/%s.properties", locale);

        properties = new Properties();
        properties.load(Phrases.class.getClassLoader().getResourceAsStream(propertiesFilePath));
    }
}

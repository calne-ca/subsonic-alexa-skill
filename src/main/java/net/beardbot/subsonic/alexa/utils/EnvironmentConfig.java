package net.beardbot.subsonic.alexa.utils;

public class EnvironmentConfig {
    public static String dynamoDbTableName(){
        return System.getenv("DYNAMODB_TABLE");
    }

    public static String subsonicUrl(){
        return System.getenv("SUBSONIC_URL");
    }

    public static String subsonicUsername(){
        return System.getenv("SUBSONIC_USERNAME");
    }

    public static String subsonicPassword(){
        return System.getenv("SUBSONIC_PASSWORD");
    }

    public static String locale(){
        return System.getenv("LOCALE");
    }
}

package net.beardbot.subsonic.alexa;

import com.amazon.ask.Skill;
import com.amazon.ask.SkillStreamHandler;
import com.amazon.ask.Skills;
import net.beardbot.subsonic.alexa.utils.EnvironmentConfig;

import static net.beardbot.subsonic.alexa.utils.ReflectionUtil.*;

public class SubsonicStreamHandler extends SkillStreamHandler {

    private static Skill getSkill() {
        return Skills.standard()
                .addRequestHandlers(collectRequestHandlers())
                .addRequestInterceptors(collectRequestInterceptors())
                .addResponseInterceptors(collectResponseInterceptors())
                .withTableName(EnvironmentConfig.dynamoDbTableName())
                .build();
    }

    public SubsonicStreamHandler() {
        super(getSkill());
    }
}

package net.beardbot.subsonic.alexa.interceptor;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.model.Response;
import com.amazon.ask.request.interceptor.GenericRequestInterceptor;
import com.amazon.ask.request.interceptor.GenericResponseInterceptor;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import net.beardbot.subsonic.alexa.model.SubsonicUserAttributes;

import java.util.Optional;

@Slf4j
public class AttributePersistenceInterceptor implements GenericRequestInterceptor<HandlerInput>, GenericResponseInterceptor<HandlerInput, Optional<Response>> {
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    @SneakyThrows
    public void process(HandlerInput handlerInput) {
        log.info("Request interceptor {} has been called", AttributePersistenceInterceptor.class.getSimpleName());

        var attributes = handlerInput.getAttributesManager().getPersistentAttributes();

        if (attributes.isEmpty()){
            log.info("Didn't find any persistent attributes. Creating initial attributes");
            attributes.put(SubsonicUserAttributes.KEY, new SubsonicUserAttributes());
        } else {
            var userAttributesJson = attributes.get(SubsonicUserAttributes.KEY).toString();
            var userAttributes = safeDeserialize(userAttributesJson);

            attributes.put(SubsonicUserAttributes.KEY, userAttributes);
        }

        log.info("Loaded persistent attributes {}", attributes);
    }

    @Override
    @SneakyThrows
    public void process(HandlerInput handlerInput, Optional<Response> response) {
        log.info("Response interceptor {} has been called", AttributePersistenceInterceptor.class.getSimpleName());

        var attributes = handlerInput.getAttributesManager().getPersistentAttributes();

        var userAttributes = (SubsonicUserAttributes) attributes.get(SubsonicUserAttributes.KEY);
        var userAttributesJson = objectMapper.writeValueAsString(userAttributes);

        attributes.put(SubsonicUserAttributes.KEY, userAttributesJson);

        log.info("Saving user attributes to database");
        handlerInput.getAttributesManager().savePersistentAttributes();
        log.info("Saved persistent attributes {}", handlerInput.getAttributesManager().getPersistentAttributes());
    }

    private SubsonicUserAttributes safeDeserialize(String userAttributesJson){
        try {
            return objectMapper.readValue(userAttributesJson, SubsonicUserAttributes.class);
        } catch (Exception e){
            log.error("Deserialization of user attributes failed. Resetting user attributes", e);
            return new SubsonicUserAttributes();
        }
    }
}

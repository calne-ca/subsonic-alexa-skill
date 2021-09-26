package net.beardbot.subsonic.alexa.utils;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.model.Response;
import com.amazon.ask.request.handler.GenericRequestHandler;
import com.amazon.ask.request.interceptor.GenericRequestInterceptor;
import com.amazon.ask.request.interceptor.GenericResponseInterceptor;
import lombok.SneakyThrows;
import net.beardbot.subsonic.alexa.SubsonicStreamHandler;
import org.reflections.Reflections;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@SuppressWarnings("unchecked")
public class ReflectionUtil {

    private static final String PACKAGE_TO_SCAN = SubsonicStreamHandler.class.getPackageName();
    private static final Reflections REFLECTIONS = new Reflections(PACKAGE_TO_SCAN);

    public static List<GenericRequestHandler<HandlerInput, Optional<Response>>> collectRequestHandlers(){
        var handlerClasses = REFLECTIONS.getSubTypesOf(GenericRequestHandler.class);

        return handlerClasses.stream()
                .filter(c->c.getPackageName().contains(PACKAGE_TO_SCAN))
                .map(ReflectionUtil::createRequestHandlerObject)
                .collect(Collectors.toList());
    }

    public static List<GenericRequestInterceptor<HandlerInput>> collectRequestInterceptors(){
        var handlerClasses = REFLECTIONS.getSubTypesOf(GenericRequestInterceptor.class);

        return handlerClasses.stream()
                .filter(c->c.getPackageName().contains(PACKAGE_TO_SCAN))
                .map(ReflectionUtil::createRequestInterceptorObject)
                .collect(Collectors.toList());
    }

    public static List<GenericResponseInterceptor<HandlerInput, Optional<Response>>> collectResponseInterceptors(){
        var handlerClasses = REFLECTIONS.getSubTypesOf(GenericResponseInterceptor.class);

        return handlerClasses.stream()
                .filter(c->c.getPackageName().contains(PACKAGE_TO_SCAN))
                .map(ReflectionUtil::createResponseInterceptorObject)
                .collect(Collectors.toList());
    }

    @SneakyThrows
    private static GenericRequestHandler<HandlerInput, Optional<Response>> createRequestHandlerObject(Class<? extends GenericRequestHandler> clazz){
        return clazz.getDeclaredConstructor().newInstance();
    }

    @SneakyThrows
    private static GenericRequestInterceptor<HandlerInput> createRequestInterceptorObject(Class<? extends GenericRequestInterceptor> clazz){
        return clazz.getDeclaredConstructor().newInstance();
    }

    @SneakyThrows
    private static GenericResponseInterceptor<HandlerInput, Optional<Response>> createResponseInterceptorObject(Class<? extends GenericResponseInterceptor> clazz){
        return clazz.getDeclaredConstructor().newInstance();
    }
}

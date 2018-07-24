package net.sarangnamu.libcore;

import android.support.annotation.NonNull;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.io.File;
import java.io.IOException;

/**
 * Created by <a href="mailto:aucd29@hanwha.com">Burke Choi</a> on 2018. 7. 24. <p/>
 */
public class Json {
    private static ObjectMapper mMapper;

    private static void init() {
        if (mMapper == null) {
            mMapper = new ObjectMapper();

            mMapper.configure(JsonParser.Feature.ALLOW_COMMENTS, true);
            mMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
            mMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            mMapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
        }
    }

    public static ObjectMapper mapper() {
        init();
        return mMapper;
    }

    public static <T> T parse(@NonNull String jsonString, @NonNull Class<?> clazz) throws IOException, NullPointerException {
        return (T) mapper().readValue(jsonString, clazz);
    }

    public static <T> T parse(@NonNull String jsonString, @NonNull TypeReference valueTypeRef) throws IOException {
        return mapper().readValue(jsonString, valueTypeRef);
    }

    public static String stringify(@NonNull Object obj) throws JsonProcessingException {
        return mapper().writeValueAsString(obj);
    }
}

package no.capraconsulting.kurs2019.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class JsonUtils {
    private static Gson gson = new GsonBuilder()
            .create();

    private JsonUtils() {
    }

    public static Gson getGson() {
        return gson;
    }

    public static <T> T fromJSON(String toDeserialize, Class<T> target) {
        return gson.fromJson(toDeserialize, target);
    }

    public static String toJSON(Object toSerialize) {
        return gson.toJson(toSerialize);
    }

}

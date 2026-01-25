package we.employ.you.util;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import java.util.Arrays;
import java.util.Locale;
import java.util.Map;

public class JsonUtil {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private static final ObjectMapper PRETTY_MAPPER = new ObjectMapper();

    static {
        OBJECT_MAPPER.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        OBJECT_MAPPER.registerModule(new JavaTimeModule());
        PRETTY_MAPPER.enable(SerializationFeature.INDENT_OUTPUT);
    }

    public static String toJson(Object o) {
        return toJson(o, false);
    }

    public static String toJson(Object o, boolean prettyPrint) {
        StringBuffer json = new StringBuffer();
        ObjectMapper mapper = prettyPrint ? PRETTY_MAPPER : OBJECT_MAPPER;
        try {
            json.append(mapper.writeValueAsString(o));
        } catch (JsonProcessingException e) {
            LogUtil.logStackTrace(e);
        }
        return json.toString();
    }

    public static <T> T fromJson(String request, Class<T> clazz) {
        T result = null;
        if (request != null) {
            try {
                result = OBJECT_MAPPER.readValue(request, clazz);
            } catch (Exception e) {
            	LogUtil.logStackTrace(e);
            }
        }
        return result;
    }

    public static Map<String, Object> fromJson(String request) {
        Map<String, Object> result = null;
        if (request != null) {
            try {
                TypeReference<Map<String, Object>> typeRef = new TypeReference<Map<String, Object>>() {
                };
                result = OBJECT_MAPPER.readValue(request, typeRef);
            } catch (Exception e) {
            	LogUtil.logStackTrace(e);
            }
        }
        return result;
    }

    public static HttpHeaders getHttpHeader() {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setContentLanguage(Locale.US);

        return headers;
    }
}
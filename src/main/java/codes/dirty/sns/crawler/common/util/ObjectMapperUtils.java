package codes.dirty.sns.crawler.common.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import lombok.extern.slf4j.Slf4j;


@Slf4j
public final class ObjectMapperUtils {

    static ObjectMapper objectMapper = new ObjectMapper();

    private ObjectMapperUtils() {
    }

    public static String toJsonByObject(Object value) {
        String string;
        try {
            string = objectMapper.writeValueAsString(value);
        } catch (JsonProcessingException e) {
            log.error("Mapping failed. [{}]", e.getMessage());
            throw new RuntimeException("Mapping failed.", e);
        }
        return string;
    }

    public static <T> T toObjectByJson(String json, Class<T> type) {
        try {
            return objectMapper.readValue(json.getBytes(StandardCharsets.UTF_8), type);
        } catch (IOException e) {
            log.error("Mapping failed. [{}]", e.getMessage());
            throw new RuntimeException("Mapping failed.", e);
        }
    }
}

package bourse.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;

import java.util.List;

public class JsonUtils {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Converti un objet en json stringified
     * @param object objet à transformer
     */
    public static String toJson(Object object) {
        try {
            return objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Erreur lors de la sérialisation JSON", e);
        }
    }

    /**
     * Converti un json stringified dans le type voulu
     * @param json String qui contient le json
     * @param targetType type de destination
     */
    public static <T> T fromJson(String json, Class<T> targetType) {
        try {
            return objectMapper.readValue(json, targetType);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Erreur lors de la désérialisation JSON", e);
        }
    }


    /**
     * Converti un json stringified dans une liste du type voulu
     * @param json String qui contient le json
     * @param targetType type de destination
     */
    public static <T> List<T> fromJsonList(String json, Class<T> targetType) {
        try {
            CollectionType listType = objectMapper.getTypeFactory().constructCollectionType(List.class, targetType);
            return objectMapper.readValue(json, listType);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Erreur lors de la désérialisation JSON de la liste", e);
        }
    }
}


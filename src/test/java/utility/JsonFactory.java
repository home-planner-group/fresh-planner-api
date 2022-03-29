package utility;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.test.web.servlet.MvcResult;

import java.io.UnsupportedEncodingException;

/**
 * Converter for objects in the controller tests.
 */
public class JsonFactory {
    public static String convertToJson(Object obj) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(obj);
    }

    public static <T> T convertToObject(String jsonString, Class<T> targetClass) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(jsonString, targetClass);
    }

    public static <T> T convertToObject(MvcResult result, Class<T> responseClass) throws UnsupportedEncodingException, JsonProcessingException {
        return convertToObject(result.getResponse().getContentAsString(), responseClass);
    }
}

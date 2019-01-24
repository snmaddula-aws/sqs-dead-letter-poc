package snmaddula.sqs.deadletter.poc.util;

import static org.springframework.util.ReflectionUtils.rethrowRuntimeException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

/**
 * 
 * @author snmaddula
 *
 */
public class MappingUtil {

	private static ObjectMapper mapper = new ObjectMapper();
	private static ObjectWriter writer = mapper.writerWithDefaultPrettyPrinter();

	public static String toJson(Object o) {
		String json = null;
		try {
			json = writer.writeValueAsString(o);
		} catch (JsonProcessingException e) {
			rethrowRuntimeException(e);
		}
		return json;
	}

	public static <T> T toObject(String json, Class<T> clazz) {
		T t = null;
		try {
			t = mapper.readValue(json, clazz);
		} catch (Exception ex) {
			rethrowRuntimeException(ex);
		}
		return t;
	}
}

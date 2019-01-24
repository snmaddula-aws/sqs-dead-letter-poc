package com.dell.sqspoc.util;

import static org.springframework.util.ReflectionUtils.rethrowRuntimeException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

public class MappingUtil {

	private static ObjectWriter writer = new ObjectMapper().writerWithDefaultPrettyPrinter();
	
	public static String toJson(Object o) {
		String json = null;
		try {
			json = writer.writeValueAsString(o);
		} catch (JsonProcessingException e) {
			rethrowRuntimeException(e);
		}
		return json;
	}
}

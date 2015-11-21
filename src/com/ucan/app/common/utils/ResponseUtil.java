package com.ucan.app.common.utils;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

public class ResponseUtil {

	public static String getStatuCode(String s) {

		ObjectMapper mapper = new ObjectMapper();
		String statuCode = "205";
		try {
			@SuppressWarnings("unchecked")
			List<HashMap<String, String>> tmp = mapper.readValue(s, List.class);
			statuCode = String.valueOf(tmp.get(0).get("statuCode"));
		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return statuCode;
	}

}

package com.annpurna.cli.common.util;

import com.google.gson.Gson;

public class JsonParser {

	private static Gson gson = new Gson();

	public static  <T>  T deserialize(String json, Class <T> t) {
		return gson.fromJson(json, t);
	}
	
	public static String serialize(Object obj) {
		return gson.toJson(obj);
	}
}

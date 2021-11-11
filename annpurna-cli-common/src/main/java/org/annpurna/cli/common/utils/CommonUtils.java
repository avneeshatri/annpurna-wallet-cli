package org.annpurna.cli.common.utils;

import static java.nio.charset.StandardCharsets.UTF_8;

import org.json.JSONObject;

import com.annpurna.cli.common.model.CommercialPaper;

public class CommonUtils {

	 public static String deserialize(byte[] data) {
	        return new String(data, UTF_8);
	 }
}

package com.joeyism.app.web.rest;

import java.util.Iterator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ParameterConstructor {

	JSONObject parameters = null;

	public ParameterConstructor() {
		parameters = new JSONObject();
	}

	public void addStocks(JSONObject obj, int yearsback) throws JSONException {
		Iterator<?> keys = obj.keys();

		parameters.put("Normalized", false);
		parameters.put("NumberOfDays", 365*yearsback);
		parameters.put("DataPeriod", "Day");
		JSONArray elements = new JSONArray();

		while (keys.hasNext()) {
			String key = (String) keys.next();
			if (obj.getBoolean(key) == true) {
				JSONObject eachElement = new JSONObject();
				eachElement.put("Symbol", key);
				eachElement.put("Type", "price");
				eachElement.put("Params", new JSONArray("['c']"));
				elements.put(eachElement);
			}
		}
		parameters.put("Elements", elements);
	}

	public String params() {
		return parameters.toString();
	}

}

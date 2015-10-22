package com.joeyism.app;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class HighchartsDataConstructor {

	JSONArray results = null;
	long day = 86400000L;
	long year = 31536926000L;
	int yearsback = 1;

	public HighchartsDataConstructor() {
		results = new JSONArray();
	}

	public void setYearsBack(int yb){
		yearsback = yb;
	}
	
	public void parseResponse(String responseBody) throws JSONException, ParseException {

		JSONObject responseObj = new JSONObject(responseBody);
		Date now = new Date();

		JSONArray elements = responseObj.getJSONArray("Elements");
		JSONArray time = responseObj.getJSONArray("Dates");

		for (int i = 0; i < elements.length(); i++) {
			JSONObject element = elements.getJSONObject(i);
			JSONObject result = new JSONObject();
			JSONArray series = element.getJSONObject("DataSeries").getJSONObject("close").getJSONArray("values");
			JSONArray data = new JSONArray();
			for (int j = 0; j < series.length(); j++){
				JSONArray thisdata = new JSONArray();
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
				Date thistime = sdf.parse((String) time.get(j));
				thisdata.put(thistime.getTime());
				thisdata.put(series.get(j));
				data.put(thisdata);
			}
			result.put("data", data);
			result.put("name", element.getString("Symbol"));

			result.put("pointStart", now.getTime() - yearsback*year);
			result.put("pointInterval", day);
			results.put(result);
		}
	}
	
	public String toString(){
		return results.toString();
	}
}

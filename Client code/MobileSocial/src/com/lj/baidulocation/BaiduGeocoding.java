package com.lj.baidulocation;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.yg.commons.HttpGetter;


public class BaiduGeocoding 
{
	private final static String API_KEY = "8oXFzfld2gPBY7FDoZBSBY95";
	private final static String URL_STRING = "http://api.map.baidu.com/geocoder/v2/";
	private final static String LOCATION_FLAG = "locationFlag";
	private final static String HEAD_STRING = "?ak=" + API_KEY + "&callback=renderReverse&location=" + LOCATION_FLAG + "&output=json&pois=1";
	private final static String JSON_HEAD = "renderReverse&&renderReverse";
	
	private BaiduGeocoding()
	{
	}
	
	public static String geocodeGPS2Addr(double lat, double lng)
	{
		String url = URL_STRING + HEAD_STRING;
		url = url.replace(LOCATION_FLAG, lat + "," + lng);
		String jsonStr = HttpGetter.getResponse(url).replace(JSON_HEAD, "").replace("(", "").replace(")", "");
		return getPositionFromJson(jsonStr);
	}
	
	private static String getPositionFromJson(String jsonStr)
	{
		String result = null;
		try {
			JSONObject jsonObject = new JSONObject(jsonStr);
			int status = (Integer) jsonObject.get("status");
			if (status != 0)
				return null;
			jsonObject = (JSONObject) jsonObject.get("result");
			result = jsonObject.getString("sematic_description");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return result;
	}
	
	public static void main(String[] args) 
	{
		System.out.println(geocodeGPS2Addr(39.906281, 116.404242));
	}
}

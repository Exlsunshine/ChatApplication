package com.lj.translator;


import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.yg.commons.HttpGetter;





public class BaiduTranslator 
{
	private final String KEY_FLAG = "YourApiKey";
	private final String FROM_FLAG = "fromFlag";
	private final String TO_FLAG = "toFlag";
	private final String SENTANCE = "sentence";
	
	public static final String AUTO = "auto";
	public static final String EN = "en";
	public static final String ZH = "zh";
	public static final String JP = "jp";
	public static final String KOR = "kor";
	public static final String DE = "de";
	
	private final String API_KEY = "5onnGVPtq5zoIZTgg2pAeVOT";
	private final String URL_STRING = "http://openapi.baidu.com/public/2.0/bmt/translate";
	private final String HEAD_STRING = "?client_id=" + KEY_FLAG +"&q=" + SENTANCE + "&from=" + FROM_FLAG + "&to=" + TO_FLAG;
	
	public BaiduTranslator() 
	{
	}
	
	public String translate(String text, String fromLanguage, String toLanguage)
	{
		String result = null;
		String url = URL_STRING + HEAD_STRING;
		url = url.replace(KEY_FLAG, API_KEY).replace(FROM_FLAG, fromLanguage)
				.replace(TO_FLAG, toLanguage)
				.replace(SENTANCE, text.replace(" ", "%20"));
		HttpGet get = new HttpGet(url);
		HttpClient client = new DefaultHttpClient();  
		get.setHeader("Content-Type", "application/x-www-form-urlencoded; charset=utf-8");
		String jsonStr = null;
		try 
		{
			HttpResponse response = client.execute(get);//执行Post方法   
			jsonStr = EntityUtils.toString(response.getEntity());  
		} catch (Exception e)
		{  
			jsonStr = null;
	    }  
		if (jsonStr == null)
			return null;
		result = getTransResultFromJsonStr(jsonStr);
		return result;
	}
	
	public String autoTranslateTo(String text, String toLanguage)
	{
		return translate(text, AUTO, toLanguage);
	}
	
	private String getTransResultFromJsonStr(String jsonStr)
	{
		String result = null;
		try {
			JSONObject jsonObject = new JSONObject(jsonStr);
			JSONArray jsonArray = jsonObject.getJSONArray("trans_result");
			result = jsonArray.getJSONObject(0).getString("dst");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		return result;
	}
	
	public static void main(String[] args) 
	{
		BaiduTranslator translator = new BaiduTranslator();
		String result = translator.autoTranslateTo("你叫什么名字", BaiduTranslator.EN);
		
		System.out.println(result);
	}
}

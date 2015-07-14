package com.yg.commons;


import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import android.util.Log;

public class HttpGetter 
{
	private static final int TIMEOUT = 5000;
	public static String getResponse(String httpUrl)
	{
		BufferedReader reader = null;
	    String result = null;
	    StringBuffer sbf = new StringBuffer();
	    try {
	        URL url = new URL(httpUrl);
	        HttpURLConnection connection = (HttpURLConnection) url
	                .openConnection();
	        connection.setRequestMethod("GET");
	        connection.setConnectTimeout(TIMEOUT);
	        connection.connect();
	        java.io.InputStream is = connection.getInputStream();
	        reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
	        String strRead = null;
	        while ((strRead = reader.readLine()) != null) {
	            sbf.append(strRead);
	            sbf.append("\r\n");
	        }
	        reader.close();
	        result = sbf.toString();
	    } catch (Exception e) {
	        Log.e("Http Get Exception", "Http Get Exception");
	        return null;
	    }
	    return result;
	}
	
	public static void main(String[] args) 
	{
		System.out.println(HttpGetter.getResponse("http://www.baidu.com"));
	}
}

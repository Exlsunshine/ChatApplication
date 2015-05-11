package com.util;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * ������ǵ�����<br>��Ҫ��HometownHandler.getInstance()���ø��ֺ���
 * @author EXLsunshine
 *
 */
public class HometownHandler
{
	private static String cityProvienceDataPath = "C:\\Users\\USER007\\Desktop\\IM\\Source code\\Server code\\ProvinceAndCityJson.js";
	//private static String cityProvienceDataPath = "C:\\Users\\USER007\\Desktop\\IM\\data\\hometown\\ProvinceAndCityJson.js";
	private HashMap<String,HashMap<String, ArrayList<String>>> proviences = null;
	private static HometownHandler instance = null;
	
	private HometownHandler()
	{
		proviences = new HashMap<String,HashMap<String, ArrayList<String>>>();
		
		try 
		{
			loadData();
		} catch (IOException | JSONException e) {
			e.printStackTrace();
		}
	}
	
	public static HometownHandler getInstance()
	{
		if (instance == null)
		{
			synchronized(HometownHandler.class)
            {
                if(instance == null)
                	instance = new HometownHandler();
            }
		}
		
		return instance;
	}
	
	/**
	 * ���ʡ���б�
	 * @return ʡ���б�
	 */
	public String [] getProvinces()
	{
		return Arrays.copyOf(proviences.keySet().toArray(), proviences.keySet().toArray().length, String[].class);
	}
	
	/**
	 * ���ָ��ʡ�еĳ����б�
	 * @param province ʡ��
	 * @return �����б�<br>
	 * null ��ʾָ����province������
	 */
	public String [] getCities(String province)
	{
		if (!proviences.containsKey(province))
			return null;
		return Arrays.copyOf(proviences.get(province).keySet().toArray(), proviences.get(province).keySet().toArray().length, String[].class);
	}
	
	/**
	 * ���ָ��ʡ�С�ָ�����е����б�
	 * @param province ʡ��
	 * @param city ����
	 * @return ���б�<br>
	 * null ��ʾָ����province��city������
	 */
	public String [] getDistricts(String province, String city)
	{
		if (!proviences.containsKey(province) || !proviences.get(province).containsKey(city))
			return null;
		return Arrays.copyOf(proviences.get(province).get(city).toArray(), proviences.get(province).get(city).toArray().length, String[].class);
	}
	
	/**
	 * ����ʡ�������ļ�·��(����������ã���Ĭ�ϵľͺ�)
	 * @param filePath Ҫ��ȡ���ļ�·��
	 */
	public void setCityProvienceDataPath(String filePath)
	{
		cityProvienceDataPath = filePath;
	}
	
	private void loadData() throws IOException, JSONException
	{
		StringBuffer file = new StringBuffer();
		BufferedReader reader = new BufferedReader(new FileReader(cityProvienceDataPath));
		
		char[] buf = new char[1024];
        int numRead = 0;
        while((numRead=reader.read(buf)) != -1)
        {
            String readData = String.valueOf(buf, 0, numRead);
            file.append(readData);
        }
        reader.close();

        JSONObject obj = new JSONObject(file.toString());
        JSONArray arr = new JSONArray(obj.get("province").toString());
        
        for (int i = 0; i < arr.length(); i++)
        {
        	//get province name
        	String provience = (String) arr.getJSONObject(i).get("name");
        	JSONArray a = arr.getJSONObject(i).getJSONArray("city");
        	
        	HashMap<String, ArrayList<String>> cities = new HashMap<String, ArrayList<String>>();
        	for (int j = 0; j < a.length(); j++)
        	{
        		//get city name
        		String city = a.getJSONObject(j).getString("name");
        		
        		JSONArray ai = a.getJSONObject(j).getJSONArray("area");
            	ArrayList<String> districts = new ArrayList<String>();
        		for (int k = 0; k < ai.length(); k++)
        		{
        			//get district name
        			String district = (String) ai.getJSONObject(k).get("name");
        			districts.add(district);
        		}
        		cities.put(city, districts);
        	}
        	
        	proviences.put(provience, cities);
        }
	}
}
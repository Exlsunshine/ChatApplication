package com.commonapi;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class PackString
{
	private JSONObject msg = null;
	
	/**
	 * 将contents转换成JASON类型的字符串<br>
	 * msgName与contents应满足如下格式：<br>
	 * msgName: [{map1.key:map1.value}, {map2.key:map2.value}, {map3.key:map3.value}...]<br>
	 * 如：<br>
	 * "friends":[{"name":"bob","age":20},{"name":"Alice","age":30}]
	 * 
	 * @param msgName 消息名称
	 * @param contents 消息内容
	 * @return JSON格式的字符串
	 * @throws JSONException
	 */
	public static String arrylist2JsonString(String msgName, ArrayList<HashMap<String, Object>> contents) throws JSONException
	{
		if (contents == null)
		{
			System.out.println("Error:\tMap null.");
			return null;
		}
		
		if (contents.size() == 0)
		{
			System.out.println("Error:\tMap size is 0.");
			return null;
		}
		
		JSONObject result = new JSONObject();
		JSONArray items = new JSONArray();
		for (int i = 0; i < contents.size(); i++)
		{
			HashMap<String, Object> item = contents.get(i);
			
			JSONObject obj = new JSONObject();
			for (String key : item.keySet())
				obj.put(key, item.get(key));
			items.put(obj);
		}
		result.put(msgName, items);

		return result.toString();
	}
	
	/**
	 * 将contents转换成JASON类型的字符串<br>
	 * msgName与contents应满足如下格式：<br>
	 * msgName: [{map1.key:map1.value}, {map2.key:map2.value}, {map3.key:map3.value}...]<br>
	 * 如：<br>
	 * "friends":[{"name":"bob","age":20},{"name":"Alice","age":30}]
	 * 
	 * @param msgName 消息名称
	 * @param contents 消息内容
	 * @param _i 用于区分第二个参数的类型(用一个重载版本{@link #arrylist2JsonString(String, ArrayList)})
	 * @return JSON格式的字符串
	 * @throws JSONException
	 */
	public static String arrylist2JsonString(String msgName, ArrayList<HashMap<String, String>> contents, int _i) throws JSONException
	{
		if (contents == null)
		{
			System.out.println("Error:\tMap null.");
			return null;
		}
		
		if (contents.size() == 0)
		{
			System.out.println("Error:\tMap size is 0.");
			return null;
		}
		
		JSONObject result = new JSONObject();
		JSONArray items = new JSONArray();
		for (int i = 0; i < contents.size(); i++)
		{
			HashMap<String, String> item = contents.get(i);
			
			JSONObject obj = new JSONObject();
			for (String key : item.keySet())
				obj.put(key, item.get(key));
			items.put(obj);
		}
		result.put(msgName, items);

		return result.toString();
	}
	
	public PackString(String jsonString)
	{
		msg = string2Json(jsonString);
	}
	
	private JSONObject string2Json(String jsonString)
	{
		if (jsonString == null || jsonString.length() == 0)
		{
			System.out.println("Error:\tjsonString is null or size is 0.");
			return null;
		}
		
		JSONObject json = null;
		try
		{
			json = new JSONObject(jsonString);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		return json;
	}
	
	/**
	 * 将JSON格式的字符串转换为ArrayList<HashMap<String, Object>>
	 * @param msgName 消息名称
	 * @return 转换后的JSON消息的ArrayList<HashMap<String, Object>>对象
	 */
	public ArrayList<HashMap<String, Object>> jsonString2Arrylist(String msgName)
	{
		ArrayList<HashMap<String, Object>> list = null;
		try
		{
			JSONArray arr = (JSONArray) msg.get(msgName);
			list = new ArrayList<HashMap<String, Object>>();
			
			for (int i = 0; i < arr.length(); i++)
			{
				JSONObject item = arr.getJSONObject(i);
				Iterator it = item.keys();

				HashMap<String , Object> map = new HashMap<String, Object>();
				while (it.hasNext())
				{
					String key = (String) it.next();
					map.put(key, item.get(key));
				}
				
				list.add(map);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		
		return list;
	}
	
	public static void main(String[] args)
	{
		ArrayList<HashMap<String, Object>> items = new ArrayList<HashMap<String, Object>>();
		HashMap<String, Object> item = new HashMap<String, Object>();
		item.put("name", "bob");
		item.put("age", 100);
		items.add(item);
		
		item = new HashMap<String, Object>();
		item.put("name", "Alice");
		item.put("age", 1200);
		items.add(item);
		
		item = new HashMap<String, Object>();
		item.put("name", "John");
		item.put("age", 300);
		items.add(item);
		
		item = new HashMap<String, Object>();
		item.put("name", "Mahoon");
		item.put("age", 400);
		items.add(item);
		
		try
		{
			PackString ps = new PackString(PackString.arrylist2JsonString("friends", items));
			ArrayList<HashMap<String, Object>> result = ps.jsonString2Arrylist("friends");
			for (int i = 0; i < result.size(); i++)
			{
				HashMap<String, Object> map = result.get(i);
				for (String key : map.keySet())
					System.out.print(key + "\t" + map.get(key) + "\t");
				System.out.println();
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
}






/*
private Object getValue(int index, String key, String msgName)
{
	if (key == null || key.length() == 0)
	{
		System.out.println("Error:\tkey is null or size is 0.");
		return null;
	}
	if (msgName == null || msgName.length() == 0)
	{
		System.out.println("Error:\tmsgName is null or size is 0.");
		return null;
	}
	if (index < 0)
	{
		System.out.println("Error:\tindex illegal.");
		return null;
	}
	
	Object obj = null;
	try
	{
		JSONArray arr = (JSONArray) msg.get(msgName);
		JSONObject item = arr.getJSONObject(index);
		obj = item.get(key);
	} catch (JSONException e) {
		e.printStackTrace();
	}
	
	return obj;
}

public static String generateJsonString(String msgInfo, String [] keys, Object [] values)
{
	if (keys == null || values == null)
	{
		System.out.println("Error:\tKeys or values null.");
		return null;
	}
	
	if (keys.length != values.length)
	{
		System.out.println("Error:\tKeys length and values length are not equal.");
		return null;
	}
	
	HashMap<String, Object> map = new HashMap<String, Object>();
	
	for (int i = 0; i < keys.length; i++)
		map.put(keys[i], values[i]);

	return generateJsonString(null, null);
}*/
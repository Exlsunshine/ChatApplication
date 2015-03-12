package com.test.packstring;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class PackString
{
	/**
	 * 将contents转换成JASON类型的字符串<br>
	 * msgInfo与contents应满足如下格式：<br>
	 * msgInfo: [{map1.key:map1.value}, {map2.key:map2.value}, {map3.key:map3.value}...]<br>
	 * 如：<br>
	 * "friends":[{"name":"bob","age":20},{"name":"Alice","age":30}]
	 * 
	 * @param msgInfo 消息描述内容
	 * @param contents 消息内容
	 * @return JSON格式的字符串
	 * @throws JSONException
	 */
	public static String generateJsonString(String msgInfo, ArrayList<Map<String, Object>> contents) throws JSONException
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
			Map<String, Object> item = contents.get(i);
			
			JSONObject obj = new JSONObject();
			for (String key : item.keySet())
				obj.put(key, item.get(key));
			items.put(obj);
		}
		result.put(msgInfo, items);

		return result.toString();
	}
	
	public static void main(String[] args)
	{
		ArrayList<Map<String, Object>> items = new ArrayList<Map<String, Object>>();
		Map<String, Object> item = new HashMap<String, Object>();
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
		
		try {
			System.out.println(generateJsonString("friends", items));
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
}












/*public static String generateJsonString(String msgInfo, String [] keys, Object [] values)
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
	
	Map<String, Object> map = new HashMap<String, Object>();
	
	for (int i = 0; i < keys.length; i++)
		map.put(keys[i], values[i]);

	return generateJsonString(null, null);
}*/
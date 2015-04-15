package com.lj.gameSettingPackage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;

import com.commonapi.ConstantValues;
import com.commonapi.PackString;
import com.database.SQLServerEnd;

public class GameSong {

//	private final String DATABASE_NAME = "StrangerDB";
	private final String DATABASE_NAME = "JMMSRDB";
	private final String TABLE_NAME = "game_song_data";
	private SQLServerEnd sql = new SQLServerEnd(DATABASE_NAME, TABLE_NAME);
	private final int DATA_NUM = 10;
	private final int SELECT_NUM = 3;
	
	private int[] getSongID(int n)
	{
		int[] id = new int[n];
		for (int i = 0; i < n; i++)
			id[i] = i + 1;
		return id;
	}
	
	private ArrayList<HashMap<String, String>> getSelectResult(int[] id)
	{
		String[] query = {"song", "song_path"};
		String[] condition = {"id", "id", "id"};
		String[] conditionVal = new String[id.length];
		for (int i = 0; i < id.length; i++)
			conditionVal[i] = String.valueOf(id[i]);
		return sql.excecuteRawQuery("SELECT TOP 3 * FROM game_song_data ORDER BY NEWID()", query);
	}
	
	private String audio2String(String audioPath) throws Exception
	{
		FileInputStream fis = new FileInputStream(audioPath);
		ByteArrayOutputStream baos = new ByteArrayOutputStream(); 
		byte[] buffer = new byte[1024]; 
		int count = 0;
		while((count = fis.read(buffer)) >= 0)   
		    baos.write(buffer, 0, count);
		String result = new String(new BASE64Encoder().encode(baos.toByteArray())); 
		fis.close();
		baos.close();
		return result;
	}
	
	private String getCharList(String song)
	{
		String str = song + "就阿斯科利大家阿斯科利大家阿斯利康放假唉哦发建筑师客户发送加和法地位大家说服之靴不达瓦里可能缚梦者那份勤奋";
		return str.substring(0, 20);
	}
	
	public String getSongData() throws Exception
	{
		int[] id = getSongID(SELECT_NUM);
		ArrayList<HashMap<String, String>> selectResult = getSelectResult(id);
		ArrayList<HashMap<String, String>> result = new ArrayList<HashMap<String,String>>();
		for (int i = 0; i < selectResult.size(); i++)
		{
			HashMap<String, String> map = selectResult.get(i);
			HashMap<String, String> item = new HashMap<String, String>();
			String song = map.get("song");
			item.put("song", song);
			String path = map.get("song_path");
			System.out.println(i +" " + path + "ss " + song);
			String record = audio2String(path);
			item.put("record", record);
			item.put("char", getCharList(song));
			result.add(item);
		}
		return PackString.arrylist2JsonString("song", result, 0);
	}
}

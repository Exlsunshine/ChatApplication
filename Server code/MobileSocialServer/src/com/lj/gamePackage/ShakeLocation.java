package com.lj.gamePackage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.json.JSONException;

import com.commonapi.ConstantValues;
import com.commonapi.PackString;
import com.database.SQLServerEnd;

public class ShakeLocation 
{
	//数据库名
	//private final String DATABASE_NAME = "StrangerDB";
		//表名
	private final String DATABASE_NAME = "JMMSRDB";
	private final String TABLE_NAME = "shake_location";

	private SQLServerEnd sql = new SQLServerEnd(DATABASE_NAME, TABLE_NAME);
	
	public int uploadLocation(int userId, String longitude, String latitude)
	{
		String[] condition = {"user_id"};
        String[] conditionVal = {String.valueOf(userId)};
        boolean result = sql.isConditionExist(condition, conditionVal);
        if (result)
        	sql.delete(condition, conditionVal);
         
        String[] name = {"user_id", "longitude", "latitude"};
        String[] value = {String.valueOf(userId), longitude, latitude};
        sql.insert(name, value);
        System.out.println("User：[" + userId + "] 上传坐标 ： " + longitude + "," + latitude);
        return ConstantValues.InstructionCode.SUCCESS;
	}
	
	private String getNearbyUserFromDB(int userId) throws Exception
	{
		String[] query = {"user_id", "longitude", "latitude", "game_type", "nick_name", "sex"};
	/*	String[] condition = {"1"};
		String[] conditionVal = {"1"};*/
		String sqle = "select shake_location.user_id, shake_location.longitude, shake_location.latitude,user_settings.game_type, user_basic_info.nick_name, user_basic_info.sex from shake_location,user_settings,user_basic_info where shake_location.user_id = user_settings.user_id and shake_location.user_id = user_basic_info.id";
		ArrayList<HashMap<String, String>> map = sql.excecuteRawQuery(sqle, query);
		String str = PackString.arrylist2JsonString("nearbyusers", map, 0);
		return str; 
	}
	
	public String getNearbyUser(int userId) throws Exception
	{
		String result = getNearbyUserFromDB(userId);
		return result;
	}
	/*
	public static void main(String[] args) throws Exception {
		ShakeLocation s = new ShakeLocation();
	//	s.getNearbyUserFromDB(32);
		Random r = new Random();
		float lon = 116.49334f;
		float lat = 39.879635f;
		for (int i = 0; i < 15; i++)
		{
			String[] name = {"user_id", "longitude", "latitude"};
			float a = (float)r.nextDouble() / 100;
			int c = r.nextInt(2);
			if (c == 1)
				a = -a;
			float b = (float)r.nextDouble() / 100;
			c = r.nextInt(2);
			if (c == 1)
				b = -b;
	        String[] value = {String.valueOf(i), String.valueOf(lon + a), String.valueOf(lat + b)};
	  //      ShakeLocation s = new ShakeLocation(); 
	        s.sql.insert(name, value);
	        System.out.println("User：[" + i + "] 上传坐标 ： " + lon + " " + a + "," + lat + "" + b);
		}
		
		Integer a = new Integer(1);
		ArrayList<Integer> s = new ArrayList<Integer>();
		System.out.println(Integer.class.equals(s.getClass()));
		System.out.println(s.getClass().toString());
	}*/
}

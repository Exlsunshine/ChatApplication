package com.lj.gamePackage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.json.JSONException;

import com.commonapi.ConstantValues;
import com.commonapi.PackString;
import com.database.SQLServerEnd;
import com.lj.statistics.UserStatistics;

public class ShakeLocation 
{
	//数据库名
	//private final String DATABASE_NAME = "StrangerDB";
		//表名
	private final String DATABASE_NAME = "JMMSRDB";
	private final String TABLE_NAME = "shake_location";
	private final double MAP_SHAKE_SIZE = 10;
	private final String MAP_SHAKE_TIME = "'10:00:00.000'";

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
//		String sqle = "select shake_location.user_id, shake_location.longitude, shake_location.latitude,user_settings.game_type, user_basic_info.nick_name, user_basic_info.sex from shake_location,user_settings,user_basic_info where shake_location.user_id = user_settings.user_id and shake_location.user_id = user_basic_info.id";
		
		String sqle = "";
		String selectStr = "a.user_id, a.longitude, a.latitude,user_settings.game_type, user_basic_info.nick_name, user_basic_info.sex";
		String fromStr = "(select * from shake_location where" + 
						" abs(shake_location.latitude - (select shake_location.latitude from shake_location where shake_location.user_id = " + userId + ")) < " + MAP_SHAKE_SIZE + 
						" and abs(shake_location.longitude - (select shake_location.longitude from shake_location where shake_location.user_id = " + userId + ")) < " + MAP_SHAKE_SIZE + ")" + " as a,"
						+ " user_settings,user_basic_info";
		String whereStr = "a.user_id = user_settings.user_id and a.user_id = user_basic_info.id";
		String whereNotInStr = "a.user_id not in (select user_relationship.second_userid from user_relationship where user_relationship.first_userid =" + userId + ")";
		String whereTime = "a.shaketime >  getdate() - " + MAP_SHAKE_TIME;
		sqle = "select " + selectStr + " from " + fromStr + " where " + whereStr + " and " + whereNotInStr + " and " + whereTime;
		System.out.println(sqle);
		ArrayList<HashMap<String, String>> map = sql.excecuteRawQuery(sqle, query);
		if (map.size() == 0)
			return "null";
		String str = PackString.arrylist2JsonString("nearbyusers", map, 0);
		return str; 
	}
	
	public String getNearbyUser(int userId) throws Exception
	{
		UserStatistics statisitcs = new UserStatistics();
		statisitcs.increaseStatistic(userId, ConstantValues.InstructionCode.STATISTICS_SHAKE_NUM_TYPE);
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

package com.lj.setting.achievement;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;


import android.util.Log;

import com.yg.user.PackString;
import com.yg.user.WebServiceAPI;

public class UserStatistics 
{
	private WebServiceAPI web = new WebServiceAPI("statistics.lj.com", "UserStatistics");
	
	private int gUserID;
	
	private ArrayList<HashMap<String, Object>> gUserStatistics = null;
	
	public UserStatistics(int userID) 
	{
		gUserID = userID;
	}
	
	private int getLength(String registerTime, String currentTime)
	{
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");  
		Calendar cal = Calendar.getInstance();    
        try {
			cal.setTime(sdf.parse(registerTime));
		} catch (ParseException e) {
			e.printStackTrace();
		}    
        long time1 = cal.getTimeInMillis();                 
        try {
			cal.setTime(sdf.parse(currentTime));
		} catch (ParseException e) {
			e.printStackTrace();
		}    
        long time2 = cal.getTimeInMillis();         
        long between_days=(time2-time1)/(1000*3600*24);  
           
        int length = Integer.parseInt(String.valueOf(between_days));
        length /= 3;
        return length;
	}
	
	private ArrayList<HashMap<String, Object>> getUserStatistics(int userID)
	{
		String[] name = {"userID"};
		Object[] values = {userID};
		Object result = web.callFuntion("getStatistics", name, values);
		PackString ps = new PackString(result.toString());
		
		ArrayList<HashMap<String, Object>> statistics = ps.jsonString2Arrylist("statistics");
		
		HashMap<String, Object> map = statistics.get(0);
		String registerTime = (String) map.get("register_time");
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		String currentTime = df.format(new Date());
		
		int length = getLength(registerTime, currentTime);
		
		map.put("register_time", length);
		return statistics;
	}
	
	public ArrayList<HashMap<String, Object>> getUserStatistics()
	{
		if (gUserStatistics == null)
			gUserStatistics = getUserStatistics(gUserID);
		return gUserStatistics;
	}
	
}

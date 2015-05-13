package com.yg.ui.dialog.implementation;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import android.util.Log;

public class DateUtil
{
	private static final String DEBUG_TAG = "DateUtil______";
	/**
	 * 给出两个时间点的文字形式的时间间隔描述
	 * @param currentDateStr 较近的时间
	 * @param earlyDateStr 较早的时间
	 * @return 如"x分钟前","x天前"<br>
	 * null 表示输入的日期格式或前后关系有误
	 */
	public static String getSuggestion(String currentDateStr, String earlyDateStr)
	{
		if ((currentDateStr.length() != earlyDateStr.length())
			|| (currentDateStr.length() != 19) || (earlyDateStr.length() != 19))
		{
			Log.e(DEBUG_TAG, "输入的日期格式有误");
			return null;
		}
		
		Date current = null, early = null;
		
		try {
			DateFormat format = new SimpleDateFormat("yyyy-MM-dd-hh-mm-ss", Locale.ENGLISH);
			current = format.parse(currentDateStr);
			early = format.parse(earlyDateStr);
			
			if (current.before(early))
			{
				Log.e(DEBUG_TAG, "输入的日期先后顺序有误");
				return null;
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}

		long diff = Math.abs((current.getTime() - early.getTime())) / 1000;
		long day = diff / 3600 / 24;
		long hour = (diff - day * 3600 * 24) / 3600;
		long min = (diff - day * 3600 * 24 - hour * 3600) / 60;
		//long seconds = diff - day * 3600 * 24 - hour * 3600 - min * 60;
		
		if (day > 0 && hour != 0 || min != 0)
			day++;
		
		if (day == 1)
			return "昨天";
		else if (day >= 2 && day <= 7)
			return String.valueOf(day) + " 天前";
		else if (day > 7)
			return earlyDateStr.substring(0, 10);
		
		if (hour >= 1)
			return String.valueOf(hour) + " 小时前";
		
		if (min > 1 && min < 5)
			return "几分钟前";
		if (min >= 5)
			return String.valueOf(min) + " 分钟前";
		
		return "刚刚";
	}
	
	public static boolean lessThan5Mins(String dateStr, String dateStr2)
	{
		Date date = null, date2 = null;
		
		try {
			DateFormat format = new SimpleDateFormat("yyyy-MM-dd-hh-mm-ss", Locale.ENGLISH);
			date = format.parse(dateStr);
			date2 = format.parse(dateStr2);
		} catch (ParseException e) {
			e.printStackTrace();
		}

		long diff = Math.abs(date2.getTime() - date.getTime()) / 1000;
		
		if (diff <= 5 * 60)
			return true;
		else
			return false;
	}
	
	public static void main(String[] args)
	{
		System.out.println(DateUtil.getSuggestion("2015-04-10-11-05-45", "2015-04-10-11-05-50")); 
		System.out.println(DateUtil.lessThan5Mins("2015-04-10-11-05-45", "2015-04-10-11-10-46")); 
	}
}
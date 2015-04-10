package com.yg.ui.dialog.implementation;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DateUtil
{
	public static String getSuggestion(String currentDateStr, String earlyDateStr)
	{
		Date current = null, early = null;
		
		try {
			DateFormat format = new SimpleDateFormat("yyyy-MM-dd-hh-mm-ss", Locale.ENGLISH);
			current = format.parse(currentDateStr);
			early = format.parse(earlyDateStr);
		} catch (ParseException e) {
			e.printStackTrace();
		}

		long diff = Math.abs((current.getTime() - early.getTime())) / 1000;
		long day = diff / 3600 / 24;
		long hour = (diff - day * 3600 * 24) / 3600;
		long min = (diff - day * 3600 * 24 - hour * 3600) / 60;
		//long seconds = diff - day * 3600 * 24 - hour * 3600 - min * 60;
		
		if (day == 1)
			return "Yesterday";
		if (day >= 2 && day < 5)
			return String.valueOf(day) + " days ago";
		
		if (hour >= 1)
			return String.valueOf(hour) + " hours ago";
		
		if (min > 1 && min < 5)
			return "Few minitues ago";
		if (min >= 5)
			return String.valueOf(hour) + " minitues ago";
		
		return "Few seconds ago";
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
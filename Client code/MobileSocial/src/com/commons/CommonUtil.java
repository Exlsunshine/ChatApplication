package com.commons;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class CommonUtil
{
	public static String now()
	{
		Calendar cal = Calendar.getInstance();
	    SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd hh:mm:ss", Locale.CHINA);
	    /*
	    DateFormat df;
	    df = DateFormat.getDateTimeInstance(DateFormat.FULL, DateFormat.FULL, Locale.CHINA);
	    Log.i(DEBUG_TAG, "Full is: " + "\t" + df.format(cal.getTime()));
	    */
	    return sdf.format(cal.getTime());
	}
}

package com.yg.commons;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import android.os.Handler;
import android.os.Message;

import com.yg.user.WebServiceAPI;

public class CommonUtil
{
	public static String now()
	{
		Calendar cal = Calendar.getInstance();
	    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss", Locale.CHINA);
	    /*
	    DateFormat df;
	    df = DateFormat.getDateTimeInstance(DateFormat.FULL, DateFormat.FULL, Locale.CHINA);
	    Log.i(DEBUG_TAG, "Full is: " + "\t" + df.format(cal.getTime()));
	    */
	    return sdf.format(cal.getTime());
	}
	public static void sendNetWorkErrorMessage(Handler handle)
	{
		Message msg = new Message();
		msg.what = ConstantValues.InstructionCode.ERROR_NETWORK;
		handle.sendMessage(msg);
	}
	
	public static boolean isNetWorkError(Object result)
	{
		if (result.getClass().equals(Integer.class) && Integer.valueOf(result.toString()) == ConstantValues.InstructionCode.ERROR_NETWORK)
			return true;
		else
			return false;
	}
	public static String [] getDistrictList(String province, String city)
	{
		String [] params = new String[2];
		Object [] vlaues = new Object[2];
		params[0] = "province";
		params[1] = "city";
		vlaues[0] = province;
		vlaues[1] = city;
		
		WebServiceAPI wsAPI = new WebServiceAPI("network.com", "NetworkHandler");
		Object ret = wsAPI.callFuntion("getDistrictList", params, vlaues);
		String str = ret.toString();

		return str.split("-");
	}
	
	public static String [] getCityList(String province)
	{
		String [] params = new String[1];
		Object [] vlaues = new Object[1];
		params[0] = "province";
		vlaues[0] = province;
		
		WebServiceAPI wsAPI = new WebServiceAPI("network.com", "NetworkHandler");
		Object ret = wsAPI.callFuntion("getCityList", params, vlaues);
		String str = ret.toString();

		return str.split("-");
	}
	
	public static String [] getProvienceList()
	{
		WebServiceAPI wsAPI = new WebServiceAPI("network.com", "NetworkHandler");
		Object ret = wsAPI.callFuntion("getProvienceList");
		String str = ret.toString();
		
		return str.split("-");
	}
}

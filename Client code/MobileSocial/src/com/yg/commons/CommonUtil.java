package com.yg.commons;

import java.io.File;
import java.io.IOException;
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
	    return sdf.format(cal.getTime());
	}
	
	public static void sendNetWorkErrorMessage(Handler handle)
	{
		Message msg = new Message();
		msg.what = ConstantValues.InstructionCode.ERROR_NETWORK;
		handle.sendMessage(msg);
	}
	
	/**
	 * 删除指定路径内全部内容(包括文件夹自身)
	 * @param path 指定的路径
	 */
	void deleteFiles(String path)
	{
	    File file = new File(path);

	    if (file.exists()) 
	    {
	        String deleteCmd = "rm -r -f " + path;
	        Runtime runtime = Runtime.getRuntime();
	        try
	        {
	            runtime.exec(deleteCmd);
	        } catch (IOException e) { }
	    }
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
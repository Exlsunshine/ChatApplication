package com.lj.userbasicsetting;


import com.yg.commons.CommonUtil;
import com.yg.commons.ConstantValues;

import android.os.Handler;
import android.os.Message;

public class ThreadGetCityArray extends Thread
{
	private String myProvince;
	private Handler myHandler; 
	public ThreadGetCityArray(Handler handle, String province) 
	{
		// TODO Auto-generated constructor stub
		myHandler = handle;
		myProvince = province;
	}
	
	@Override
	public void run() 
	{
		// TODO Auto-generated method stub
		super.run();
		String[] array = CommonUtil.getCityList(myProvince);
		if (array == null)
			CommonUtil.sendNetWorkErrorMessage(myHandler);
		else
		{
			Message msg = new Message();
			msg.obj = array;
			msg.what = ConstantValues.InstructionCode.USERSET_HANDLER_CITY;
			myHandler.sendMessage(msg);
		}
	}

}

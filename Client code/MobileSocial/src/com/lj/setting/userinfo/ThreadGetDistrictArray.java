package com.lj.setting.userinfo;

import com.yg.commons.CommonUtil;
import com.yg.commons.ConstantValues;

import android.os.Handler;
import android.os.Message;

public class ThreadGetDistrictArray extends Thread
{
	private String myProvince;
	private String myCity;
	private Handler myHandler;
	
	public ThreadGetDistrictArray(Handler handle, String province, String city) 
	{
		myHandler = handle;
		myProvince = province;
		myCity = city;
	}
	
	@Override
	public void run() 
	{
		super.run();
		String[] array = CommonUtil.getDistrictList(myProvince, myCity);
		if (array == null)
			CommonUtil.sendNetWorkErrorMessage(myHandler);
		else
		{
			Message msg = new Message();
			msg.obj = array;
			msg.what = ConstantValues.InstructionCode.USERSET_HANDLER_DISTRICT;
			myHandler.sendMessage(msg);
		}
	}
}

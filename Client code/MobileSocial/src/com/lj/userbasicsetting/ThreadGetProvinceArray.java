package com.lj.userbasicsetting;

import com.yg.commons.CommonUtil;
import com.yg.commons.ConstantValues;

import android.os.Handler;
import android.os.Message;

public class ThreadGetProvinceArray extends Thread
{
	private Handler myHandler;
	
	public ThreadGetProvinceArray(Handler handle) 
	{
		myHandler = handle;
	}
	
	@Override
	public void run() 
	{
		super.run();
		String[] array = CommonUtil.getProvienceList();
		if (array == null)
			CommonUtil.sendNetWorkErrorMessage(myHandler);
		else
		{
			Message msg = new Message();
			msg.obj = array;
			msg.what = ConstantValues.InstructionCode.USERSET_HANDLER_PROVINCE;
			myHandler.sendMessage(msg);
		}
	}
}

package com.lj.networktest;

import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;

import com.yg.commons.CommonUtil;
import com.yg.user.WebServiceAPI;

public class ThreadNetworkTest extends Thread
{
	private final String PACKAGE_NAME = "network.com";
	private final String CLASS_NAME = "NetworkHandler";
	private final String METHOD_NAME = "testConnect";
	private final int TEST_NUM = 5;
	
	public static int NETWORK_ERROR = 0x1000;
	public static int NETWORK_CORRECT = 0x1001;
	
	private Handler gHandler;
	private int gButtonType;
	
	public ThreadNetworkTest(Handler handler, int buttonType) 
	{
		gHandler = handler;
		gButtonType = buttonType;
	}
	
	@Override
	public void run() 
	{
		super.run();
		long startTime=System.currentTimeMillis();
		WebServiceAPI webAPI = new WebServiceAPI(PACKAGE_NAME, CLASS_NAME);
		
		for (int i = 1; i <= TEST_NUM; i++)
		{
			String testStr = "Test network condition:" + i;
			String[] name = {"array", "n"};
			Object[] values = {testStr, i};
			Object result = webAPI.callFuntion(METHOD_NAME, name, values);
			if (CommonUtil.isNetWorkError(result))
			{
				long endTime = System.currentTimeMillis();
				long time = endTime - startTime;
				if (time < 2000)
					SystemClock.sleep(2000 - time);
				gHandler.sendEmptyMessage(NETWORK_ERROR);
				return;
			}
		}
		long endTime = System.currentTimeMillis();
		long time = endTime - startTime;
		if (time < 2000)
			SystemClock.sleep(2000 - time);
		Message msg = new Message();
		msg.what = NETWORK_CORRECT;
		msg.arg1 = gButtonType;
		gHandler.sendMessage(msg);
		
	}
}

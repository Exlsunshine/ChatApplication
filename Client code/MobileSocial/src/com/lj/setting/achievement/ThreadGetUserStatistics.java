package com.lj.setting.achievement;

import java.util.ArrayList;
import java.util.HashMap;

import com.yg.commons.ConstantValues;

import android.os.Handler;
import android.os.Message;

public class ThreadGetUserStatistics extends Thread
{
	private int gUserID;
	private Handler gHandler = null;
	
	public ThreadGetUserStatistics(int userID, Handler handler) 
	{
		gUserID = userID;
		gHandler = handler;
	}
	
	@Override
	public void run() 
	{
		super.run();
		UserStatistics userStatistics = new UserStatistics(gUserID);
		ArrayList<HashMap<String, Object>> result = userStatistics.getUserStatistics();
		Message msg = new Message();
		msg.what = ConstantValues.InstructionCode.USERSTATISTICS_HANDLER_GETDATA;
		msg.obj = userStatistics;
		gHandler.sendMessage(msg);
	}
}

package com.lj.bazingaball;

import com.yg.commons.ConstantValues;

import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;

public class ThreadButtonMove extends Thread
{
	private Handler gHandler;
	
	public ThreadButtonMove(Handler handler) 
	{
		gHandler = handler;
	}
	
	@Override
	public void run() 
	{
		super.run();
		while (true)
		{
			Message msg = new Message();
			msg.what = ConstantValues.InstructionCode.BAZINGABALL_HANDLER_MOVE;
			gHandler.sendMessage(msg);
			SystemClock.sleep(20);
		}
		
	}
}

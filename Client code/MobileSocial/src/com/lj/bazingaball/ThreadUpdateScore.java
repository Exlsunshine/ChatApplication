package com.lj.bazingaball;

import com.yg.commons.ConstantValues;

import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.util.Log;

public class ThreadUpdateScore extends Thread
{
	private Handler gHandler;
	private int myScore;
	private boolean gAlive;
	
	public ThreadUpdateScore(Handler handler) 
	{
		gHandler = handler;
		myScore = 0;
		gAlive = true;
		Log.e("sssssssssssssssssssssssssssssssssss", "wwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwww");
	}
	
	@Override
	public void run() 
	{
		super.run();
		while (true)
		{
			if (!gAlive)
				return;
			myScore++;
			Message msg = new Message();
			msg.what = ConstantValues.InstructionCode.BAZINGABALL_HANDLER_UPDATE_SCORE;
			msg.arg1 = myScore;
			gHandler.sendMessage(msg);
			SystemClock.sleep(100);
		}
	}
	
	public void disableUpdateScore()
	{
		gAlive = false;
	}
}

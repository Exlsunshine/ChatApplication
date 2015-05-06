package com.lj.setting.game;

import com.lj.shake.WebGameSetting;
import com.yg.commons.ConstantValues;

import android.os.Handler;
import android.os.Message;

public class ThreadGetGameType extends Thread
{
	private int gUserID;
	private Handler gHandler;
	
	public ThreadGetGameType(int userID, Handler handler) 
	{
		gUserID = userID;
		gHandler = handler;
	}
	
	@Override
	public void run() 
	{
		super.run();
		WebGameSetting wgs = new WebGameSetting();
		int gametype = wgs.getSetGameType(gUserID);
		Message msg = new Message();
		msg.what = ConstantValues.InstructionCode.GAMESET_HANDLER_INIT_GAMETYPE;
		msg.arg1 = gametype;
		gHandler.sendMessage(msg);
	}
}

package com.lj.setting.game;

import com.yg.commons.ConstantValues;
import com.yg.user.WebServiceAPI;

import android.os.Handler;
import android.os.Message;

public class ThreadGetBazingaScore extends Thread
{
	private int gUserID;
	private Handler gHandler;
	private  WebServiceAPI webserviceMole = new WebServiceAPI(ConstantValues.InstructionCode.PACKAGE_GAME_SETTING, "GameMole");
	
	public ThreadGetBazingaScore(int userID, Handler handler) 
	{
		gUserID = userID;
		gHandler = handler;
	}
	
	public void run() 
	{
		String[] name = {"userID"};
		Object[] values = {gUserID};
		Object result = webserviceMole.callFuntion("getMoleScore", name, values);
		Message msg = new Message();
		msg.what = ConstantValues.InstructionCode.GAMESET_HANDLER_INIT_MOLE;
		msg.obj = result;
		gHandler.sendMessage(msg);
	};
}

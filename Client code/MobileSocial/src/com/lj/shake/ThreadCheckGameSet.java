package com.lj.shake;

import com.yg.commons.CommonUtil;
import com.yg.commons.ConstantValues;

import android.os.Handler;
import android.os.Message;

public class ThreadCheckGameSet extends Thread
{
	private WebGameSetting webGameSetting;
	private int userId;
	private Handler myhandle;
	
	public ThreadCheckGameSet(Handler handle, int userid) 
	{
		webGameSetting = new WebGameSetting();
		userId = userid;
		myhandle = handle;
	}
	
	@Override
	public void run() 
	{
		super.run();
		int gameSet = webGameSetting.checkSetGame(userId);
		if (gameSet == ConstantValues.InstructionCode.ERROR_NETWORK)
		{
			CommonUtil.sendNetWorkErrorMessage(myhandle);
			return;
		}
		else if (gameSet == ConstantValues.InstructionCode.GAME_NOT_SET)
		{
			Message msg = new Message();
			msg.what = ConstantValues.InstructionCode.SHAKE_HANDLER_USER_GAME_NOT_SET;
			myhandle.sendMessage(msg);
		}
		else
		{
			Message msg = new Message();
			msg.what = ConstantValues.InstructionCode.HANDLER_WAIT_FOR_DATA;
			myhandle.sendMessage(msg);
		}
	}
}

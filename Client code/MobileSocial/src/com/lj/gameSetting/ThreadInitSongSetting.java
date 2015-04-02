package com.lj.gameSetting;

import java.util.ArrayList;
import java.util.HashMap;
import com.yg.commons.CommonUtil;
import com.yg.commons.ConstantValues;


import android.os.Handler;
import android.os.Message;

public class ThreadInitSongSetting extends Thread
{
	private int userID;
	private Handler handle;
	
	public ThreadInitSongSetting(int userid, Handler h) 
	{
		userID = userid;
		handle = h;
	}
	
	@Override
	public void run() 
	{
		super.run();
		ArrayList<HashMap<String, Object>> songSetting = SongPuzzleSetting.getInitSongSetting(userID);
		if (songSetting == null)
		{
			Message msg = new Message();
			CommonUtil.sendNetWorkErrorMessage(handle);
		}
		else
		{
			Message msg = new Message();
			msg.what =  ConstantValues.InstructionCode.GAMESET_HANDLER_INIT_SONG;
			msg.obj = songSetting;
			handle.sendMessage(msg);
		}
	}
}

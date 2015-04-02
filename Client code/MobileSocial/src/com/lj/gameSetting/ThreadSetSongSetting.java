package com.lj.gameSetting;

import java.util.ArrayList;
import java.util.HashMap;
import com.yg.commons.CommonUtil;
import android.os.Handler;

public class ThreadSetSongSetting extends Thread
{
	private int userID;
	private Handler handle;
	private ArrayList<HashMap<String, Object>> map;
	
	public ThreadSetSongSetting(int userid, Handler h, ArrayList<HashMap<String, Object>> m) 
	{
		userID = userid;
		handle = h;
		map = m;
	}
	
	@Override
	public void run() 
	{
		super.run();
		try 
		{
			Object result = SongPuzzleSetting.setSongGame(userID, map);
			if (CommonUtil.isNetWorkError(result))
				CommonUtil.sendNetWorkErrorMessage(handle);
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
	}
}

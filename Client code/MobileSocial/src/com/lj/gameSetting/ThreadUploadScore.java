package com.lj.gameSetting;

import com.yg.commons.CommonUtil;
import com.yg.user.WebServiceAPI;

import android.os.Handler;

public class ThreadUploadScore extends Thread
{
	private int userID;
	private int score;
	private Handler handle;
	private WebServiceAPI webserviceMole = new WebServiceAPI("gameSettingPackage", "GameMole");
	public ThreadUploadScore(Handler h, int userid, int s) 
	{
		userID = userid;
		score = s;
		handle = h;
	}
	
	@Override
	public void run()
	{
		super.run();
		String[] name = {"userID", "score"};
		Object[] values = {userID, score};
		Object result = webserviceMole.callFuntion("uploadScore", name, values);
		if (CommonUtil.isNetWorkError(result))
			CommonUtil.sendNetWorkErrorMessage(handle);
	}
}

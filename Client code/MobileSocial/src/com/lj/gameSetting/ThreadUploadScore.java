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
		// TODO Auto-generated constructor stub
		userID = userid;
		score = s;
		handle = h;
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		super.run();
		String[] name = {"userID", "score"};
		Object[] values = {userID, score};
		Object result = webserviceMole.callFuntion("uploadScore", name, values);
		if (CommonUtil.isNetWorkError(result))
			CommonUtil.sendNetWorkErrorMessage(handle);
	}
}

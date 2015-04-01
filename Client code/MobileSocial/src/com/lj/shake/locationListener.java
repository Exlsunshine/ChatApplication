package com.lj.shake;

import android.os.Handler;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;

public class locationListener implements BDLocationListener
{

	private Handler myHandler;
	private int userID;
	public locationListener(Handler handler, int userid) 
	{
		myHandler = handler;
		userID = userid;
	}
	@Override
	public void onReceiveLocation(BDLocation location) 
	{
		Thread thread = new ThreadGetNearbyUser(myHandler, userID, location);
		thread.start();
	}

}

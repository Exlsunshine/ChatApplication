package com.lj.baidulocation;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class PositionDetector 
{
	public final static int ADDR_RESULT_SUCCESS = 1;
	public final static int ADDR_RESULT_ERROR = 2;
	
	private Handler handler = null;
	private LocationClient locationClient = null;
	private Context context = null;
	
	public PositionDetector(Handler handler, Context context) 
	{
		this.handler = handler;
		this.context = context;
		initBaiduLocation();
	}
	public void detect()
	{
		if (!locationClient.isStarted())
			locationClient.start();
		locationClient.requestLocation();
	}
	
	private void initBaiduLocation()
	{
		LocationClientOption option = new LocationClientOption();
		option.setCoorType("bd09ll");
		locationClient = new LocationClient(context); 
		locationClient.setLocOption(option);
		locationClient.registerLocationListener(new LocationListener());
	}
	
	
	public class LocationListener implements BDLocationListener
	{
		@Override
		public void onReceiveLocation(BDLocation location) 
		{
			final double lat = location.getLatitude();
			final double lng = location.getLongitude();
			new Thread(new Runnable() 
			{
				@Override
				public void run() 
				{
					String result = BaiduGeocoding.geocodeGPS2Addr(lat, lng);
					Message msg = new Message();
					msg.what = result == null ? ADDR_RESULT_ERROR : ADDR_RESULT_SUCCESS;
					msg.obj = result;
					handler.sendMessage(msg);
				}
			}).start();
		}
	}
}

package com.lj.shake;

import android.graphics.Point;
import android.os.Handler;
import android.os.Message;

import com.baidu.mapapi.map.BaiduMap.OnMarkerClickListener;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.model.LatLng;
import com.yg.commons.ConstantValues;


public class markerClickListener implements OnMarkerClickListener
{
	private Handler myHandler;

	public markerClickListener(Handler handler) 
	{
		myHandler = handler;
	}
	@Override
	public boolean onMarkerClick(Marker marker)
	{
		Message msg = new Message();
		msg.obj = marker;
		msg.what = ConstantValues.InstructionCode.SHAKE_HANDLER_MARKER_CLICK;
		myHandler.sendMessage(msg);
		return false;
	}
}

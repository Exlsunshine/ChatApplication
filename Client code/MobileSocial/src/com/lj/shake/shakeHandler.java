package com.lj.shake;

import java.util.ArrayList;

import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.example.testmobiledatabase.R;
import com.lj.customview.UserDataWindowView;
import com.lj.customview.ViewGrougTest;
import com.yg.commons.ConstantValues;


import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Toast;

public class shakeHandler extends Handler
{
	private int viewX;
	private int viewY;
	ActivityShake myContext;

	public shakeHandler(ActivityShake context) 
	{
		myContext = context;
	}
	
	private void locateMyLocation(UserShakeData userShakeData)
	{
		LatLng cenpt = new LatLng(userShakeData.getLatitude() ,userShakeData.getLongitude());
		MapStatus mMapStatus = new MapStatus.Builder().target(cenpt).zoom(ConstantValues.InstructionCode.MAP_ZOOM_INITIALIZATION).build();
		MapStatusUpdate mMapStatusUpdate = MapStatusUpdateFactory.newMapStatus(mMapStatus);
		myContext.baiduMap.setMapStatus(mMapStatusUpdate);
		BitmapDescriptor markIcon = BitmapDescriptorFactory.fromResource(R.drawable.my_location);
		OverlayOptions option = new MarkerOptions().position(cenpt).icon(markIcon);  
		myContext.baiduMap.addOverlay(option);
	}
	
	private void locateOtherLocation(UserShakeData userShakeData)
	{
		LatLng cenpt = new LatLng(userShakeData.getLatitude() ,userShakeData.getLongitude());
		BitmapDescriptor markIcon = BitmapDescriptorFactory.fromResource(R.drawable.user_location);
		OverlayOptions option = new MarkerOptions().position(cenpt).icon(markIcon);  
		Marker marker = (Marker)myContext.baiduMap.addOverlay(option);
		Bundle bundle = new Bundle();
		bundle.putSerializable("nickname", userShakeData.getNickName());
		bundle.putSerializable("gametype", userShakeData.getGameType());
		marker.setExtraInfo(bundle);
	}
	
	private void locateUsers(ArrayList<UserShakeData> userShakeDataList)
	{
		for (int i = 0; i < userShakeDataList.size(); i++)
		{
			UserShakeData userShakeData = userShakeDataList.get(i);
			if (userShakeData.getUserId() == ConstantValues.user.getID())
				locateMyLocation(userShakeData);
			else
				locateOtherLocation(userShakeData);	
		}
	}
	
	@Override
	public void handleMessage(Message msg) 
	{
		super.handleMessage(msg);
		switch (msg.what)
		{
		case ConstantValues.InstructionCode.ERROR_NETWORK:
			Toast.makeText(myContext, "网络错误", Toast.LENGTH_LONG).show();
			break;
		case ConstantValues.InstructionCode.SHAKE_HANDLER_USER_GAME_NOT_SET:
			Toast.makeText(myContext, "游戏没有设置", Toast.LENGTH_LONG).show();
			break;
		case ConstantValues.InstructionCode.HANDLER_WAIT_FOR_DATA:
			myContext.sensorManager.unregisterListener(myContext.shakelistener);
			myContext.loadingView.setVisibility(View.VISIBLE);
			myContext.locationClient.start();
			break;
		case ConstantValues.InstructionCode.HANDLER_SUCCESS_GET_DATA:
			myContext.loadingView.setVisibility(View.INVISIBLE);
			myContext.mapView.setVisibility(View.VISIBLE);
			@SuppressWarnings("unchecked")
			ArrayList<UserShakeData> userShakeDataList = (ArrayList<UserShakeData>) msg.obj;
			locateUsers(userShakeDataList);
			break;
		case ConstantValues.InstructionCode.SHAKE_HANDLER_SHAKE_SENSOR:
			myContext.vibrator.vibrate(ConstantValues.InstructionCode.VIBRATE_TIME);
			break;
		case ConstantValues.InstructionCode.SHAKE_HANDLER_MARKER_CLICK:
			Marker marker = (Marker) msg.obj;
			Bundle data = marker.getExtraInfo(); 
			if (data == null)
				return;
			String nickname = data.get("nickname").toString();
			int gametype = Integer.valueOf(data.get("gametype").toString());
			final LatLng ll = marker.getPosition();
	        Point p = myContext.baiduMap.getProjection().toScreenLocation(ll);
	        if (myContext.userDataWindowView != null)
	        	myContext.mainLayout.removeView(myContext.userDataWindowView);
	        myContext.userDataWindowView = new ViewGrougTest(myContext, p.x, p.y - UserDataWindowView.H, nickname, null, gametype);
	        myContext.mainLayout.addView(myContext.userDataWindowView);
			break;
		case ConstantValues.InstructionCode.SHAKE_HANDLER_MAP_STATUS_CHANGE:
			myContext.mainLayout.removeView(myContext.userDataWindowView);
			break;
		case ConstantValues.InstructionCode.SHAKE_HANDLER_MAP_TOUCH_DOWN:
			if (myContext.userDataWindowView == null)
				return;
			viewX = (int) myContext.userDataWindowView.getPos_X();
	        viewY = (int) myContext.userDataWindowView.getPos_Y();
			break;
		case ConstantValues.InstructionCode.SHAKE_HANDLER_MAP_TOUCH_MOVE:
			if (myContext.userDataWindowView == null)
				return;
			myContext.userDataWindowView.setPosition(msg.arg1 + viewX, msg.arg2 + viewY);
			break;
		case ConstantValues.InstructionCode.SHAKE_HANDLER_MAP_FAST_MOVE:
			myContext.mainLayout.removeView(myContext.userDataWindowView);
			break;
		}
	}
}

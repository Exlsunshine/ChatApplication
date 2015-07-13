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
import com.baidu.mapapi.map.TextOptions;
import com.baidu.mapapi.model.LatLng;
import com.example.testmobiledatabase.R;
import com.lj.bazingaball.ActivityBazingaBall;
import com.lj.eightpuzzle.ActivityEightPuzzleGame;
import com.lj.songpuzzle.ActivitySongPuzzle;
import com.yg.commons.CommonUtil;
import com.yg.commons.ConstantValues;


import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.sax.StartElementListener;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;

public class shakeHandler extends Handler
{
	private int viewX;
	private int viewY;
	ActivityShake myContext;
	ArrayList<UserShakeData> gUserShakeDataList = null;
	ArrayList<UserShakeData> gUserShakeDataListMale = null;
	ArrayList<UserShakeData> gUserShakeDataListFemale = null;
	
	private final int SEX_MALE = 1;
	private final int SEX_FEMALE = 2;
	private final int SEX_NONE = 0;
	private int gSexStatus = SEX_NONE;

	private OnClickListener gSexClickListener = new OnClickListener() 
	{
		@Override
		public void onClick(View v) 
		{
			int id = v.getId();
			myContext.baiduMap.clear();
			if (id == R.id.lj_map_female && gSexStatus != SEX_FEMALE)
			{ 
				gSexStatus = SEX_FEMALE;
				locateUsers(gUserShakeDataListFemale);
				myContext.gFemaleSelect.setImageResource(R.drawable.lj_map_female_select);
				myContext.gMaleSelect.setImageResource(R.drawable.lj_map_male_unselect);
			}
			else if (id == R.id.lj_map_male && gSexStatus != SEX_MALE)
			{
				gSexStatus = SEX_MALE;
				locateUsers(gUserShakeDataListMale);
				myContext.gFemaleSelect.setImageResource(R.drawable.lj_map_female_unselect);
				myContext.gMaleSelect.setImageResource(R.drawable.lj_map_male_select);
			}
			else if ((id == R.id.lj_map_female && gSexStatus == SEX_FEMALE) || (id == R.id.lj_map_male && gSexStatus == SEX_MALE))
			{
				gSexStatus = SEX_NONE;
				locateUsers(gUserShakeDataList);
				myContext.gFemaleSelect.setImageResource(R.drawable.lj_map_female_unselect);
				myContext.gMaleSelect.setImageResource(R.drawable.lj_map_male_unselect);
			}
		}
	};
	
	public shakeHandler(ActivityShake context) 
	{ 
		myContext = context;
	} 
	
	private void locatePotin(LatLng cenpt)
	{
		final LatLng currentPoint = myContext.baiduMap.getMapStatus().target;
		final double deltaLat = (cenpt.latitude - currentPoint.latitude) / 10;
		final double deltaLng = (cenpt.longitude - currentPoint.longitude) / 10;
		new Thread()
		{
			public void run() 
			{
				for (int i = 0;i < 10; i++)
				{
					LatLng point = new LatLng(currentPoint.latitude + i * deltaLat, currentPoint.longitude + i * deltaLng);
					MapStatus mMapStatus = new MapStatus.Builder().target(point).zoom(ConstantValues.InstructionCode.MAP_ZOOM_INITIALIZATION).build();
					MapStatusUpdate mMapStatusUpdate = MapStatusUpdateFactory.newMapStatus(mMapStatus);
					myContext.baiduMap.setMapStatus(mMapStatusUpdate);
					SystemClock.sleep(50);
				}
			};
		}.start();
	}
	
	private void locateMyLocation(UserShakeData userShakeData)
	{
		LatLng cenpt = new LatLng(userShakeData.getLatitude() ,userShakeData.getLongitude());
		locatePotin(cenpt);
		BitmapDescriptor markIcon = BitmapDescriptorFactory.fromResource(R.drawable.lj_map_my_location);
		OverlayOptions option = new MarkerOptions().position(cenpt).icon(markIcon);  
		myContext.baiduMap.addOverlay(option);
	} 
	
	private Bitmap getBitmapFromView(View view) {  
        view.destroyDrawingCache();  
        view.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),  
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));  
        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());  
        view.setDrawingCacheEnabled(true);  
        Bitmap bitmap = view.getDrawingCache(true);  
        return bitmap;  
    }
	
	private void locateOtherLocation(UserShakeData userShakeData, int index)
	{ 
		LatLng cenpt = new LatLng(userShakeData.getLatitude() ,userShakeData.getLongitude());
		String sex = userShakeData.getSex(); 
		TextView view = new TextView(myContext);
		view.setTextColor(Color.WHITE);
		view.setTextSize(15);
		view.setGravity(Gravity.CENTER_HORIZONTAL); 
		view.setText(String.valueOf(index + 1));
		if (sex.equals("female"))
			view.setBackgroundResource(R.drawable.lj_map_female_location);
		else
			view.setBackgroundResource(R.drawable.lj_map_male_location);
		BitmapDescriptor markIcon = BitmapDescriptorFactory.fromBitmap(getBitmapFromView(view));
		OverlayOptions option = new MarkerOptions().position(cenpt).icon(markIcon);
		Marker marker = (Marker)myContext.baiduMap.addOverlay(option);
		Bundle bundle = new Bundle();
		bundle.putSerializable("index", index);
		marker.setExtraInfo(bundle);
	}
	
	private void locateUsers(ArrayList<UserShakeData> userShakeDataList)
	{
		UserShakeData temp = null;
		for (int i = 0; i < userShakeDataList.size(); i++)
		{
			UserShakeData userShakeData = userShakeDataList.get(i);
			if (userShakeData.getUserId() == ConstantValues.user.getID())
			{
				locateMyLocation(userShakeData);
				temp = userShakeData;
				break;
			}
		}
		if (temp != null)
			userShakeDataList.remove(temp);
		for (int i = 0; i < userShakeDataList.size(); i++)
		{
			UserShakeData userShakeData = userShakeDataList.get(i);
			locateOtherLocation(userShakeData, i);	
		}
		myContext.userDataListView.initView(myContext, myContext.dpiWidth, userShakeDataList, this);
	}
	
	private void initUserShakeData()
	{
		gUserShakeDataListFemale = new ArrayList<UserShakeData>();
		gUserShakeDataListMale = new ArrayList<UserShakeData>();
		for (int i = 0; i < gUserShakeDataList.size(); i++)
		{
			UserShakeData temp = gUserShakeDataList.get(i);
			if (temp.getSex().equals("male"))
				gUserShakeDataListMale.add(temp);
			else
				gUserShakeDataListFemale.add(temp);
		}
	}
	
	@Override
	public void handleMessage(Message msg) 
	{
		super.handleMessage(msg);
		switch (msg.what)
		{
		case ConstantValues.InstructionCode.ERROR_NETWORK:
			Toast.makeText(myContext, "网络连接失败", Toast.LENGTH_LONG).show();
			break;
		case ConstantValues.InstructionCode.SHAKE_HANDLER_USER_GAME_NOT_SET:
			Toast.makeText(myContext, "请先设置自己的解密游戏设置", Toast.LENGTH_LONG).show();
			break;
		case ConstantValues.InstructionCode.HANDLER_WAIT_FOR_DATA:
			myContext.sensorManager.unregisterListener(myContext.shakelistener);
			myContext.locationClient.start();
			break;
		case ConstantValues.InstructionCode.HANDLER_SUCCESS_GET_DATA:
			gUserShakeDataList = (ArrayList<UserShakeData>) msg.obj;
			if (gUserShakeDataList.size() == 1)
			{
				Toast.makeText(myContext, "很抱歉您的附近没有其他用户。", Toast.LENGTH_SHORT).show();
				return;
			}
			myContext.gShakeBall.startZoominAnimation();
			break;
		case ConstantValues.InstructionCode.SHAKE_HANDLER_MAP_SHOW:
			myContext.gShakeBall.setVisibility(View.GONE);
			myContext.gShakeBallLayout.setVisibility(View.GONE);
			myContext.mapView.setVisibility(View.VISIBLE);
			myContext.userDataListView.setVisibility(View.VISIBLE);
			myContext.userDataListView.bringToFront();
			myContext.gFemaleSelect.bringToFront();
			myContext.gMaleSelect.bringToFront();
			myContext.findViewById(R.id.lj_map_linear).bringToFront();
			myContext.gFemaleSelect.setOnClickListener(gSexClickListener);
			myContext.gMaleSelect.setOnClickListener(gSexClickListener);
			myContext.findViewById(R.id.lj_map_male_female_layout).bringToFront();
			myContext.findViewById(R.id.lj_map_male_female_layout).setVisibility(View.VISIBLE);
			locateUsers(gUserShakeDataList);
			initUserShakeData();
			break;
		case ConstantValues.InstructionCode.SHAKE_HANDLER_SHAKE_SENSOR:
			myContext.sensorManager.unregisterListener(myContext.shakelistener);
			myContext.vibrator.vibrate(ConstantValues.InstructionCode.VIBRATE_TIME);
			float[] values = (float[]) msg.obj;
			float x = values[0];
			float y = values[1];
			float z =  values[2];
			myContext.gShakeBall.setRotateAnimation(Math.abs((int)(10000 / z * 2)));
			myContext.gShakeBall.setVelocity((int)(x * 1.5) , (int)(y * 1.5));
			myContext.gShakeBall.setStartRotate(true);
			break;
		case ConstantValues.InstructionCode.SHAKE_HANDLER_MARKER_CLICK:
			Marker marker = (Marker) msg.obj;
			Bundle data = marker.getExtraInfo(); 
			if (data == null)
				return;
			int index = data.getInt("index");
			myContext.userDataListView.setDataOfIndex(index);
			UserShakeData userShakeData = null;//gUserShakeDataList.get(indexs);
			if (gSexStatus == SEX_MALE)
				userShakeData = gUserShakeDataListMale.get(index);
			else if (gSexStatus == SEX_FEMALE)
				userShakeData = gUserShakeDataListFemale.get(index);
			else
				userShakeData = gUserShakeDataList.get(index);
			LatLng cenpt = new LatLng(userShakeData.getLatitude() ,userShakeData.getLongitude());
			locatePotin(cenpt);
			break;
		case ConstantValues.InstructionCode.SHAKE_HANDLER_GAME:
			int id = msg.arg1;
			int type = msg.arg2;
			Intent intent = new Intent();
			switch (type)
			{
			case 1:
				intent.setClass(myContext, ActivityEightPuzzleGame.class);
				intent.putExtra("userID", id);
				intent.putExtra("nickname", msg.obj.toString());
				break;
			case 2:
				intent.setClass(myContext, ActivitySongPuzzle.class);
				intent.putExtra("userID", id);
				break;
			case 3:
				if (!CommonUtil.isSdkVersionValid())
				{
					Toast.makeText(myContext, "此游戏类型暂时仅对Android 4.4及以上用户开放", Toast.LENGTH_SHORT).show();
					return;
				}
				intent.setClass(myContext, ActivityBazingaBall.class);
				intent.putExtra("userID", id);
				break;
			}
			myContext.startActivityForResult(intent, ActivityShake.REQUEST_CODE_BEGINGAME);
			break;
		case ConstantValues.InstructionCode.SHAKE_HANDLER_CHANGE_MARK:
			int indexs = msg.arg1;
			UserShakeData userShakeDatas = null;//gUserShakeDataList.get(indexs);
			if (gSexStatus == SEX_MALE)
				userShakeDatas = gUserShakeDataListMale.get(indexs);
			else if (gSexStatus == SEX_FEMALE)
				userShakeDatas = gUserShakeDataListFemale.get(indexs);
			else
				userShakeDatas = gUserShakeDataList.get(indexs);
			LatLng cenpts = new LatLng(userShakeDatas.getLatitude() ,userShakeDatas.getLongitude());
			locatePotin(cenpts);
			break;
		case ConstantValues.InstructionCode.SHAKE_HANDLER_COLLISION:
			myContext.vibrator.vibrate(ConstantValues.InstructionCode.VIBRATE_TIME);
			break;
		}
	}
}

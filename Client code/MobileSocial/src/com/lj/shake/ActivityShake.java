package com.lj.shake;

import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapView;
import com.example.testmobiledatabase.R;
import com.yg.commons.ConstantValues;
import com.yg.guide.manager.GuideComplete;
import com.yg.guide.manager.UserGuide;
import com.yg.guide.tourguide.Overlay;
import com.yg.ui.dialog.DialogActivity;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.Display;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ActivityShake extends Activity
{
	public int dpiWidth;
	public int dpiHight;
	
	public static final int REQUEST_CODE_BEGINGAME = 0x20;
	public static final int RESULT_CODE_FRIENDADD = 0x21;
	
	public SensorManager sensorManager;
	public LocationClient locationClient = null;
	public Vibrator vibrator;
	
	public RelativeLayout mainLayout;
	public MapView mapView;
	public BaiduMap baiduMap;
	
	public ImageView gMaleSelect;
	public ImageView gFemaleSelect;
	
	public shakeListener shakelistener;
	public locationListener locationlistener;
	public markerClickListener markerclicklistener;
	
	private shakeHandler myHandler;
	
	public LinearLayout gShakeBallLayout;
	public ShakeBall gShakeBall;
	
	private boolean flag = false;
	
	public RelativeLayoutUserInfoList userDataListView;
	
	private void initDpi()
	{
		Display display = getWindowManager().getDefaultDisplay();
		dpiWidth = display.getWidth();
		dpiHight = display.getHeight();
	}
	
	private void initView()
	{
        gShakeBallLayout = (LinearLayout)findViewById(R.id.lj_shake_ball_layout);

        mapView = new MapView(this);
		baiduMap = mapView.getMap();
		mapView.setVisibility(View.INVISIBLE);
		mainLayout.addView(mapView);
		
		userDataListView = (RelativeLayoutUserInfoList)findViewById(R.id.lj_map_userinfo);
		
		gMaleSelect = (ImageView)findViewById(R.id.lj_map_male);
		gFemaleSelect = (ImageView)findViewById(R.id.lj_map_female);
	}
	
	private void initListener()
	{
		myHandler = new shakeHandler(this);
		shakelistener = new shakeListener(myHandler, ConstantValues.user.getID());
		sensorManager = (SensorManager) this.getSystemService(Service.SENSOR_SERVICE); 
		sensorManager.registerListener(shakelistener, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);  
		vibrator = (Vibrator)getSystemService(Context.VIBRATOR_SERVICE);  
		
		locationlistener = new locationListener(myHandler, ConstantValues.user.getID());
		LocationClientOption option = new LocationClientOption();
		option.setCoorType("bd09ll");
		locationClient = new LocationClient(getApplicationContext()); 
		locationClient.setLocOption(option);
        locationClient.registerLocationListener(locationlistener);
        
        markerclicklistener = new markerClickListener(myHandler);
        baiduMap.setOnMarkerClickListener(markerclicklistener);
        gShakeBall.setHandler(myHandler);
	}
	
	private UserGuide userGuide;
	
	private void setupUserGuide()
	{
		userGuide = new UserGuide(this, "�� �� ��", "ҡһҡ", Gravity.CENTER, Overlay.Style.Circle, "#CC3399");
		userGuide.beginWith(gShakeBall, true, new GuideComplete()
		{
			@Override
			public void onUserGuideCompleted()
			{
				initListener();
				UserGuide.disableUserGuide(UserGuide.SHAKE_ACTIVITY);
			}
		});
	}
	
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        SDKInitializer.initialize(getApplicationContext());
        setContentView(R.layout.lj_layout_shake);
        mainLayout = (RelativeLayout)findViewById(R.id.lj_shake_layout);
        initDpi();
        initView();
        setupDialogActionBar();
        
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) 
    {
    	super.onWindowFocusChanged(hasFocus);
    	if (hasFocus && !flag)
    	{
    		gShakeBall = (ShakeBall)findViewById(R.id.lj_shake_ball);
    		gShakeBall.setMoveRange(mainLayout.getWidth(), mainLayout.getHeight());
    		flag = true;
    		if (UserGuide.isNeedUserGuide(ActivityShake.this, UserGuide.SHAKE_ACTIVITY))
    			setupUserGuide();
    		else
    			initListener();
    	}
    }
    
    private void setupDialogActionBar()
	{
		getActionBar().setBackgroundDrawable(new ColorDrawable(Color.rgb(0x1E, 0x90, 0xFF)));
		getActionBar().setDisplayShowHomeEnabled(false);
		getActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM); 
		getActionBar().setCustomView(R.layout.lj_common_actionbar);
	
		LinearLayout back = (LinearLayout)findViewById(R.id.lj_common_actionbar_back);
		TextView titleTextView = (TextView)findViewById(R.id.lj_common_actionbar_title);
		titleTextView.setText("ҡһҡ");
		back.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				sensorManager.unregisterListener(shakelistener);
				finish();
			}
		});
	}
    
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) 
    {
    	if (keyCode == KeyEvent.KEYCODE_BACK)
    	{
    		sensorManager.unregisterListener(shakelistener);
    		finish();
    	}
    	return super.onKeyDown(keyCode, event); 
    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) 
    {
    	super.onActivityResult(requestCode, resultCode, data);
    	if (requestCode == REQUEST_CODE_BEGINGAME && resultCode == RESULT_CODE_FRIENDADD)
    		finish();
    }
}

package com.lj.shake;

import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapView;
import com.example.testmobiledatabase.R;
import com.lj.customview.LoadingView;
import com.lj.customview.ViewGrougTest;
import com.yg.commons.ConstantValues;
import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;

public class ActivityShake extends Activity
{
	private int dpiWidth;
	private int dpiHight;
	
	public SensorManager sensorManager;
	public LocationClient locationClient = null;
	public Vibrator vibrator;
	
	public RelativeLayout mainLayout;
	public LoadingView loadingView;
	public MapView mapView;
	public BaiduMap baiduMap;
	public ViewGrougTest userDataWindowView;
	
	public shakeListener shakelistener;
	public locationListener locationlistener;
	public markerClickListener markerclicklistener;
	public mapStatusChangeListener mapstatuslistener;
	public mapTouchListener maptouchlistener;
	
	shakeHandler myHandler;
	
	private void initDpi()
	{
		Display display = getWindowManager().getDefaultDisplay();
		dpiWidth = display.getWidth();
		dpiHight = display.getHeight();
	}
	
	private void initView()
	{
		//laytou
		mainLayout = new RelativeLayout(this);
		//载入等待动画
		loadingView = new LoadingView(this, dpiWidth >> 1, dpiHight >> 1);
        loadingView.setVisibility(View.INVISIBLE);
        mainLayout.addView(loadingView);
        //百度地图
		mapView = new MapView(this);
		baiduMap = mapView.getMap();
		mapView.setVisibility(View.INVISIBLE);
		mainLayout.addView(mapView);
	}
	
	private void initListener()
	{
		myHandler = new shakeHandler(this);
		//传感器
		shakelistener = new shakeListener(myHandler, ConstantValues.user.getID());
		sensorManager = (SensorManager) this.getSystemService(Service.SENSOR_SERVICE); 
		sensorManager.registerListener(shakelistener, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);  
		vibrator = (Vibrator)getSystemService(Context.VIBRATOR_SERVICE);  
		
		//位置监听
		locationlistener = new locationListener(myHandler, ConstantValues.user.getID());
		LocationClientOption option = new LocationClientOption();
		option.setCoorType("bd09ll");
		locationClient = new LocationClient(getApplicationContext()); 
		locationClient.setLocOption(option);
        locationClient.registerLocationListener(locationlistener);
        
        //百度地图
        markerclicklistener = new markerClickListener(myHandler);
        mapstatuslistener = new mapStatusChangeListener(myHandler);
        maptouchlistener = new mapTouchListener(myHandler);
        baiduMap.setOnMarkerClickListener(markerclicklistener);
        baiduMap.setOnMapTouchListener(maptouchlistener);
        baiduMap.setOnMapStatusChangeListener(mapstatuslistener);
	}
	
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        SDKInitializer.initialize(getApplicationContext());
        initDpi();
        initView();
        initListener();
		setContentView(mainLayout);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) 
    {
        int id = item.getItemId();
        if (id == R.id.action_settings)
            return true;
        return super.onOptionsItemSelected(item);
    }
}

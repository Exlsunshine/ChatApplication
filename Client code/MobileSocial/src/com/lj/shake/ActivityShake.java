package com.lj.shake;

import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapView;
import com.example.testmobiledatabase.R;
import com.lj.customview.LoadingView;
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
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;

public class ActivityShake extends Activity
{
	public int dpiWidth;
	public int dpiHight;
	
	public SensorManager sensorManager;
	public LocationClient locationClient = null;
	public Vibrator vibrator;
	
	public RelativeLayout mainLayout;
	public LoadingView loadingView;
	public MapView mapView;
	public BaiduMap baiduMap;
	
	public ImageView gMaleSelect;
	public ImageView gFemaleSelect;
	
	public shakeListener shakelistener;
	public locationListener locationlistener;
	public markerClickListener markerclicklistener;
	public mapStatusChangeListener mapstatuslistener;
	public mapTouchListener maptouchlistener;
	
	shakeHandler myHandler;
	
	public RelativeLayoutUserInfoList userDataListView;
	
	private void initDpi()
	{
		Display display = getWindowManager().getDefaultDisplay();
		dpiWidth = display.getWidth();
		dpiHight = display.getHeight();
	}
	
	private void initView()
	{
		//laytou
	//	mainLayout = new RelativeLayout(this);

		loadingView = new LoadingView(this, dpiWidth >> 1, dpiHight >> 1);
        loadingView.setVisibility(View.INVISIBLE);
        mainLayout.addView(loadingView);

        mapView = new MapView(this);
		baiduMap = mapView.getMap();
		mapView.setVisibility(View.INVISIBLE);
		mainLayout.addView(mapView);
		
		userDataListView = (RelativeLayoutUserInfoList)findViewById(R.id.lj_map_userinfo);
		
		gMaleSelect = (ImageView)findViewById(R.id.lj_map_male);
		gFemaleSelect = (ImageView)findViewById(R.id.lj_map_female);
		
/*		userDataListView = new RelativeLayoutUserInfoList(this);
		RelativeLayout.LayoutParams layout = new LayoutParams(LayoutParams.FILL_PARENT, 100);
		layout.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
		layout.bottomMargin = 10;
		userDataListView.setLayoutParams(layout);
		mainLayout.addView(userDataListView);*/
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
        setContentView(R.layout.lj_layout_shake);
        mainLayout = (RelativeLayout)findViewById(R.id.lj_shake_layout);
        initDpi();
        initView();
        initListener();
	//	setContentView(mainLayout);
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

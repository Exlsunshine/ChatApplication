package com.lj.setting.userinfo;

import com.example.testmobiledatabase.R;
import com.lj.datapicker.ArrayWheelAdapter;
import com.lj.datapicker.OnWheelChangedListener;
import com.lj.datapicker.WheelView;
import com.yg.commons.ConstantValues;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ActivityHometownSetting extends Activity
{
	private WheelView gPickerProvience = null;
	private WheelView gPickerCity = null;
	private WheelView gPickerDistrict = null;
	
	private TextView gHometownText = null;
	
	private String[] gProviences = null;
	private String[] gCitys = null;
	private String[] gDistrict = null;
	
	private String generateHometown(String provience, String city, String district)
	{
		return provience + " " + city + " " + district;
	}
	
	private OnWheelChangedListener gHometownChangeListener = new OnWheelChangedListener() 
	{
		@Override
		public void onChanged(WheelView wheel, int oldValue, int newValue) 
		{
			int id = wheel.getId();
			if (id == gPickerProvience.getId())
			{
				int provienceIndex = gPickerProvience.getCurrentItem();
		//		gHometownText.setText(generateHometown(gProviences[provienceIndex], "---", "---"));
				new ThreadGetCityArray(gHandler, gProviences[provienceIndex]).start();
			}
			else if (id == gPickerCity.getId())
			{
				int cityIndex = gPickerCity.getCurrentItem();
				int provienceIndex = gPickerProvience.getCurrentItem();
	//			gHometownText.setText(generateHometown(gProviences[provienceIndex], gCitys[cityIndex], "---"));
				new ThreadGetDistrictArray(gHandler, gProviences[provienceIndex], gCitys[cityIndex]).start();
			}
			else if (id == gPickerDistrict.getId())
			{
				int cityIndex = gPickerCity.getCurrentItem();
				int provienceIndex = gPickerProvience.getCurrentItem();
				int districtIndex = gPickerDistrict.getCurrentItem();
				gHometownText.setText(generateHometown(gProviences[provienceIndex], gCitys[cityIndex], gDistrict[districtIndex]));
			}
		}
	};
	
	private int getIndexOfArray(String str, String[] array)
	{
		if (str.equals("---"))
			return 0;
		for (int i = 0; i < array.length; i++)
			if (str.equals(array[i]))
				return i;
		return 0;
	}
	
	private Handler gHandler = new Handler()
	{
		public void handleMessage(android.os.Message msg) 
		{
			if (msg.what == ConstantValues.InstructionCode.USERSET_HANDLER_PROVINCE)
			{
				gProviences = (String[]) msg.obj;
				gPickerProvience.setAdapter(new ArrayWheelAdapter<String>(gProviences));
				String provience = gHometownText.getText().toString().split(" ")[0];
				int index = getIndexOfArray(provience, gProviences);
				gPickerProvience.setCurrentItem(index);
				new ThreadGetCityArray(gHandler, provience).start();
			}
			if (msg.what == ConstantValues.InstructionCode.USERSET_HANDLER_CITY)
			{
				gCitys = (String[]) msg.obj;
				gPickerCity.setAdapter(new ArrayWheelAdapter<String>(gCitys));
				String city = gHometownText.getText().toString().split(" ")[1];
				int index = getIndexOfArray(city, gCitys);
				gPickerCity.setCurrentItem(index);
				new ThreadGetDistrictArray(gHandler, gProviences[gPickerProvience.getCurrentItem()], gCitys[index]).start();
			}
			if (msg.what == ConstantValues.InstructionCode.USERSET_HANDLER_DISTRICT)
			{
				gDistrict = (String[]) msg.obj;
				gPickerDistrict.setAdapter(new ArrayWheelAdapter<String>(gDistrict));
				String district = gHometownText.getText().toString().split(" ")[2];
				int index = getIndexOfArray(district, gDistrict);
				gPickerDistrict.setCurrentItem(index);
				int cityIndex = gPickerCity.getCurrentItem();
				int provienceIndex = gPickerProvience.getCurrentItem();
				int districtIndex = gPickerDistrict.getCurrentItem();
				gHometownText.setText(generateHometown(gProviences[provienceIndex], gCitys[cityIndex], gDistrict[districtIndex]));
			}
		};
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.lj_setting_hometown_layout);
		
		gPickerProvience = (WheelView) findViewById(R.id.lj_picker_hometown_provience);
		gPickerCity = (WheelView) findViewById(R.id.lj_picker_hometown_city);
		gPickerDistrict = (WheelView) findViewById(R.id.lj_picker_hometown_district);
		
		gPickerProvience.addChangingListener(gHometownChangeListener);
		gPickerCity.addChangingListener(gHometownChangeListener);
		gPickerDistrict.addChangingListener(gHometownChangeListener);
		
		gHometownText = (TextView) findViewById(R.id.lj_hometown_setting_hometown);
		
		Intent intent = getIntent();
		String hometown = intent.getStringExtra("hometown");
		gHometownText.setText(hometown);
		new ThreadGetProvinceArray(gHandler).start();
		setupDialogActionBar();
	}
	
	private void setupDialogActionBar()
	{
		getActionBar().setBackgroundDrawable(new ColorDrawable(Color.rgb(0x1E, 0x90, 0xFF)));
		getActionBar().setDisplayShowHomeEnabled(false);
		getActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM); 
		getActionBar().setCustomView(R.layout.lj_common_actionbar);
	
		LinearLayout back = (LinearLayout)findViewById(R.id.lj_common_actionbar_back);
		TextView titleText = (TextView)findViewById(R.id.lj_common_actionbar_title);
		titleText.setText("¹ÊÏç");
		back.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				Intent intent = new Intent();  
				intent.putExtra("hometown", gHometownText.getText().toString());
				setResult(FragmentUserInfoSetting.ACTIVITY_RESULT_CODE_HOMETOWN, intent);  
				finish();
			}
		});
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) 
	{
		if (keyCode == KeyEvent.KEYCODE_BACK)
		{
			Intent intent = new Intent();  
			intent.putExtra("hometown", gHometownText.getText().toString());
			setResult(FragmentUserInfoSetting.ACTIVITY_RESULT_CODE_HOMETOWN, intent);  
			finish();
		}
		return super.onKeyDown(keyCode, event);
	}
}

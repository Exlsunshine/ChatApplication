package com.lj.setting.userinfo;

import java.text.SimpleDateFormat;

import com.example.testmobiledatabase.R;
import com.lj.datapicker.NumericWheelAdapter;
import com.lj.datapicker.OnWheelChangedListener;
import com.lj.datapicker.WheelView;
import com.yg.commons.CommonUtil;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ActivityAgeSetting extends Activity
{
	private WheelView gPickerYear = null;
	private WheelView gPickerMonth = null;
	private WheelView gPickerDay = null;
	
	private TextView gAgeText = null;
	private TextView gConstellationText = null;
	
	private final int BEGIN_YEAR = 1985;
	
	private String generateStringDate(int year, int month, int day)
	{
		String str = year + "-";
		if (month < 10)
			str += "0" + month + "-";
		else
			str += month + "-";
		if (day < 10)
			str += "0" + day;
		else
			str += day;
		return str;
	}
	
	private OnWheelChangedListener gAgeChangeListener = new OnWheelChangedListener() 
	{
		@Override
		public void onChanged(WheelView wheel, int oldValue, int newValue) 
		{
			int id = wheel.getId();
			int year = gPickerYear.getCurrentItem() + BEGIN_YEAR;
			int month = gPickerMonth.getCurrentItem() + 1;
			int day = gPickerDay.getCurrentItem() + 1;
			if (id == R.id.lj_picker_date_year) 
				gPickerDay.setAdapter(new NumericWheelAdapter(1, getDayNum(year, month)));
			else if (id == R.id.lj_picker_date_month)
				gPickerDay.setAdapter(new NumericWheelAdapter(1, getDayNum(year, month)));
			int nums = gPickerDay.getAdapter().getItemsCount();
			if (day > nums)
			{
				gPickerDay.setCurrentItem(nums - 1);
				day = nums;
			}
			gAgeText.setText(generateStringDate(year, month, day));
			gConstellationText.setText(CommonUtil.getConstellation(month, day));
		}
	};

	private boolean isLeapYear(int year)
	{
		if (year % 400 == 0 || (year % 4 == 0 && year % 100 != 0))
			return true;
		else
			return false;
	}
	
	private int getDayNum(int year, int month)
	{
		switch (month)
		{
		case 1 : return 31;
		case 3 : return 31;
		case 4 : return 30;
		case 5 : return 31;
		case 6 : return 30;
		case 7 : return 31;
		case 8 : return 31;
		case 9 : return 30;
		case 10 : return 31;
		case 11 : return 30;
		case 12 : return 31;
		}
		if (isLeapYear(year))
			return 29;
		else
			return 28;
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.lj_setting_age_layout);
		
		gPickerYear = (WheelView) findViewById(R.id.lj_picker_date_year);
		gPickerMonth = (WheelView) findViewById(R.id.lj_picker_date_month);
		gPickerDay = (WheelView) findViewById(R.id.lj_picker_date_day);
		
		gAgeText = (TextView) findViewById(R.id.lj_age_setting_age);
		gConstellationText = (TextView ) findViewById(R.id.lj_age_setting_constellation);
		
		Intent intent = getIntent();
		String date = intent.getStringExtra("date");
		String[] str = date.split("-");
		int year = Integer.valueOf(str[0]);
		int month = Integer.valueOf(str[1]);
		int day = Integer.valueOf(str[2]);
		
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy"); 
		String endYear = sdf.format(new java.util.Date());
		gPickerYear.setAdapter(new NumericWheelAdapter(BEGIN_YEAR, Integer.valueOf(endYear)));
		gPickerYear.setLabel("年");
		gPickerYear.setCurrentItem(year - BEGIN_YEAR);
		
		gPickerMonth.setAdapter(new NumericWheelAdapter(1, 12));
		gPickerMonth.setLabel("月");
		gPickerMonth.setCurrentItem(month - 1);
		
		gPickerDay.setAdapter(new NumericWheelAdapter(1, getDayNum(year, month)));
		gPickerDay.setLabel("日");
		gPickerDay.setCurrentItem(day - 1);
		
		gPickerYear.addChangingListener(gAgeChangeListener);
		gPickerMonth.addChangingListener(gAgeChangeListener);
		gPickerDay.addChangingListener(gAgeChangeListener);
		
		gAgeText.setText(date);
		gConstellationText.setText(CommonUtil.getConstellation(month, day));
		
		setupDialogActionBar();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) 
	{
		if (keyCode == KeyEvent.KEYCODE_BACK)
		{
			Intent intent = new Intent();  
			intent.putExtra("date", gAgeText.getText().toString());
			setResult(FragmentUserInfoSetting.ACTIVITY_RESULT_CODE_AGE, intent);  
			finish();
		}
		return super.onKeyDown(keyCode, event);
	}

	private void setupDialogActionBar()
	{
		getActionBar().setBackgroundDrawable(new ColorDrawable(Color.rgb(0x1E, 0x90, 0xFF)));
		getActionBar().setDisplayShowHomeEnabled(false);
		getActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM); 
		getActionBar().setCustomView(R.layout.lj_common_actionbar);
	
		LinearLayout back = (LinearLayout)findViewById(R.id.lj_common_actionbar_back);
		TextView titleText = (TextView)findViewById(R.id.lj_common_actionbar_title);
		titleText.setText("生日");
		back.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				Intent intent = new Intent();  
				intent.putExtra("date", gAgeText.getText().toString());
				setResult(FragmentUserInfoSetting.ACTIVITY_RESULT_CODE_AGE, intent);  
				finish();
			}
		});
	}
}

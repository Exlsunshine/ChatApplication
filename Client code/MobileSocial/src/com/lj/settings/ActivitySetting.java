package com.lj.settings;

import com.example.testmobiledatabase.R;
import com.lj.gameSetting.ActivityGameSetting;
import com.lj.userbasicsetting.ActivityUserBasicSetting;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View.OnClickListener;
import android.view.View;
import android.widget.Button;

public class ActivitySetting extends Activity
{
	Button gamesetBtn;
	Button usersetBtn;
	Button syssetBtn;
	Button lookBtn;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.lj_layout_set);
		
		gamesetBtn = (Button)findViewById(R.id.lj_gameset);
		usersetBtn = (Button)findViewById(R.id.lj_userset);
		syssetBtn = (Button)findViewById(R.id.lj_sysset);
		lookBtn = (Button)findViewById(R.id.lj_look);
		
		gamesetBtn.setOnClickListener(new OnClickListener() 
		{
			@Override
			public void onClick(View arg0) 
			{
				Intent intent = new Intent();
				intent.setClass(ActivitySetting.this, ActivityGameSetting.class);
				startActivity(intent);
			}
		});
		
		usersetBtn.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View arg0) 
			{
				Intent intent = new Intent();
				intent.setClass(ActivitySetting.this, ActivityUserBasicSetting.class);
				startActivity(intent);
			}
		});
		
		syssetBtn.setOnClickListener(new OnClickListener() 
		{
			@Override
			public void onClick(View arg0) 
			{
			}
		});
		
		lookBtn.setOnClickListener(new OnClickListener() 
		{
			@Override
			public void onClick(View arg0) 
			{
			}
		});
	}
}

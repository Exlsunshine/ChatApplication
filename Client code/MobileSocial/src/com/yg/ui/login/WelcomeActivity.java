package com.yg.ui.login;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;

import com.example.testmobiledatabase.R;

public class WelcomeActivity extends Activity 
{
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.yg_welcome_activity);
		
		new Handler().postDelayed(new Runnable() 
		{
			@Override
			public void run() 
			{
				Intent intent = new Intent(WelcomeActivity.this, LoginGuideActivity.class);
				startActivity(intent);
				WelcomeActivity.this.finish();
			}
		}, 1000);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) 
	{
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
}
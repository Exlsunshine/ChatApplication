package com.yg.ui.login;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;

import com.example.testmobiledatabase.R;
import com.yg.ui.login.implementation.LoginInfo;

public class WelcomeActivity extends Activity 
{
	public static final String AUTO_LOGIN_FLAG = "audo_login_flag";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.yg_welcome_activity);
		
		LoginInfo loginInfo = new LoginInfo(WelcomeActivity.this);
		
		if (loginInfo.getLoginAccount() != null && loginInfo.getLoginPassword() != null)
		{
			Intent intent = new Intent(WelcomeActivity.this, LoginGuideActivity.class);
			intent.putExtra("index", 3);
			intent.putExtra(AUTO_LOGIN_FLAG, true);
			startActivity(intent);
			WelcomeActivity.this.finish();
		}
		else
		{
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
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) 
	{
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
}
package com.yg.ui;

import com.example.testmobiledatabase.R;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class FriendListActivity extends Activity 
{
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_friend_list);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) 
	{
		getMenuInflater().inflate(R.menu.friend_list, menu);
		return true;
	}
}

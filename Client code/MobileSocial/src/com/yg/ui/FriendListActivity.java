package com.yg.ui;

import com.example.testmobiledatabase.R;
import com.example.testmobiledatabase.R.layout;
import com.example.testmobiledatabase.R.menu;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class FriendListActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_friend_list);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.friend_list, menu);
		return true;
	}

}

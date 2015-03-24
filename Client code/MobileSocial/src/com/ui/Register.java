package com.ui;

import com.example.testmobiledatabase.R;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class Register extends Activity
{
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);

		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		getMenuInflater().inflate(R.menu.register, menu);
		return true;
	}
}
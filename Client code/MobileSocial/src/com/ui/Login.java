package com.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.testmobiledatabase.R;
import com.user.ClientUser;

public class Login extends Activity
{
	EditText password, loginAccount;
	Button login, register;
	TextView forget;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		
		initLayout();
		initClickListeners();
	}
	
	private void initClickListeners()
	{
		login.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View arg0)
			{
				if (loginAccount.getText().toString() == null)
				{
					Toast.makeText(Login.this, "Please specify login account first.", Toast.LENGTH_SHORT).show();
					return ;
				}
				if (password.getText().toString() == null)
				{
					Toast.makeText(Login.this, "Please specify password.", Toast.LENGTH_SHORT).show();
					return ;
				}
				
				int id = ClientUser.validateIdentity(loginAccount.getText().toString(), password.getText().toString());
				if (id != -1)
				{
					Toast.makeText(Login.this, "Ok!", Toast.LENGTH_SHORT).show();
				}
				else
				{
					Toast.makeText(Login.this, "Password or login account error.", Toast.LENGTH_SHORT).show();
				}
			}
		});
		
		register.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View arg0)
			{
			}
		});
		
		forget.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View arg0)
			{
				Intent it = new Intent(Login.this, Forget.class);
				startActivity(it);
			}
		});
	}
	
	private void initLayout()
	{
		password = (EditText)findViewById(R.id.password);
		loginAccount = (EditText)findViewById(R.id.login_account);
		login  = (Button)findViewById(R.id.login);
		register  = (Button)findViewById(R.id.register);
		forget  = (TextView)findViewById(R.id.forget);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		getMenuInflater().inflate(R.menu.login, menu);
		return true;
	}
}
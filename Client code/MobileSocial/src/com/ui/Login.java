package com.ui;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.testmobiledatabase.R;
import com.message.ConvertUtil;
import com.user.ClientUser;
import com.user.ImageTransportation;
import com.user.PackString;

public class Login extends Activity
{
	EditText password, loginAccount;
	Button login, register;
	TextView forget;
	ImageView portrait;
	
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
				String accountVal = loginAccount.getText().toString();
				String pwdVal = password.getText().toString();
				
				if (accountVal == null || accountVal.length() == 0)
				{
					Toast.makeText(Login.this, "Please specify login account first.", Toast.LENGTH_SHORT).show();
					return ;
				}
				if (pwdVal == null || pwdVal.length() == 0)
				{
					Toast.makeText(Login.this, "Please specify password.", Toast.LENGTH_SHORT).show();
					return ;
				}
				
				final String profile = ClientUser.validateIdentity(accountVal, pwdVal);
				if (profile != null)
				{
					Toast.makeText(Login.this, "Ok!", Toast.LENGTH_SHORT).show();
					Log.i("___________", profile);
					
					runOnUiThread(new Runnable()
					{
						@Override
						public void run()
						{
							PackString ps = new PackString(profile);
							ArrayList<HashMap<String, Object>> list = ps.jsonString2Arrylist("userProfile");
							portrait.setBackground(ConvertUtil.bitmap2Drawable(
											ImageTransportation.string2Bitmap((String) list.get(0).get("portrait")),
											Login.this));
						}
					});
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
				Intent it = new Intent(Login.this, Register.class);
				startActivity(it);
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
		portrait = (ImageView)findViewById(R.id.login_portrait);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		getMenuInflater().inflate(R.menu.login, menu);
		return true;
	}
}
package com.yg.ui;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.view.View.OnClickListener;

import com.example.testmobiledatabase.R;
import com.yg.user.WebServiceAPI;

public class Forget extends Activity 
{
	private Button confirm;
	private EditText email;
	private WebServiceAPI wsAPI = new WebServiceAPI("network.com", "NetworkHandler");
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_forget);
		
		setupActionBar();
		initLayout();
		initClickListeners();
	}
	
	private void initLayout()
	{
		confirm = (Button)findViewById(R.id.confirm);
		email = (EditText)findViewById(R.id.email);
	}
	
	private void initClickListeners()
	{
		confirm.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View arg0)
			{
				String emailAccount = email.getText().toString();
				if (emailAccount == null || emailAccount.length() == 0)
				{
					Toast.makeText(Forget.this, "Please specify your email.", Toast.LENGTH_SHORT).show();
					return;
				}
				else
				{
					String [] params = new String[1];
					Object [] vlaues = new Object[1];
					params[0] = "email";
					vlaues[0] = emailAccount;
					
					Object ret = wsAPI.callFuntion("sendResetPwdRequestMail", params, vlaues);
					int code = Integer.parseInt(ret.toString());
					if (code != 0)
						Toast.makeText(Forget.this, "This email has not been registered.", Toast.LENGTH_SHORT).show();
					else
						Toast.makeText(Forget.this, "Reset instruction has been sent to " + emailAccount, Toast.LENGTH_SHORT).show();
					
					Log.i("______", "Reset password status: " + String.valueOf(code));
				}
			}
		});
	}

	private void setupActionBar() 
	{
		ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setTitle("Login");
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) 
	{
		getMenuInflater().inflate(R.menu.forget, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		switch(item.getItemId())
		{
		case android.R.id.home:
			this.finish();
			break;
		default:
			break;
		}
		return true;
	}
}
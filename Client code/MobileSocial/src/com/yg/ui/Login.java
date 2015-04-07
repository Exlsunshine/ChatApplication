package com.yg.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import com.example.testmobiledatabase.R;

public class Login extends Activity
{
	//EditText password, loginAccount;
	//Button login;
	Button register;
	//TextView forget;
	//ImageView portrait;
	
	//Handler handler;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		
		initLayout();
		initClickListeners();
		
		/*handler = new Handler()
		{
			@Override
			public void handleMessage(Message msg)
			{
				super.handleMessage(msg);

				Bundle bundle = msg.getData();
				String profile = bundle.getString("profile");
				String pwdVal = bundle.getString("pwdVal");
				String accountVal = bundle.getString("accountVal");
				Log.i("___________", profile);
				
				PackString ps = new PackString(profile);
				ArrayList<HashMap<String, Object>> list = ps.jsonString2Arrylist("userProfile");

				int userID = Integer.parseInt((String) list.get(0).get("id"));
				String password = pwdVal;
				String loginAccount = accountVal;
				String nickName = (String) list.get(0).get("nick_name");
				String email = (String) list.get(0).get("email");
				byte [] portrait = ConvertUtil.bitmap2Bytes(ImageTransportation.string2Bitmap((String) list.get(0).get("portrait")));
				String sex = (String) list.get(0).get("sex");
				String birthday = (String) list.get(0).get("birthday");
				String phoneNumber = (String) list.get(0).get("phone_number");
				String hometown = (String) list.get(0).get("hometown");
				
				ConstantValues.user = new ClientUser(userID, password, loginAccount, nickName, email, portrait, sex, birthday, phoneNumber, hometown, null);
				ConstantValues.user.signin();
						
				Intent intent = new Intent(Login.this, MainActivity.class);
				startActivity(intent);
			}
		};*/
	}
	
	private void initClickListeners()
	{
		/*login.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View arg0)
			{			
				final String accountVal = loginAccount.getText().toString();
				final String pwdVal = password.getText().toString();
				
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
				
				Thread td = new Thread(new Runnable()
				{
					@Override
					public void run()
					{
						final String profile = ClientUser.validateIdentity(accountVal, pwdVal);
						if (profile != null)
						{
							Message msg = new Message();
							Bundle bundle = new Bundle();
							bundle.putString("profile", profile);
							bundle.putString("pwdVal", pwdVal);
							bundle.putString("accountVal", accountVal);
							msg.setData(bundle);
							
							handler.sendMessage(msg);
						}
						else
							Toast.makeText(Login.this, "Password or login account error.", Toast.LENGTH_SHORT).show();
					}
				});
				td.start();
			}
		});*/
		
		register.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View arg0)
			{
				Intent it = new Intent(Login.this, Register.class);
				startActivity(it);
			}
		});
		
		/*forget.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View arg0)
			{
				Intent it = new Intent(Login.this, Forget.class);
				startActivity(it);
			}
		});*/
	}
	
	private void initLayout()
	{
		//password = (EditText)findViewById(R.id.password);
		//loginAccount = (EditText)findViewById(R.id.login_account);
		//login  = (Button)findViewById(R.id.login);
		register  = (Button)findViewById(R.id.register);
		//forget  = (TextView)findViewById(R.id.forget);
		//portrait = (ImageView)findViewById(R.id.login_portrait);
		
		//loginAccount.setText("UserG@126.com");
		//password.setText("10");
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		getMenuInflater().inflate(R.menu.login, menu);
		return true;
	}
}
package com.lj.setting.userinfo;


import com.example.testmobiledatabase.R;
import com.yg.commons.ConstantValues;
import com.yg.user.ClientUser;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class ActivityPasswordSetting extends Activity
{
	private final int HANDLER_SHOW_WRONG_PASSWORD = 1;
	private final int HANDLER_SUCCESS_CHANGE_PASSWORD = 2;
	private EditText gOldPasswordEditText = null;
	private EditText gNewPasswordEditText = null;
	private ImageView gPasswordConfirm = null;
	private CheckBox gShowPasswordCheckBox = null;
	private TextView gPasswordWrongText = null;
	
	private Handler gHandler = new Handler()
	{
		public void handleMessage(android.os.Message msg) 
		{
			if (msg.what == HANDLER_SHOW_WRONG_PASSWORD)
				gPasswordWrongText.setVisibility(View.VISIBLE);
			else if (msg.what == HANDLER_SUCCESS_CHANGE_PASSWORD)
			{
				Toast.makeText(ActivityPasswordSetting.this, "密码修改成功", Toast.LENGTH_LONG).show();
				finish();
			}
		}
	};
	private OnClickListener gConfirmClickListener = new OnClickListener() 
	{
		@Override
		public void onClick(View v) 
		{
			
			//判断正误
			new Thread()
			{
				public void run() 
				{
					String oldPassword = gOldPasswordEditText.getText().toString();
					String newPassword = gNewPasswordEditText.getText().toString();
					if (ClientUser.validateIdentity(ConstantValues.user.getLoginAccount(), oldPassword) == null)
					{
						Message msg = new Message();
						msg.what = HANDLER_SHOW_WRONG_PASSWORD;
						gHandler.sendMessage(msg);
					}
					else
					{
						ConstantValues.user.setPassword(newPassword);
						Message msg = new Message();
						msg.what = HANDLER_SUCCESS_CHANGE_PASSWORD;
						gHandler.sendMessage(msg);
					}
				}
			}.start();
		}
	};
	
	private OnCheckedChangeListener gCheckBoxChangeListener = new OnCheckedChangeListener() 
	{
		@Override
		public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) 
		{
			if (isChecked)
			{
				gOldPasswordEditText.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
				gNewPasswordEditText.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
			}
			else
			{
				gOldPasswordEditText.setTransformationMethod(PasswordTransformationMethod.getInstance());
				gNewPasswordEditText.setTransformationMethod(PasswordTransformationMethod.getInstance());
			}
		}
	};
	
	
	@Override
	
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.lj_setting_password_layout);
		
		gOldPasswordEditText = (EditText)findViewById(R.id.lj_setting_password_oldpassword);
		gNewPasswordEditText = (EditText)findViewById(R.id.lj_setting_password_newpassword);
		gPasswordConfirm = (ImageView)findViewById(R.id.lj_setting_password_img);
		gShowPasswordCheckBox = (CheckBox)findViewById(R.id.lj_setting_password_checkbox);
		
		gShowPasswordCheckBox.setOnCheckedChangeListener(gCheckBoxChangeListener);
		gPasswordConfirm.setOnClickListener(gConfirmClickListener);
		
		gPasswordWrongText = (TextView)findViewById(R.id.lj_setting_password_wrong);
	}
}

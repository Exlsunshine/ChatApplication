package com.yg.ui.login.implementation;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceActivity;

public class LoginInfo
{
	private static final String LOGIN_ACCOUNT = "login_account";
	private static final String LOGIN_PASSWORD = "login_password";
	private static final String SHARED_PREFERENCE_NAME = "jmmsr_login_information";

	private SharedPreferences preference;
	private Context context;
	
	public LoginInfo(Context context)
	{
		this.context = context;
		preference = this.context.getSharedPreferences(SHARED_PREFERENCE_NAME, PreferenceActivity.MODE_PRIVATE);
	}
	
	public String getLoginAccount()
	{
		return preference.getString(LOGIN_ACCOUNT, null);
	}
	
	public String getLoginPassword()
	{
		return preference.getString(LOGIN_PASSWORD, null);
	}
	
	public void saveLoginAccount(String account)
	{
		preference.edit().putString(LOGIN_ACCOUNT, account).commit();
	}
	
	public void saveLoginPassword(String password)
	{
		preference.edit().putString(LOGIN_PASSWORD, password).commit();
	}
	
	public void logoff()
	{
		saveLoginAccount(null);
		saveLoginPassword(null);
	}
}
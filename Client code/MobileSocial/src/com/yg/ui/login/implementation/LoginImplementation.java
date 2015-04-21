package com.yg.ui.login.implementation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.yg.commons.ConstantValues;
import com.yg.user.ClientUser;
import com.yg.user.PackString;

public class LoginImplementation
{
	private static final String DEBUG_TAG = "LoginImplementation______";
	private String accountVal;
	private String pwdVal;
	private Handler handler;
	private Thread loginThread;
	
	public LoginImplementation(String accountVal, String pwdVal)
	{
		this.accountVal = accountVal;
		this.pwdVal = pwdVal;
		this.loginThread = new Thread(new Runnable()
		{
			@Override
			public void run() 
			{
				Log.i(DEBUG_TAG, "login success.");
			}
		});
		
		handler = new Handler()
		{
			@Override
			public void handleMessage(Message msg) 
			{
				Bundle bundle = msg.getData();
				int userID = bundle.getInt("userID");
				String password = bundle.getString("password");
				String loginAccount = bundle.getString("loginAccount");
				String nickName = bundle.getString("nickName");
				String email = bundle.getString("email");
				String portraitPath = bundle.getString("portraitPath");
				String sex = bundle.getString("sex");
				String birthday = bundle.getString("birthday");
				String phoneNumber = bundle.getString("phoneNumber");
				String hometown = bundle.getString("hometown");
				
				ConstantValues.user = new ClientUser(userID, password, loginAccount, nickName, email, portraitPath, sex, birthday, phoneNumber, hometown, null);
				ConstantValues.user.signin();
				loginThread.start();
				
				super.handleMessage(msg);
			}
		};
	}
	
	/**
	 * 尝试登陆服务器
	 * 
	 * @return 服务器处理结果：<br>
	 *         1 表示指定的邮箱为null或长度为零<br>
	 *         2 表示指定的密码为null或长度为零<br>
	 *         3 表示指定的邮箱和密码为null或长度为零<br>
	 *         4 表示邮箱与密码不匹配<br>
	 *         5 表示未知异常<br>
	 *         0 表示重置密码成功
	 */
	public int tryToLogin()
	{
		ExecutorService pool = Executors.newFixedThreadPool(1);
		Callable<Integer> callable = new SendRequestToServer();
		Future<Integer> future = pool.submit(callable);
		
		int result = 5;
		
		try {
			result = future.get();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
		try {
			loginThread.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		Log.i(DEBUG_TAG, "Result is : " + result);
		
		return result;
	}
	
	private class SendRequestToServer implements Callable<Integer>
	{
		/**
		 * 检查参数是否合法
		 * @return 	检查结果代码<br>
		 * 			1 表示表示指定的邮箱为null或长度为零<br>
		 * 			2 表示指定的密码为null或长度为零<br>
		 * 			3 表示指定的邮箱和密码为null或长度为零<br>
		 * 			0 表示合法
		 */
		private int checkValidation()
		{
			int status = 0;
			if (accountVal == null || accountVal.length() == 0)
				status = (status | 0x0001);
			if (pwdVal == null || pwdVal.length() == 0)
				status = (status | 0x0010);
			
			if (status == 0x0010)
				status = 2;
			else if (status == 0x0011)
				status = 3;
			
			return status;
		}
		
		@Override
		public Integer call() throws Exception
		{
			int status = checkValidation();
			
			if (status == 0)
			{
				String profile = ClientUser.validateIdentity(accountVal, pwdVal);
				if (profile != null)
				{
					PackString ps = new PackString(profile);
					ArrayList<HashMap<String, Object>> list = ps.jsonString2Arrylist("userProfile");
	
					int userID = Integer.parseInt((String) list.get(0).get("id"));
					String password = pwdVal;
					String loginAccount = accountVal;
					String nickName = (String) list.get(0).get("nick_name");
					String email = (String) list.get(0).get("email");
					String portraitPath = (String) list.get(0).get("portrait_path");
					String sex = (String) list.get(0).get("sex");
					String birthday = (String) list.get(0).get("birthday");
					String phoneNumber = (String) list.get(0).get("phone_number");
					String hometown = (String) list.get(0).get("hometown");
					
					Message msg = new Message();
					Bundle bundle = new Bundle();
					bundle.putInt("userID", userID);
					bundle.putString("password", password);
					bundle.putString("loginAccount", loginAccount);
					bundle.putString("nickName", nickName);
					bundle.putString("email", email);
					bundle.putString("portraitPath", portraitPath);
					bundle.putString("sex", sex);
					bundle.putString("birthday", birthday);
					bundle.putString("phoneNumber", phoneNumber);
					bundle.putString("hometown", hometown);
					msg.setData(bundle);
					handler.sendMessage(msg);
					
					status = 0;
				}
				else
					status = 4;//Toast.makeText(Login.this, "Password or login account error.", Toast.LENGTH_SHORT).show();
			}
			
			Log.i(DEBUG_TAG, "Status is : " + status);
			
			return status;
		}
	}
}
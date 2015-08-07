package com.yg.video;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

public class VideoAccountManager
{
	public final static String LOGIN_ACCOUNT = "login";
	public final static String LOGIN_PASSWORD = "password";

	private static VideoAccountManager videoManager = null;
	private ArrayList<VideoAccount> freeAccounts;
	private HashMap<String, VideoAccount> busyAccounts;

	public static VideoAccountManager getInstance()
	{
		if (videoManager == null)
			videoManager = new VideoAccountManager();

		return videoManager;
	}
	
	public synchronized String getAvailableAccount() throws JSONException
	{
		JSONObject obj = new JSONObject();

		if (isServiceAvailable())
		{
			VideoAccount user = freeAccounts.get(0);
			busyAccounts.put(user.getLogin(), user);
			freeAccounts.remove(0);

			obj.put(LOGIN_ACCOUNT, user.getLogin());
			obj.put(LOGIN_PASSWORD, user.getPassword());
		}
		
		System.out.println(new Date().toString() + "\tgetAvailableAccount()\tReturning\t" + obj.toString());
		return obj.toString();
	}

	public synchronized void logoffAccount(String accountInfo)
	{
		try
		{
			JSONObject obj = new JSONObject(accountInfo);
			String login = (String) obj.get(LOGIN_ACCOUNT);
			String password = (String) obj.get(LOGIN_PASSWORD);

			freeAccounts.add(busyAccounts.get(login));
			busyAccounts.remove(login);

			System.out.println(new Date().toString() + "\tlogoffAccount()\tLogoff for user:\t" + login);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	private VideoAccountManager()
	{
		freeAccounts = new ArrayList<VideoAccount>();
		loadFreeAccountData();
		
		busyAccounts = new HashMap<String, VideoAccount>();
	}
	
	private void loadFreeAccountData()
	{
		freeAccounts.add(new VideoAccount("userA", "userAPassword"));
		freeAccounts.add(new VideoAccount("userB", "userBPassword"));
	}

	private boolean isServiceAvailable()
	{
		if (freeAccounts.size() > 0)
			return true;
		else
			return false;
	}

	public static void main(String[] args)
	{
		/*try {
			String info = VideoManager.getInstance().getAvailableAccount();
			System.out.println(info);

			String info2 = VideoManager.getInstance().getAvailableAccount();
			System.out.println(info2);
			
			String info3 = VideoManager.getInstance().getAvailableAccount();
			System.out.println(info3);
			
			VideoManager.getInstance().logoffAccount(info);
			
			String info4 = VideoManager.getInstance().getAvailableAccount();
			System.out.println(info4);
		} catch (JSONException e) {
			e.printStackTrace();
		}*/
	}
}
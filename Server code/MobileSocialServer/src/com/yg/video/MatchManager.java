package com.yg.video;

import java.util.Date;
import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

public class MatchManager 
{
	private static final int SUCCESS = 0x01;
	private static final int ERROR = 0x02;
	
	private HashMap<String, VideoAccount> waitingList = null;
	private static MatchManager matchManager = null;
	
	public synchronized String getOnlineUser(String callerUserInfo)
	{
		JSONObject obj = new JSONObject();
		
		try
		{
			VideoAccount caller = stringToVideoAccount(callerUserInfo);
			
			if (!waitingList.containsKey(caller.getLogin()))
				acceptMatching(callerUserInfo);
			
			for (String key : waitingList.keySet())
			{
				if ((waitingList.get(key).getStatus() == VideoAccount.FREE) 
						&& !waitingList.get(key).getLogin().equals(caller.getLogin()))
				{
					waitingList.get(key).setStatus(VideoAccount.BUSY);
					waitingList.get(caller.getLogin()).setStatus(VideoAccount.BUSY);
					
					obj.put(VideoAccountManager.LOGIN_ACCOUNT, waitingList.get(key).getLogin());
					obj.put(VideoAccountManager.LOGIN_PASSWORD, waitingList.get(key).getPassword());
					break;
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		System.out.println(new Date().toString() + "\tgetOnlineUser()\tReturning\t" + obj.toString());
		
		return obj.toString();
	}
	
	public synchronized int setAsFree(String callerUserInfo)
	{
		try {
			VideoAccount acc = stringToVideoAccount(callerUserInfo);
			waitingList.get(acc.getLogin()).setStatus(VideoAccount.FREE);
			return SUCCESS;
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		return ERROR;
	} 
	
	public synchronized int acceptMatching(String callerUserInfo)
	{
		try {
			VideoAccount acc = stringToVideoAccount(callerUserInfo);
			waitingList.put(acc.getLogin(), acc);
			System.out.println(new Date().toString() + "\tacceptMatching()\tReturning\t" + "SUCCESS");
			return SUCCESS;
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		System.out.println(new Date().toString() + "\tacceptMatching()\tReturning\t" + "ERROR");
		return ERROR;
	}
	
	public synchronized int stopMatching(String callerUserInfo)
	{
		try {
			VideoAccount acc = stringToVideoAccount(callerUserInfo);
			waitingList.remove(acc.getLogin());
			System.out.println(new Date().toString() + "\tstopMatching()\tReturning\t" + "SUCCESS");
			return SUCCESS;
		} catch (JSONException e) {
			e.printStackTrace();
		}
	
		System.out.println(new Date().toString() + "\tstopMatching()\tReturning\t" + "ERROR");
		return ERROR;
	}
	
	private VideoAccount stringToVideoAccount(String userInfo) throws JSONException
	{
		JSONObject obj = new JSONObject(userInfo);
		String login = (String) obj.get(VideoAccountManager.LOGIN_ACCOUNT);
		String password = (String) obj.get(VideoAccountManager.LOGIN_PASSWORD);
		
		return new VideoAccount(login, password);
	}
	
	private MatchManager()
	{
		waitingList = new HashMap<String, VideoAccount>();
	}
	
	public static MatchManager getInstance()
	{
		if (matchManager == null)
			matchManager = new MatchManager();
		
		return matchManager;
	}
}
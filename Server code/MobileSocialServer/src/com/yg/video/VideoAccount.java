package com.yg.video;

public class VideoAccount 
{
	public static final int FREE = 0x01;
	public static final int BUSY = 0x02;
	
	private String login;
	private String password;
	private int status;

	public VideoAccount(String login, String password)
	{
		this.login = login;
		this.password = password;
		this.setStatus(FREE);
	}

	public String getLogin()
	{
		return login;
	}

	public String getPassword()
	{
		return password;
	}
	
	public int getStatus() 
	{
		return status;
	}

	public void setStatus(int status) 
	{
		this.status = status;
	}
	
	@Override
	public String toString()
	{
		return login + "\t" + password;
	}
}
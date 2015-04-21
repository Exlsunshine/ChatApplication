package com.yg.user;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

public abstract class AbstractUser
{
	private static final String DEBUG_TAG = "AbstractUser______";
	protected int id;
	protected String loginAccount;
	protected String nickName;
	protected String email;
	protected String phoneNumber;
	protected String sex;
	protected String birthday;
	protected byte[] portrait;
	protected String hometown;
	protected Bitmap portraitBmp;
	
	protected String portraitPath;
	
	public AbstractUser(int id, String loginAccount, String nickName, String email, String phoneNumber,
			String sex, String birthday, byte[] portrait, String hometown)
	{
		this.id = id;
		this.loginAccount = loginAccount;
		this.nickName = nickName;
		this.email = email;
		this.phoneNumber = phoneNumber;
		this.sex = sex;
		this.birthday = birthday;
		this.portrait = portrait;
		this.hometown = hometown;
		this.portraitBmp = null;
	}
	
	public AbstractUser(int id, String loginAccount, String nickName, String email, String phoneNumber,
			String sex, String birthday, String portraitPath, String hometown)
	{
		this.id = id;
		this.loginAccount = loginAccount;
		this.nickName = nickName;
		this.email = email;
		this.phoneNumber = phoneNumber;
		this.sex = sex;
		this.birthday = birthday;
		this.portraitPath = portraitPath;
		this.hometown = hometown;
		this.portraitBmp = null;
	}
	
	/**
	 * ��ȡ�û�ͷ��(Bitmap����)
	 * @return ͷ��(Bitmap����)
	 */
	public Bitmap getPortraitBmp()
	{
		if (portraitBmp == null)
		{
			DownloadThread download = new DownloadThread(portraitPath);
			download.start();
			
			synchronized (download) 
			{
				try {
					download.wait();
					portraitBmp = download.bitmap;
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
		
		return portraitBmp;
	}
	
	private class DownloadThread extends Thread
	{
		private Bitmap bitmap = null;
		private String url = null;
		
		public DownloadThread(String url)
		{
			this.url = url;
		}
		
		@Override
		public void run() 
		{
			super.run();
			synchronized (this) 
			{
				try 
				{
					BitmapFactory.Options options = new BitmapFactory.Options();
					options.inPurgeable = true;
					options.inInputShareable = true;
					options.inSampleSize = 2;
					
					Log.i(DEBUG_TAG, "Downloading portrait at " + url);
					InputStream is = new java.net.URL(url).openStream();
					bitmap = BitmapFactory.decodeStream(is, null, options);
				} catch (MalformedURLException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
				
				notify();
			}
		}
	}
	
	/*public Bitmap getPortraitBmp()
	{
		if (portraitBmp == null)
			portraitBmp = BitmapFactory.decodeByteArray(portrait, 0, portrait.length);
		
		return portraitBmp;
	}*/
	
	/**
	 * ��ȡ�û�ID
	 * @return �û�ID
	 */
	public int getID() { return id; }
	
	/**
	 * ��ȡ�û��ǳ�
	 * @return �ǳ�
	 */
	public String getNickName() { return nickName; }
	
	/**
	 * ��ȡ�û�����
	 * @return ����
	 */
	public String getEmail() { return email; }

	/**
	 * ��ȡ�û��Ա�
	 * @return �Ա�
	 */
	public String getSex() { return sex; }
	
	/**
	 * ��ȡ�û�����
	 * @return ����
	 */
	public String getBirthday() { return birthday; }
	
	/**
	 * ��ȡ�û�ͷ��
	 * @return ͷ��
	 */
	public byte[] getPortrait() { return portrait; }
	
	/**
	 * ��ȡ�û�����
	 * @return ����
	 */
	public String getHometown() { return hometown; }
	
	/**
	 * ��ȡ�û��ֻ���
	 * @return �ֻ���
	 */
	public String getPhoneNumber() { return phoneNumber; }
	
	/**
	 * ��ȡ�û���½�˺�
	 * @return ��½�˺�
	 */
	public String getLoginAccount() { return loginAccount; }
}
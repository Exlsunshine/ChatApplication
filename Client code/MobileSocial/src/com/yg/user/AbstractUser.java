package com.yg.user;

import android.graphics.Bitmap;
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
	protected String hometown;
	protected Bitmap portraitBmp;
	protected String portraitUrl;
	
	public AbstractUser(int id, String loginAccount, String nickName, String email, String phoneNumber,
			String sex, String birthday, String portraitUrl, String hometown)
	{
		this.id = id;
		this.loginAccount = loginAccount;
		this.nickName = nickName;
		this.email = email;
		this.phoneNumber = phoneNumber;
		this.sex = sex;
		this.birthday = birthday;
		this.portraitUrl = portraitUrl;
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
			DownloadManager dm = new DownloadManager(portraitUrl);
			portraitBmp = dm.getBmpFile();
		}
		
		//Log.i(DEBUG_TAG, "Return portrait.");
		
		return portraitBmp;
	}
	
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
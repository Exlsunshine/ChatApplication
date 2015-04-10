package com.yg.user;

import com.yg.commons.ConstantValues;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public abstract class AbstractUser
{
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
	
	/**
	 * 获取用户头像(Bitmap类型)
	 * @return 头像(Bitmap类型)
	 */
	public Bitmap getPortraitBmp()
	{
		if (portraitBmp == null)
			portraitBmp = BitmapFactory.decodeByteArray(portrait, 0, portrait.length);
		
		return portraitBmp;
	}
	
	/**
	 * 获取用户ID
	 * @return 用户ID
	 */
	public int getID() { return id; }
	
	/**
	 * 获取用户昵称
	 * @return 昵称
	 */
	public String getNickName() { return nickName; }
	
	/**
	 * 获取用户邮箱
	 * @return 邮箱
	 */
	public String getEmail() { return email; }

	/**
	 * 获取用户性别
	 * @return 性别
	 */
	public String getSex() { return sex; }
	
	/**
	 * 获取用户生日
	 * @return 生日
	 */
	public String getBirthday() { return birthday; }
	
	/**
	 * 获取用户头像
	 * @return 头像
	 */
	public byte[] getPortrait() { return portrait; }
	
	/**
	 * 获取用户家乡
	 * @return 家乡
	 */
	public String getHometown() { return hometown; }
	
	/**
	 * 获取用户手机号
	 * @return 手机号
	 */
	public String getPhoneNumber() { return phoneNumber; }
	
	/**
	 * 获取用户登陆账号
	 * @return 登陆账号
	 */
	public String getLoginAccount() { return loginAccount; }
}
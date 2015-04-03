package com.yg.user;

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
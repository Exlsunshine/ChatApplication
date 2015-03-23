package com.user;

public class FriendUser extends AbstractUser
{
	private String groupName;
	private String alias;
	private boolean closeFriend;
	private String city;
	private String province;
	
	public FriendUser(int id, String loginAccount, String nickName, String email, String phoneNumber,
					String sex, String birthday, byte[] portrait, String hometown)
	{
		super(id, loginAccount, nickName, email, phoneNumber, sex, birthday, portrait, hometown);
	}
	
	public FriendUser(int id, String loginAccount, String nickName, String email, String phoneNumber,
					String sex, String birthday, byte[] portrait, String hometown, String groupName,
					String alias, boolean closeFriend, String city, String province)
	{
		 
		super(id, loginAccount, nickName, email, phoneNumber, sex, birthday, portrait, hometown);
		
		this.hometown = city + " " + province;
		this.groupName = groupName;
		this.alias = alias;
		this.closeFriend = closeFriend;
		this.city = city;
		this.province = province;
	}
	
	/**
	 * ��ȡ����������ڵķ�����
	 * @return ������
	 */
	public String getGroupName() { return groupName; }
	
	/**
	 * ��ȡ�û���������������õı���
	 * @return ����
	 */
	public String getAlias() { return alias; }
	
	/**
	 * ��ȡ����������ڳ���
	 * @return ��������
	 */
	public String getCity() { return city; }
	
	/**
	 * ��ȡ�����������ʡ��
	 * @return ʡ������
	 */
	public String getProvince() { return province; }
	
	/**
	 * ��ѯ�û��Ƿ����������Ϊ���Ǳ����
	 * @return
	 */
	public boolean isCloseFriend() { return closeFriend; }
}
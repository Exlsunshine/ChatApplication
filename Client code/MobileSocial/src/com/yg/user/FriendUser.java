package com.yg.user;

public class FriendUser extends AbstractUser
{
	private String groupName;
	private String alias;
	private boolean closeFriend;
	
	public FriendUser(int id, String loginAccount, String nickName, String email, String phoneNumber,
					String sex, String birthday, String portraitUrl, String hometown,
					String groupName, String alias, boolean closeFriend)
	{
		super(id, loginAccount, nickName, email, phoneNumber, sex, birthday, portraitUrl, hometown);
		
		this.groupName = groupName;
		this.alias = alias;
		this.closeFriend = closeFriend;
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
	 * ��ѯ�û��Ƿ����������Ϊ���Ǳ����
	 * @return
	 */
	public boolean isCloseFriend() { return closeFriend; }
}
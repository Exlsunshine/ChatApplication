package com.yg.user;

import com.yg.ui.friendlist.implementation.PinyinUtils;

public class FriendUser extends AbstractUser implements Comparable<FriendUser>
{
	private String groupName;
	private String alias;
	private boolean closeFriend;
	private String fullName;
	private String fullNamePinyin;
	
	public FriendUser(int id, String loginAccount, String nickName, String email, String phoneNumber,
					String sex, String birthday, String portraitUrl, String hometown,
					String groupName, String alias, boolean closeFriend)
	{
		super(id, loginAccount, nickName, email, phoneNumber, sex, birthday, portraitUrl, hometown);
		
		this.groupName = groupName;
		this.alias = alias;
		this.closeFriend = closeFriend;
		this.fullName = null;
		this.fullNamePinyin = null;
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

	/**
	 * ��ȡ�ú��ѵ�ȫ�������û�������������õı�ע�� + �ú����Լ����õ��ǳ�<br>
	 * ��ʽ�磺���(����)
	 * @return ȫ��
	 */
	public String getFullName()
	{
		if (fullName == null)
		{
			if (alias != null && alias.length() > 0)
				fullName = alias + "(" + nickName + ")";
			else
				fullName = nickName;
		}

		return fullName;
	}
	
	/**
	 * ��ȡ�ú��ѵ�ȫ����ƴ����ʽ(��������ʽ������)
	 * @return ȫ����ƴ����ʽ
	 */
	public String getFullNameInPinyin()
	{
		if (fullNamePinyin == null)
			fullNamePinyin = PinyinUtils.getPinYin(getFullName());
		
		return fullNamePinyin;
	}
	
	@Override
	public int compareTo(FriendUser another)
	{
		return getFullNameInPinyin().compareToIgnoreCase(another.getFullNameInPinyin());
	}
}
package com.yg.user;

import com.yg.ui.friendlist.implementation.PinyinUtils;

public class FriendUser extends AbstractUser implements Comparable<FriendUser>
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

	@Override
	public int compareTo(FriendUser another)
	{
		String str1 = (alias == null)? nickName : alias + " " + nickName;
		String str2 = (another.getAlias() == null)? another.getNickName() : another.getAlias() + " " + another.getNickName();
		
		String pinyin1 = PinyinUtils.getPinYin(str1);
		String pinyin2 = PinyinUtils.getPinYin(str2);
		
		return pinyin1.compareToIgnoreCase(pinyin2);
	}
}
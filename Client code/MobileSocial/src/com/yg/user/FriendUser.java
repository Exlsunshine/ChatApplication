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
	 * 获取这个好友所在的分组名
	 * @return 分组名
	 */
	public String getGroupName() { return groupName; }
	
	/**
	 * 获取用户对这个好友所设置的别名
	 * @return 别名
	 */
	public String getAlias() { return alias; }
	
	/**
	 * 查询用户是否将这个好友设为了星标好友
	 * @return
	 */
	public boolean isCloseFriend() { return closeFriend; }

	/**
	 * 获取该好友的全名，即用户对这个好友设置的备注名 + 该好友自己设置的昵称<br>
	 * 格式如：哥哥(张三)
	 * @return 全名
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
	 * 获取该好友的全名的拼音形式(带数字形式的声调)
	 * @return 全名的拼音形式
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
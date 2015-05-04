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
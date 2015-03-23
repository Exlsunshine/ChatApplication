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
	 * 获取这个好友所在城市
	 * @return 城市名称
	 */
	public String getCity() { return city; }
	
	/**
	 * 获取这个好友所在省份
	 * @return 省市名称
	 */
	public String getProvince() { return province; }
	
	/**
	 * 查询用户是否将这个好友设为了星标好友
	 * @return
	 */
	public boolean isCloseFriend() { return closeFriend; }
}
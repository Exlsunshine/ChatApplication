package com.doc;
import java.util.ArrayList;

import com.Message.AbstractMessage;
import com.Message.Dialog;
import com.game.AbstractGame;

class User
{
	public User(String loginAccount);

	/**
	 * 设置用户密码
	 * @param password 密码
	 */
	public void setPassword(String password);
	
	/**
	 * 设置用户昵称
	 * @param nickName 昵称
	 */
	public void setNickName(String nickName);
	
	/**
	 * 设置用户邮箱
	 * @param email 邮箱
	 */
	public void setEmail(String email);
	
	/**
	 * 设置用户性别
	 * @param sex 性别
	 */
	public void setSex(String sex);
	
	/**
	 * 设置用户生日
	 * @param birthday 生日
	 */
	public void setBirthday(String birthday);
	
	/**
	 * 设置用户头像
	 * @param portraitPath 用户头像的路径
	 */
	public void setPortrait(String portraitPath);
	
	/**
	 * 设置用户家乡
	 * @param hometown 家乡
	 */
	public void setHometown(String hometown);
	
	/**
	 * 设置用户手机号
	 * @param phoneNumber 手机号
	 */
	public void setPhoneNumber(String phoneNumber);
	
	/**
	 * 获取用户昵称
	 * @return 昵称
	 */
	public String getNickName();
	
	/**
	 * 获取用户邮箱
	 * @return 邮箱
	 */
	public String getEmail();

	/**
	 * 获取用户性别
	 * @return 性别
	 */
	public String getSex();
	
	/**
	 * 获取用户生日
	 * @return 生日
	 */
	public String getBirthday();
	
	/**
	 * 获取用户头像
	 * @return 头像在服务器中的路径（待商榷）
	 */
	public String getPortrait();
	
	/**
	 * 获取用户家乡
	 * @return 家乡
	 */
	public String getHometown();
	
	/**
	 * 获取用户手机号
	 * @return 手机号
	 */
	public String getPhoneNumber();
	
	/**
	 * 获取用户登陆账号
	 * @return 登陆账号
	 */
	public String getLoginAccount();

	/**
	 * 登陆
	 */
	public void signin();
	
	/**
	 * 注销
	 */
	public void signoff();

	/**
	 * 开始摇一摇
	 */
	public void shakeAround();
	
	/**
	 * 与other这个用户建立一个对话
	 * @param other 想要与之建立对话的目标用户
	 */
	public void loadDialogWith(User other);
	
	/**
	 * 将msg这条消息发送个other这个用户
	 * @param other 待接收的目标用户
	 * @param msg 待发送的消息
	 * @return true 发送成功<br>
	 * false 发送失败
	 */
	public boolean sendMsgTo(User other,AbstractMessage msg);
	

	/**
	 * 重新初始化该对象（所有数据从新从服务器或本地获取）
	 */
	public void reloadEverything();
	
	/**
	 * 将other这个用户设置一个别名alias
	 * @param other 待设置的目标用户
	 * @param alias 别名
	 */
	public void setAlias(User other, String alias);
	
	/**
	 * 将other这个用户设置到groupName这个分组
	 * @param other 待设置的目标用户
	 * @param groupName 分组名称
	 */
	public void moveFriendToGroup(User other, Stirng groupName);
	
	/**
	 * 将other这个用户设为星标用户
	 * @param other 待设置的目标用户
	 */
	public void markCloseFriend(User other);
	
	/**
	 * 将other这个用户加入黑名单
	 * @param other 待设置的目标用户
	 */
	public void blockUser(User other);
	
	/**
	 * 将other这个用户解除好友关系
	 * @param other 待设置的目标用户
	 */
	public void deleteUser(User other);
	
	/**
	 * 获取好友列表
	 * @return 当前用户的所有好友
	 */
	public ArrayList<User> getFriendList()
	{
		//str = get JSON string from server
		PackString ps = new PackString(str);
		JSON
	}
	
	/**
	 * 获取最近聊天的所有本地对话
	 * @return 本地所有对话
	 */
	public ArrayList<Dialog> getRecentDialog();
	
	/**
	 * 从服务器验证用户身份
	 * @return true 身份验证通过<br>
	 * false 身份验证未通过
	 */
	public boolean identityVarified();


	private ArrayList<User> friendList;

}
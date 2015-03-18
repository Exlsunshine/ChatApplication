package com.user;

import java.util.ArrayList;
import java.util.Map;
import android.content.Context;
import android.util.Log;
import com.dialog.Dialog;
import com.message.AbstractMessage;

public class ClientUser extends AbstractUser
{
	private String wsNamespace = "http://network.com";
	private String wsEndpoint = "http://192.168.95.1:8080/WebServiceProject/services/Main";
	private WebServiceAPI wsAPI = new WebServiceAPI(wsNamespace, wsEndpoint);
	private Context context;
	
	/****************************		以下是网络操作相关内容		****************************/
	public ClientUser(String loginAccount, Context context)
	{
		super(0, null, null, null, null, null, null, null, null);
	}
	
	/**
	 * 设置用户密码
	 * @param password 密码
	 */
	public void setPassword(String password)
	{
		String [] params = new String[2];
		Object [] vlaues = new Object[2];
		params[0] = "userID";
		params[1] = "password";
		vlaues[0] = this.id;
		vlaues[1] = password;
		
		Object ret = wsAPI.callFuntion("setPassword", params, vlaues);
		
		Log.e("______", ret.toString());
	}
	
	/**
	 * 设置用户昵称
	 * @param nickName 昵称
	 */
	public void setNickName(String nickName) 
	{
		String [] params = new String[2];
		Object [] vlaues = new Object[2];
		params[0] = "userID";
		params[1] = "nickName";
		vlaues[0] = this.id;
		vlaues[1] = nickName;
		
		Object ret = wsAPI.callFuntion("setNickName", params, vlaues);
		
		Log.e("______", ret.toString());
	}
	
	/**
	 * 设置用户邮箱
	 * @param email 邮箱
	 */
	public void setEmail(String email) 
	{
		String [] params = new String[2];
		Object [] vlaues = new Object[2];
		params[0] = "userID";
		params[1] = "email";
		vlaues[0] = this.id;
		vlaues[1] = email;
		
		Object ret = wsAPI.callFuntion("setEmail", params, vlaues);
		
		Log.e("______", ret.toString());
	}
	
	/**
	 * 设置用户性别
	 * @param sex 性别
	 */
	public void setSex(String sex)
	{
		String [] params = new String[2];
		Object [] vlaues = new Object[2];
		params[0] = "userID";
		params[1] = "sex";
		vlaues[0] = this.id;
		vlaues[1] = sex;
		
		Object ret = wsAPI.callFuntion("setSex", params, vlaues);
		
		Log.e("______", ret.toString());
	}
	
	/**
	 * 设置用户生日
	 * @param birthday 生日
	 */
	public void setBirthday(String birthday) 
	{
		String [] params = new String[2];
		Object [] vlaues = new Object[2];
		params[0] = "userID";
		params[1] = "birthday";
		vlaues[0] = this.id;
		vlaues[1] = birthday;
		
		Object ret = wsAPI.callFuntion("setBirthday", params, vlaues);
		
		Log.e("______", ret.toString());
	}
	
	/**
	 * 设置用户头像
	 * @param portraitPath 用户头像的路径
	 */
	public void setPortrait(String portraitPath) {
	}
	
	/**
	 * 设置用户家乡
	 * @param hometown 家乡
	 */
	public void setHometown(String hometown)
	{
		String [] params = new String[2];
		Object [] vlaues = new Object[2];
		params[0] = "userID";
		params[1] = "hometown";
		vlaues[0] = this.id;
		vlaues[1] = hometown;
		
		Object ret = wsAPI.callFuntion("setHometown", params, vlaues);
		
		Log.e("______", ret.toString());
	}
	
	/**
	 * 设置用户手机号
	 * @param phoneNumber 手机号
	 */
	public void setPhoneNumber(String phoneNumber)
	{
		String [] params = new String[2];
		Object [] vlaues = new Object[2];
		params[0] = "userID";
		params[1] = "phoneNumber";
		vlaues[0] = this.id;
		vlaues[1] = phoneNumber;
		
		Object ret = wsAPI.callFuntion("setPhoneNumber", params, vlaues);
		
		Log.e("______", ret.toString());
	}

	/**
	 * 登陆
	 */
	public void signin() {
	}
	
	/**
	 * 注销
	 */
	public void signoff() {
	}

	/**
	 * 开始摇一摇
	 */
	public void shakeAround() {
	}
	
	/**
	 * 将msg这条消息发送个other这个用户
	 * @param other 待接收的目标用户
	 * @param msg 待发送的消息
	 * @return true 发送成功<br>
	 * false 发送失败
	 */
	public boolean sendMsgTo(ClientUser other,AbstractMessage msg) {
		return false;
	}
	

	/**
	 * 重新初始化该对象（所有数据从新从服务器或本地获取）
	 */
	public void reloadEverything() {
	}
	
	/**
	 * 将other这个用户设置一个别名alias
	 * @param other 待设置的目标用户
	 * @param alias 别名
	 */
	public void setAlias(ClientUser other, String alias) 
	{
		String [] params = new String[3];
		Object [] vlaues = new Object[3];
		params[0] = "userID";
		params[1] = "anotherUserID";
		params[3] = "alias";
		vlaues[0] = this.id;
		vlaues[1] = other.getID();
		vlaues[1] = alias;
		
		Object ret = wsAPI.callFuntion("setAlias", params, vlaues);
		
		Log.e("______", ret.toString());
	}
	
	/**
	 * 将other这个用户设置到groupName这个分组
	 * @param other 待设置的目标用户
	 * @param groupName 分组名称
	 */
	public void moveFriendToGroup(ClientUser other, String groupName)
	{
		String [] params = new String[3];
		Object [] vlaues = new Object[3];
		params[0] = "userID";
		params[1] = "anotherUserID";
		params[3] = "groupName";
		vlaues[0] = this.id;
		vlaues[1] = other.getID();
		vlaues[1] = groupName;
		
		Object ret = wsAPI.callFuntion("moveFriendToGroup", params, vlaues);
		
		Log.e("______", ret.toString());
	}
	
	/**
	 * 将other这个用户设为星标用户
	 * @param other 待设置的目标用户
	 */
	public void markCloseFriend(ClientUser other)
	{
		String [] params = new String[2];
		Object [] vlaues = new Object[2];
		params[0] = "userID";
		params[1] = "anotherUserID";
		vlaues[0] = this.id;
		vlaues[1] = other.getID();
		
		Object ret = wsAPI.callFuntion("markCloseFriend", params, vlaues);
		
		Log.e("______", ret.toString());
	}
	
	/**
	 * 将other这个用户加入黑名单
	 * @param other 待设置的目标用户
	 */
	public void blockUser(ClientUser other)
	{
		String [] params = new String[2];
		Object [] vlaues = new Object[2];
		params[0] = "userID";
		params[1] = "anotherUserID";
		vlaues[0] = this.id;
		vlaues[1] = other.getID();
		
		Object ret = wsAPI.callFuntion("blockUser", params, vlaues);
		
		Log.e("______", ret.toString());
	}
	
	/**
	 * 将other这个用户解除好友关系
	 * @param other 待设置的目标用户
	 */
	public void deleteUser(ClientUser other)
	{
		String [] params = new String[2];
		Object [] vlaues = new Object[2];
		params[0] = "userID";
		params[1] = "anotherUserID";
		vlaues[0] = this.id;
		vlaues[1] = other.getID();
		
		Object ret = wsAPI.callFuntion("deleteUser", params, vlaues);
		
		Log.e("______", ret.toString());
	}
	
	/**
	 * 从服务器验证用户身份
	 * @return true 身份验证通过<br>
	 * false 身份验证未通过
	 */
	public boolean identityVarified() {
		return false;
	}

	/**
	 * 获取好友列表
	 * @return 当前用户的所有好友
	 */
	public ArrayList<AbstractUser> getFriendList()
	{
		/**********		str应从服务器处获取		**********/
		String str = "{\"friends_list\":[{\"birthday\":\"1992-12-02\",\"loginAccount\":\"Xiao Account\",\"hometown\":\"Beijing\",\"phoneNumber\":\"13587649098\",\"user_id\":1,\"nickName\":\"Xiao ming\",\"sex\":\"male\",\"portrait\":[1,2,3],\"email\":\"aaa@sina.com\"},{\"birthday\":\"1991-02-12\",\"loginAccount\":\"Li Account\",\"hometown\":\"Chongqing\",\"phoneNumber\":\"17587649098\",\"user_id\":2,\"nickName\":\"Li ying\",\"sex\":\"male\",\"portrait\":[16,26,36],\"email\":\"bbb@sina.com\"},{\"birthday\":\"1993-05-12\",\"loginAccount\":\"Sun Account\",\"hometown\":\"Shanghai\",\"phoneNumber\":\"19587649098\",\"user_id\":3,\"nickName\":\"Sun ming\",\"sex\":\"male\",\"portrait\":[2,3,4],\"email\":\"ccc@sina.com\"}]}";

		PackString ps = new PackString(str);
		ArrayList<Map<String, Object>> result = ps.jsonString2Arrylist(JSON_MSG_KEY_FRIENDS_LIST);
		for (int i = 0; i < result.size(); i++)
		{
			Map<String, Object> map = result.get(i);
			
			FriendUser friend = new FriendUser(Integer.parseInt((String) map.get(JSON_INFO_KEY_USER_ID)),
					(String) map.get(JSON_INFO_KEY_USER_LOGIN_ACNT), 
					(String) map.get(JSON_INFO_KEY_USER_NICK_NAME),
					(String) map.get(JSON_INFO_KEY_USER_EMAIL), 
					(String) map.get(JSON_INFO_KEY_USER_PHONE_NO), 
					(String) map.get(JSON_INFO_KEY_USER_SEX), 
					(String) map.get(JSON_INFO_KEY_USER_BIRTHDAY), 
					(byte[]) map.get(JSON_INFO_KEY_USER_PORTRAIT),
					(String) map.get(JSON_INFO_KEY_USER_HOMETOWN));
			friendList.add(friend);
		}
		
		return friendList;
	}
	
	private String JSON_MSG_KEY_FRIENDS_LIST = "friends_list";
	private String JSON_INFO_KEY_USER_ID = "user_id";
	private String JSON_INFO_KEY_USER_LOGIN_ACNT = "loginAccount";
	private String JSON_INFO_KEY_USER_NICK_NAME = "nickName";
	private String JSON_INFO_KEY_USER_EMAIL = "email";
	private String JSON_INFO_KEY_USER_PHONE_NO = "phoneNumber";
	private String JSON_INFO_KEY_USER_SEX = "sex";
	private String JSON_INFO_KEY_USER_BIRTHDAY = "birthday";
	private String JSON_INFO_KEY_USER_PORTRAIT = "portrait";
	private String JSON_INFO_KEY_USER_HOMETOWN = "hometown";
	
	private ArrayList<AbstractUser> friendList;
	private ArrayList<Dialog> dialogList;
	
	
	
	
	
	
	
	
	
	
	
	
	/**
	 * 获取最近聊天的所有本地对话
	 * @return 本地所有对话
	 */
	public ArrayList<Dialog> getRecentDialogs()
	{
		dialogList = new ArrayList<Dialog>();
		
		for (int i = 0; i < friendList.size(); i++)
		{
			Dialog dialog = new Dialog(id, friendList.get(i).getID(), context);
			if (dialog.getMessageNum() > 0)
				dialogList.add(dialog);
		}
		
		return dialogList;
	}
	
	/**
	 * 与other这个用户建立一个对话
	 * @param other 想要与之建立对话的目标用户
	 * @return 建立好的对话
	 */
	public Dialog loadDialogWith(AbstractUser other)
	{
		Dialog dialog = null;
		
		if (dialogList != null)
		{
			dialog = getDialogWith(other.getID());
			if (dialog != null)
				return dialog;
		}
		else
			dialogList = new ArrayList<Dialog>();
			
		dialog = new Dialog(id, other.getID(), context);
		dialogList.add(dialog);
			
		return dialog;
	}
	
	/**
	 * 从本地对话中查找：当前用户与ID为targetUserID的用户之间的对话
	 * @param targetUserID 另一用户ID
	 * @return 当前用户与ID为targetUserID的用户之间的对话<br>
	 * null 表示本地不存在与targetUserID用户的对话记录
	 */
	private Dialog getDialogWith(int targetUserID)
	{
		if (dialogList == null || dialogList.size() == 0)
			return null;
		
		for (int i = 0; i < dialogList.size(); i++)
			if (dialogList.get(i).getAnotherUserID() == targetUserID)
				return dialogList.get(i);
		
		return null;
	}
}
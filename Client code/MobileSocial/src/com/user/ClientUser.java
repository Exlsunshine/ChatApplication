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
	
	/****************************		��������������������		****************************/
	public ClientUser(String loginAccount, Context context)
	{
		super(0, null, null, null, null, null, null, null, null);
	}
	
	/**
	 * �����û�����
	 * @param password ����
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
	 * �����û��ǳ�
	 * @param nickName �ǳ�
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
	 * �����û�����
	 * @param email ����
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
	 * �����û��Ա�
	 * @param sex �Ա�
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
	 * �����û�����
	 * @param birthday ����
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
	 * �����û�ͷ��
	 * @param portraitPath �û�ͷ���·��
	 */
	public void setPortrait(String portraitPath) {
	}
	
	/**
	 * �����û�����
	 * @param hometown ����
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
	 * �����û��ֻ���
	 * @param phoneNumber �ֻ���
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
	 * ��½
	 */
	public void signin() {
	}
	
	/**
	 * ע��
	 */
	public void signoff() {
	}

	/**
	 * ��ʼҡһҡ
	 */
	public void shakeAround() {
	}
	
	/**
	 * ��msg������Ϣ���͸�other����û�
	 * @param other �����յ�Ŀ���û�
	 * @param msg �����͵���Ϣ
	 * @return true ���ͳɹ�<br>
	 * false ����ʧ��
	 */
	public boolean sendMsgTo(ClientUser other,AbstractMessage msg) {
		return false;
	}
	

	/**
	 * ���³�ʼ���ö����������ݴ��´ӷ������򱾵ػ�ȡ��
	 */
	public void reloadEverything() {
	}
	
	/**
	 * ��other����û�����һ������alias
	 * @param other �����õ�Ŀ���û�
	 * @param alias ����
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
	 * ��other����û����õ�groupName�������
	 * @param other �����õ�Ŀ���û�
	 * @param groupName ��������
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
	 * ��other����û���Ϊ�Ǳ��û�
	 * @param other �����õ�Ŀ���û�
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
	 * ��other����û����������
	 * @param other �����õ�Ŀ���û�
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
	 * ��other����û�������ѹ�ϵ
	 * @param other �����õ�Ŀ���û�
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
	 * �ӷ�������֤�û����
	 * @return true �����֤ͨ��<br>
	 * false �����֤δͨ��
	 */
	public boolean identityVarified() {
		return false;
	}

	/**
	 * ��ȡ�����б�
	 * @return ��ǰ�û������к���
	 */
	public ArrayList<AbstractUser> getFriendList()
	{
		/**********		strӦ�ӷ���������ȡ		**********/
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
	 * ��ȡ�����������б��ضԻ�
	 * @return �������жԻ�
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
	 * ��other����û�����һ���Ի�
	 * @param other ��Ҫ��֮�����Ի���Ŀ���û�
	 * @return �����õĶԻ�
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
	 * �ӱ��ضԻ��в��ң���ǰ�û���IDΪtargetUserID���û�֮��ĶԻ�
	 * @param targetUserID ��һ�û�ID
	 * @return ��ǰ�û���IDΪtargetUserID���û�֮��ĶԻ�<br>
	 * null ��ʾ���ز�������targetUserID�û��ĶԻ���¼
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
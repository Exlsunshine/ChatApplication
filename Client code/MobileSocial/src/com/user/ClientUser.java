package com.user;

import java.util.ArrayList;
import java.util.Map;

import android.content.Context;

import com.dialog.Dialog;
import com.message.AbstractMessage;


public class ClientUser extends AbstractUser
{
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
	public void setPassword(String password) {
	}
	
	/**
	 * �����û��ǳ�
	 * @param nickName �ǳ�
	 */
	public void setNickName(String nickName) {
	}
	
	/**
	 * �����û�����
	 * @param email ����
	 */
	public void setEmail(String email) {
	}
	
	/**
	 * �����û��Ա�
	 * @param sex �Ա�
	 */
	public void setSex(String sex) {
	}
	
	/**
	 * �����û�����
	 * @param birthday ����
	 */
	public void setBirthday(String birthday) {
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
	public void setHometown(String hometown) {
	}
	
	/**
	 * �����û��ֻ���
	 * @param phoneNumber �ֻ���
	 */
	public void setPhoneNumber(String phoneNumber) {
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
	public void setAlias(ClientUser other, String alias) {
	}
	
	/**
	 * ��other����û����õ�groupName�������
	 * @param other �����õ�Ŀ���û�
	 * @param groupName ��������
	 */
	public void moveFriendToGroup(ClientUser other, String groupName) {
	}
	
	/**
	 * ��other����û���Ϊ�Ǳ��û�
	 * @param other �����õ�Ŀ���û�
	 */
	public void markCloseFriend(ClientUser other) {
	}
	
	/**
	 * ��other����û����������
	 * @param other �����õ�Ŀ���û�
	 */
	public void blockUser(ClientUser other) {
	}
	
	/**
	 * ��other����û�������ѹ�ϵ
	 * @param other �����õ�Ŀ���û�
	 */
	public void deleteUser(ClientUser other){
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
	public ArrayList<Dialog> getRecentDialog()
	{
		dialogList = new ArrayList<Dialog>();
		
		for (int i = 0; i < friendList.size(); i++)
			dialogList.add(new Dialog(id, friendList.get(i).getID(), context));
		
		return dialogList;
	}
	
	/**
	 * ��other����û�����һ���Ի�
	 * @param other ��Ҫ��֮�����Ի���Ŀ���û�
	 */
	public Dialog loadDialogWith(AbstractUser other)
	{
		return new Dialog(id, other.getID(), context);
	}
}
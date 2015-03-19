package com.user;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.configs.ConstantValues;
import com.dialog.Dialog;
import com.message.AbstractMessage;
import com.message.ImageMessage;
import com.message.TextMessage;
import com.network.openfire.OpenfireHandler;

public class ClientUser extends AbstractUser
{
	private final static String DEBUG_TAG = "______ClientUser";
	private WebServiceAPI wsAPI = new WebServiceAPI(ConstantValues.Configs.WEBSERVICE_NAMESPACE, ConstantValues.Configs.WEBSERVICE_ENDPOINT);
	private Context context;
	private OpenfireHandler ofhandler;
	
	/****************************		��������������������		****************************/
	public ClientUser(int userID, String password, String loginAccount, Context context)
	{
		super(89, null, null, null, null, null, null, null, null);
		
		this.context = context;
		ofhandler = new OpenfireHandler(String.valueOf(getID()), password);
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
	public void setPortrait(String portraitPath)
	{
		File portrait = new File(portraitPath);
		if (!portrait.exists())
			return ;
		
		Bitmap bmp = BitmapFactory.decodeFile(portraitPath);
		
		try 
		{
			String portraitInStr = ImageTransportation.image2String(bmp);
			
			String [] params = new String[2];
			Object [] vlaues = new Object[2];
			params[0] = "userID";
			params[1] = "portrait";
			vlaues[0] = this.id;
			vlaues[1] = portraitInStr;
			
			System.out.println(portraitInStr.length());
			
			Object ret = wsAPI.callFuntion("setPortrait", params, vlaues);
			Log.e("______", ret.toString());
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * �����û�����
	 * @param hometown ����
	 */
	public void setHometown(int hometownID)
	{
		String [] params = new String[2];
		Object [] vlaues = new Object[2];
		params[0] = "userID";
		params[1] = "hometownID";
		vlaues[0] = this.id;
		vlaues[1] = hometownID;
		
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
	public void signin()
	{
		ofhandler.signin();
	}
	
	/**
	 * ע��
	 */
	public void signoff()
	{
		ofhandler.signoff();
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
	 */
	public void sendMsgTo(FriendUser other,AbstractMessage msg)
	{
		for (int i = 0; i < dialogList.size(); i++)
		{
			if (dialogList.get(i).getAnotherUserID() == other.getID())
			{
				switch (msg.getMessageType())
				{
				case ConstantValues.InstructionCode.MESSAGE_TYPE_TEXT:
					ofhandler.send(((TextMessage)msg).getText(), String.valueOf(other.getID()));
					break;
				case ConstantValues.InstructionCode.MESSAGE_TYPE_IMAGE:
					break;
				case ConstantValues.InstructionCode.MESSAGE_TYPE_AUDIO:
					break;
				default:
					break;
				}
				
				dialogList.get(i).appendMessage(msg);
				return ;
			}
		}
		
		Log.e(DEBUG_TAG, "Send abort: You mast call makeDialogWith(FriendUser other) first"
				+ ", then call sendMsgTo(FriendUser other,AbstractMessage msg)");
	}
	
	private int uploadMessageToServer(AbstractMessage msg)
	{
		if (msg.getMessageType() == ConstantValues.InstructionCode.MESSAGE_TYPE_IMAGE)
		{
			ImageMessage imgMsg = (ImageMessage) msg;
			
		}
		else if (msg.getMessageType() == ConstantValues.InstructionCode.MESSAGE_TYPE_AUDIO)
		{
			
		}
		
		return 0;
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
	public void setAlias(FriendUser other, String alias) 
	{
		String [] params = new String[3];
		Object [] vlaues = new Object[3];
		params[0] = "userID";
		params[1] = "anotherUserID";
		params[2] = "alias";
		vlaues[0] = this.id;
		vlaues[1] = other.getID();
		vlaues[2] = alias;
		
		Object ret = wsAPI.callFuntion("setAlias", params, vlaues);
		
		Log.e("______", ret.toString());
	}
	
	/**
	 * ��other����û����õ�groupName�������
	 * @param other �����õ�Ŀ���û�
	 * @param groupName ��������
	 */
	public void moveFriendToGroup(FriendUser other, String groupName)
	{
		String [] params = new String[3];
		Object [] vlaues = new Object[3];
		params[0] = "userID";
		params[1] = "anotherUserID";
		params[2] = "groupName";
		vlaues[0] = this.id;
		vlaues[1] = other.getID();
		vlaues[2] = groupName;
		
		Object ret = wsAPI.callFuntion("moveFriendToGroup", params, vlaues);
		
		Log.e("______", ret.toString());
	}
	
	/**
	 * ��other����û����Ϊ�Ǳ��û���ȡ�����Ǳ���
	 * @param other �����õ�Ŀ���û�
	 * @param enable ʹ��ѡ��  true ��ʾ����Ϊ�Ǳ����;false ��ʾȡ�������Ǳ����
	 */
	public void setAsCloseFriend(FriendUser other, boolean enable)
	{
		String [] params = new String[3];
		Object [] vlaues = new Object[3];
		params[0] = "userID";
		params[1] = "anotherUserID";
		params[2] = "enable";
		vlaues[0] = this.id;
		vlaues[1] = other.getID();
		vlaues[2] = enable;
		
		Object ret = wsAPI.callFuntion("setAsCloseFriend", params, vlaues);
		
		Log.e("______", ret.toString());
	}
	
	/**
	 * ��other����û����������
	 * @param other �����õ�Ŀ���û�
	 */
	public void blockUser(FriendUser other)
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
	public void deleteUser(FriendUser other)
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
	public boolean identityVarified()
	{
		Log.e(DEBUG_TAG, "****************************");
		Log.e(DEBUG_TAG, "identityVarified() not supported yet!");
		Log.e(DEBUG_TAG, "****************************");
		return false;
	}
	*/

	/**
	 * ��ȡ�����б�
	 * @return ��ǰ�û������к���
	 */
	public ArrayList<AbstractUser> getFriendList()
	{
		/**********		strӦ�ӷ���������ȡ		**********/
		String [] params = new String[1];
		Object [] vlaues = new Object[1];
		params[0] = "userID";
		vlaues[0] = this.id;
		
		Object ret = wsAPI.callFuntion("getFriendList", params, vlaues);
		String str = ret.toString();
		Log.e("______", str);
		
		PackString ps = new PackString(str);
		ArrayList<HashMap<String, Object>> result = ps.jsonString2Arrylist(JSON_MSG_KEY_FRIENDS_LIST);
		friendList = new ArrayList<AbstractUser>();
		for (int i = 0; i < result.size(); i++)
		{
			Map<String, Object> map = result.get(i);
			
			FriendUser friend = new FriendUser(
					Integer.parseInt(map.get(JSON_INFO_KEY_USER_ID).toString()),
					(String) map.get(JSON_INFO_KEY_USER_LOGIN_ACNT), 
					(String) map.get(JSON_INFO_KEY_USER_NICK_NAME),
					(String) map.get(JSON_INFO_KEY_USER_EMAIL), 
					(String) map.get(JSON_INFO_KEY_USER_PHONE_NO), 
					(String) map.get(JSON_INFO_KEY_USER_SEX), 
					(String) map.get(JSON_INFO_KEY_USER_BIRTHDAY), 
					object2Bytes(map.get(JSON_INFO_KEY_USER_PORTRAIT)),
					(String) map.get(JSON_INFO_KEY_USER_HOMETOWN));
			friendList.add(friend);
		}
		
		return friendList;
		
		/**********		strӦ�ӷ���������ȡ(���ز��԰汾)		**********/
		/*//String str = "{\"friends_list\":[{\"birthday\":\"1992-12-02\",\"loginAccount\":\"Xiao Account\",\"hometown\":\"Beijing\",\"phoneNumber\":\"13587649098\",\"user_id\":1,\"nickName\":\"Xiao ming\",\"sex\":\"male\",\"portrait\":[1,2,3],\"email\":\"aaa@sina.com\"},{\"birthday\":\"1991-02-12\",\"loginAccount\":\"Li Account\",\"hometown\":\"Chongqing\",\"phoneNumber\":\"17587649098\",\"user_id\":2,\"nickName\":\"Li ying\",\"sex\":\"male\",\"portrait\":[16,26,36],\"email\":\"bbb@sina.com\"},{\"birthday\":\"1993-05-12\",\"loginAccount\":\"Sun Account\",\"hometown\":\"Shanghai\",\"phoneNumber\":\"19587649098\",\"user_id\":3,\"nickName\":\"Sun ming\",\"sex\":\"male\",\"portrait\":[2,3,4],\"email\":\"ccc@sina.com\"}]}";

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
		
		return friendList;*/
	}
	
	private byte [] object2Bytes(Object obj)
	{
		try
		{
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			ObjectOutputStream oos = new ObjectOutputStream(bos);
			oos.writeObject(obj);
			return bos.toByteArray();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
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
	public Dialog makeDialogWith(FriendUser other)
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
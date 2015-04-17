package com.yg.user;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.yg.commons.ConstantValues;
import com.yg.dialog.Dialog;
import com.yg.message.AbstractMessage;
import com.yg.message.AudioMessage;
import com.yg.message.ConvertUtil;
import com.yg.message.ImageMessage;
import com.yg.message.TextMessage;
import com.yg.network.openfire.OpenfireHandler;

public class ClientUser extends AbstractUser
{
	private final static String DEBUG_TAG = "______ClientUser";
	private Context context;
	private OpenfireHandler ofhandler;
	private Handler msgHandler;
	private WebServiceAPI wsAPI = new WebServiceAPI("network.com", "NetworkHandler");
	
	private String JSON_INFO_KEY_USER_FRIENDS_LIST = "friends_list";
	private String JSON_INFO_KEY_USER_ID = "user_id";
	private String JSON_INFO_KEY_USER_LOGIN_ACNT = "login_account";
	private String JSON_INFO_KEY_USER_NICK_NAME = "nick_name";
	private String JSON_INFO_KEY_USER_EMAIL = "email";
	private String JSON_INFO_KEY_USER_SEX = "sex";
	private String JSON_INFO_KEY_USER_BIRTHDAY = "birthday";
	private String JSON_INFO_KEY_USER_PORTRAIT = "portrait";
	private String JSON_INFO_KEY_USER_PHONE_NO = "phone_number";
	private String JSON_INFO_KEY_USER_HOMETOWN = "hometown";
	private String JSON_INFO_KEY_USER_GROUP_NAME = "group_name";
	private String JSON_INFO_KEY_USER_ALIAS = "alias";
	private String JSON_INFO_KEY_USER_CLOSE_FRIEND_FLAG = "close_friend_flag";
	
	private ArrayList<FriendUser> friendList = null;
	private ArrayList<Dialog> dialogList;
	
	public ClientUser(int userID, String password, String loginAccount, String nickName, String email, byte [] portrait, String sex, String birthday, String phoneNumber, String hometown, Context context)
	{
		super(userID, loginAccount, nickName, email, phoneNumber, sex, birthday, portrait, hometown);
		
		this.context = context;
		msgHandler = new Handler(new IncomingMessageHandlerCallback());
		ofhandler = new OpenfireHandler(String.valueOf(getID()), password, msgHandler);
	}
	
	public void setContext(Context context)
	{
		this.context = context;
	}
	
	private class IncomingMessageHandlerCallback implements Handler.Callback
	{
		@Override
		public boolean handleMessage(Message msg)
		{
			Log.w(DEBUG_TAG, "Get a message.");
			
			JSONObject json;
			int fromUserID = 0;
			String body = null;
			String date = null;
			Dialog dialog = null;
			
			try
			{
				json = new JSONObject((String) msg.obj);
				fromUserID = Integer.parseInt(((String)json.get(ConstantValues.InstructionCode.MESSAGE_RECEIVEED_FROM_USERID)).replace("@" + ConstantValues.Configs.OPENFIRE_SERVER_NAME, ""));
				body = (String)json.get(ConstantValues.InstructionCode.MESSAGE_RECEIVEED_BODY);
				date = (String)json.get(ConstantValues.InstructionCode.MESSAGE_RECEIVEED_DATE);
				dialog = makeDialogWith(getFriendByID(fromUserID));
			} catch (JSONException e) {
				e.printStackTrace();
			}
			
			switch (msg.what) 
			{
			case ConstantValues.InstructionCode.MESSAGE_TYPE_AUDIO:
				AudioTransportation audioTransport = new AudioTransportation();
				int audioID = Integer.parseInt(body.replace(ConstantValues.InstructionCode.MESSAGE_AUDIO_FLAG, ""));
				byte [] content = audioTransport.downloadAudio(audioID);
				AudioMessage audioMsg = new AudioMessage(fromUserID, getID(), content, date, false);
				dialog.appendMessage(audioMsg);
				sendBroadcast(ConstantValues.InstructionCode.MESSAGE_BROADCAST_RECV_AUDIO, fromUserID, getID());
				break;
				
			case ConstantValues.InstructionCode.MESSAGE_TYPE_IMAGE:
				ImageTransportation imgTransport = new ImageTransportation();
				int imgID = Integer.parseInt(body.replace(ConstantValues.InstructionCode.MESSAGE_IMAGE_FLAG, ""));
				Bitmap bmp = imgTransport.downloadImage(imgID);
				ImageMessage imgMsg = new ImageMessage(fromUserID, getID(), bmp, date, false);
				dialog.appendMessage(imgMsg);
				sendBroadcast(ConstantValues.InstructionCode.MESSAGE_BROADCAST_RECV_IMAGE, fromUserID, getID());
				break;
				
			case ConstantValues.InstructionCode.MESSAGE_TYPE_TEXT:
				TextMessage txtMsg = new TextMessage(fromUserID, getID(), body, date, false);
				dialog.appendMessage(txtMsg);
				sendBroadcast(ConstantValues.InstructionCode.MESSAGE_BROADCAST_RECV_TEXT, fromUserID, getID());
				break;
			default:
				Log.e(DEBUG_TAG, "Message handler error: Unkonwn type message " + String.valueOf(msg.what) + ".");
				break;
			}
			return false;
		}
	}
	
	private void sendBroadcast(String broadcastType, int fromUserID, int toUserID)
	{
		Intent intent = new Intent(broadcastType); 
		intent.putExtra("fromUserID", fromUserID);
		intent.putExtra("toUserID", toUserID);
		context.sendBroadcast(intent);
	}
	
	/**
	 * ��ȡָ��userID���û�
	 * @param userID �û�ID
	 * @return ָ����userID�û�<br>null ��ʾ�����б��в����ڴ��û���������б���δ��ʼ��
	 */
	private FriendUser getFriendByID(int userID)
	{
		FriendUser friend = null;
		
		if (friendList == null)
			getFriendListFromServer();
		
		if (friendList != null)
		{
			for (int i = 0 ; i < friendList.size(); i++)
				if (friendList.get(i).getID() == userID)
				{
					friend = friendList.get(i);
					Log.i(DEBUG_TAG, "Get friend successful.");
					break;
				}
		}
		
		return friend;
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

		this.nickName = nickName;
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

		this.email = email;
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
		
		this.sex = sex;
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
		
		this.birthday = birthday;
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
			
			this.portraitBmp = bmp;
			this.portrait = ConvertUtil.bitmap2Bytes(bmp);
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
	public void setHometown(String hometown)
	{
		String [] params = new String[2];
		Object [] vlaues = new Object[2];
		params[0] = "userID";
		params[1] = "hometownID";
		vlaues[0] = this.id;
		vlaues[1] = hometown;
		
		Object ret = wsAPI.callFuntion("setHometown", params, vlaues);
		
		this.hometown = hometown;
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
		
		this.phoneNumber = phoneNumber;
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
	public void shakeAround()
	{
	}
	
	/**
	 * ��msg������Ϣ���͸�other����û�
	 * @param other �����յ�Ŀ���û�
	 * @param msg �����͵���Ϣ
	 */
	public void sendMsgTo(FriendUser other,AbstractMessage msg)
	{
		if (dialogList == null)
			getRecentDialogs();
		makeDialogWith(other);
		
		for (int i = 0; i < dialogList.size(); i++)
		{
			if (dialogList.get(i).getAnotherUserID() == other.getID())
			{
				switch (msg.getMessageType())
				{
				//�����Ϣ�������ı�����ֱ����Openfire�����other
				case ConstantValues.InstructionCode.MESSAGE_TYPE_TEXT:
					ofhandler.send(((TextMessage)msg).getText(), String.valueOf(other.getID()));
					break;
				
				//�����Ϣ������ͼƬ
				//1	�����ϴ�ͼƬ�������������һ�ȡһ��ͼƬ�����ݿ��д�ŵ�id
				//2 ���͸�otherһ����Ϣ�ı���Ϣ:"___msg_type_img_download_request_id_is_%d",����%d��ͼƬ�����ݿ��д�ŵ�id
				case ConstantValues.InstructionCode.MESSAGE_TYPE_IMAGE:
					int imgID = uploadImageToServer((ImageMessage)msg, other);
					ofhandler.send(ConstantValues.InstructionCode.MESSAGE_IMAGE_FLAG + String.valueOf(imgID)
							, String.valueOf(other.getID()));
					break;
				
				//�����Ϣ��������Ƶ��
				//1	�����ϴ���Ƶ�������������һ�ȡһ����Ƶ�����ݿ��д�ŵ�id
				//2 ���͸�otherһ����Ϣ�ı���Ϣ:"___msg_type_audio_download_request_id_is_%d",����%d����Ƶ�ļ������ݿ��д�ŵ�id
				case ConstantValues.InstructionCode.MESSAGE_TYPE_AUDIO:
					int audioID = uploadAudioToServer((AudioMessage)msg, other);
					ofhandler.send(ConstantValues.InstructionCode.MESSAGE_AUDIO_FLAG + String.valueOf(audioID)
							, String.valueOf(other.getID()));
					break;
				default:
					break;
				}
				
				//����dialog����
				dialogList.get(i).appendMessage(msg);
				return ;
			}
		}
		
		Log.e(DEBUG_TAG, "Send abort: You mast call makeDialogWith(FriendUser other) first"
				+ ", then call sendMsgTo(FriendUser other,AbstractMessage msg)");
	}
	
	private int uploadAudioToServer(AudioMessage msg, FriendUser other)
	{
		AudioTransportation audioTransport = new AudioTransportation();
		int audioID = -1;
		
		try {
			audioID = audioTransport.uploadAduio(getID(), other.getID(), msg.getAudioPath());
		} catch (Exception e) {
			audioID = -1;
			e.printStackTrace();
		}
		return audioID;
	}
	
	private int uploadImageToServer(ImageMessage msg, FriendUser other)
	{
		ImageTransportation imgTransport = new ImageTransportation();
		int imgID = -1;
		try
		{
			imgID = imgTransport.uploadImage(getID(), other.getID(), msg.getImage());
		} catch (Exception e)
		{
			e.printStackTrace();
			imgID = -1;
		}
		
		return imgID;
	}

	/**
	 * ���³�ʼ���ö����������ݴ��´ӷ������򱾵ػ�ȡ��
	 */
	public void reloadEverything() 
	{
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

	private void getFriendListFromServer()
	{
		/**********		strӦ�ӷ���������ȡ		**********/
		String [] params = new String[1];
		Object [] vlaues = new Object[1];
		params[0] = "userID";
		vlaues[0] = this.id;
		
		Object ret = wsAPI.callFuntion("getFriendList", params, vlaues);
		if (ret == null)
			return;
		String str = ret.toString();
		Log.i(DEBUG_TAG, "Friend list is: " + str);
		
		PackString ps = new PackString(str);
		ArrayList<HashMap<String, Object>> result = ps.jsonString2Arrylist(JSON_INFO_KEY_USER_FRIENDS_LIST);
		friendList = new ArrayList<FriendUser>();
		for (int i = 0; i < result.size(); i++)
		{
			HashMap<String, Object> map = result.get(i);
			
			FriendUser friend = new FriendUser(Integer.parseInt(map.get(JSON_INFO_KEY_USER_ID).toString()),
							(String) map.get(JSON_INFO_KEY_USER_LOGIN_ACNT),
							(String) map.get(JSON_INFO_KEY_USER_NICK_NAME),
							(String) map.get(JSON_INFO_KEY_USER_EMAIL),
							(String) map.get(JSON_INFO_KEY_USER_PHONE_NO),
							(String) map.get(JSON_INFO_KEY_USER_SEX),
							(String) map.get(JSON_INFO_KEY_USER_BIRTHDAY),
							ConvertUtil.bitmap2Bytes(ImageTransportation.string2Bitmap((String) map.get(JSON_INFO_KEY_USER_PORTRAIT))),
							(String) map.get(JSON_INFO_KEY_USER_HOMETOWN),
							(String) map.get(JSON_INFO_KEY_USER_GROUP_NAME), 
							(String) map.get(JSON_INFO_KEY_USER_ALIAS),
							Integer.parseInt((String) map.get(JSON_INFO_KEY_USER_CLOSE_FRIEND_FLAG)) == 1);
			friendList.add(friend);
		}
		printFriendlist();
	}
	
	/**
	 * ��ȡ�����б�
	 * @return ��ǰ�û������к���
	 */
	public ArrayList<FriendUser> getFriendList()
	{
		if (friendList != null)
			return friendList;
		
		getFriendListFromServer();
		
		return friendList;
	}

	
	private void printFriendlist()
	{
		for (int i = 0; i < friendList.size(); i++)
		{
			Log.i(DEBUG_TAG, " **************** " + String.valueOf(i) + " **************** ");

			Log.i(DEBUG_TAG, "id: " + String.valueOf(friendList.get(i).getID()));
			Log.i(DEBUG_TAG, "longinAccount: " +  friendList.get(i).getLoginAccount());
			Log.i(DEBUG_TAG, "nickname: " +  friendList.get(i).getNickName());
			Log.i(DEBUG_TAG, "email: " +  friendList.get(i).getEmail());
			Log.i(DEBUG_TAG, "phone: " +  friendList.get(i).getPhoneNumber());
			Log.i(DEBUG_TAG, "sex: " +  friendList.get(i).getSex());
			Log.i(DEBUG_TAG, "birthday: " +  friendList.get(i).getBirthday());

			Log.i(DEBUG_TAG, "group: " +  friendList.get(i).getGroupName());
			Log.i(DEBUG_TAG, "alias: " + friendList.get(i).getAlias());
			Log.i(DEBUG_TAG, "close?: " +  String.valueOf(friendList.get(i).isCloseFriend()));
			Log.i(DEBUG_TAG, "hometown: " +  friendList.get(i).getHometown());
		}
	}
	
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
	 * @return �����õĶԻ�<br>
	 * null ��ʾ�޷���other����û������Ի�����Ϊ����Ĳ���Ϊnull
	 */
	public Dialog makeDialogWith(FriendUser other)
	{
		if (other == null)
		{
			Log.i(DEBUG_TAG, "Friend is null.");
			return null;
		}
		
		Dialog dialog = null;
		
		if (dialogList != null)
		{
			dialog = getDialogWith(other.getID());
			if (dialog != null)
				return dialog;
		}
		else
			dialogList = new ArrayList<Dialog>();
			
		dialog = new Dialog(this.getID(), other.getID(), context);
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
	
	/**
	 * ��֤�û������
	 * @param loginAccount ��½�˺�
	 * @param password ��½����
	 * @return �û���Ϣ(Json��ʽ)<br>
	 * null��ʾ�����֤ʧ�ܻ���������
	 */
	public static String validateIdentity(String loginAccount, String password)
	{
		Log.i(DEBUG_TAG, "Going to validate " + loginAccount + " with " + password);
		
		String [] params = new String[2];
		Object [] vlaues = new Object[2];
		params[0] = "loginAccount";
		params[1] = "password";
		vlaues[0] = loginAccount;
		vlaues[1] = password;
		
		WebServiceAPI wsAPI = new WebServiceAPI("network.com", "NetworkHandler");
		Object ret = wsAPI.callFuntion("validateIdentity", params, vlaues);
		
		return ret == null ? null : ret.toString();
	}
	
	public static int createNewUser(String loginAccount, String pwd, String nickname,
			String email, String sex, String birthday, String portraitPath, String hometown, String phoneNumber)
	{
		File portrait = new File(portraitPath);
		if (!portrait.exists())
			return -1;
		
		Bitmap bmp = BitmapFactory.decodeFile(portraitPath);
		
		try {
			String portraitStr = ImageTransportation.image2String(bmp);
			String [] params = new String[] {	"loginAccount", "pwd", "nickname",
												"email", "sex", "birthday", 
												"portrait", "hometown", "phoneNumber"};
			Object [] vlaues = new Object[] {loginAccount, pwd, nickname, email, sex, birthday,
					portraitStr, hometown, phoneNumber};
			
			WebServiceAPI wsAPI = new WebServiceAPI("network.com", "NetworkHandler");
			Object ret = wsAPI.callFuntion("createNewUser", params, vlaues);
			return Integer.parseInt(ret.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return -2;
	}
}
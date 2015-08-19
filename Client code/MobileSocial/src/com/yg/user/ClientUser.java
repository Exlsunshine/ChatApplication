package com.yg.user;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
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

import com.tp.messege.PostManager;
import com.yg.commons.CommonUtil;
import com.yg.commons.ConstantValues;
import com.yg.dialog.Dialog;
import com.yg.message.AbstractMessage;
import com.yg.message.AudioMessage;
import com.yg.message.ImageMessage;
import com.yg.message.TextMessage;
import com.yg.network.openfire.OpenfireHandler;
import com.yg.ui.friendlist.FriendDetailActivity;
import com.yg.ui.login.implementation.LoginInfo;

public class ClientUser extends AbstractUser
{
	private final static String DEBUG_TAG = "______ClientUser";
	private final static String PACKAGE_NAME = "network.com";
	private final static String CLASS_NAME = "NetworkHandler";
	private Context context;
	private OpenfireHandler ofhandler;
	private Handler msgHandler;
	
	
	private String JSON_INFO_KEY_USER_FRIENDS_LIST = "friends_list";
	private String JSON_INFO_KEY_USER_ID = "user_id";
	private String JSON_INFO_KEY_USER_LOGIN_ACNT = "login_account";
	private String JSON_INFO_KEY_USER_NICK_NAME = "nick_name";
	private String JSON_INFO_KEY_USER_EMAIL = "email";
	private String JSON_INFO_KEY_USER_SEX = "sex";
	private String JSON_INFO_KEY_USER_BIRTHDAY = "birthday";
	private String JSON_INFO_KEY_USER_PORTRAIT = "portrait_path";
	private String JSON_INFO_KEY_USER_PHONE_NO = "phone_number";
	private String JSON_INFO_KEY_USER_HOMETOWN = "hometown";
	private String JSON_INFO_KEY_USER_GROUP_NAME = "group_name";
	private String JSON_INFO_KEY_USER_ALIAS = "alias";
	private String JSON_INFO_KEY_USER_CLOSE_FRIEND_FLAG = "close_friend_flag";
	
	private ArrayList<FriendUser> friendList = null;
	private ArrayList<Dialog> dialogList = null;
	
	public PostManager pm = null;
	
	public ClientUser(int userID, String password, String loginAccount, String nickName, String email, String portraitUrl, String sex, String birthday, String phoneNumber, String hometown, Context context)
	{
		super(userID, loginAccount, nickName, email, phoneNumber, sex, birthday, portraitUrl, hometown);
		
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
			Log.w(DEBUG_TAG, "Get a message." + msg.what);
			final int what = msg.what;
			Log.w(DEBUG_TAG, "Get a message.2" + what);
			
			//JSONObject json;
			//int fromUserID = 0;
			//String body = null;
			//String date = null;
			//Dialog dialog = null;
			
			try
			{
				final JSONObject json = new JSONObject((String) msg.obj);
				final int fromUserID = Integer.parseInt(((String)json.get(ConstantValues.InstructionCode.MESSAGE_RECEIVEED_FROM_USERID)).replace("@" + ConstantValues.Configs.OPENFIRE_SERVER_NAME, ""));
				/**
				 * Hot fix begin
				 */
				final int  fTemp = fromUserID;
				Thread td = new Thread(new Runnable()
				{
					@Override
					public void run()
					{
						hotfix(fTemp);
						
						String body = null;
						String date = null;
						try {
							body = (String)json.get(ConstantValues.InstructionCode.MESSAGE_RECEIVEED_BODY);
							date = (String)json.get(ConstantValues.InstructionCode.MESSAGE_RECEIVEED_DATE);
							} catch (JSONException e) {
							e.printStackTrace();
						}
						
						FriendUser fri = getFriendByID(fromUserID);
						if (fri == null)
							return ;
						Dialog dialog = makeDialogWith(fri);
						
						switch (what) 
						{
						case ConstantValues.InstructionCode.MESSAGE_TYPE_AUDIO:
							AudioTransportation audioTransport = new AudioTransportation();
							String audioUrl = body.replace(ConstantValues.InstructionCode.MESSAGE_AUDIO_FLAG, "");
							byte [] content = audioTransport.downloadAudio(audioUrl);
							AudioMessage audioMsg = new AudioMessage(fromUserID, getID(), content, date, false);
							dialog.appendMessage(audioMsg);
							sendBroadcast(ConstantValues.InstructionCode.MESSAGE_BROADCAST_RECV_AUDIO, fromUserID, getID());
							break;
							
						case ConstantValues.InstructionCode.MESSAGE_TYPE_IMAGE:
							ImageTransportation imgTransport = new ImageTransportation();
							String imgUrl = body.replace(ConstantValues.InstructionCode.MESSAGE_IMAGE_FLAG, "");
							Bitmap bmp = imgTransport.downloadImage(imgUrl);
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
							Log.e(DEBUG_TAG, "Message handler error: Unkonwn type message " + String.valueOf(what) + ".");
							break;
						}
					}
				});
				td.start();
				/**
				 * Hot fix end
				 */
			} catch (JSONException e) {
				e.printStackTrace();
			}
			
			
			return false;
		}
	}
	
	private void hotfix(int userID)
	{
		for (int i = 0; i < getFriendList().size(); i++)
		{
			if (userID == getFriendList().get(i).getID())
				return;
		}
		
		getFriendListFromServer();
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
		WebServiceAPI wsAPI = new WebServiceAPI(PACKAGE_NAME, CLASS_NAME);
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
		WebServiceAPI wsAPI = new WebServiceAPI(PACKAGE_NAME, CLASS_NAME);
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
		WebServiceAPI wsAPI = new WebServiceAPI(PACKAGE_NAME, CLASS_NAME);
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
		
		WebServiceAPI wsAPI = new WebServiceAPI(PACKAGE_NAME, CLASS_NAME);
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
		
		WebServiceAPI wsAPI = new WebServiceAPI(PACKAGE_NAME, CLASS_NAME);
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
			String [] params = new String[1];
			Object [] vlaues = new Object[1];
			params[0] = "userID";
			vlaues[0] = this.id;
			
			WebServiceAPI wsAPI = new WebServiceAPI(PACKAGE_NAME, CLASS_NAME);
			Object ret = wsAPI.callFuntion("setPortrait", params, vlaues);
			
			String portraitUrl = ret.toString();
			if (portraitUrl != null)
			{
				Log.i(DEBUG_TAG, "Uploading image to " + portraitUrl);
				String serverIP = portraitUrl.substring(portraitUrl.indexOf("http://") + 7, portraitUrl.indexOf(':', 7));
				String newFileName = portraitUrl.substring(portraitUrl.indexOf('/', 7) + 1, portraitUrl.length());
				String serverPort = portraitUrl.substring(portraitUrl.indexOf(':', 5) + 1, portraitUrl.indexOf(':', 5) + 5);
				UploadManager um = new UploadManager(portrait, newFileName, serverIP, serverPort);
				um.excecuteUpload();
				
				this.portraitBmp = bmp;
			}
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
		
		WebServiceAPI wsAPI = new WebServiceAPI(PACKAGE_NAME, CLASS_NAME);
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
		
		WebServiceAPI wsAPI = new WebServiceAPI(PACKAGE_NAME, CLASS_NAME);
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
		pm = new PostManager(getID());
	}
	
	/**
	 * ע��
	 */
	public void signoff(Context context)
	{
		LoginInfo loginInfo = new LoginInfo(context);
		loginInfo.logoff();
		
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
	public void sendMsgTo(final FriendUser other,AbstractMessage msg)
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
					final String imgUrl = uploadImageToServer((ImageMessage)msg, other);
					new Thread(new Runnable() {
						
						@Override
						public void run() {
							try {
								Thread.sleep(3000);
								ofhandler.send(ConstantValues.InstructionCode.MESSAGE_IMAGE_FLAG + imgUrl
										, String.valueOf(other.getID()));
							} catch (InterruptedException e1) {
								e1.printStackTrace();
							}
							
						}
					}).start();
					
					
					break;
				
				//�����Ϣ��������Ƶ��
				//1	�����ϴ���Ƶ�������������һ�ȡһ����Ƶ�����ݿ��д�ŵ�id
				//2 ���͸�otherһ����Ϣ�ı���Ϣ:"___msg_type_audio_download_request_id_is_%d",����%d����Ƶ�ļ������ݿ��д�ŵ�id
				case ConstantValues.InstructionCode.MESSAGE_TYPE_AUDIO:
					uploadAudioToServer((AudioMessage)msg, other, ofhandler, String.valueOf(other.getID()));
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
	
	private String uploadAudioToServer(AudioMessage msg, FriendUser other, OpenfireHandler handler, String oID)
	{
		AudioTransportation audioTransport = new AudioTransportation();
		String audioUrl = null;
		
		try {
			audioUrl = audioTransport.uploadAduio(getID(), other.getID(), new File(msg.getContent()), handler, oID);
		} catch (Exception e) {
			e.printStackTrace();
			audioUrl = null;
		}
		return audioUrl;
	}
	
	private String uploadImageToServer(ImageMessage msg, FriendUser other)
	{
		ImageTransportation imgTransport = new ImageTransportation();
		String imgUrl = null;
		try
		{
			imgUrl = imgTransport.uploadImage(getID(), other.getID(), new File(msg.getContent()));
		} catch (Exception e)
		{
			e.printStackTrace();
			imgUrl = null;
		}
		
		return imgUrl;
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
		
		WebServiceAPI wsAPI = new WebServiceAPI(PACKAGE_NAME, CLASS_NAME);
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
		
		WebServiceAPI wsAPI = new WebServiceAPI(PACKAGE_NAME, CLASS_NAME);
		Object ret = wsAPI.callFuntion("moveFriendToGroup", params, vlaues);
		
		Log.e("______", ret.toString());
	}
	
	/**
	 * ��other����û����Ϊ�Ǳ��û���ȡ�����Ǳ���
	 * @param other �����õ�Ŀ���û�
	 * @param enable ʹ��ѡ��  true ��ʾ����Ϊ�Ǳ����;false ��ʾȡ�������Ǳ����
	 */
	public void setAsCloseFriend(int friendID, boolean enable)
	{
		FriendUser other = getFriendByID(friendID);
		if (other == null)
		{
			Log.e(DEBUG_TAG, "Does not find friend with given ID: " + friendID);
			return ;
		}
			
		String [] params = new String[3];
		Object [] vlaues = new Object[3];
		params[0] = "userID";
		params[1] = "anotherUserID";
		params[2] = "enable";
		vlaues[0] = this.id;
		vlaues[1] = other.getID();
		vlaues[2] = enable;
		
		WebServiceAPI wsAPI = new WebServiceAPI(PACKAGE_NAME, CLASS_NAME);
		Object ret = wsAPI.callFuntion("setAsCloseFriend", params, vlaues);
		
		Log.i(DEBUG_TAG, ret.toString());
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
		
		WebServiceAPI wsAPI = new WebServiceAPI(PACKAGE_NAME, CLASS_NAME);
		Object ret = wsAPI.callFuntion("blockUser", params, vlaues);
		
		Log.e("______", ret.toString());
	}
	
	/**
	 * ��other����û�������ѹ�ϵ
	 * @param other �����õ�Ŀ���û�
	 */
	public void deleteUser(int friendID)
	{
		FriendUser other = getFriendByID(friendID);
		if (other == null)
		{
			Log.e(DEBUG_TAG, "Does not find friend with given ID: " + friendID);
			return ;
		}
		
		String [] params = new String[2];
		Object [] vlaues = new Object[2];
		params[0] = "userID";
		params[1] = "anotherUserID";
		vlaues[0] = this.id;
		vlaues[1] = other.getID();
		
		WebServiceAPI wsAPI = new WebServiceAPI(PACKAGE_NAME, CLASS_NAME);
		Object ret = wsAPI.callFuntion("deleteUser", params, vlaues);
		Log.i("______", ret.toString());
		
		removeFriendFromList(friendID);
	}
	
	private void removeFriendFromList(int friendID)
	{
		if (friendList != null)
		{
			for (int i = 0 ; i < friendList.size(); i++)
				if (friendList.get(i).getID() == friendID)
				{
					friendList.remove(i);
					Log.i(DEBUG_TAG, "Remove friend successful.");
					break;
				}
		}
	}
	
	private void getFriendListFromServer()
	{
		friendList = new ArrayList<FriendUser>();
		
		/**********		strӦ�ӷ���������ȡ		**********/
		String [] params = new String[1];
		Object [] vlaues = new Object[1];
		params[0] = "userID";
		vlaues[0] = this.id;
		
		WebServiceAPI wsAPI = new WebServiceAPI(PACKAGE_NAME, CLASS_NAME);
		Object ret = wsAPI.callFuntion("getFriendList", params, vlaues);
		
		if (ret == null)
			return;
		String str = ret.toString();
		Log.i(DEBUG_TAG, "Friend list is: " + str);
		
		PackString ps = new PackString(str);
		ArrayList<HashMap<String, Object>> result = ps.jsonString2Arrylist(JSON_INFO_KEY_USER_FRIENDS_LIST);
		
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
							(String) map.get(JSON_INFO_KEY_USER_PORTRAIT),
							(String) map.get(JSON_INFO_KEY_USER_HOMETOWN),
							(String) map.get(JSON_INFO_KEY_USER_GROUP_NAME), 
							(String) map.get(JSON_INFO_KEY_USER_ALIAS),
							Integer.parseInt((String) map.get(JSON_INFO_KEY_USER_CLOSE_FRIEND_FLAG)) == 1);
			friendList.add(friend);
		}
		
		Collections.sort(friendList);
		
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
		
		if (friendList == null)
			Log.e(DEBUG_TAG, "2Friend list is null.");
		
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
		if (dialogList != null)
			return dialogList;
		
		dialogList = new ArrayList<Dialog>();
		
		if (friendList == null)
			Log.e(DEBUG_TAG, "3Friend list is null.");
		
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
	 * @see #makeDialogWith(FriendUser)
	 */
	public Dialog makeDialogWith(int friendID)
	{
		return makeDialogWith(getFriendByID(friendID));
	}
	
	/**
	 * �����ָ���û��ı��������¼
	 * @param friendID ָ�����û�ID
	 */
	public void eraseDialogHistory(int friendID)
	{
		Dialog dialog = makeDialogWith(friendID);
		
		if (dialog != null)
			dialog.deleteDialogHistory();
		
		for (int i = 0; i < dialogList.size(); i++)
			if (dialogList.get(i).getAnotherUserID() == friendID)
				dialogList.remove(i);
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
		
		try {
			String [] params = new String[] {	"loginAccount", "pwd", "nickname",
												"email", "sex", "birthday", 
												"portrait", "hometown", "phoneNumber"};
			Object [] vlaues = new Object[] {loginAccount, pwd, nickname, email, sex, birthday,
					"temp", hometown, phoneNumber};
			
			WebServiceAPI wsAPI = new WebServiceAPI("network.com", "NetworkHandler");
			Object ret = wsAPI.callFuntion("createNewUser", params, vlaues);
			
			String jsonStr = ret.toString();
			if (jsonStr == null)
				return -3;
			
			PackString ps = new PackString(jsonStr);
			ArrayList<HashMap<String, Object>> list = ps.jsonString2Arrylist("newUserProfile");
			int currentUserID = (Integer) list.get(0).get("currentID");
			String portraitUrl = (String) list.get(0).get("portraitUrl");
			
			Log.i(DEBUG_TAG, "Uploading image to " + portraitUrl);
			String serverIP = portraitUrl.substring(portraitUrl.indexOf("http://") + 7, portraitUrl.indexOf(':', 7));
			String newFileName = portraitUrl.substring(portraitUrl.indexOf('/', 7) + 1, portraitUrl.length());
			String serverPort = portraitUrl.substring(portraitUrl.indexOf(':', 5) + 1, portraitUrl.indexOf(':', 5) + 5);
			UploadManager um = new UploadManager(portrait, newFileName, serverIP, serverPort);
			um.excecuteUpload();
			
			return currentUserID;
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return -2;
	}
	
	/**
	 * ������������Ϊ˫�����
	 * @param targetUserID targetUserID Ŀ���û�ID
	 * @param context activity context
	 * @return 0��ʾ�Ӻ��ѳɹ�<br>-1��ʾʧ��
	 */
	public int makeFriendWith(int targetUserID, Context context)
	{
		String [] params = new String[2];
		Object [] vlaues = new Object[2];
		params[0] = "userID";
		params[1] = "targetUserID";
		vlaues[0] = this.id;
		vlaues[1] = targetUserID;
		
		WebServiceAPI wsAPI = new WebServiceAPI(PACKAGE_NAME, CLASS_NAME);
		Object ret = wsAPI.callFuntion("makeFriendWith", params, vlaues);
		
		int resultCode = Integer.parseInt(ret.toString());
		
		if (resultCode == 0)
			getFriendListFromServer();
		else
			return -1;
		
		FriendUser friend = getFriendByID(targetUserID);
		sendMsgTo(friend, new TextMessage(this.id, targetUserID, "Hi " + friend.getNickName() + ", ����" + getNickName() + ", ��ð� :)", CommonUtil.now(), true));
		
		Intent broadcastIntent = new Intent(ConstantValues.InstructionCode.MESSAGE_BROADCAST_SEND_COMPLETED);
		context.sendBroadcast(broadcastIntent);
		
		Intent intent = new Intent(context, FriendDetailActivity.class);
		intent.putExtra("friendUserID", targetUserID);
		context.startActivity(intent);
		
		return 0;
	}
}
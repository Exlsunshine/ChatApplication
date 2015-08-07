package com.yg.network.openfire;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import z.org.jivesoftware.smack.AccountManager;
import z.org.jivesoftware.smack.ConnectionConfiguration;
import z.org.jivesoftware.smack.PacketListener;
import z.org.jivesoftware.smack.XMPPConnection;
import z.org.jivesoftware.smack.XMPPException;
import z.org.jivesoftware.smack.filter.MessageTypeFilter;
import z.org.jivesoftware.smack.filter.PacketFilter;
import z.org.jivesoftware.smack.packet.Message;
import z.org.jivesoftware.smack.packet.Packet;
import z.org.jivesoftware.smack.util.StringUtils;

import android.os.Handler;
import android.util.Log;

import com.yg.commons.CommonUtil;
import com.yg.commons.ConstantValues;

public class OpenfireHandler
{
	private static final String DEBUG_TAG = "______OpenfireHandler";
	private String userName = null;
	private String password = null;
	private Handler msgHandler = null;
	
	private XMPPConnection connection = null;
	
	public OpenfireHandler(final String userName, final String password, Handler msgHandler)
	{
		this.userName = userName;
		this.password = "123456";//password;
		this.msgHandler = msgHandler;
	}
	
	//µÇÂ½
	public void signin()
	{
		Thread td = new Thread(new Runnable()
		{
			@Override
			public void run()
			{
				if (establishConnection())
				{				
					try
					{
						connection.login(userName, password);
						Log.i("XMPPChatDemoActivity", "Logged in as " + connection.getUser());
						registerMessageReceiver();
					} catch (XMPPException ex) {
						Log.e("XMPPChatDemoActivity", "Failed to log in as " + userName);
						Log.e("XMPPChatDemoActivity", ex.toString());
						connection = null;
					}
				}
			}
		});
		td.start();
	}
	
	public void signoff()
	{
		if (connection == null)
			return ;
		
		if (!connection.isConnected())
			return ;
		
		connection.disconnect();
	}

	public void send(final String text, final String targetUserID)
	{
		final String ID = targetUserID + "@" + ConstantValues.Configs.OPENFIRE_SERVER_NAME;
		Thread td = new Thread(new Runnable()
		{
			@Override
			public void run()
			{
				try {
					Thread.sleep(3000);
					Log.i("XMPPChatDemoActivity", "Sending text " + text + " to " + ID);
					Message msg = new Message(ID, Message.Type.chat);
					msg.setBody(text);
					
					if (establishConnection())
						connection.sendPacket(msg);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		});
		td.start();
		
	}
	
	private void registerMessageReceiver()
	{
		// Add a packet listener to get messages sent to us
		PacketFilter filter = new MessageTypeFilter(Message.Type.chat);
		this.connection.addPacketListener(new PacketListener()
		{
			@Override
			public void processPacket(Packet packet)
			{
				Message msg = (Message) packet;
				if (msg.getBody() != null)
				{
					String fromName = StringUtils.parseBareAddress(msg.getFrom());
					Log.i("XMPPChatDemoActivity", "Text Recieved: " + msg.getBody() + " from " + fromName );
					
					notifyClientUser(msg);
				}
			}
		}, filter);
	}

	private void notifyClientUser(Message msg)
	{
		Log.i(DEBUG_TAG, "Message has been received, notifing ClientUser.");
		
		String str = null;
		try
		{
			str = packMsg2JsonString(msg);
		}
		catch (JSONException e)
		{
			str = null;
			e.printStackTrace();
		}
		
		String msgBody = msg.getBody();
		android.os.Message androidMsg = new android.os.Message();
		androidMsg.what = getMessageType(msgBody);
		androidMsg.obj = str;
		
		Log.w(DEBUG_TAG, "Str is " + str + " Message type is " + androidMsg.what);
		msgHandler.sendMessage(androidMsg);
	}
	
	private String packMsg2JsonString(Message msg) throws JSONException
	{
		JSONObject json = new JSONObject();
		json.put(ConstantValues.InstructionCode.MESSAGE_RECEIVEED_FROM_USERID, StringUtils.parseBareAddress(msg.getFrom()));
		json.put(ConstantValues.InstructionCode.MESSAGE_RECEIVEED_BODY, msg.getBody());
		json.put(ConstantValues.InstructionCode.MESSAGE_RECEIVEED_DATE, CommonUtil.now());
		
		return json.toString();
	}
	
	private int getMessageType(String msg)
	{
		if (msg.contains(ConstantValues.InstructionCode.MESSAGE_IMAGE_FLAG))
			return ConstantValues.InstructionCode.MESSAGE_TYPE_IMAGE;
		if (msg.contains(ConstantValues.InstructionCode.MESSAGE_AUDIO_FLAG))
			return ConstantValues.InstructionCode.MESSAGE_TYPE_AUDIO;
		else
			return ConstantValues.InstructionCode.MESSAGE_TYPE_TEXT;
	}
	
	private boolean establishConnection()
	{
		if (connection == null)
		{
			ConnectionConfiguration connConfig = new ConnectionConfiguration(ConstantValues.Configs.OPENFIRE_SERVER_IP, ConstantValues.Configs.OPENFIRE_SERVER_PORT);
			connection = new XMPPConnection(connConfig);
		}
		
		if (!connection.isConnected())
		{
			try
			{
				connection.connect();
				Log.i("XMPPChatDemoActivity", "Connected to " + connection.getHost());
			} catch (XMPPException ex)
			{
				Log.e("XMPPChatDemoActivity", "Failed to connect to " + connection.getHost());
				Log.e("XMPPChatDemoActivity", ex.toString());
				connection = null;
				return false;
			}
		}
		
		return true;
	}

	public static void createuser(final String userName, final String password)
	{
		Thread td = new Thread(new Runnable()
		{
			@Override
			public void run()
			{
				ConnectionConfiguration connConfig = new ConnectionConfiguration(ConstantValues.Configs.OPENFIRE_SERVER_IP, ConstantValues.Configs.OPENFIRE_SERVER_PORT);
				XMPPConnection connection = new XMPPConnection(connConfig);
	
				try
				{
					connection.connect();
					
					Log.i("XMPPChatDemoActivity", "Connected to " + connection.getHost());
					AccountManager accountManager = new AccountManager(connection);
					boolean mark = accountManager.supportsAccountCreation();
					if (mark == true)
						Log.i("XMPPChatDemoActivity", "support" );
					try
					{
						Log.i("XMPPChatDemoActivity", "Creating user..................." );
						Map<String, String> attributes = new HashMap<String, String>();
						attributes.put("username", userName);
						attributes.put("password", "123456");
						attributes.put("email", userName + "@foo.com");
						attributes.put("name", "full_name" + userName);
						
					    accountManager.createAccount("REG_USERNAME", "REG_PASSWORD", attributes);
					} catch (XMPPException e) {
					    Log.d(e.getMessage(), "failed to create user");
					}
				} catch (XMPPException e) {
					Log.e("XMPPChatDemoActivity", "Create user error: Failed to connect to " + connection.getHost());
					Log.e("XMPPChatDemoActivity", e.toString());
				}
			}
		});
		td.start();
	}
}
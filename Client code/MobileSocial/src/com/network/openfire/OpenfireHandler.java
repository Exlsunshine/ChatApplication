package com.network.openfire;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.jivesoftware.smack.AccountManager;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.filter.MessageTypeFilter;
import org.jivesoftware.smack.filter.PacketFilter;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smack.util.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;

import com.configs.ConstantValues;

import android.os.Handler;
import android.util.Log;

public class OpenfireHandler
{
	private String userName = null;
	private String password = null;
	private Handler msgHandler = null;
	
	private XMPPConnection connection = null;
	
	public OpenfireHandler(final String userName, final String password, Handler msgHandler)
	{
		this.userName = userName;
		this.password = password;
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

	public void send(String text, String targetUserID)
	{
		targetUserID = targetUserID + "@" + ConstantValues.Configs.OPENFIRE_SERVER_NAME;

		Log.i("XMPPChatDemoActivity", "Sending text " + text + " to " + targetUserID);
		Message msg = new Message(targetUserID, Message.Type.chat);
		msg.setBody(text);
		
		if (establishConnection())
			connection.sendPacket(msg);
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
					
					notifyMsgHandler(msg);
				}
			}
		}, filter);
	}

	private String packMsg2JsonString(Message msg) throws JSONException
	{
		JSONObject json = new JSONObject();
		json.put("from", StringUtils.parseBareAddress(msg.getFrom()));
		json.put("body", msg.getBody());
		json.put("date", now());
		
		return json.toString();
	}
	
	private static String now()
	{
		Calendar cal = Calendar.getInstance();
	    SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd hh:mm:ss", Locale.CHINA);
	    return sdf.format(cal.getTime());
	}
	
	private void notifyMsgHandler(Message msg)
	{
		String str = null;
		try {
			str = packMsg2JsonString(msg);
		} catch (JSONException e) {
			str = null;
			e.printStackTrace();
		}
		
		String msgBody = msg.getBody();
		android.os.Message androidMsg = new android.os.Message();
		androidMsg.what = getMessageType(msgBody);
		androidMsg.obj = str;
		
		msgHandler.sendMessage(androidMsg);
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
			} catch (XMPPException ex) {
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
						attributes.put("password", password);
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
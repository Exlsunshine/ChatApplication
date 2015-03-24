package com.ui;

import java.io.IOException;
import java.util.ArrayList;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.commons.CommonUtil;
import com.commons.ConstantValues;
import com.dialog.Dialog;
import com.example.testmobiledatabase.R;
import com.message.AbstractMessage;
import com.message.AudioMessage;
import com.message.ConvertUtil;
import com.message.ImageMessage;
import com.message.TextMessage;
import com.user.ClientUser;
import com.user.FriendUser;

public class ClientUserUnitTest extends Activity
{
	private final static String DEBUG_TAG = "______ClientUserUnitTest";
	
	private ClientUser user = null;
	private FriendUser friend = null;
	private Button sendTxt, sendImg, recvTxt, recvImg, singin, getList, sendAudio, recvAudio, play, stop;
	private ImageView iv;
	private TextView tv;
	
	private Dialog dialog;
	private int imgIndex = 0;
	private int audioIndex = 0;
	private AudioMessage audioMsg;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_client_user_unit_test);
		
		init();
		
		play.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View arg0)
			{
				audioMsg.play(ClientUserUnitTest.this);
			}
		});
		
		stop.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View arg0)
			{
				audioMsg.stop();
			}
		});
		
		
		sendAudio.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View arg0)
			{
				try {
					AudioMessage audio = new AudioMessage(user.getID(), friend.getID(), 
							ConvertUtil.amr2Bytes("/mnt/sdcard/TestMobileDatabase/voice/FinalAudio.amr"), CommonUtil.now(), false);
					user.sendMsgTo(friend, audio);
				
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});
		
		recvAudio.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View arg0)
			{
				dialog = user.makeDialogWith(friend);
				String str = "";
				ArrayList<AbstractMessage> list = dialog.getDialogHistory();
				if (list.get(audioIndex).getMessageType() == ConstantValues.InstructionCode.MESSAGE_TYPE_AUDIO)
				{
					audioMsg = (AudioMessage)(list.get(audioIndex));
					Log.i(DEBUG_TAG, "Get audio message at " + String.valueOf(audioIndex) + " \nTotal message number is " + String.valueOf(list.size()));
				}	
				audioIndex = (audioIndex + 1) % list.size();
			}
		});
		
		singin.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View arg0)
			{
				user.signin();
			}
		});
		
		getList.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View arg0)
			{
				user.getFriendList();
			}
		});
		
		sendTxt.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View arg0)
			{
				TextMessage txtMsg = new TextMessage(user.getID(), friend.getID(), ConvertUtil.string2Bytes("这是一个测试this is a test!"), CommonUtil.now(), false);
				user.sendMsgTo(friend, txtMsg);
			}
		});
		
		sendImg.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View arg0)
			{
				Bitmap bmp = BitmapFactory.decodeFile("/mnt/sdcard/DCIM/Camera/test.jpg");
				ImageMessage imgMsg = new ImageMessage(user.getID(), friend.getID(), bmp, CommonUtil.now(), false);
				user.sendMsgTo(friend, imgMsg);
			}
		});
		
		recvTxt.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View arg0)
			{
				dialog = user.makeDialogWith(friend);
				String str = "";
				ArrayList<AbstractMessage> list = dialog.getDialogHistory();
				for (int i = 0; i < list.size(); i++)
				{
					if (list.get(i).getMessageType() != ConstantValues.InstructionCode.MESSAGE_TYPE_TEXT)
						continue;
					
					TextMessage textMsg = (TextMessage)(list.get(i));
					str += String.valueOf(textMsg.getFromUserID()) + "->" + String.valueOf(textMsg.getToUserID()) 
							+ " : " + textMsg.getText() + " at " + textMsg.getDate() + "\n";
				}
				tv.setText(str);
			}
		});
		
		recvImg.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View arg0)
			{
				dialog = user.makeDialogWith(friend);
				String str = "";
				ArrayList<AbstractMessage> list = dialog.getDialogHistory();
				Log.i(DEBUG_TAG, String.valueOf(imgIndex) + "/" + String.valueOf(list.size()));
				if (list.get(imgIndex).getMessageType() == ConstantValues.InstructionCode.MESSAGE_TYPE_IMAGE)
				{
					ImageMessage imgMsg = (ImageMessage)(list.get(imgIndex));
					str = String.valueOf(imgMsg.getFromUserID()) + "->" + String.valueOf(imgMsg.getToUserID()) 
							+ " at " + imgMsg.getDate() + "\n";
					iv.setBackground(ConvertUtil.bitmap2Drawable(imgMsg.getImage(), ClientUserUnitTest.this));
					Log.i(DEBUG_TAG, str);
				}
				imgIndex = (imgIndex + 1) % list.size();
			}
		});
	}
	
	private void init()
	{
		user = new ClientUser(4, "4", "4", this);
		friend = new FriendUser(5, null, null, null, null, null, null, null, null);
		
		sendTxt = (Button)findViewById(R.id.send_text);
		sendImg = (Button)findViewById(R.id.send_image);
		recvTxt = (Button)findViewById(R.id.receive_text);
		recvImg = (Button)findViewById(R.id.receive_img);
		singin = (Button)findViewById(R.id.signin);
		getList = (Button)findViewById(R.id.get_friend_list);
		sendAudio = (Button)findViewById(R.id.send_audio);
		recvAudio = (Button)findViewById(R.id.receive_audio);
		play = (Button)findViewById(R.id.play);
		stop = (Button)findViewById(R.id.stop);

		iv = (ImageView)findViewById(R.id.iv);
		tv = (TextView)findViewById(R.id.tv);
		tv.setMovementMethod(new ScrollingMovementMethod());
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.client_user_unit_test, menu);
		return true;
	}

}

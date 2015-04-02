package com.yg.ui;

import java.util.ArrayList;

import com.example.testmobiledatabase.R;
import com.yg.commons.ConstantValues;
import com.yg.dialog.Dialog;
import com.yg.message.AbstractMessage;
import com.yg.message.ImageMessage;
import com.yg.user.FriendUser;

import android.os.Bundle;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;

public class RecentDialogActivity extends Activity 
{
	private Button send, receive;
	private ImageView image;
	private FriendUser friend = new FriendUser(13, null, null, null, null, null, null, null, null, null, null, true);
	private static final String DEBUG_TAG = "RecentDialogActivity_____________";
	
	
	private int index = 0;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_recent_dialog);
		
		initLayout();
		setupClickListeners();
	}
	
	private void initLayout()
	{
		send = (Button)findViewById(R.id.activity_recent_dialog_send);
		receive = (Button)findViewById(R.id.activity_recent_dialog_receive);
		image = (ImageView)findViewById(R.id.activity_recent_dialog_image);
	}
	
	private void setupClickListeners()
	{
		send.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View arg0) 
			{
				Thread td = new Thread(new Runnable()
				{
					@Override
					public void run()
					{
						Bitmap bitmap = BitmapFactory.decodeFile("/mnt/sdcard/DCIM/Camera/test.jpg");
						ImageMessage imageMsg = new ImageMessage(10, 13, bitmap, "2015-04-04", true);
						ConstantValues.user.getFriendList();
						ConstantValues.user.sendMsgTo(friend, imageMsg);
					}
				});
				td.start();
			}
		});
		
		receive.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View arg0) 
			{
				Dialog dialog = ConstantValues.user.makeDialogWith(friend);
				ArrayList<AbstractMessage> msgs = dialog.getDialogHistory();
				
				if (index < msgs.size())
				{
					if (msgs.get(index).getMessageType() == ConstantValues.InstructionCode.MESSAGE_TYPE_IMAGE)
					{
						final ImageMessage imgMsg = (ImageMessage) msgs.get(index);
						runOnUiThread(new Runnable()
						{
							@Override
							public void run() 
							{
								image.setImageBitmap(imgMsg.getImage());
							}
						});
					}
					else
						Log.w(DEBUG_TAG, "Not a image. " + index);
				}
				
				index = (index + 1) % msgs.size();
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		getMenuInflater().inflate(R.menu.recent_dialog, menu);
		return true;
	}
}

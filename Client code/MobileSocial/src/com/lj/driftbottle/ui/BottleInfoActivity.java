package com.lj.driftbottle.ui;

import com.example.testmobiledatabase.R;
import com.lj.driftbottle.logic.CommBottle;
import com.lj.driftbottle.logic.FirstBottle;
import com.lj.setting.achievement.ThreadGameChallengFail;
import com.yg.commons.ConstantValues;
import com.yg.user.DownloadManager;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class BottleInfoActivity extends Activity
{
	private CommBottle bottle = null;
	private final int REPLY_HANDLER = 0x02;
	
	private Handler handler = new Handler()
	{
		public void handleMessage(android.os.Message msg) 
		{
			if (msg.what == REPLY_HANDLER)
			{
				Intent intent = new Intent(BottleInfoActivity.this, chuck_animation.class);
				startActivity(intent);
				setResult(RESULT_OK);
				finish();
			}
		};
	};
	
	private void setupDialogActionBar()
	{
		getActionBar().setBackgroundDrawable(new ColorDrawable(Color.rgb(0x1E, 0x90, 0xFF)));
		getActionBar().setDisplayShowHomeEnabled(false);
		getActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM); 
		getActionBar().setCustomView(R.layout.lj_common_actionbar);
	
		LinearLayout back = (LinearLayout)findViewById(R.id.lj_common_actionbar_back);
		TextView titleTextView = (TextView)findViewById(R.id.lj_common_actionbar_title);
		titleTextView.setText("漂流瓶");
		back.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				finish();
			}
		});
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.lj_bottle_info_layout);
		Intent intent = getIntent();
		int bottleID = intent.getIntExtra("bottleID", 0);
		bottle = DriftBottleActivity.bottleManager.getBottleByID(bottleID);
		
		BottleHistoryText bottleHistory = (BottleHistoryText)findViewById(R.id.lj_bottle_info_history);
		bottleHistory.setInfo(bottle);
		
	//	final BottleEditText bottleEdit = (BottleEditText)findViewById(R.id.lj_bottle_info_append);
		
	/*	Button btn = bottleEdit.getSendBtn();
		btn.setOnClickListener(new OnClickListener() 
		{
			@Override
			public void onClick(View arg0) 
			{
				String text = bottleEdit.getText();
				if (bottleEdit.isTextLengthValid(text.length()))
				{
					bottle.appentText(text);
					new Thread(new Runnable() 
					{
						@Override
						public void run() 
						{
							DriftBottleActivity.bottleManager.reply(bottle);
							handler.sendEmptyMessage(REPLY_HANDLER);
						}
					}).start();
				}
				else
					bottleEdit.shake();
			}
		});*/
		/*findViewById(R.id.lj_bottle_info_reply).setOnClickListener(new OnClickListener() 
		{
			@Override
			public void onClick(View arg0) 
			{
				EditText editText = (EditText) findViewById(R.id.lj_bottle_info_append);
				String appendText = editText.getText().toString();
				if (appendText.length() == 0)
					Toast.makeText(getApplicationContext(), "你未写入消息", Toast.LENGTH_SHORT).show();
				bottle.appentText(appendText);
				new Thread(new Runnable() {
					@Override
					public void run() {
						DriftBottleActivity.bottleManager.reply(bottle);
						handler.sendEmptyMessage(REPLY_HANDLER);
					}
				}).start();
				
			}
		});*/
		setupDialogActionBar();
	}
}

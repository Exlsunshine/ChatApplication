package com.lj.driftbottle.ui;

import com.example.testmobiledatabase.R;
import com.lj.driftbottle.logic.CommBottle;
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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class BottleInfoActivity extends Activity
{
	private CommBottle bottle = null;
	private final int PORTRAIT_DOWNLOAD_HANDLER = 0x01;
	private final int REPLY_HANDLER = 0x02;
	
	private Handler handler = new Handler()
	{
		public void handleMessage(android.os.Message msg) 
		{
			if (msg.what == PORTRAIT_DOWNLOAD_HANDLER)
			{
				Bitmap bitmap = (Bitmap) msg.obj;
				((ImageView)findViewById(R.id.lj_bottle_info_img)).setImageBitmap(bitmap);
			}
			else if (msg.what == REPLY_HANDLER)
			{
				Intent intent = new Intent(BottleInfoActivity.this, chuck_animation.class);
				startActivity(intent);
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
		((TextView)findViewById(R.id.lj_bottle_info_history)).setText(bottle.getHistoryText());
		
		findViewById(R.id.lj_bottle_info_reply).setOnClickListener(new OnClickListener() 
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
		});
		setupDialogActionBar();
		new Thread(new GetPortraitRun(bottle.getPortraitURL())).start();
	}
	
	private class GetPortraitRun implements Runnable
	{
		private String url;
		public GetPortraitRun(String url) 
		{
			this.url = url;
		}
		@Override
		public void run()
		{
			Bitmap bitmap = MyBottle.portraitTable.get(url);
			if (bitmap == null)
			{
				bitmap = new DownloadManager(url).getBmpFile();
				MyBottle.portraitTable.put(url, bitmap);
			}
			Message msg = new Message();
			msg.what = PORTRAIT_DOWNLOAD_HANDLER;
			msg.obj = bitmap;
			handler.sendMessage(msg);
		}
	}
}

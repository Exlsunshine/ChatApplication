package com.lj.driftbottle.ui;

import java.util.Calendar;

import com.example.testmobiledatabase.R;
import com.lj.driftbottle.logic.BottleManager;
import com.lj.driftbottle.logic.CommBottle;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;

public class PickupBottle extends Activity 
{
	private BottleManager bottleManager = null;
	private AnimationDrawable ad;
	private ImageView pick_spray1, pick_spray2, voice_msg, close;
	private RelativeLayout pick_up_layout;
	int hour, minute, sec;
	
	private final int NO_BOTTLE_HANDLER = 0x01;
	private final int PICK_BOTTLE_HANDLER = 0x02;

	private Handler handler = new Handler()
	{
		public void handleMessage(android.os.Message msg)
		{
			if (msg.what == NO_BOTTLE_HANDLER)
				voice_msg.setBackgroundResource(R.drawable.lj_bottle_star);
			else if (msg.what == PICK_BOTTLE_HANDLER)
			{
				final int bottleID = msg.arg1;
				voice_msg.setBackgroundResource(R.drawable.bottle_picked_text_msg);
				voice_msg.setOnClickListener(new OnClickListener() 
				{
					@Override
					public void onClick(View arg0) 
					{
						Intent intent = new Intent();
						intent.putExtra("bottleID", bottleID);
						intent.setClass(getApplicationContext(), BottleInfoActivity.class);
						startActivity(intent);
						finish();
					}
				});
			}
		};
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.pick_up_bottle);
		bottleManager = BottleManager.getInstance();
		// 初始化
		pick_spray1 = (ImageView) findViewById(R.id.lj_pick_spray1);
		pick_spray2 = (ImageView) findViewById(R.id.lj_pick_spray2);
		voice_msg = (ImageView) findViewById(R.id.bottle_picked_voice_msg);
		close = (ImageView) findViewById(R.id.bottle_picked_close);
		pick_up_layout = (RelativeLayout) findViewById(R.id.pick_up_layout);

		// 获取系统时间
		Calendar c = Calendar.getInstance();
		c.setTimeInMillis(System.currentTimeMillis());
		hour = c.get(Calendar.HOUR_OF_DAY);
		minute = c.get(Calendar.MINUTE);
		sec = c.get(Calendar.SECOND);
		if (hour >= 18 || hour <= 6)
			pick_up_layout.setBackgroundResource(R.drawable.bottle_pick_bg_spotlight_night);
		else
			pick_up_layout.setBackgroundResource(R.drawable.bottle_pick_bg_spotlight_day);
		close.setOnClickListener(new OnClickListener() 
		{
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		new Handler().postDelayed(new Runnable() {

			@Override
			public void run() {
				pick_spray1.setVisibility(View.VISIBLE);
				ad.setOneShot(true);
				ad.start();
			}
		}, 1000);
		new Handler().postDelayed(new Runnable() {

			@Override
			public void run() {
				pick_spray1.setVisibility(View.GONE);
				pick_spray2.setVisibility(View.VISIBLE);
				ad.setOneShot(true);
				ad.start();
			}
		}, 2000);

		new Handler().postDelayed(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				pick_spray1.setVisibility(View.GONE);
				pick_spray2.setVisibility(View.GONE);
				voice_msg.setVisibility(View.VISIBLE);
				doStartAnimation(R.anim.pick_up_scale);
				close.setVisibility(View.VISIBLE);
			}
		}, 3000);
		
		new Thread(new Runnable() 
		{
			@Override
			public void run() 
			{
				CommBottle bottle = bottleManager.pickup();
				if (bottle == null)
				{
					handler.sendEmptyMessage(NO_BOTTLE_HANDLER);
					return;
				}
				else
				{
					Message msg = new Message();
					msg.what = PICK_BOTTLE_HANDLER;
					msg.arg1 = bottle.getBottleID();
					handler.sendMessage(msg);
				}
			}
		}).start();
	}

	private void doStartAnimation(int animId) {
		Animation animation = AnimationUtils.loadAnimation(this, animId);
		voice_msg.startAnimation(animation);
	}

	// 播放语音动画
	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		// TODO Auto-generated method stub
		super.onWindowFocusChanged(hasFocus);
		ad = (AnimationDrawable) getResources().getDrawable(
				R.anim.pick_up_spray);
		if (pick_spray1 != null && pick_spray2 != null) {
			pick_spray1.setBackground(ad);
			pick_spray2.setBackground(ad);
		}

	}

}

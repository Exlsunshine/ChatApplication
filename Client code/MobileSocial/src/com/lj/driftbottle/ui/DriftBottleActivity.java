package com.lj.driftbottle.ui;

import java.util.Calendar;

import com.example.testmobiledatabase.R;
import com.lj.driftbottle.logic.BottleManager;
import com.lj.driftbottle.logic.FirstBottle;
import com.yg.commons.ConstantValues;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.View.OnClickListener;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class DriftBottleActivity extends Activity
{
	public static BottleManager bottleManager = null;
	private PopupWindow popupWindow;
	private EditText chuck_edit;
	private TranslateAnimation ani0, ani1, ani2;
	private int m = 2;
	private AnimationDrawable ad, ad1, ad2;

	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.lj_drift_bottle_main_layout);
		if (bottleManager == null)
			bottleManager = new BottleManager(ConstantValues.user.getID());
		
		setBackground();
		setAnimation();
		setClickListener();
	}
	
	private TranslateAnimation createAnimation(float fromX, float toX, float fromY, float toY)
	{
		TranslateAnimation animation = new TranslateAnimation(Animation.RELATIVE_TO_PARENT, fromX, Animation.RELATIVE_TO_PARENT, toX, Animation.RELATIVE_TO_SELF, fromY, Animation.RELATIVE_TO_SELF, toY);
		animation.setInterpolator(new AccelerateDecelerateInterpolator());
		animation.setDuration(15000);
		animation.setFillEnabled(true);
		animation.setFillAfter(true);
		animation.setAnimationListener(animationListener);
		return animation;
	}
	
	private void setAnimation()
	{
		ani0 = createAnimation(0.0f, 0.3f, 0.0f, -0.3f);
		ani1 = createAnimation(0.3f, 0.6f, -0.3f, 0.2f);
		ani2 = createAnimation(0.6f, 0.0f, 0.2f, 0.0f);
		findViewById(R.id.bottle_img).startAnimation(ani0);
	}
	
	private void setClickListener()
	{
		findViewById(R.id.bottle_night_throw_bottle).setOnClickListener(listener);
		findViewById(R.id.bottle_night_pick_bottle).setOnClickListener(listener);
		findViewById(R.id.bottle_night_my_bottle).setOnClickListener(listener);

		
		findViewById(R.id.bottle_bottom_throw_bottle).setOnClickListener(listener);
		findViewById(R.id.bottle_bottom_pick_bottle).setOnClickListener(listener);
		findViewById(R.id.bottle_bottom_my_bottle).setOnClickListener(listener);
		
		findViewById(R.id.bottle_back).setOnClickListener(listener);
	}
	
	private boolean isNight()
	{
		Calendar c = Calendar.getInstance();
		c.setTimeInMillis(System.currentTimeMillis());
		int hour = c.get(Calendar.HOUR_OF_DAY);
		return hour >=18 || hour <= 6;
	}
	
	private void setBackground()
	{
		RelativeLayout bottle_main_layout = (RelativeLayout) findViewById(R.id.bottle_main_layout);
		RelativeLayout bottle_bottom_layout = (RelativeLayout) findViewById(R.id.bottle_bottom_layout);
		RelativeLayout bottle_night_bottom_layout = (RelativeLayout) findViewById(R.id.bottle_night_bottom_layout);
		ImageView bottle_night_moon = (ImageView) findViewById(R.id.bottle_night_moon);
		ImageView ballonImg = (ImageView) findViewById(R.id.bottle_img);
		ImageView bottle_night_floodlight = (ImageView) findViewById(R.id.bottle_night_floodlight);
		if (isNight())
		{
			bottle_main_layout.setBackgroundResource(R.drawable.bottle_night_bg);
			ballonImg.setVisibility(View.GONE);
			bottle_bottom_layout.setVisibility(View.GONE);
			bottle_night_moon.setVisibility(View.VISIBLE);
			bottle_night_floodlight.setVisibility(View.VISIBLE);
			bottle_night_bottom_layout.setVisibility(View.VISIBLE);

			new Handler().postDelayed(new Runnable() {

				@Override
				public void run() {
					ad.setOneShot(false);
					ad.start();
				}
			}, 1000);
		}
		else
		{
			bottle_main_layout.setBackgroundResource(R.drawable.bottle_day_bg);
			ballonImg.setVisibility(View.VISIBLE);
			bottle_bottom_layout.setVisibility(View.VISIBLE);
			bottle_night_moon.setVisibility(View.GONE);
			bottle_night_floodlight.setVisibility(View.GONE);
			bottle_night_bottom_layout.setVisibility(View.GONE);
		}
	}
	
	private final int THROW_BOTTLE_HANDLER = 0x01;

	private Handler handler = new Handler()
	{
		public void handleMessage(android.os.Message msg) 
		{
			if (msg.what == THROW_BOTTLE_HANDLER)
			{
				Toast.makeText(getApplicationContext(), "您扔了一个漂流瓶", Toast.LENGTH_SHORT).show();
				Intent intent = new Intent(DriftBottleActivity.this, chuck_animation.class);
				startActivity(intent);
				popupWindow.dismiss();
			}
		};
	};
	
	private OnClickListener listener = new OnClickListener() 
	{
		@Override
		public void onClick(View v) 
		{
			int id = v.getId();
			if (id == R.id.bottle_bottom_throw_bottle || id == R.id.bottle_night_throw_bottle)
			{
				initChuckPop();
				popupWindow.showAtLocation(findViewById(R.id.bottle_bottom_throw_bottle), Gravity.BOTTOM, 0, 0);
			}
			else if (id == R.id.bottle_bottom_pick_bottle || id == R.id.bottle_night_pick_bottle)
			{
				Intent intent = new Intent(DriftBottleActivity.this, PickupBottle.class);
				startActivity(intent);
			}
			else if (id == R.id.bottle_bottom_my_bottle || id == R.id.bottle_night_my_bottle)
			{
				Intent intent = new Intent(DriftBottleActivity.this, MyBottle.class);
				startActivity(intent);
			}
			else if (id == R.id.bottle_back)
				finish();
			else if (id == R.id.lj_chuck_cancle)
				popupWindow.dismiss();
			else if (id == R.id.lj_chuck_throw)
			{
				String text = chuck_edit.getText().toString();
				if (text.length() == 0)
				{
					Toast.makeText(getApplicationContext(), "你未写入消息", Toast.LENGTH_SHORT).show();
					return;
				}
				final FirstBottle firstBottle = bottleManager.createNewBottle();
				firstBottle.appentText(text);
				new Thread(new Runnable() 
				{
					@Override
					public void run() 
					{
						bottleManager.throwBottle(firstBottle);
						handler.sendEmptyMessage(THROW_BOTTLE_HANDLER);
					}
				}).start();
			}
		}
	};

	final AnimationListener animationListener = new AnimationListener() {

		@Override
		public void onAnimationEnd(Animation animation) 
		{
			ImageView ballonImg = (ImageView) findViewById(R.id.bottle_img);
			if (animation == ani0)
				ballonImg.startAnimation(ani1);
			if (animation == ani1)
				ballonImg.startAnimation(ani2);
			if (animation == ani2)
				ballonImg.startAnimation(ani0);
		}

		@Override
		public void onAnimationRepeat(Animation animation) {
		}

		@Override
		public void onAnimationStart(Animation animation) {
		}
	};

	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		super.onWindowFocusChanged(hasFocus);

		ImageView bottle_night_floodlight = (ImageView) findViewById(R.id.bottle_night_floodlight);
		ImageView bottle_night_floodlight_1 = (ImageView) findViewById(R.id.bottle_night_floodlight_1);
		ImageView bottle_night_floodlight_2 = (ImageView) findViewById(R.id.bottle_night_floodlight_2);

		if (bottle_night_floodlight != null) {
			ad = (AnimationDrawable) getResources().getDrawable(
					R.anim.bottle_night_floodlight);
			bottle_night_floodlight.setBackgroundDrawable(ad);
		}

		else if (bottle_night_floodlight_1 != null) {
			ad1 = (AnimationDrawable) getResources().getDrawable(
					R.anim.bottle_night_floodlight1);
			bottle_night_floodlight_1.setBackgroundDrawable(ad1);
		} else if (bottle_night_floodlight_2 != null) {
			ad2 = (AnimationDrawable) getResources().getDrawable(
					R.anim.bottle_night_floodlight2);
			bottle_night_floodlight_2.setBackgroundDrawable(ad2);
		}
	}

	private void popupKeyboard(View view)
	{
		InputMethodManager inputMethodManager = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
		inputMethodManager.showSoftInput(view,InputMethodManager.RESULT_SHOWN);
		inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
	}
	
	// 扔瓶子的窗口
	public void initChuckPop() 
	{
		LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
		View popView = inflater.inflate(R.layout.chuck_pop, null);
		popupWindow = new PopupWindow(popView, WindowManager.LayoutParams.FILL_PARENT, WindowManager.LayoutParams.FILL_PARENT);
		ColorDrawable colorDrawable = new ColorDrawable(0xb0000000);
		popupWindow.setBackgroundDrawable(colorDrawable);
		popupWindow.setTouchable(true);
		popupWindow.setOutsideTouchable(true);
		popupWindow.setFocusable(true);

		popView.setOnTouchListener(new OnTouchListener() 
		{
			@Override
			public boolean onTouch(View v, MotionEvent event) 
			{
				popupWindow.dismiss();
				popupWindow = null;
				return true;
			}
		});

		chuck_edit = (EditText) popView.findViewById(R.id.lj_chuck_edit);
		popView.findViewById(R.id.lj_chuck_cancle).setOnClickListener(listener);
		popView.findViewById(R.id.lj_chuck_throw).setOnClickListener(listener);

		chuck_edit.setVisibility(View.VISIBLE);
		popupKeyboard(chuck_edit);
	}

	// 屏幕触摸事件
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// 获取触摸坐标
		RelativeLayout layout = (RelativeLayout) findViewById(R.id.bottle_layout_title);

		switch (event.getAction()) {
		// //触摸屏幕时刻
		case MotionEvent.ACTION_DOWN:
			m++;
			if (m % 2 == 0)
				layout.setVisibility(View.GONE);
			else
				layout.setVisibility(View.VISIBLE);

			break;
		}
		return super.onTouchEvent(event);

	}

}

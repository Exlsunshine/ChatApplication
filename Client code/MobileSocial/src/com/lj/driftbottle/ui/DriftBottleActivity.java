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
import android.view.animation.AnimationUtils;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class DriftBottleActivity extends Activity
{
	public static BottleManager bottleManager = null;
	private PopupWindow popupWindow, pop1;
	private View view;
	private LayoutInflater inflater;
	RelativeLayout layout, bottle_main_layout, bottle_bottom_layout,
			bottle_night_bottom_layout;
	private ImageView bottle_night_moon, bottle_night_floodlight,
			bottle_night_floodlight_1, bottle_night_floodlight_2;
	private EditText chuck_edit;
	private Button chuck_cancle, chuck_throw, bottle_back;
	private ImageView bottle_img, bottle_throw_bottle, bottle_pick_bottle, bottle_my_bottle, bottle_setting;
	private TranslateAnimation ani0, ani1, ani2;
	ImageView chuck_empty2, chuck_empty1, chuck_spray;
	ImageView bottle_night_throw_bottle, bottle_night_pick_bottle, bottle_night_my_bottle;
	int m = 2;

	int hour, minute, sec;

	AnimationDrawable ad, ad1, ad2;

	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.lj_drift_bottle_main_layout);
		if (bottleManager == null)
			bottleManager = new BottleManager(ConstantValues.user.getID());

		// 初始化
		//漂流瓶主leyout
		bottle_main_layout = (RelativeLayout) findViewById(R.id.bottle_main_layout);
		//底部 扔，捡，我的瓶子 按钮layout
		bottle_bottom_layout = (RelativeLayout) findViewById(R.id.bottle_bottom_layout);
		bottle_night_bottom_layout = (RelativeLayout) findViewById(R.id.bottle_night_bottom_layout);
		
		bottle_night_moon = (ImageView) findViewById(R.id.bottle_night_moon);
		
		bottle_night_floodlight = (ImageView) findViewById(R.id.bottle_night_floodlight);
		bottle_night_floodlight_1 = (ImageView) findViewById(R.id.bottle_night_floodlight_1);
		bottle_night_floodlight_2 = (ImageView) findViewById(R.id.bottle_night_floodlight_2);
		
		bottle_night_throw_bottle = (ImageView) findViewById(R.id.bottle_night_throw_bottle);
		bottle_night_pick_bottle = (ImageView) findViewById(R.id.bottle_night_pick_bottle);
		bottle_night_my_bottle = (ImageView) findViewById(R.id.bottle_night_my_bottle);
		bottle_night_throw_bottle.setOnClickListener(listener);
		bottle_night_pick_bottle.setOnClickListener(listener);
		bottle_night_my_bottle.setOnClickListener(listener);

		//热气球
		bottle_img = (ImageView) findViewById(R.id.bottle_img);
		bottle_throw_bottle = (ImageView) findViewById(R.id.bottle_bottom_throw_bottle);
		bottle_throw_bottle.setOnClickListener(listener);
		bottle_pick_bottle = (ImageView) findViewById(R.id.bottle_bottom_pick_bottle);
		bottle_pick_bottle.setOnClickListener(listener);
		bottle_my_bottle = (ImageView) findViewById(R.id.bottle_bottom_my_bottle);
		bottle_my_bottle.setOnClickListener(listener);
		
		//title栏
		bottle_setting = (ImageView) findViewById(R.id.bottle_setting);
		
		//title layout
		layout = (RelativeLayout) findViewById(R.id.bottle_layout_title);
		bottle_back = (Button) findViewById(R.id.bottle_back);
		bottle_back.setOnClickListener(listener);
		bottle_setting.setOnClickListener(listener);
		ani0 = new TranslateAnimation(Animation.RELATIVE_TO_PARENT, 0.0f,
				Animation.RELATIVE_TO_PARENT, 0.3f, Animation.RELATIVE_TO_SELF,
				0.0f, Animation.RELATIVE_TO_SELF, -0.3f);
		ani1 = new TranslateAnimation(Animation.RELATIVE_TO_PARENT, 0.3f,
				Animation.RELATIVE_TO_PARENT, 0.6f, Animation.RELATIVE_TO_SELF,
				-0.3f, Animation.RELATIVE_TO_SELF, 0.2f);
		ani2 = new TranslateAnimation(Animation.RELATIVE_TO_PARENT, 0.6f,
				Animation.RELATIVE_TO_PARENT, 0.0f, Animation.RELATIVE_TO_SELF,
				0.2f, Animation.RELATIVE_TO_SELF, 0.0f);
		ani0.setInterpolator(new AccelerateDecelerateInterpolator());
		ani0.setDuration(15000);
		ani0.setFillEnabled(true);
		ani0.setFillAfter(true);
		ani0.setAnimationListener(animationListener);
		ani1.setInterpolator(new AccelerateDecelerateInterpolator());
		ani1.setDuration(15000);
		ani1.setFillEnabled(true);
		ani1.setFillAfter(true);
		ani1.setAnimationListener(animationListener);
		ani2.setInterpolator(new AccelerateDecelerateInterpolator());
		ani2.setDuration(15000);
		ani2.setFillEnabled(true);
		ani2.setFillAfter(true);
		ani2.setAnimationListener(animationListener);

		bottle_img.startAnimation(ani0);

		// 获取系统时间
		Calendar c = Calendar.getInstance();
		c.setTimeInMillis(System.currentTimeMillis());
		hour = c.get(Calendar.HOUR_OF_DAY);
		minute = c.get(Calendar.MINUTE);
		sec = c.get(Calendar.SECOND);
		if (hour >= 18 || hour <= 6) {
			bottle_main_layout
					.setBackgroundResource(R.drawable.bottle_night_bg);
			bottle_img.setVisibility(View.GONE);
			bottle_bottom_layout.setVisibility(View.GONE);
			bottle_night_moon.setVisibility(View.VISIBLE);
			bottle_night_floodlight.setVisibility(View.VISIBLE);
			bottle_night_bottom_layout.setVisibility(View.VISIBLE);
			// 探照灯
			// ad=(AnimationDrawable)
			// getResources().getDrawable(R.anim.bottle_night_floodlight);
			// bottle_night_floodlight.setBackgroundDrawable(ad);
			// ad=(AnimationDrawable) bottle_night_floodlight.getBackground();
			// ad.setOneShot(false);
			// ad.start();

			new Handler().postDelayed(new Runnable() {

				@Override
				public void run() {
					ad.setOneShot(false);
					ad.start();
				}
			}, 1000);

		} else {
			bottle_main_layout.setBackgroundResource(R.drawable.bottle_day_bg);
			bottle_img.setVisibility(View.VISIBLE);
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
			else if (id == R.id.bottle_setting)
			{
				Intent intent = new Intent(DriftBottleActivity.this, Driftbottle_setting.class);
				startActivity(intent);
			}
			else if (id == R.id.chuck_cancle)
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

	private void doStartAnimation(int animId) {
		Animation animation = AnimationUtils.loadAnimation(this, animId);
		chuck_empty1.startAnimation(animation);
		chuck_empty2.startAnimation(animation);
	}

	final AnimationListener animationListener = new AnimationListener() {

		@Override
		public void onAnimationEnd(Animation animation) {
			if (animation == ani0) {
				bottle_img.startAnimation(ani1);
			}
			if (animation == ani1) {
				bottle_img.startAnimation(ani2);
			}
			if (animation == ani2) {
				bottle_img.startAnimation(ani0);
			}
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
		// TODO Auto-generated method stubs
		super.onWindowFocusChanged(hasFocus);

		if (bottle_night_floodlight != null) {
			ad = (AnimationDrawable) getResources().getDrawable(
					R.anim.bottle_night_floodlight);
			bottle_night_floodlight.setBackgroundDrawable(ad);
		}

		else if (bottle_night_floodlight_1 != null) {
			ad1 = (AnimationDrawable) getResources().getDrawable(
					R.anim.bottle_night_floodlight1);
			bottle_night_floodlight_1.setBackgroundDrawable(ad);
		} else if (bottle_night_floodlight_2 != null) {
			ad2 = (AnimationDrawable) getResources().getDrawable(
					R.anim.bottle_night_floodlight2);
			bottle_night_floodlight_2.setBackgroundDrawable(ad);
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
		inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
		view = inflater.inflate(R.layout.chuck_pop, null);
		popupWindow = new PopupWindow(view, WindowManager.LayoutParams.FILL_PARENT, WindowManager.LayoutParams.FILL_PARENT);
		ColorDrawable colorDrawable = new ColorDrawable(0xb0000000);
		popupWindow.setBackgroundDrawable(colorDrawable);
		popupWindow.setTouchable(true);
		popupWindow.setOutsideTouchable(true);
		popupWindow.setFocusable(true);

		view.setOnTouchListener(new OnTouchListener() 
		{
			@Override
			public boolean onTouch(View v, MotionEvent event) 
			{
				popupWindow.dismiss();
				popupWindow = null;
				return true;
			}
		});

		chuck_edit = (EditText) view.findViewById(R.id.lj_chuck_edit);
		chuck_cancle = (Button) view.findViewById(R.id.lj_chuck_cancle);
		chuck_throw = (Button) view.findViewById(R.id.lj_chuck_throw);

		chuck_cancle.setOnClickListener(listener);
		chuck_throw.setOnClickListener(listener);
		
		chuck_edit.setVisibility(View.VISIBLE);
		popupKeyboard(chuck_edit);
	}

	// 扔瓶子的动画
	public void initChuckPop1() {
		inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
		view = inflater.inflate(R.layout.chuck_pop1, null);
		pop1 = new PopupWindow(view, WindowManager.LayoutParams.FILL_PARENT,
				WindowManager.LayoutParams.FILL_PARENT);
		pop1.setTouchable(true);
		pop1.setOutsideTouchable(true);
		pop1.setFocusable(true);

		view.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				int height = view.findViewById(R.id.chuck_pop_layout).getTop();
				int y = (int) event.getY();
				if (event.getAction() == MotionEvent.ACTION_UP
						|| event.getAction() == MotionEvent.ACTION_DOWN) {
					if (y < height) {
						pop1.dismiss();
						pop1 = null;
					}
					if (y > height) {
						pop1.dismiss();
						pop1 = null;
					}
				}
				return true;
			}
		});

		chuck_empty2 = (ImageView) view.findViewById(R.id.lj_chuck_empty2);
		chuck_empty1 = (ImageView) view.findViewById(R.id.lj_chuck_empty1);
		chuck_spray = (ImageView) view.findViewById(R.id.lj_chuck_spray);

	}

	// 屏幕触摸事件
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// 获取触摸坐标
		float x = event.getX();
		float y = event.getY();

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

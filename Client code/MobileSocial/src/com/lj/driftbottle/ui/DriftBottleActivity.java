package com.lj.driftbottle.ui;

import java.util.Calendar;

import com.example.testmobiledatabase.R;
import com.lj.driftbottle.logic.BottleManager;
import com.lj.driftbottle.logic.FirstBottle;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.yg.commons.ConstantValues;

import android.app.Activity;
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
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.TranslateAnimation;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class DriftBottleActivity extends Activity
{
	private BottleManager bottleManager = null;
	private PopupWindow popupWindow;
	private TranslateAnimation ani0, ani1, ani2;
	private AnimationDrawable ad1;

	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.lj_drift_bottle_main_layout);
		bottleManager = BottleManager.getInstance();
		bottleManager.init(ConstantValues.user.getID());
		ImageLoader imageLoader = ImageLoader.getInstance();
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getApplicationContext()).build();
		imageLoader.init(config);
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
		if (isNight())
		{
			bottle_main_layout.setBackgroundResource(R.drawable.bottle_night_bg);
			ballonImg.setVisibility(View.GONE);
			bottle_bottom_layout.setVisibility(View.GONE);
			bottle_night_moon.setVisibility(View.VISIBLE);
			bottle_night_bottom_layout.setVisibility(View.VISIBLE);
		}
		else
		{
			bottle_main_layout.setBackgroundResource(R.drawable.bottle_day_bg);
			ballonImg.setVisibility(View.VISIBLE);
			bottle_bottom_layout.setVisibility(View.VISIBLE);
			bottle_night_moon.setVisibility(View.GONE);
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
				startActivityForResult(intent, 0);
			}
			else if (id == R.id.bottle_back)
				finish();
			else if (id == R.id.lj_throw_bottle_back)
				popupWindow.dismiss();
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

		ImageView bottle_night_floodlight_1 = (ImageView) findViewById(R.id.bottle_night_floodlight_1);

		if (bottle_night_floodlight_1 != null) 
		{
			ad1 = (AnimationDrawable) getResources().getDrawable(R.anim.bottle_night_floodlight1);
			bottle_night_floodlight_1.setBackground(ad1);
		} 
	}

	// ��ƿ�ӵĴ���
	public void initChuckPop() 
	{
		LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
		ViewGroup group = null;
		View popView = inflater.inflate(R.layout.chuck_pop, group, false);
		popupWindow = new PopupWindow(popView, WindowManager.LayoutParams.MATCH_PARENT, findViewById(R.id.bottle_main_layout).getHeight());
		ColorDrawable colorDrawable = new ColorDrawable(0xb0000000);
		popupWindow.setBackgroundDrawable(colorDrawable);
		popupWindow.setTouchable(true);
		popupWindow.setOutsideTouchable(true);
		popupWindow.setFocusable(true);

		
		final EditText bottleEdit = (EditText) popView.findViewById(R.id.lj_driftbottle_edit);
		final TextView textView = (TextView) popView.findViewById(R.id.lj_bottle_throw_num);
		bottleEdit.addTextChangedListener(new EditWatcher(bottleEdit, textView));
		popView.findViewById(R.id.lj_bottle_throw_btn).setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View arg0) 
			{
				String text = bottleEdit.getText().toString();
				if (EditWatcher.isTextLengthValid(text))
				{
					final FirstBottle firstBottle = bottleManager.createNewBottle();
					firstBottle.appentText(text, ConstantValues.user.getID());
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
				else
				{
					Animation shake = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.yg_loginguide_page3_login_dialog_anim_shake);
					textView.startAnimation(shake);
				}
			}
		});
		popView.findViewById(R.id.lj_throw_bottle_back).setOnClickListener(listener);
	}

	// ��Ļ�����¼�
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// ��ȡ��������
		RelativeLayout layout = (RelativeLayout) findViewById(R.id.bottle_layout_title);

		switch (event.getAction()) {
		// //������Ļʱ��
		case MotionEvent.ACTION_DOWN:
			if (layout.getVisibility() == View.GONE)
				layout.setVisibility(View.VISIBLE);
			else if (layout.getVisibility() == View.VISIBLE)
				layout.setVisibility(View.GONE);
			break;
		}
		return super.onTouchEvent(event);

	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == 0 && resultCode == RESULT_OK)
		{
			initChuckPop();
			popupWindow.showAtLocation(findViewById(R.id.bottle_bottom_throw_bottle), Gravity.BOTTOM, 0, 0);
		}
	}
	

}

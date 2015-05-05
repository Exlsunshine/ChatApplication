package com.lj.shake;

import java.util.ArrayList;


import com.example.testmobiledatabase.R;
import com.lj.shake.UserShakeData;
import com.yg.commons.ConstantValues;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;

public class RelativeLayoutUserInfoList extends FrameLayout
{
	private final float MOVE_THREAD = 0.5f;
	private final float TOUCH_THREAD = 1.5f;
	private final int SPEED_THREOLD = 500;
	
	private FrameLayoutUserInfo gUserInfoViewCenter = null;
	private FrameLayoutUserInfo gUserInfoViewLeft = null;
	private FrameLayoutUserInfo gUserInfoViewRight = null;
	private GestureDetector gestureDetector;
	private UserDataModel gUserData = null;
	private int gViewWidth = 0;
	private int gViewHeight = 0;
	private int gPhoneWidth = 0;
	
	private int gTop = 0;
	private int gCenterLeft = 0;
	private int gLeftLeft = 0;
	private int gRightLeft = 0;
	
	private boolean flag = true;
	
	private Handler gHandler;
	
	
	
	private AnimationListener viewTranslateFalseListener = new AnimationListener() 
	{
		@Override
		public void onAnimationStart(Animation animation) 
		{
		}
		
		@Override
		public void onAnimationRepeat(Animation animation) 
		{
		}
		
		@Override
		public void onAnimationEnd(Animation animation) 
		{
			setData();
			gUserInfoViewCenter.clearAnimation();
			gUserInfoViewLeft.clearAnimation();
			gUserInfoViewRight.clearAnimation();
			gUserInfoViewCenter.layout(gCenterLeft, gTop, gCenterLeft + gViewWidth, gTop + gViewHeight);
			gUserInfoViewLeft.layout(gLeftLeft, gTop, gLeftLeft + gViewWidth, gTop + gViewHeight);
			gUserInfoViewRight.layout(gRightLeft, gTop, gRightLeft + gViewWidth, gTop + gViewHeight);
			Message msg = new Message();
			msg.what = ConstantValues.InstructionCode.SHAKE_HANDLER_CHANGE_MARK;
			msg.arg1 = gUserData.getCurrentIndex();
			gHandler.sendMessage(msg);
		}
	};
	
	private void startTranslateAnimation(int offset)
	{
		TranslateAnimation animation = new TranslateAnimation(0, offset, 0, 0);
		animation.setDuration(300);
		animation.setAnimationListener(viewTranslateFalseListener);
		gUserInfoViewRight.startAnimation(animation);
		gUserInfoViewCenter.startAnimation(animation);
		gUserInfoViewLeft.startAnimation(animation);
	}
	
	GestureDetector.OnGestureListener onGestureListener = new GestureDetector.SimpleOnGestureListener()
	{
		public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) 
		{
		
			if (Math.abs(velocityX) - Math.abs(velocityY) >= SPEED_THREOLD)
			{
				int x = (int) gUserInfoViewCenter.getX();
				if (velocityX > 0)
				{
					startTranslateAnimation(gRightLeft - x);
					gUserData.gotoPrior();
					return true;
				}
				else
				{
					startTranslateAnimation(gLeftLeft - x);
					gUserData.gotoNext();
					return true;
				}
			}
			return false;
		};
	};
	private OnTouchListener userInfoCenterOnTouchListener = new OnTouchListener() 
	{
		private int touchX;
		
		
		@Override
		public boolean onTouch(View v, MotionEvent event)
		{
			if (gestureDetector.onTouchEvent(event))
				return true;
			int action = event.getAction();
			if (action == MotionEvent.ACTION_DOWN)
			{
				touchX = (int) event.getX();
				if (flag)
				{
					gCenterLeft = gUserInfoViewCenter.getLeft();
					gLeftLeft = gUserInfoViewLeft.getLeft();
					gRightLeft = gUserInfoViewRight.getLeft();
					flag = false;
				}
			}
			else if (action == MotionEvent.ACTION_MOVE)
			{
				int dx = (int)event.getX()- touchX;
	            int left = gUserInfoViewCenter.getLeft() + dx;
	            gUserInfoViewCenter.layout(left, gTop, left + gUserInfoViewCenter.getWidth(), gTop + gUserInfoViewCenter.getHeight());
	            gUserInfoViewLeft.layout(gUserInfoViewLeft.getLeft() + dx, gTop, gUserInfoViewLeft.getLeft() + dx + gViewWidth, gTop + gViewHeight);
	            gUserInfoViewRight.layout(gUserInfoViewRight.getLeft() + dx, gTop, gUserInfoViewRight.getLeft() + dx + gViewWidth, gTop + gViewHeight);
			}
			else if (action == MotionEvent.ACTION_UP)
			{
				int x = (int) gUserInfoViewCenter.getX();
				if (x > gPhoneWidth * MOVE_THREAD) //right
				{
					startTranslateAnimation(gRightLeft - x);
					gUserData.gotoPrior();
				}
				else if (x + gViewWidth < gPhoneWidth * MOVE_THREAD)
				{
					startTranslateAnimation(gLeftLeft - x);
					gUserData.gotoNext();
				}
				else
					startTranslateAnimation(gCenterLeft - x);
			}
			return true;
		}
	};
	
	private OnTouchListener beginGameOnTouchListener = new OnTouchListener() 
	{
		private int touchX;
		private boolean clickFlag = true;
		@Override
		public boolean onTouch(View v, MotionEvent event) 
		{
			gestureDetector.onTouchEvent(event);
			int action = event.getAction();
			userInfoCenterOnTouchListener.onTouch(v, event);
			if (action == MotionEvent.ACTION_DOWN)
			{
				clickFlag = true;
				v.setBackgroundResource(R.drawable.lj_map_textview_touch);
				touchX = (int) event.getX();
			}
			else if (action == MotionEvent.ACTION_MOVE)
			{
				if (Math.abs(event.getX()- touchX) >= TOUCH_THREAD)
				{
					clickFlag = false;
					v.setBackgroundResource(R.drawable.lj_map_textview_circular);
				}
			}
			else if (action == MotionEvent.ACTION_UP)
			{
				v.setBackgroundResource(R.drawable.lj_map_textview_circular);
				if (clickFlag == true)
				{
					Log.e("click", "click");
					Message msg = new Message();
					msg.what = ConstantValues.InstructionCode.SHAKE_HANDLER_GAME;
					UserShakeData data = gUserData.getCurrentUserData();
					msg.arg1 = data.getUserId();
					msg.arg2 = data.getGameType();
					gHandler.sendMessage(msg);
				}
			}
			return true;
		}
	};

	public RelativeLayoutUserInfoList(Context context) 
	{
		super(context);
		init(context);
	}
	
	public RelativeLayoutUserInfoList(Context context, AttributeSet attrs) 
	{
		super(context, attrs);
		init(context);
	}

	private void init(Context context)
	{
		LayoutInflater.from(context).inflate(R.layout.lj_map_userinfo_list, this, true);
		gestureDetector = new GestureDetector(onGestureListener);
	}
	
	public void initView(Context context, int width, ArrayList<UserShakeData> data, Handler handler)
	{
		gHandler = handler;
		gPhoneWidth = width;
		gUserData = new UserDataModel(data);
		
		gUserInfoViewCenter = (FrameLayoutUserInfo)findViewById(R.id.lj_map_userinfo_center);
		gUserInfoViewCenter.setOnTouchListener(userInfoCenterOnTouchListener);
		gUserInfoViewCenter.setBeginGameTouchListener(beginGameOnTouchListener);
		FrameLayout.LayoutParams centerLayoutParams = (LayoutParams) gUserInfoViewCenter.getLayoutParams();
		gViewWidth = gUserInfoViewCenter.getWidth();
		gViewHeight = gUserInfoViewCenter.getHeight();
		gCenterLeft = centerLayoutParams.leftMargin;
		
		gUserInfoViewLeft = new FrameLayoutUserInfo(context);
		FrameLayout.LayoutParams leftLayoutParams = new LayoutParams(gViewWidth, gViewHeight);
		leftLayoutParams.leftMargin = -gViewWidth + centerLayoutParams.leftMargin * 2 / 3;
		gUserInfoViewLeft.setLayoutParams(leftLayoutParams);
		this.addView(gUserInfoViewLeft);
		
		gUserInfoViewRight = new FrameLayoutUserInfo(context);
		FrameLayout.LayoutParams rightLayoutParams = new LayoutParams(gViewWidth, gViewHeight);
		rightLayoutParams.leftMargin = gViewWidth + centerLayoutParams.leftMargin + centerLayoutParams.leftMargin * 1 / 3;
		gUserInfoViewRight.setLayoutParams(rightLayoutParams);
		this.addView(gUserInfoViewRight);
		
		setData();
	}

	private void setData(FrameLayoutUserInfo view, int index, UserShakeData data)
	{
		view.initUserData(data.getUserId(), index + ". " + data.getNickName(), data.getGameType());
	}
	
	private void setData()
	{
		UserShakeData currentData = gUserData.getCurrentUserData();
		int currentIndex = gUserData.getCurrentIndex() + 1;
		setData(gUserInfoViewCenter, currentIndex, currentData);
		
		UserShakeData nextData = gUserData.getNextUserData();
		int nextIndex = gUserData.getNextIndex() + 1;
		setData(gUserInfoViewRight, nextIndex, nextData);
		
		UserShakeData priorData = gUserData.getPriorUserData();
		int priorIndex = gUserData.getPriorIndex() + 1;
		setData(gUserInfoViewLeft, priorIndex, priorData);
		
	}
	
	public void setDataOfIndex(int index)
	{
		gUserData.setIndex(index);
		setData();
	}
}

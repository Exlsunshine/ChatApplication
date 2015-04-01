package com.lj.shake;

import android.os.Handler;
import android.os.Message;
import android.view.MotionEvent;
import android.view.VelocityTracker;

import com.baidu.mapapi.map.BaiduMap.OnMapTouchListener;
import com.yg.commons.ConstantValues;

public class mapTouchListener implements OnMapTouchListener
{
	private int mTouchX = 0;
	private int mTouchY = 0;
	float viewX = 0;
    float viewY = 0;
	private Handler myHandler;
	private VelocityTracker mVelocityTracker ;
	public mapTouchListener(Handler handler)
	{
		myHandler = handler;
	}
	private void obtainVelocityTracker(MotionEvent event) 
	{
        if (mVelocityTracker == null) 
                mVelocityTracker = VelocityTracker.obtain();
        mVelocityTracker.addMovement(event);
	}
	private void releaseVelocityTracker() {
        if (mVelocityTracker != null) {
                mVelocityTracker.recycle();
                mVelocityTracker = null; 
        }
	}
	@Override
	public void onTouch(MotionEvent event) 
	{
		int action = event.getAction();  
		if (action == MotionEvent.ACTION_DOWN)
		{
			Message msg = new Message();
			msg.what = ConstantValues.InstructionCode.SHAKE_HANDLER_MAP_TOUCH_DOWN;
			mTouchX = (int) event.getX();
			mTouchY = (int) event.getY();
			msg.arg1 = mTouchX;
			msg.arg2 = mTouchY;
			myHandler.sendMessage(msg);
		}
		obtainVelocityTracker(event);
		if (action == MotionEvent.ACTION_MOVE)
		{
			float mCurX = event.getX();
        	float mCurY = event.getY();
        	Message msg = new Message();
        	msg.what = ConstantValues.InstructionCode.SHAKE_HANDLER_MAP_TOUCH_MOVE;
        	msg.arg1 = (int) (mCurX - mTouchX);
        	msg.arg2 = (int) (mCurY - mTouchY);
        	myHandler.sendMessage(msg);
		}
		if (action == MotionEvent.ACTION_UP)
		{
			final VelocityTracker velocityTracker = mVelocityTracker;
			velocityTracker.computeCurrentVelocity(1000, ConstantValues.InstructionCode.MAP_MOVE_MAX_SPEED);
        	int initialVelocity = (int) velocityTracker.getYVelocity();
        	releaseVelocityTracker();
        	if (Math.abs(initialVelocity) == ConstantValues.InstructionCode.MAP_MOVE_MAX_SPEED)
        	{
        		Message msg = new Message();
        		msg.what = ConstantValues.InstructionCode.SHAKE_HANDLER_MAP_FAST_MOVE;
        		myHandler.sendMessage(msg);
        	}
		}
	}

}

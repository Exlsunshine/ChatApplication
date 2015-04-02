package com.lj.shake;

import com.yg.commons.ConstantValues;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.os.Handler;
import android.os.Message;

public class shakeListener implements SensorEventListener
{
	private Handler myHandler;
	private int userID;
	
	private boolean isShake(float[] values)
	{
		if ((Math.abs(values[0]) > ConstantValues.InstructionCode.THRESHOLD_SPEED 
			|| Math.abs(values[1]) > ConstantValues.InstructionCode.THRESHOLD_SPEED 
			|| Math.abs(values[2]) > ConstantValues.InstructionCode.THRESHOLD_SPEED)) 
			return true;
		else
			return false;
	}
	
	public shakeListener(Handler handler, int userid) 
	{
		myHandler = handler;
		userID = userid;
	}

	@Override
	public void onSensorChanged(SensorEvent event) 
	{
		float[] values = event.values;
		if (isShake(values)) 
		{
			Message msg = new Message();
			msg.what = ConstantValues.InstructionCode.SHAKE_HANDLER_SHAKE_SENSOR;
			myHandler.sendMessage(msg);
			Thread thread = new ThreadCheckGameSet(myHandler, userID);
			thread.start();
		}
		
	}

	@Override
	public void onAccuracyChanged(Sensor arg0, int arg1) 
	{
	}
}

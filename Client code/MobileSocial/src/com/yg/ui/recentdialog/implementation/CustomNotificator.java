package com.yg.ui.recentdialog.implementation;

import android.content.Context;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Vibrator;

public class CustomNotificator
{
	private Vibrator vibrator;
	private Ringtone ringtone;
	private Context context;
	private boolean vibratorEnable;
	private boolean ringtoneEnable;
	
	/**
	 * 不带通知栏的提醒
	 * @param context 应传入Activity.this
	 * @param vibratorEnable true表示开启震动提醒   false表示不使用震动提醒
	 * @param ringtoneEnable true表示开启声音提醒   false表示不使用声音提醒
	 */
	public CustomNotificator(Context context, boolean vibratorEnable, boolean ringtoneEnable)
	{
		this.context = context;
		this.vibratorEnable = vibratorEnable;
		this.ringtoneEnable = ringtoneEnable;
	}
	
	/**
	 * 开始提醒
	 */
	public void startNotify()
	{
		if (vibratorEnable)
			startVibrate();
		if (ringtoneEnable)
			startRing();
	}
	
	/**
	 * 停止提醒<b>(由于提醒震动和提醒铃音时间都较短，所以一般不需要手动调用停止函数)</b>
	 */
	public void stopNotify()
	{
		if (vibratorEnable)
			stopVibrate();
		if (ringtoneEnable)
			stopRing();
	}
	
	private void startVibrate()
	{
		long pattern[] = {0, 100, 200, 300, 400};
		vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
		vibrator.vibrate(pattern, -1);
	}

	private void stopVibrate()
	{
		vibrator.cancel();
	}
	
	private void startRing()
	{
		try 
		{
		    Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
		    ringtone = RingtoneManager.getRingtone(context, notification);
		    ringtone.play();
		} catch (Exception e) {
		    e.printStackTrace();
		}
	}
	
	private void stopRing()
	{
		try 
		{
		    ringtone.stop();
		} catch (Exception e) {
		    e.printStackTrace();
		}
	}
}
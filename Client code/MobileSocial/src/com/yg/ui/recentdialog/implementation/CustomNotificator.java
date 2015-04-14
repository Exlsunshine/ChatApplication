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
	 * ����֪ͨ��������
	 * @param context Ӧ����Activity.this
	 * @param vibratorEnable true��ʾ����������   false��ʾ��ʹ��������
	 * @param ringtoneEnable true��ʾ������������   false��ʾ��ʹ����������
	 */
	public CustomNotificator(Context context, boolean vibratorEnable, boolean ringtoneEnable)
	{
		this.context = context;
		this.vibratorEnable = vibratorEnable;
		this.ringtoneEnable = ringtoneEnable;
	}
	
	/**
	 * ��ʼ����
	 */
	public void startNotify()
	{
		if (vibratorEnable)
			startVibrate();
		if (ringtoneEnable)
			startRing();
	}
	
	/**
	 * ֹͣ����<b>(���������𶯺���������ʱ�䶼�϶̣�����һ�㲻��Ҫ�ֶ�����ֹͣ����)</b>
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
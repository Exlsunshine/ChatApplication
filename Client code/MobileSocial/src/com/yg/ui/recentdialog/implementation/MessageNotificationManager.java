package com.yg.ui.recentdialog.implementation;

import android.R;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;

public class MessageNotificationManager 
{
	private Context context;
	
	public MessageNotificationManager(Context context)
	{
		this.context = context;
	}

	/**
	 * ����һ��ϵͳ��Ϣ��״̬��
	 * @param sticker �յ�ϵͳ��Ϣʱ��״̬����ʾ����ʾ��Ϣ(Ӧ����"XXX����һ����Ϣ")
	 * @param title ϵͳ��Ϣ����(Ӧ�����û���)
	 * @param content ϵͳ��Ϣ����(Ӧ����Է���������Ϣ����)
	 * @param portrait ��ͼ(Ӧ�ô����û�ͷ��)
	 */
	public void showNotification(String sticker, String title, String content, Bitmap portrait, Class<?> cls) 
	{
		Intent notificationIntent = new Intent(context, cls);
		notificationIntent.putExtra("key", title);
		
		PendingIntent contentIntent = PendingIntent.getActivity(context,
		        1234, notificationIntent, PendingIntent.FLAG_CANCEL_CURRENT);

		Notification.Builder builder = new Notification.Builder(context);

		builder.setContentIntent(contentIntent)
		            .setSmallIcon(R.drawable.ic_lock_idle_alarm)
		            .setLargeIcon(portrait)
		            .setTicker(sticker)
		            .setWhen(System.currentTimeMillis())
		            .setAutoCancel(true)
		            .setContentTitle(title)
		            .setContentText(content);

		builder.setLights(0x00FF00FF,100,3000);
		builder.setPriority(Notification.PRIORITY_DEFAULT);
		Notification notification = builder.build();
		notification.defaults = Notification.DEFAULT_LIGHTS | Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE;  
		
		NotificationManager nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
		nm.notify(0, notification);
	}

	public void clearNotification()
	{
		NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
		notificationManager.cancel(0);
	}
}

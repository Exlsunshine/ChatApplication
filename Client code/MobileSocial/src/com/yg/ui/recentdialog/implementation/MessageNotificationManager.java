package com.yg.ui.recentdialog.implementation;

import com.example.testmobiledatabase.R;

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
	 * 推送一条系统消息到状态栏
	 * @param sticker 收到系统消息时在状态栏显示的提示信息(应传入"XXX发来一条消息")
	 * @param title 系统消息标题(应传入用户名)
	 * @param content 系统消息内容(应传入对方发来的消息内容)
	 * @param portrait 大图(应该传入用户头像)
	 */
	public void showNotification(String sticker, String title, String content, Bitmap portrait, int friendUserID, Class<?> cls) 
	{
		Intent notificationIntent = new Intent(context, cls);
		notificationIntent.putExtra("reveiewer", friendUserID);
		
		PendingIntent contentIntent = PendingIntent.getActivity(context,
		        1234, notificationIntent, PendingIntent.FLAG_CANCEL_CURRENT);

		Notification.Builder builder = new Notification.Builder(context);

		builder.setContentIntent(contentIntent)
		            .setSmallIcon(R.drawable.yg_message_notificationmanager_small_icon)
		            .setLargeIcon(portrait)
		            .setTicker(sticker)
		            .setWhen(System.currentTimeMillis())
		            .setAutoCancel(true)
		            .setContentTitle(title)
		            .setContentText(content);

		builder.setLights(0x00FF00FF, 100, 3000);
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

package com.yg.ui.recentdialog;

import java.util.ArrayList;
import java.util.List;

import com.example.testmobiledatabase.R;
import com.yg.commons.ConstantValues;
import com.yg.dialog.Dialog;
import com.yg.message.AbstractMessage;
import com.yg.message.TextMessage;
import com.yg.ui.friendlist.implementation.CircleBitmap;
import com.yg.ui.recentdialog.implementation.RecentDialogAdapter;
import com.yg.ui.recentdialog.implementation.RecentDialogListView;
import com.yg.ui.recentdialog.implementation.RecentDialogListView.OnRefreshListener;
import com.yg.ui.recentdialog.implementation.RecentDialogListView.RemoveListener;
import com.yg.user.FriendUser;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class RecentDialogActivity extends Activity implements RemoveListener, OnRefreshListener 
{
	List<String> names = new ArrayList<String>();
	List<String> messages = new ArrayList<String>();
	List<String> dates = new ArrayList<String>();
	List<Bitmap> portraits = new ArrayList<Bitmap>();
	
	RecentDialogAdapter myAdapter = null;
	RecentDialogListView finalListView = null;
	Bitmap bmp;
	
	private void loadRecentDialogs()
	{
		Thread td = new Thread(new Runnable()
		{
			@Override
			public void run() 
			{
				ArrayList<FriendUser> friends = ConstantValues.user.getFriendList();
				ConstantValues.user.setContext(RecentDialogActivity.this);
				ArrayList<Dialog> dialogs = ConstantValues.user.getRecentDialogs();
				
				for (int i = 0; i <dialogs.size(); i++)
				{
					int friendID = dialogs.get(i).getAnotherUserID();
					for (int j = 0; j <friends.size(); j++)
					{
						if (friends.get(j).getID() == friendID)
						{
							names.add(friends.get(i).getAlias());
							messages.add(getLastMessage(dialogs.get(i).getLastMessage()));
							dates.add(dialogs.get(i).getLastMessage().getDate());
							bmp = BitmapFactory.decodeByteArray(friends.get(i).getPortrait(), 0, friends.get(i).getPortrait().length);
							bmp = CircleBitmap.circleBitmap(bmp);
							portraits.add(bmp);
						}
					}
				}
			}
		});
		td.start();
		try {
			td.join();
			myAdapter.notifyDataSetChanged();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	private String getLastMessage(AbstractMessage msg)
	{
		String result = null;
		switch (msg.getMessageType()) {
		case ConstantValues.InstructionCode.MESSAGE_TYPE_TEXT:
			result = ((TextMessage)msg).getText();
			break;
		case ConstantValues.InstructionCode.MESSAGE_TYPE_IMAGE:
			result = "[Picture]";
			break;
		case ConstantValues.InstructionCode.MESSAGE_TYPE_AUDIO:
			result = "[Audio]";
			break;
		default:
			break;
		}

		return result;
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_recent_dialog);
		
		finalListView = (RecentDialogListView) super.findViewById(R.id.yg_recent_dialog_listview);
		finalListView.setRemoveListener(this);

		myAdapter = new RecentDialogAdapter(this, names, messages, dates, portraits);
		finalListView.setAdapter(myAdapter);
		finalListView.setOnItemClickListener(new OnItemClickListenerImpl());

		finalListView.setonRefreshListener(new OnRefreshListener()
		{
			public void onRefresh()
			{
				new AsyncTask<Void, Void, Void>()
				{
					protected Void doInBackground(Void... params) 
					{
						try
						{
							refreshRecentDialogs();
						} catch (Exception e) {
							e.printStackTrace();
						}

						return null;
					}

					@Override
					protected void onPostExecute(Void result)
					{
						myAdapter.notifyDataSetChanged();
						finalListView.Refresh();
					}
				}.execute(null, null, null);

				new AsyncTask<Void, Void, Void>()
				{
					protected Void doInBackground(Void... params) 
					{
						try {
							Thread.sleep(1000);
						} catch (Exception e) {
							e.printStackTrace();
						}

						return null;
					}

					@Override
					protected void onPostExecute(Void result) 
					{
						finalListView.onRefreshComplete();
					}
				}.execute(null, null, null);
			}
		});
		
		loadRecentDialogs();
		
		registerReceiver(broadcastReceiver, intentFilter());
	}
	
	public void refreshRecentDialogs()
	{
		ArrayList<FriendUser> friends = ConstantValues.user.getFriendList();
		ConstantValues.user.setContext(RecentDialogActivity.this);
		ArrayList<Dialog> dialogs = ConstantValues.user.getRecentDialogs();
		
		names.clear();
		messages.clear();
		dates.clear();
		portraits.clear();
		for (int i = 0; i <dialogs.size(); i++)
		{
			int friendID = dialogs.get(i).getAnotherUserID();
			for (int j = 0; j <friends.size(); j++)
			{
				if (friends.get(j).getID() == friendID)
				{
					names.add(friends.get(i).getAlias());
					messages.add(getLastMessage(dialogs.get(i).getLastMessage()));
					dates.add(dialogs.get(i).getLastMessage().getDate());
					bmp = BitmapFactory.decodeByteArray(friends.get(i).getPortrait(), 0, friends.get(i).getPortrait().length);
					bmp = CircleBitmap.circleBitmap(bmp);
					portraits.add(bmp);
				}
			}
		}
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		getMenuInflater().inflate(R.menu.recent_dialog, menu);
		return true;
	}

	@Override
	public void onRefresh() 
	{
		
	}
	
	
	public class OnItemClickListenerImpl implements OnItemClickListener
	{
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id)
		{
		}
	}
	
	@Override
	public void removeItem(int position, int id) 
	{
		if (id == 0)
			Toast.makeText(this, "没有呼叫功能", Toast.LENGTH_SHORT).show();
		else 
			myAdapter.remove(position - 2);
	}
	
	private IntentFilter intentFilter()
	{
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(ConstantValues.InstructionCode.MESSAGE_BROADCAST_RECV_AUDIO);
		intentFilter.addAction(ConstantValues.InstructionCode.MESSAGE_BROADCAST_RECV_IMAGE);
		intentFilter.addAction(ConstantValues.InstructionCode.MESSAGE_BROADCAST_RECV_TEXT);
		
		return intentFilter;
	}
	
	private BroadcastReceiver broadcastReceiver = new BroadcastReceiver()
	{
		@Override
		public void onReceive(Context context, Intent intent)
		{
			if ((intent.getAction().equals(ConstantValues.InstructionCode.MESSAGE_BROADCAST_RECV_AUDIO))
			|| (intent.getAction().equals(ConstantValues.InstructionCode.MESSAGE_BROADCAST_RECV_IMAGE))
			|| (intent.getAction().equals(ConstantValues.InstructionCode.MESSAGE_BROADCAST_RECV_TEXT)))
			{
				refreshRecentDialogs();
				myAdapter.notifyDataSetChanged();
			}
		}
	};
}
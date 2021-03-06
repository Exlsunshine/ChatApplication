package com.yg.ui.recentdialog;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.example.testmobiledatabase.R;
import com.yg.commons.CommonUtil;
import com.yg.commons.ConstantValues;
import com.yg.dialog.Dialog;
import com.yg.message.AbstractMessage;
import com.yg.message.TextMessage;
import com.yg.ui.MainActivity;
import com.yg.ui.dialog.DialogActivity;
import com.yg.ui.dialog.implementation.DateUtil;
import com.yg.ui.friendlist.implementation.CircleBitmap;
import com.yg.ui.recentdialog.implementation.CustomNotificator;
import com.yg.ui.recentdialog.implementation.MessageNotificationManager;
import com.yg.ui.recentdialog.implementation.RecentDialogAdapter;
import com.yg.ui.recentdialog.implementation.RecentDialogListView;
//import com.yg.ui.recentdialog.implementation.RecentDialogListView.OnRefreshListener;
import com.yg.ui.recentdialog.implementation.RecentDialogListView.RemoveListener;
import com.yg.user.FriendUser;

public class RecentDialogActivity extends Activity implements RemoveListener//, OnRefreshListener 
{
	private static final String DEBUG_TAG = "RecentDialogActivity______";
	private List<String> names = new ArrayList<String>();
	private List<String> messages = new ArrayList<String>();
	private List<String> dates = new ArrayList<String>();
	private List<Integer> ids = new ArrayList<Integer>();
	private List<Bitmap> portraits = new ArrayList<Bitmap>();
	private List<Integer> unreadAmount = new ArrayList<Integer>();
	
	private RecentDialogAdapter myAdapter = null;
	private RecentDialogListView finalListView = null;
	private Bitmap bmp;
	
	private int currentChattingWithID = -1;
	private boolean isRunningAtBackground = false;
	
	private LinearLayout emptyMsgBackground;
	
	@Override
	protected void onResume() 
	{
		super.onResume();
		
		isRunningAtBackground = false;
		
		System.gc();
	}

	@Override
	protected void onPause() 
	{
		super.onPause();
		
		isRunningAtBackground = true;
	}
	
	@Override
	public void onBackPressed() 
	{
		MainActivity.minimizeApp(RecentDialogActivity.this);
	}
	
	private void loadRecentDialogs()
	{
		Thread td = new Thread(new Runnable()
		{
			@Override
			public void run() 
			{
				ArrayList<FriendUser> friends = ConstantValues.user.getFriendList();
				if (friends == null)
					return;
				
				ConstantValues.user.setContext(RecentDialogActivity.this);
				ArrayList<Dialog> dialogs = ConstantValues.user.getRecentDialogs();
				
				refreshListViewBackground(dialogs.size());
				
				for (int i = 0; i <dialogs.size(); i++)
				{
					int friendID = dialogs.get(i).getAnotherUserID();
					for (int j = 0; j <friends.size(); j++)
					{
						if (friends.get(j).getID() == friendID)
						{
							ids.add(friendID);
							names.add(friends.get(j).getAlias() == null ? friends.get(j).getNickName() : friends.get(j).getAlias());
							messages.add(getLastMessage(dialogs.get(i).getLastMessage()));
							//dates.add(dialogs.get(i).getLastMessage().getDate());
							dates.add(DateUtil.getSuggestion(CommonUtil.now(), dialogs.get(i).getLastMessage().getDate()));
							bmp = friends.get(j).getPortraitBmp();//BitmapFactory.decodeByteArray(friends.get(i).getPortrait(), 0, friends.get(i).getPortrait().length);
							bmp = CircleBitmap.circleBitmap(bmp);
							portraits.add(bmp);
							unreadAmount.add(dialogs.get(i).getUnreadAmount());
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
		switch (msg.getMessageType()) 
		{
		case ConstantValues.InstructionCode.MESSAGE_TYPE_TEXT:
			result = ((TextMessage)msg).getText();
			break;
		case ConstantValues.InstructionCode.MESSAGE_TYPE_IMAGE:
			result = "[图片]";//发来消息时候更新最近一条消息的地方别忘了也要改
			break;
		case ConstantValues.InstructionCode.MESSAGE_TYPE_AUDIO:
			result = "[音频]";//发来消息时候更新最近一条消息的地方别忘了也要改
			break;
		default:
			break;
		}

		return result;
	}
	
	private Handler handler;
	private static final int UPDATE_WITH_DEFAULT_BACKGROUND = 0x01;
	private static final int UPDATE_WITH_NORMAL_BACKGROUND = 0x02;
	
	private void refreshListViewBackground(int count)
	{
		if (count == 0)
		{
			Message msg = new Message();
			msg.what = UPDATE_WITH_DEFAULT_BACKGROUND;
			handler.sendEmptyMessage(UPDATE_WITH_DEFAULT_BACKGROUND);
		}
		else
		{
			Message msg = new Message();
			msg.what = UPDATE_WITH_NORMAL_BACKGROUND;
			handler.sendEmptyMessage(UPDATE_WITH_NORMAL_BACKGROUND);
		}
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_recent_dialog);
		
		finalListView = (RecentDialogListView) super.findViewById(R.id.yg_recent_dialog_listview);
		finalListView.setRemoveListener(this);

		myAdapter = new RecentDialogAdapter(this, names, messages, dates, portraits, ids, unreadAmount);
		finalListView.setAdapter(myAdapter);
		finalListView.setOnItemClickListener(new OnItemClickListenerImpl());

		emptyMsgBackground = (LinearLayout) super.findViewById(R.id.yg_recent_dialog_empty_background);
		/*finalListView.setonRefreshListener(new OnRefreshListener()
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
		});*/
		
		handler = new Handler()
		{
			@Override
			public void handleMessage(Message msg) 
			{
				super.handleMessage(msg);
				
				switch (msg.what)
				{
				case UPDATE_WITH_DEFAULT_BACKGROUND:
					emptyMsgBackground.setVisibility(View.VISIBLE);
					finalListView.setVisibility(View.INVISIBLE);
//					finalListView.setBackgroundResource(R.drawable.yg_recent_dialog_background);
					break;
				case UPDATE_WITH_NORMAL_BACKGROUND:
//					finalListView.setBackgroundColor(Color.parseColor("#F0EFF5"));
					emptyMsgBackground.setVisibility(View.INVISIBLE);
					finalListView.setVisibility(View.VISIBLE);
					break;
				default:
					break;
				}
			};
		};
		
		loadRecentDialogs();
		
		registerReceiver(broadcastReceiver, intentFilter());
	}
	
	public void refreshRecentDialogs()
	{
		ArrayList<FriendUser> friends = ConstantValues.user.getFriendList();
		if (friends == null)
			return;
		
		ConstantValues.user.setContext(RecentDialogActivity.this);
		ArrayList<Dialog> dialogs = ConstantValues.user.getRecentDialogs();
		
		ids.clear();
		names.clear();
		messages.clear();
		dates.clear();
		portraits.clear();
		unreadAmount.clear();
		
		refreshListViewBackground(dialogs.size());
		
		for (int i = 0; i <dialogs.size(); i++)
		{
			int friendID = dialogs.get(i).getAnotherUserID();
			for (int j = 0; j <friends.size(); j++)
			{
				if (friends.get(j).getID() == friendID)
				{
					ids.add(friendID);
					names.add(friends.get(j).getAlias() == null ? friends.get(j).getNickName() : friends.get(j).getAlias());
					messages.add(getLastMessage(dialogs.get(i).getLastMessage()));
					//dates.add(dialogs.get(i).getLastMessage().getDate());
					dates.add(DateUtil.getSuggestion(CommonUtil.now(), dialogs.get(i).getLastMessage().getDate()));
					bmp = friends.get(j).getPortraitBmp();//BitmapFactory.decodeByteArray(friends.get(i).getPortrait(), 0, friends.get(i).getPortrait().length);
					bmp = CircleBitmap.circleBitmap(bmp);
					portraits.add(bmp);
					unreadAmount.add(dialogs.get(i).getUnreadAmount());
				}
			}
			
			Log.i(DEBUG_TAG, String.valueOf(i) + " unread is: " + String.valueOf(dialogs.get(i).getUnreadAmount()));
		}
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		return false;
		//getMenuInflater().inflate(R.menu.recent_dialog, menu);
		//return true;
	}

/*	@Override
	public void onRefresh() 
	{
	}*/
	
	public class OnItemClickListenerImpl implements OnItemClickListener
	{
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id)
		{
			int friendID = (int) myAdapter.getItemId(position - 2);
			currentChattingWithID = friendID;
			Intent intent = new Intent(RecentDialogActivity.this, DialogActivity.class);
			intent.putExtra("reveiewer", friendID);
			
			RecentDialogActivity.this.startActivity(intent);
		}
	}
	
	@Override
	public void removeItem(int position, int id) 
	{
		new DeleteDataFromDB().execute(position);
		/*if (id == 0)
			Toast.makeText(this, "没有呼叫功能", Toast.LENGTH_SHORT).show();
		else 
			myAdapter.remove(position - 2);*/
	}
	
	private class DeleteDataFromDB extends AsyncTask<Integer, Void, Void>
	{
		private int position;
		
		@Override
		protected Void doInBackground(Integer... params)
		{
			position = params[0];
			ConstantValues.user.eraseDialogHistory(ids.get(position - 2));
			return null;
		}
		
		@Override
		protected void onPostExecute(Void result)
		{
			super.onPostExecute(result);
			
			myAdapter.remove(position - 2);
			myAdapter.notifyDataSetChanged();
			
			refreshListViewBackground(myAdapter.getCount());
		}
	}
	
	private IntentFilter intentFilter()
	{
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(ConstantValues.InstructionCode.MESSAGE_BROADCAST_RECV_AUDIO);
		intentFilter.addAction(ConstantValues.InstructionCode.MESSAGE_BROADCAST_RECV_IMAGE);
		intentFilter.addAction(ConstantValues.InstructionCode.MESSAGE_BROADCAST_RECV_TEXT);
		intentFilter.addAction(ConstantValues.InstructionCode.MESSAGE_BROADCAST_SEND_COMPLETED);
		intentFilter.addAction(ConstantValues.InstructionCode.CURRENT_CHAT_WITH_NOTIFICATION);
		intentFilter.addAction(ConstantValues.InstructionCode.CLEAR_MESSAGE_RED_DOT);
		intentFilter.addAction(ConstantValues.InstructionCode.ERASE_LOCAL_HISTORY);
		
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
				
				int fromUserID = intent.getIntExtra("fromUserID", -1);
				Intent processCompletedIntent = new Intent(ConstantValues.InstructionCode.MESSAGE_BROADCAST_RECV_COMPLETED);
				processCompletedIntent.putExtra("fromUserID", fromUserID);
				sendBroadcast(processCompletedIntent);
				
				if (!isRunningAtBackground)
				{
					CustomNotificator notificator = new CustomNotificator(RecentDialogActivity.this, true, false);
					notificator.startNotify();
				}
				else if (currentChattingWithID != fromUserID)
				{
					MessageNotificationManager msgManager = new MessageNotificationManager(RecentDialogActivity.this);
					FriendUser friend = getFriendByID(fromUserID);
					Dialog dialog = ConstantValues.user.makeDialogWith(friend);
					String msg = null;
					
					if (dialog.getLastMessage().getMessageType() == ConstantValues.InstructionCode.MESSAGE_TYPE_AUDIO)
						msg = "[音频]";//加载数据库消息的地方别忘了也要改
					else if (dialog.getLastMessage().getMessageType() == ConstantValues.InstructionCode.MESSAGE_TYPE_IMAGE)
						msg = "[图片]";//加载数据库消息的地方别忘了也要改
					else
						msg = ((TextMessage)dialog.getLastMessage()).getText();
					
					msgManager.showNotification((friend.getAlias() == null ? friend.getNickName() : friend.getAlias()) + "发来一条消息", 
							friend.getAlias() == null ? friend.getNickName() : friend.getAlias(), msg, friend.getPortraitBmp(),
							friend.getID(), DialogActivity.class);
				}
				else if (currentChattingWithID == fromUserID)
				{
					ConstantValues.user.setContext(RecentDialogActivity.this);
					ArrayList<Dialog> dialogs = ConstantValues.user.getRecentDialogs();
					
					for (int i = 0; i <dialogs.size(); i++)
					{
						int friendID = dialogs.get(i).getAnotherUserID();
						if (fromUserID == friendID)
							dialogs.get(i).markAllAsReaded();
					}
					
					refreshRecentDialogs();
					myAdapter.notifyDataSetChanged();
				}
			}
			else if (intent.getAction().equals(ConstantValues.InstructionCode.MESSAGE_BROADCAST_SEND_COMPLETED))
			{
				refreshRecentDialogs();
				myAdapter.notifyDataSetChanged();
			}
			else if (intent.getAction().equals(ConstantValues.InstructionCode.CURRENT_CHAT_WITH_NOTIFICATION))
			{
				int fromUserID = intent.getIntExtra("fromUserID", -1);
				currentChattingWithID = fromUserID;
			}
			else if (intent.getAction().equals(ConstantValues.InstructionCode.CLEAR_MESSAGE_RED_DOT))
			{
				boolean needRefresh = false;
				int friendUserID = intent.getIntExtra("friendUserID", -1);
				
				if (friendUserID != -1)
				{
					ConstantValues.user.setContext(RecentDialogActivity.this);
					ArrayList<Dialog> dialogs = ConstantValues.user.getRecentDialogs();
					
					for (int i = 0; i <dialogs.size(); i++)
					{
						int friendID = dialogs.get(i).getAnotherUserID();
						if (friendUserID == friendID)
						{
							if (dialogs.get(i).getUnreadAmount() > 0)
							{
								needRefresh = true;
								dialogs.get(i).markAllAsReaded();
							}
						}
					}
					
					if (needRefresh)
					{
						refreshRecentDialogs();
						myAdapter.notifyDataSetChanged();
					}
				}
			}
			else if (intent.getAction().equals(ConstantValues.InstructionCode.ERASE_LOCAL_HISTORY))
			{
				int friendUserID = intent.getIntExtra("friendUserID", -1);
				
				for (int i = 0; i < ids.size(); i++)
				{
					if (ids.get(i) == friendUserID)
						new DeleteDataFromDB().execute(i + 2);
				}
			}
		}
	};
	
	private FriendUser getFriendByID(int id)
	{
		for (int i = 0; i < ConstantValues.user.getFriendList().size(); i++)
		{
			if (ConstantValues.user.getFriendList().get(i).getID() == id)
				return ConstantValues.user.getFriendList().get(i);
		}
		return null;
	}
}
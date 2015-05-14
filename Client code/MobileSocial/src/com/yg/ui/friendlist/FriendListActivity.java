package com.yg.ui.friendlist;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;

import com.example.testmobiledatabase.R;
import com.yg.commons.ConstantValues;
import com.yg.ui.MainActivity;
import com.yg.ui.friendlist.implementation.CircleBitmap;
import com.yg.ui.friendlist.implementation.FinalListView;
import com.yg.ui.friendlist.implementation.FinalListView.OnRefreshListener;
import com.yg.ui.friendlist.implementation.FinalListView.RemoveListener;
import com.yg.ui.friendlist.implementation.FriendlistAdapter;
import com.yg.ui.friendlist.implementation.SlideBar;
import com.yg.ui.friendlist.implementation.SlideBar.OnTouchingLetterChangedListener;
import com.yg.user.FriendUser;

public class FriendListActivity extends Activity implements RemoveListener, OnRefreshListener 
{
	private static final String DEBUG_TAG = "FriendListActivity______";
	private int firstVisibleItem;
	private int visibleItemCount;
	private boolean needRefresh = false;
	private boolean initDataFinish = false;
	
	private List<String> friendsName = new ArrayList<String>();
	private List<Bitmap> friendsPortrait = new ArrayList<Bitmap>();
	private List<Integer> friendsID = new ArrayList<Integer>();
	private List<Boolean> friendsStarMark = new ArrayList<Boolean>();
	
	private FriendlistAdapter myAdapter = null;
	private FinalListView finalListView = null;
	private Bitmap bmp;
	
	private SlideBar slidebar = null;
	private TextView indecator = null;

	private static final int FRIENDSHIP_REQUEST_STAR = 0;
	private static final int FRIENDSHIP_REQUEST_DELETE = 1;
	
	private class DownloadPortraitTask extends AsyncTask<Void, Void, Void>
	{
		@Override
		protected void onPreExecute() 
		{
			super.onPreExecute();
		}
		
		@Override
		protected Void doInBackground(Void... params)
		{
			friendsName.clear();
			friendsPortrait.clear();
			friendsID.clear();
			friendsStarMark.clear();
			ArrayList<FriendUser> friends = ConstantValues.user.getFriendList();
			if (friends == null)
				return null;

			for (int i = 0; i <friends.size(); i++)
			{
				friendsName.add(friends.get(i).getAlias() == null ? friends.get(i).getNickName() : friends.get(i).getAlias());
				bmp = friends.get(i).getPortraitBmp();
				bmp = CircleBitmap.circleBitmap(bmp);
				friendsPortrait.add(bmp);
				friendsID.add(friends.get(i).getID());
				friendsStarMark.add(friends.get(i).isCloseFriend());
			}
			
			return null;
		}
		
		@Override
		protected void onPostExecute(Void result)
		{
			setupLayout();
			myAdapter.notifyDataSetChanged();
			initDataFinish = true;
			super.onPostExecute(result);
		}
		
		@Override
		protected void onCancelled()
		{
			super.onCancelled();
		}
	}
	
	private void refreshDataFromQuery(final String query)
	{
		Thread td = new Thread(new Runnable()
		{
			@Override
			public void run() 
			{
				friendsName.clear();
				friendsPortrait.clear();
				friendsID.clear();
				friendsStarMark.clear();
				ArrayList<FriendUser> friends = ConstantValues.user.getFriendList();
				if (friends == null)
					return;

				for (int i = 0; i <friends.size(); i++)
				{
					String name = friends.get(i).getAlias() == null ? friends.get(i).getNickName() : friends.get(i).getAlias() + "(" + friends.get(i).getNickName() +")";
					
					if ((query.length() == 0 || query == null) || (name.toLowerCase().contains(query.toLowerCase())))
					{
						friendsName.add(name);
						bmp = friends.get(i).getPortraitBmp();
						bmp = CircleBitmap.circleBitmap(bmp);
						friendsPortrait.add(bmp);
						friendsID.add(friends.get(i).getID());
						friendsStarMark.add(friends.get(i).isCloseFriend());
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
	
	private void setupLayout()
	{
		indecator = (TextView) findViewById(R.id.yg_friendlist_alphabet_indicator);
				
		slidebar = (SlideBar) findViewById(R.id.yg_friendlist_sliderBar);
		slidebar.setOnTouchingLetterChangedListener(new LetterListViewListener());
		
		finalListView = (FinalListView) super.findViewById(R.id.listview);
		finalListView.setRemoveListener(this);

		myAdapter = new FriendlistAdapter(this, friendsName, friendsPortrait, friendsStarMark);
		finalListView.setAdapter(myAdapter);
		finalListView.setOnItemClickListener(new OnItemClickListenerImpl());
		
		finalListView.setOnScrollListener(new OnScrollListener()
		{
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) 
			{
				if (scrollState == SCROLL_STATE_IDLE)
				{
					Intent intent = new Intent("firstVisibleItem");
					intent.putExtra("firstVisibleItem", firstVisibleItem);
					intent.putExtra("visibleItemCount", visibleItemCount);
					sendBroadcast(intent );
				}
			}
			
			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) 
			{
				FriendListActivity.this.firstVisibleItem = firstVisibleItem;
				FriendListActivity.this.visibleItemCount = visibleItemCount;
			}
		});
	}
	
	private class LetterListViewListener implements  OnTouchingLetterChangedListener  
	{  
		@Override  
		public void onTouchingLetterChanged(final String s, float y, float x)  
		{
			myAdapter.enableAnimation(false);
			indecator.setVisibility(View.VISIBLE);
			indecator.setText(s);
			for (int i = 0; i < ConstantValues.user.getFriendList().size(); i++)
			{
				String fullName = (String)ConstantValues.user.getFriendList().get(i).getFullNameInPinyin();
				fullName = fullName.toUpperCase();
				if (fullName.startsWith(String.valueOf(s)))
				{
					//I don't know why I always have to set offset 2 to the listview position...
					//the same situation with onItemClickListener...
					finalListView.setSelection(i + 2);
					return ;
				}
			}
		}  
		
		@Override  
		public void onTouchingLetterEnd()  
		{  
			new Handler().postDelayed(new Runnable()
			{
				@Override
				public void run() 
				{
					myAdapter.enableAnimation(true);
				}
			}, 300);
			indecator.setVisibility(View.GONE);
		}  
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_friend_list);

		initDataFinish = false;
		needRefresh = false;
		
		DownloadPortraitTask dpt = new DownloadPortraitTask();
		dpt.execute();
	}
	
	@Override
	protected void onResume()
	{
		registerReceiver(broadcastReceiver, intentFilter());
		Log.i(DEBUG_TAG, "FriendList resume.");
		
		if (needRefresh)
		{
			//reload all friends data(in case of leaving this activity while
			//not cancel the query, that would be a UI bug.)
			refreshDataFromQuery("");
			requestForCleanActionbar();
			needRefresh = false;
		}
		
		super.onResume();
	}
	
	@Override
	protected void onPause() 
	{
		unregisterReceiver(broadcastReceiver);
		
		Log.i(DEBUG_TAG, "FriendList onPause.\n" + "initDataFinish is " + initDataFinish);
		
		//Only initial data loading finished, 
		//can we refresh the listview(Or system will crash).
		needRefresh = initDataFinish;
		super.onPause();
	}
	
	@Override
	public void onBackPressed() 
	{
		MainActivity.minimizeApp(FriendListActivity.this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) 
	{
		return false;
	}
	
	public class OnItemClickListenerImpl implements OnItemClickListener
	{
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id)
		{
			Intent intent = new Intent(FriendListActivity.this, FriendDetailActivity.class);
			intent.putExtra("friendUserID", friendsID.get(position - 2));
			startActivity(intent);
			
			requestForCleanActionbar();
		}
	}
	
	private void requestForCleanActionbar()
	{
		Intent intent = new Intent(MainActivity.CLEAR_ACTIONBAR_REQUEST);
		sendBroadcast(intent);
	}
	
	private class FriendshipOperationTask extends AsyncTask<Integer, Void, Void>
	{
		private int cmd;
		private int position;
		private int friendID;
		
		@Override
		protected Void doInBackground(Integer... params)
		{
			int position = params[0];
			int cmd = params[1];
			this.cmd = cmd;
			this.position = position;
			this.friendID = friendsID.get(position - 2);
			
			switch (cmd)
			{
			case FRIENDSHIP_REQUEST_DELETE:
				ConstantValues.user.deleteUser(friendID);
				break;
			case FRIENDSHIP_REQUEST_STAR:
				boolean isCloseFriends = !friendsStarMark.get(position - 2);
				friendsStarMark.set(position - 2, isCloseFriends);
				ConstantValues.user.setAsCloseFriend(friendID, isCloseFriends);
				break;
			default:
				break;
			}
			
			return null;
		}
		
		@Override
		protected void onPostExecute(Void result)
		{
			super.onPostExecute(result);
			
			if (cmd == FRIENDSHIP_REQUEST_DELETE)
			{
				myAdapter.remove(position - 2);
				refreshDataFromQuery("");
				requestForCleanActionbar();
				
				Intent intent = new Intent(ConstantValues.InstructionCode.ERASE_LOCAL_HISTORY);
				intent.putExtra("friendUserID", friendID);
				sendBroadcast(intent);
			}
			
			myAdapter.enableAnimation(false);
			myAdapter.notifyDataSetChanged();
			new Handler().postDelayed(new Runnable()
			{
				@Override
				public void run() 
				{
					myAdapter.enableAnimation(true);
				}
			}, 300);
		}
	}
	public void removeItem(int position, int id)
	{
		new FriendshipOperationTask().execute(position, id);
		/*//Add star to a friend
		if (id == 0)
		{
			Thread td = new Thread(new Runnable()
			{
				@Override
				public void run()
				{
					boolean isCloseFriends = !friendsStarMark.get(position - 2);
					friendsStarMark.set(position - 2, isCloseFriends);
					ConstantValues.user.setAsCloseFriend(friendsID.get(position - 2), isCloseFriends);
					
				}
			});
			td.start();
		}
		else //Delete a friend
		{
			myAdapter.remove(position - 2);
		}*/
	}

	@Override
	public void onRefresh() { }
	
	private IntentFilter intentFilter()
	{
		IntentFilter filter = new IntentFilter();
		filter.addAction("friendlist_query_refresh_request");
		
		return filter;		
	}
	
	private BroadcastReceiver broadcastReceiver = new BroadcastReceiver()
	{
		@Override
		public void onReceive(Context context, final Intent intent)
		{
			if ((intent.getAction().equals("friendlist_query_refresh_request")))
			{
				refreshDataFromQuery(intent.getStringExtra("query"));
				new Handler().postDelayed(new Runnable()
				{
					@Override
					public void run() 
					{
						Intent it = new Intent("friendlist_query_refresh_complete");
						it.putExtra("query", intent.getStringExtra("query"));
						it.putExtra("number", String.valueOf(friendsName.size()));
						sendBroadcast(it);
					}
				}, 800);
			}
		}
	};
}
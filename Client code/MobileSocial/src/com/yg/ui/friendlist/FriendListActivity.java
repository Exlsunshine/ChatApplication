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
import android.widget.AdapterView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;

import com.example.testmobiledatabase.R;
import com.yg.commons.ConstantValues;
import com.yg.ui.friendlist.implementation.CircleBitmap;
import com.yg.ui.friendlist.implementation.FinalListView;
import com.yg.ui.friendlist.implementation.FinalListView.OnRefreshListener;
import com.yg.ui.friendlist.implementation.FinalListView.RemoveListener;
import com.yg.ui.friendlist.implementation.MyAdapter;
import com.yg.user.FriendUser;

public class FriendListActivity extends Activity implements RemoveListener, OnRefreshListener 
{
	private static final String DEBUG_TAG = "FriendListActivity______";
	private int firstVisibleItem;
	private int visibleItemCount;
	
	List<String> userNmae = new ArrayList<String>();
	List<Bitmap> portrait = new ArrayList<Bitmap>();
	MyAdapter myAdapter = null;
	FinalListView finalListView = null;
	Bitmap bmp;
	
	private void refreshFriendsData()
	{
		Thread td = new Thread(new Runnable()
		{
			@Override
			public void run() 
			{
				ArrayList<FriendUser> friends = ConstantValues.user.getFriendList();

				for (int i = 0; i <friends.size(); i++)
				{
					userNmae.add(friends.get(i).getAlias() == null ? friends.get(i).getNickName() : friends.get(i).getAlias());
					bmp = friends.get(i).getPortraitBmp();//BitmapFactory.decodeByteArray(friends.get(i).getPortrait(), 0, friends.get(i).getPortrait().length);
					bmp = CircleBitmap.circleBitmap(bmp);
					portrait.add(bmp);
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
	
	private void refreshDataFromQuery(final String query)
	{
		Thread td = new Thread(new Runnable()
		{
			@Override
			public void run() 
			{
				userNmae.clear();
				portrait.clear();
				ArrayList<FriendUser> friends = ConstantValues.user.getFriendList();

				for (int i = 0; i <friends.size(); i++)
				{
					String name = friends.get(i).getAlias() == null ? friends.get(i).getNickName() : friends.get(i).getAlias() + "(" + friends.get(i).getNickName() +")";
					
					if (query.length() == 0 || query == null)
						userNmae.add(name);
					else if (name.toLowerCase().contains(query.toLowerCase()))
						userNmae.add(name);
					
					bmp = friends.get(i).getPortraitBmp();//BitmapFactory.decodeByteArray(friends.get(i).getPortrait(), 0, friends.get(i).getPortrait().length);
					bmp = CircleBitmap.circleBitmap(bmp);
					portrait.add(bmp);
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
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_friend_list);
		
		finalListView = (FinalListView) super.findViewById(R.id.listview);
		finalListView.setRemoveListener(this);

		myAdapter = new MyAdapter(this, userNmae, portrait);
		finalListView.setAdapter(myAdapter);
		finalListView.setOnItemClickListener(new OnItemClickListenerImpl());

		
		finalListView.setOnScrollListener(new OnScrollListener()
		{
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) 
			{
				if (scrollState == SCROLL_STATE_IDLE)
				{
					Log.i(DEBUG_TAG, "Idle");
					Log.i(DEBUG_TAG, "First visible item " + String.valueOf(firstVisibleItem));
					Log.i(DEBUG_TAG, "Visible item count " + String.valueOf(visibleItemCount));
					
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
							ArrayList<FriendUser> friends = ConstantValues.user.getFriendList();
							userNmae.clear();
							portrait.clear();
							for (int i = 0; i <friends.size(); i++)
							{
								userNmae.add(friends.get(i).getAlias() == null ? friends.get(i).getNickName() : friends.get(i).getAlias());
								bmp = friends.get(i).getPortraitBmp();//BitmapFactory.decodeByteArray(friends.get(i).getPortrait(), 0, friends.get(i).getPortrait().length);
								bmp = CircleBitmap.circleBitmap(bmp);
								portrait.add(bmp);
							}
							Thread.sleep(1000);
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
		
		refreshFriendsData();
		
		registerReceiver(broadcastReceiver, intentFilter());
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) 
	{
		//getMenuInflater().inflate(R.menu.friend_list, menu);
		//return true;
		return false;
	}
	
	public class OnItemClickListenerImpl implements OnItemClickListener
	{
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id)
		{
			/*String ab = (String) myAdapter.getItem(position - 2);
			Intent it1 = new Intent(MainActivity.this, Talk.class);
			it1.putExtra("reveiewer", ab);
			MainActivity.this.startActivity(it1);*/
		}
	}
	
	public void removeItem(int position, int id)
	{
		if (id == 0)
			Toast.makeText(this, "没有呼叫功能", Toast.LENGTH_SHORT).show();
		else 
			myAdapter.remove(position - 2);
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
						it.putExtra("number", String.valueOf(userNmae.size()));
						sendBroadcast(it);
					}
				}, 800);
			}
		}
	};
}

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
import android.widget.Toast;

import com.example.testmobiledatabase.R;
import com.yg.commons.ConstantValues;
import com.yg.ui.MainActivity;
import com.yg.ui.friendlist.implementation.CircleBitmap;
import com.yg.ui.friendlist.implementation.FinalListView;
import com.yg.ui.friendlist.implementation.FinalListView.OnRefreshListener;
import com.yg.ui.friendlist.implementation.FinalListView.RemoveListener;
import com.yg.ui.friendlist.implementation.FriendlistAdapter;
import com.yg.user.FriendUser;

public class FriendListActivity extends Activity implements RemoveListener, OnRefreshListener 
{
	private static final String DEBUG_TAG = "FriendListActivity______";
	private int firstVisibleItem;
	private int visibleItemCount;
	
	private List<String> friendsName = new ArrayList<String>();
	private List<Bitmap> friendsPortrait = new ArrayList<Bitmap>();
	private List<Integer> friendsID = new ArrayList<Integer>();
	private FriendlistAdapter myAdapter = null;
	private FinalListView finalListView = null;
	private Bitmap bmp;
	
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
			}
			
			return null;
		}
		
		@Override
		protected void onPostExecute(Void result)
		{
			setupLayout();
			myAdapter.notifyDataSetChanged();
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
		finalListView = (FinalListView) super.findViewById(R.id.listview);
		finalListView.setRemoveListener(this);

		myAdapter = new FriendlistAdapter(this, friendsName, friendsPortrait);
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
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_friend_list);

		DownloadPortraitTask dpt = new DownloadPortraitTask();
		dpt.execute();
	}
	
	@Override
	protected void onResume()
	{
		registerReceiver(broadcastReceiver, intentFilter());
		Log.i(DEBUG_TAG, "FriendList resume.");
		super.onResume();
	}
	
	@Override
	protected void onPause() 
	{
		unregisterReceiver(broadcastReceiver);

		//reload all friends data(in case of leaving this activity while
		//not cancel the query, that would be a UI bug.)
		refreshDataFromQuery("");
		Log.i(DEBUG_TAG, "FriendList pause.");
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
		}
	}
	
	public void removeItem(int position, int id)
	{
		if (id == 0)
			Toast.makeText(this, "û�к��й���", Toast.LENGTH_SHORT).show();
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
						it.putExtra("number", String.valueOf(friendsName.size()));
						sendBroadcast(it);
					}
				}, 800);
			}
		}
	};
}
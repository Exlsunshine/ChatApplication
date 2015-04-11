package com.yg.ui.friendlist;

import java.util.ArrayList;
import java.util.List;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.animation.Animator.AnimatorListener;
import android.app.ActionBar;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.view.animation.Animation.AnimationListener;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.SearchView.OnCloseListener;
import android.widget.SearchView.OnQueryTextListener;
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
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_friend_list);
		
		setupActionBar();
		setButtonVisibilities();
		
		finalListView = (FinalListView) super.findViewById(R.id.listview);
		finalListView.setRemoveListener(this);

		myAdapter = new MyAdapter(this, userNmae, portrait);
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
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) 
	{
		getMenuInflater().inflate(R.menu.friend_list, menu);
		return true;
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
	
	
	
	
	
	
	/*********************		以下是ActionBar相关设置		*********************/
	private void setupActionBar()
	{
		getActionBar().setBackgroundDrawable(new ColorDrawable(Color.rgb(0x1E, 0x90, 0xFF)));
		getActionBar().setDisplayShowHomeEnabled(false);
		getActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM); 
		getActionBar().setCustomView(R.layout.yg_friendlist_actionbar);
		//Color 61 182 253
	}
	
	private void setButtonVisibilities()
	{
		final TextView title = (TextView)findViewById(R.id.yg_friendlist_actionbar_default_layout_title);
		final SearchView search = (SearchView)findViewById(R.id.yg_friendlist_actionbar_default_layout_search);
		
		search.setOnQueryTextListener(new OnQueryTextListener()
		{
			@Override
			public boolean onQueryTextSubmit(String query)
			{
				flip();
				search.setQuery("", false);
				search.setIconified(true);
				return false;
			}
			
			@Override
			public boolean onQueryTextChange(String newText)
			{
				return false;
			}
		});
		
		search.setOnCloseListener(new OnCloseListener()
		{
			@Override
			public boolean onClose()
			{
				AnimationSet set = new AnimationSet(true);
			
				TranslateAnimation translateAnim = new TranslateAnimation(-300, 0, 0, 0);
				translateAnim.setDuration(500);
				ScaleAnimation scaleAnim = new ScaleAnimation(0, 1, 0, 1);
				
				scaleAnim.setDuration(300);
				set.addAnimation(translateAnim);
				set.addAnimation(scaleAnim);
				
				title.startAnimation(set);
				
				set.setAnimationListener(new AnimationListener()
				{
					@Override
					public void onAnimationStart(Animation arg0) 
					{
						title.setVisibility(View.VISIBLE);
					}
					
					@Override
					public void onAnimationRepeat(Animation arg0) { }
					
					@Override
					public void onAnimationEnd(Animation arg0) { }
				});

				return false;
			}
		});
		
		search.setOnSearchClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v) 
			{
				AnimationSet set = new AnimationSet(true);
				
				TranslateAnimation translateAnim = new TranslateAnimation(0, -300, 0, 0);
				translateAnim.setDuration(500);
				ScaleAnimation scaleAnim = new ScaleAnimation(1, 0, 1, 0);
				scaleAnim.setDuration(400);
				set.addAnimation(translateAnim);
				set.addAnimation(scaleAnim);
				
				title.startAnimation(set);
				
				set.setAnimationListener(new AnimationListener()
				{
					@Override
					public void onAnimationStart(Animation arg0) { }
					
					@Override
					public void onAnimationRepeat(Animation arg0) { }
					
					@Override
					public void onAnimationEnd(Animation arg0)
					{
						title.setVisibility(View.GONE);
					}
				});
			}
		});
	}

	private View getActionBarView() 
	{
	    Window window = getWindow();
	    View v = window.getDecorView();
	    int resId = getResources().getIdentifier("action_bar_container", "id", "android");
	    return v.findViewById(resId);
	}
	
	private void setVisibility()
	{
		RelativeLayout defaultLayout = (RelativeLayout) findViewById(R.id.yg_friendlist_actionbar_default_layout);
		RelativeLayout resultLayout = (RelativeLayout) findViewById(R.id.yg_friendlist_actionbar_result_layout);
		
		AlphaAnimation showAlpha = new AlphaAnimation(0, 1);
		showAlpha.setStartOffset(400);
		showAlpha.setDuration(300);
		AlphaAnimation hideAlpha = new AlphaAnimation(1, 0);
		hideAlpha.setDuration(300);
		
		if (defaultLayout.getVisibility() == View.GONE)
		{
			defaultLayout.setVisibility(View.VISIBLE);
			defaultLayout.startAnimation(showAlpha);
			resultLayout.setVisibility(View.GONE);
			resultLayout.startAnimation(hideAlpha);
		}
		else
		{
			defaultLayout.setVisibility(View.GONE);
			resultLayout.setVisibility(View.VISIBLE);

			defaultLayout.startAnimation(hideAlpha);
			resultLayout.startAnimation(showAlpha);
		}
	}
	
	private void flip()
	{
		View actionbar = getActionBarView();
		AnimatorSet set;
		set = (AnimatorSet) AnimatorInflater.loadAnimator(FriendListActivity.this, R.animator.yg_friendlist_flip);
		set.setTarget(actionbar);
		set.addListener(new AnimatorListener()
		{
			@Override
			public void onAnimationStart(Animator animation) 
			{
				setVisibility();
			}
			
			@Override
			public void onAnimationRepeat(Animator animation) {	}
			
			@Override
			public void onAnimationEnd(Animator animation) 
			{
				RelativeLayout resultLayout = (RelativeLayout) findViewById(R.id.yg_friendlist_actionbar_result_layout);
				
				if (resultLayout.getVisibility() != View.GONE)
				{
					ImageView delete = (ImageView)findViewById(R.id.yg_friendlist_actionbar_result_layout_delete);
					delete.setOnClickListener(new OnClickListener()
					{
						@Override
						public void onClick(View v) 
						{
							flip();
						}
					});
				}
				else
					setTitle("Friends");
			}
			
			@Override
			public void onAnimationCancel(Animator animation) { }
		});
		set.start();
	}
	/*********************		以上是ActionBar相关设置		*********************/
}

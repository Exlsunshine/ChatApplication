package com.yg.ui;

import java.util.ArrayList;
import java.util.List;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.app.ActionBar;
import android.app.TabActivity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.SearchView.OnCloseListener;
import android.widget.SearchView.OnQueryTextListener;
import android.widget.TabHost;
import android.widget.TextView;

import com.example.testmobiledatabase.R;
import com.lj.satellitemenu.SatelliteMenu;
import com.lj.satellitemenu.SatelliteMenu.SateliteClickedListener;
import com.lj.satellitemenu.SatelliteMenuItem;
import com.lj.settings.ActivitySetting;
import com.lj.shake.ActivityShake;
import com.tp.ui.PublicActivity;
import com.tp.ui.SendPostActivity;
import com.yg.ui.friendlist.FriendListActivity;
import com.yg.ui.recentdialog.RecentDialogActivity;

@SuppressWarnings("deprecation")
public class MainActivity extends TabActivity implements OnCheckedChangeListener
{
	private class TabTags
	{
		public static final String recentDialogTab = "recentDialogTab";
		public static final String friendListTab = "friendListTab";
	}
	
	private TabHost tabHost;  
	private Intent recentDialogIntent;  
	private Intent friendListIntent; 
	
	private SatelliteMenu centerControlMenu;
	private RadioButton recentDialogRb;
	private RadioButton friendListRb;
	private ImageView mask;
	
	private final int MENU_SHAKE_INDEX = 2;
	private final int MENU_FRIENDCIRCLE_INDEX = 3;
	private final int MENU_SENDPOST_INDEX = 4;
	private final int MENU_PROFILE_INDEX = 5;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);  
		setContentView(R.layout.activity_main);  
		
		setupLayout();
		setupListener();
		setupTabData();
		setupRecentDialogActionBar();
		
		registerReceiver(broadcastReceiver, intentFilter());
	}
	
	private AnimatorSet flipAnimationSet;
	
	private IntentFilter intentFilter()
	{
		IntentFilter filter = new IntentFilter();
		filter.addAction("friendlist_query_refresh_complete");
		
		return filter;		
	}
	
	private BroadcastReceiver broadcastReceiver = new BroadcastReceiver()
	{
		@Override
		public void onReceive(Context context, Intent intent)
		{
			if ((intent.getAction().equals("friendlist_query_refresh_complete")))
			{
				TextView title = (TextView)findViewById(R.id.yg_friendlist_actionbar_result_layout_title);
				title.setText(intent.getStringExtra("query") + "(" + intent.getStringExtra("number") + ")");
			}
		}
	};
	
	private void setupLayout()
	{
		recentDialogRb = (RadioButton) findViewById(R.id.main_activity_recent_dialog);
		friendListRb = (RadioButton) findViewById(R.id.main_activity_friend_list);
		mask = (ImageView)findViewById(R.id.main_activity_mask);
		
		centerControlMenu = (SatelliteMenu)findViewById(R.id.lj_menu);
		centerControlMenu.setSatelliteDistance(340);
		centerControlMenu.setMainImage(R.drawable.sat_main_style2);
		centerControlMenu.setTotalSpacingDegree(180);
		centerControlMenu.setCloseItemsOnClick(true);
		centerControlMenu.setExpandDuration(500);
		centerControlMenu.setMaskView(mask);
		
		List<SatelliteMenuItem> items = new ArrayList<SatelliteMenuItem>();
        items.add(new SatelliteMenuItem(1, android.R.color.transparent));
        items.add(new SatelliteMenuItem(2, R.drawable.ic_3));
        items.add(new SatelliteMenuItem(3, R.drawable.ic_4));
        items.add(new SatelliteMenuItem(4, R.drawable.yg_main_post_icon));
        items.add(new SatelliteMenuItem(5, R.drawable.yg_main_profile_icon));
        items.add(new SatelliteMenuItem(6, android.R.color.transparent));
        
        centerControlMenu.addItems(items);        
        
        centerControlMenu.setOnItemClickedListener(new SateliteClickedListener() 
        {
			public void eventOccured(final int id) 
			{
				Log.e("sat", "Clicked on " + id);
				mask.setVisibility(View.GONE);
				new Handler().postDelayed(new Runnable() 
				{
					@Override
					public void run() 
					{
						if (id == MENU_SHAKE_INDEX)
						{
							Intent intent = new Intent();
							intent.setClass(MainActivity.this, ActivityShake.class);
							startActivity(intent);
						}
						else if (id == MENU_FRIENDCIRCLE_INDEX)
						{
							Intent intent = new Intent();
							intent.setClass(MainActivity.this, PublicActivity.class);
							startActivity(intent);
						}
						else if (id == MENU_SENDPOST_INDEX)
						{
							Intent intent = new Intent();
							intent.setClass(MainActivity.this, SendPostActivity.class);
							startActivity(intent);
						}
						else if (id == MENU_PROFILE_INDEX)
						{
							Intent intent = new Intent();
							intent.setClass(MainActivity.this, ActivitySetting.class);
							startActivity(intent);
						}
					}
				}, 800);
			}
		});
	}
	
	private void setupListener()
	{
		recentDialogRb.setOnCheckedChangeListener(this);  
		friendListRb.setOnCheckedChangeListener(this);  
		mask.setOnTouchListener(new OnTouchListener() 
		{
			@Override
			public boolean onTouch(View v, MotionEvent event) 
			{
				int action = event.getAction();
				if (action == MotionEvent.ACTION_DOWN)
					centerControlMenu.onClick();
				return true;
			}
		});
	}
	
	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
	{
		if(isChecked)
		{  
			switch (buttonView.getId()) 
			{  
			case R.id.main_activity_recent_dialog:  
				setupRecentDialogActionBar();
				this.tabHost.setCurrentTabByTag(TabTags.recentDialogTab);  
				break;  
			case R.id.main_activity_friend_list:  
				setupActionBar();
				setButtonVisibilities();
				this.tabHost.setCurrentTabByTag(TabTags.friendListTab);  
				break; 
			}  
		}  
	}
	 
	private void setupTabData()
	{  
		recentDialogIntent = new Intent(this, RecentDialogActivity.class);  
		friendListIntent = new Intent(this, FriendListActivity.class);  
		
		tabHost = getTabHost();  
		tabHost.addTab(buildTabSpec(TabTags.recentDialogTab, TabTags.recentDialogTab, R.drawable.ic_launcher, this.recentDialogIntent));  
		tabHost.addTab(buildTabSpec(TabTags.friendListTab, TabTags.friendListTab, R.drawable.ic_launcher, this.friendListIntent));  
	}  
	      
	private TabHost.TabSpec buildTabSpec(String tag, String resLabel, int resIcon, final Intent content)
	{  
		return tabHost.newTabSpec(tag).setIndicator(resLabel, getResources().getDrawable(resIcon)).setContent(content);  
	} 

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		return false;
	}
	
	@Override
	public void onBackPressed() 
	{
		minimizeApp(MainActivity.this);
	}

	public static void minimizeApp(Context context)
	{
		Intent startMain = new Intent(Intent.ACTION_MAIN);
		startMain.addCategory(Intent.CATEGORY_HOME);
		startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startActivity(startMain);
	}
	
	/*********************		以下是RecentDialog ActionBar相关设置		*********************/
	private void setupRecentDialogActionBar()
	{
		getActionBar().setBackgroundDrawable(new ColorDrawable(Color.rgb(0x1E, 0x90, 0xFF)));
		getActionBar().setDisplayShowHomeEnabled(false);
		getActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM); 
		getActionBar().setCustomView(R.layout.yg_recent_dialog_actionbar);
	}
	/*********************		以上是RecentDialog ActionBar相关设置		*********************/
	
	/*********************		以下是FriendList ActionBar相关设置		*********************/
	private void setupActionBar()
	{
		getActionBar().setBackgroundDrawable(new ColorDrawable(Color.rgb(0x1E, 0x90, 0xFF)));
		getActionBar().setDisplayShowHomeEnabled(false);
		getActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM); 
		getActionBar().setCustomView(R.layout.yg_friendlist_actionbar);
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
				Intent intent = new Intent("friendlist_query_refresh_request");
				intent.putExtra("query", query);
				sendBroadcast(intent);
				
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
			
				TranslateAnimation translateAnim = new TranslateAnimation(-3000, 0, 0, 0);
				translateAnim.setDuration(500);
				
				set.addAnimation(translateAnim);
				
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
				
				TranslateAnimation translateAnim = new TranslateAnimation(0, -3000, 0, 0);
				translateAnim.setDuration(500);
				set.addAnimation(translateAnim);
				
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
		
		flipAnimationSet = (AnimatorSet) AnimatorInflater.loadAnimator(MainActivity.this, R.animator.yg_friendlist_flip);
		flipAnimationSet.setTarget(actionbar);
		flipAnimationSet.addListener(new AnimatorListener()
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
							Intent intent = new Intent("friendlist_query_refresh_request");
							intent.putExtra("query", "");
							sendBroadcast(intent);
							
							TextView title = (TextView)findViewById(R.id.yg_friendlist_actionbar_result_layout_title);
							title.setText("");
						}
					});
				}
				else
					setTitle("好友");
			}
			
			@Override
			public void onAnimationCancel(Animator animation) { }
		});
		flipAnimationSet.start();
	}
	/*********************		以上是FriendList ActionBar相关设置		*********************/
}
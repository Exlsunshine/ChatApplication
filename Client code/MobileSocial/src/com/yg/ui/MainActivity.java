package com.yg.ui;

import java.util.ArrayList;
import java.util.List;

import com.example.testmobiledatabase.R;
import com.lj.pathbutton.SatelliteMenu;
import com.lj.pathbutton.SatelliteMenu.SateliteClickedListener;
import com.lj.pathbutton.SatelliteMenuItem;
import com.lj.settings.ActivitySetting;
import com.lj.shake.ActivityShake;
import com.yg.ui.friendlist.FriendListActivity;
import com.yg.ui.recentdialog.RecentDialogActivity;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.animation.Animator.AnimatorListener;
import android.app.ActionBar;
import android.app.TabActivity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
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
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.SearchView.OnCloseListener;
import android.widget.SearchView.OnQueryTextListener;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.TabHost;
import android.widget.TextView;

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
	
	SatelliteMenu centerControlMenu;
	private RadioButton recentDialogRb;
	private RadioButton friendListRb;
	
	 
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);  
		setContentView(R.layout.activity_main);  
		
		setupLayout();
		setupListener();
		setupTabData();
	}
	
	private void setupLayout()
	{
		recentDialogRb = ((RadioButton) findViewById(R.id.main_activity_recent_dialog));
		friendListRb = ((RadioButton) findViewById(R.id.main_activity_friend_list));
		
		centerControlMenu = (SatelliteMenu)findViewById(R.id.lj_menu);
		centerControlMenu.setSatelliteDistance(340);
		centerControlMenu.setMainImage(R.drawable.sat_main);
		centerControlMenu.setTotalSpacingDegree(180);
		centerControlMenu.setCloseItemsOnClick(true);
		centerControlMenu.setExpandDuration(500);
		
		List<SatelliteMenuItem> items = new ArrayList<SatelliteMenuItem>();
        items.add(new SatelliteMenuItem(1, R.drawable.ic_1));
        items.add(new SatelliteMenuItem(2, R.drawable.ic_3));
        items.add(new SatelliteMenuItem(3, R.drawable.ic_4));
        items.add(new SatelliteMenuItem(4, R.drawable.ic_5));
        items.add(new SatelliteMenuItem(5, R.drawable.ic_6));
        items.add(new SatelliteMenuItem(6, R.drawable.ic_2));
        
        centerControlMenu.addItems(items);        
        
        centerControlMenu.setOnItemClickedListener(new SateliteClickedListener() 
        {
			public void eventOccured(int id) 
			{
				Log.e("sat", "Clicked on " + id);
				if (id == 1)
				{
					Intent intent = new Intent();
					intent.setClass(MainActivity.this, ActivityShake.class);
					startActivity(intent);
				}
				if (id == 2)
				{
					Intent intent = new Intent();
					intent.setClass(MainActivity.this, ActivitySetting.class);
					startActivity(intent);
				}
			}
		});
	}
	
	private void setupListener()
	{
		recentDialogRb.setOnCheckedChangeListener(this);  
		friendListRb.setOnCheckedChangeListener(this);  
	}
	
	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
	{
		if(isChecked)
		{  
			switch (buttonView.getId()) 
			{  
			case R.id.main_activity_recent_dialog:  
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
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	/*********************		以下是FriendList ActionBar相关设置		*********************/
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
		set = (AnimatorSet) AnimatorInflater.loadAnimator(MainActivity.this, R.animator.yg_friendlist_flip);
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
	/*********************		以上是FriendList ActionBar相关设置		*********************/
}
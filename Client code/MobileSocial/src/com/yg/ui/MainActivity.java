package com.yg.ui;

import java.util.ArrayList;
import java.util.List;

import com.example.testmobiledatabase.R;
import com.lj.pathbutton.SatelliteMenu;
import com.lj.pathbutton.SatelliteMenu.SateliteClickedListener;
import com.lj.pathbutton.SatelliteMenuItem;
import com.lj.settings.ActivitySetting;
import com.lj.shake.ActivityShake;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.RadioButton;
import android.widget.TabHost;

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
        
        centerControlMenu.setOnItemClickedListener(new SateliteClickedListener() {
			
			public void eventOccured(int id) {
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
}
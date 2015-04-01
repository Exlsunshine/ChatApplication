package com.yg.ui;

import com.example.testmobiledatabase.R;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.RadioButton;
import android.widget.TabHost;
import android.widget.Toast;

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
	
	private Button centerControlBtn;
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
		centerControlBtn = (Button)findViewById(R.id.main_activity_center_control_btn);
	}
	
	private void setupListener()
	{
		recentDialogRb.setOnCheckedChangeListener(this);  
		friendListRb.setOnCheckedChangeListener(this);  
		
		centerControlBtn.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View arg0) 
			{
				Toast.makeText(MainActivity.this, "You got me.", Toast.LENGTH_SHORT).show();
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
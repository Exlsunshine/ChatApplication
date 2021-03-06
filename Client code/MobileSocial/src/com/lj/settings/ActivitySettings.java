package com.lj.settings;


import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;

import com.example.testmobiledatabase.R;
import com.lj.setting.achievement.FragmentAchieve;
import com.lj.setting.game.FragmentGameSetting;
import com.lj.setting.game.GameSetting;
import com.lj.setting.userinfo.FragmentUserInfoSetting;
import com.yg.commons.ConstantValues;


import android.app.Activity;
import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v13.app.FragmentPagerAdapter;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class ActivitySettings extends Activity implements ActionBar.TabListener
{
	private final int FRAGMENT_COUNT = 3;
	private final int FRAGMENT_USERSETTING_INDEX = 0;
	private final int FRAGMENT_GAMESETTING_INDEX = 1;
	private final int FRAGMENT_ACHIEVE_INDEX = 2;
	
	private final String TITLE_USERSETTING = "信息设置";
	private final String TITLE_GAMESETTING = "游戏设置";
	private final String TITLE_ACHIEVE = "成就浏览";
	
	private FragmentUserInfoSetting gFragmentUserInfoSetting = null;
	private int gCurrentPosition = 0;
	private TextView gRightText = null;
    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    ViewPager mViewPager;
    
    private HashMap<String, String> gChangeMap = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lj_setting_activity_main);
        gChangeMap = new HashMap<String, String>();
        // Set up the action bar.
        setupDialogActionBar();
        final ActionBar actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        // When swiping between different sections, select the corresponding
        // tab. We can also use ActionBar.Tab#select() to do this if we have
        // a reference to the Tab.
        mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                actionBar.setSelectedNavigationItem(position);
            }
        });
        
        // For each of the sections in the app, add a tab to the action bar.
        for (int i = 0; i < mSectionsPagerAdapter.getCount(); i++) {
            // Create a tab with text corresponding to the page title defined by
            // the adapter. Also specify this Activity object, which implements
            // the TabListener interface, as the callback (listener) for when
            // this tab is selected.
            actionBar.addTab(
                    actionBar.newTab()
                            .setText(mSectionsPagerAdapter.getPageTitle(i))
                            .setTabListener(this));
        }
    }
    
    private void saveGame()
	{
    	if (gChangeMap.size() == 0)
    	{
    		Toast.makeText(this, "没有未保存的信息", Toast.LENGTH_LONG).show();
    		return;
    	}
		Iterator<Entry<String, String>> iter = gChangeMap.entrySet().iterator();
		while (iter.hasNext()) 
		{
			Map.Entry<String, String> entry = (Map.Entry<String, String>) iter.next();
			final String key = entry.getKey().toString();
			final String val = entry.getValue().toString();
			
			new Thread()
			{
				public void run() 
				{
					if (key.equals("game"))
					{
						int gametype = Integer.valueOf(val);
						GameSetting gameSetting = new GameSetting();
						gameSetting.setGameType(ConstantValues.user.getID(), gametype);
					}
					else if (key.equals("eightpuzzle"))
					{
						GameSetting gameSetting = new GameSetting();
						gameSetting.setEightPuzzleImage(ConstantValues.user.getID(), val);
					}
					else if (key.equals("bazinga"))
					{
						GameSetting gameSetting = new GameSetting();
						gameSetting.setBazingaScore(ConstantValues.user.getID(), Integer.valueOf(val));
					}
				};
			}.start();
		}
		gChangeMap.clear();
		Toast.makeText(this, "保存游戏设置成功", Toast.LENGTH_LONG).show();
	}
    
    private void saveUserinfo()
	{
    	if (gChangeMap.size() == 0)
    	{
    		Toast.makeText(this, "没有未保存的信息", Toast.LENGTH_LONG).show();
    		return;
    	}
		Iterator<Entry<String, String>> iter = gChangeMap.entrySet().iterator();
		while (iter.hasNext()) 
		{
			Map.Entry<String, String> entry = (Map.Entry<String, String>) iter.next();
			final String key = entry.getKey().toString();
			final String val = entry.getValue().toString();
			
			new Thread()
			{
				public void run() 
				{
					if (key.equals("nickname"))
						ConstantValues.user.setNickName(val);
					else if (key.equals("sex"))
						ConstantValues.user.setSex(val);
					else if (key.equals("date"))
						ConstantValues.user.setBirthday(val);
					else if (key.equals("hometown"))
						ConstantValues.user.setHometown(val);
					else if (key.equals("phone"))
						ConstantValues.user.setPhoneNumber(val);
					else if (key.equals("portrait"))
						ConstantValues.user.setPortrait(val);
				};
			}.start();
		}
		gChangeMap.clear();
		Toast.makeText(this, "保存资料成功", Toast.LENGTH_LONG).show();
	}
    
    private void save()
    {
    	if (gCurrentPosition == FRAGMENT_USERSETTING_INDEX)
		{
			gFragmentUserInfoSetting.clearTextFocus();
			saveUserinfo();
		}
		else if (gCurrentPosition == FRAGMENT_GAMESETTING_INDEX)
			saveGame();
    }
    
    private void showBackDialog()
	{
		final AlertDialog dialog = new AlertDialog.Builder(this,R.style.LoginDialogAnimation).create();
		dialog.setCanceledOnTouchOutside(true);
		dialog.show();
		dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
		dialog.getWindow().setSoftInputMode( WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
		
		Window window = dialog.getWindow();
		window.setContentView(R.layout.lj_setting_remind_dialog);
		Button cancel = (Button) window.findViewById(R.id.lj_setting_remind_dialog_cancel);
		cancel.setOnClickListener(new OnClickListener() 
		{
			@Override
			public void onClick(View arg0) 
			{
				dialog.dismiss();
			}
		});
		Button confirm = (Button) window.findViewById(R.id.lj_setting_remind_dialog_confirm);
		confirm.setOnClickListener(new OnClickListener() 
		{
			@Override
			public void onClick(View arg0) 
			{
				gChangeMap.clear();
				finish();
			}
		});
	}
    
    private void back()
    {
    	if (gCurrentPosition == FRAGMENT_USERSETTING_INDEX)
    		gFragmentUserInfoSetting.clearTextFocus();
    	if (!gChangeMap.isEmpty())
    		showBackDialog();
			/*new AlertDialog.Builder(ActivitySettings.this)   
			.setTitle("确认")  
			.setMessage("您有未保存的信息，是否退出？")  
			.setPositiveButton("是", new DialogInterface.OnClickListener() 
			{
				@Override
				public void onClick(DialogInterface dialog, int which) 
				{
					gChangeMap.clear();
					finish();
				}
			})  
			.setNegativeButton("否", null)  
			.show();  */
		else
		{
			gChangeMap.clear();
			finish();
		}
    }
    
    private void setupDialogActionBar()
	{
		getActionBar().setBackgroundDrawable(new ColorDrawable(Color.rgb(0x1E, 0x90, 0xFF)));
		getActionBar().setDisplayShowHomeEnabled(false);
		getActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM); 
		getActionBar().setCustomView(R.layout.lj_common_actionbar);
	
		LinearLayout back = (LinearLayout)findViewById(R.id.lj_common_actionbar_back);
		LinearLayout confirm = (LinearLayout)findViewById(R.id.lj_common_actionbar_confirm);
		TextView titleText = (TextView)findViewById(R.id.lj_common_actionbar_title);
		titleText.setText("资料");
		gRightText = (TextView)findViewById(R.id.lj_common_actionbar_confirm_text);
		gRightText.setText("保存");
		back.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				back();
			}
		});
		confirm.setOnClickListener(new OnClickListener() 
		{
			@Override
			public void onClick(View v) 
			{
				save();
			}
		});
	}

    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) 
    {
        mViewPager.setCurrentItem(tab.getPosition());
        gCurrentPosition = tab.getPosition();
        if (gRightText != null)
	        if (gCurrentPosition == FRAGMENT_USERSETTING_INDEX)
	        {
	        	gRightText.setVisibility(View.VISIBLE);
	        	gChangeMap.clear();
	        }
	        else if (gCurrentPosition == FRAGMENT_GAMESETTING_INDEX)
	        {
	        	gRightText.setVisibility(View.VISIBLE);
	        	gChangeMap.clear();
	        }
	        else if (gCurrentPosition == FRAGMENT_ACHIEVE_INDEX)
	        {
	        	gRightText.setVisibility(View.INVISIBLE);
	        	gChangeMap.clear();
	        }
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) 
    {
    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) 
    {
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter 
    {
        public SectionsPagerAdapter(FragmentManager fm) 
        {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) 
        {
        	Fragment fragment = null;
        	if (position == FRAGMENT_USERSETTING_INDEX)
        	{
        		fragment = new FragmentUserInfoSetting(ActivitySettings.this, gChangeMap);
        		gFragmentUserInfoSetting = (FragmentUserInfoSetting) fragment;
        	}
        	else if (position == FRAGMENT_GAMESETTING_INDEX)
        	{
        		fragment = new FragmentGameSetting(ActivitySettings.this, gChangeMap);
        	}
        	else if (position == FRAGMENT_ACHIEVE_INDEX)
        	{
        		fragment = new FragmentAchieve();
        	}
            return fragment;
        }

        @Override
        public int getCount() 
        {
            return FRAGMENT_COUNT;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            Locale l = Locale.getDefault();
            switch (position) {
                case 0:
                    return TITLE_USERSETTING.toUpperCase(l);
                case 1:
                    return TITLE_GAMESETTING.toUpperCase(l);
                case 2:
                    return TITLE_ACHIEVE.toUpperCase(l);
            }
            return null;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) 
    {
    	if (keyCode == KeyEvent.KEYCODE_BACK)
    	{
    		back();
    	}
    	return super.onKeyDown(keyCode, event);
    }
}

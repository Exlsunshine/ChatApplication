package com.lj.setting.achievement;



import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.example.testmobiledatabase.R;
import com.yg.commons.ConstantValues;

import android.os.Bundle;
import android.os.Handler;
import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;

public class FragmentAchieve extends Fragment
{
	private View gView;
	private Context gContext;
	
	private final int[] ACHIEVE_ITEM_ID = {R.id.lj_setting_achieve_1, R.id.lj_setting_achieve_2, R.id.lj_setting_achieve_3, R.id.lj_setting_achieve_4,
			                               R.id.lj_setting_achieve_5, R.id.lj_setting_achieve_6, R.id.lj_setting_achieve_7, R.id.lj_setting_achieve_8};
	private final int[] ACHIEVE_TITLE_ID = {R.string.lj_achieve_title_1, R.string.lj_achieve_title_2, R.string.lj_achieve_title_3, R.string.lj_achieve_title_4,
			                                R.string.lj_achieve_title_5, R.string.lj_achieve_title_6, R.string.lj_achieve_title_7, R.string.lj_achieve_title_8};
	private final String[] COLUMN_NAME_LIST = {"friends_num", "shake_num", "game_challenged_success", "game_challenged_fail",
            								   "game_challeng_success", "game_challeng_fail", "voice_num", "register_time"};
	
	private HashMap<String, LinearLayoutAchieveItem> gMapItem = null;
	
	private LinearLayoutAchieveItem[] gAchieveItemList = null;
	 
	private Handler gHandler = new Handler()
	{
		public void handleMessage(android.os.Message msg) 
		{
			if (msg.what == ConstantValues.InstructionCode.USERSTATISTICS_HANDLER_GETDATA)
			{
				UserStatistics userStatistics = (UserStatistics) msg.obj;
				ArrayList<HashMap<String, Object>> result = userStatistics.getUserStatistics();
				HashMap<String, Object> item = result.get(0);
				Iterator iter = item.entrySet().iterator();
				while (iter.hasNext()) 
				{
					Map.Entry entry = (Map.Entry) iter.next();
					String key = entry.getKey().toString();
					String val = entry.getValue().toString();
					LinearLayoutAchieveItem achieveItem = gMapItem.get(key);
					achieveItem.setAchieveProgress(Integer.valueOf(val));
				}
			}
		};
	};
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) 
	{
		gView = inflater.inflate(R.layout.lj_fragment_achieve, container, false);
        return gView;
    }
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) 
	{
		super.onActivityCreated(savedInstanceState);
		
		gAchieveItemList = new LinearLayoutAchieveItem[ACHIEVE_ITEM_ID.length];
		for (int i = 0; i < ACHIEVE_ITEM_ID.length; i++)
			gAchieveItemList[i] = (LinearLayoutAchieveItem) gView.findViewById(ACHIEVE_ITEM_ID[i]);
		initData();
		setupDialogActionBar();
	} 

	public FragmentAchieve(Context context) 
	{
		gContext = context;
	}
	
	
	private void setupDialogActionBar()
	{
		((Activity) gContext).getActionBar().setBackgroundDrawable(new ColorDrawable(Color.rgb(0x1E, 0x90, 0xFF)));
		((Activity) gContext).getActionBar().setDisplayShowHomeEnabled(false);
		((Activity) gContext).getActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM); 
		((Activity) gContext).getActionBar().setCustomView(R.layout.lj_settings_achieve_actionbar);
	
		LinearLayout back = (LinearLayout)((Activity) gContext).findViewById(R.id.lj_setting_achieve_actionbar_back);
		back.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				((Activity) gContext).finish();
			}
		});
	}
	
	private void initData()
	{
		gMapItem = new HashMap<String, LinearLayoutAchieveItem>();
		
		String[] achieveTitle = new String[ACHIEVE_TITLE_ID.length];
		for (int i = 0; i < ACHIEVE_TITLE_ID.length; i++)
		{
			achieveTitle[i] = getString(ACHIEVE_TITLE_ID[i]);
			gAchieveItemList[i].setAchieveTitleDes(achieveTitle[i], "");
			gMapItem.put(COLUMN_NAME_LIST[i], gAchieveItemList[i]);
		}
		new ThreadGetUserStatistics(ConstantValues.user.getID(), gHandler).start();
	}
}

package com.yg.ui.friendlist;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.testmobiledatabase.R;
import com.yg.commons.ConstantValues;
import com.yg.ui.dialog.DialogActivity;
import com.yg.ui.friendlist.implementation.Constellation;
import com.yg.user.FriendUser;

public class FriendDetailActivity extends Activity
{
	private ImageView portrait;
	private TextView name;
	private TextView joinDate;
	private TextView birthday;
	private TextView phoneNumber;
	private ImageView sexIcon;
	private TextView region;
	private Button message;
	
	private int friendUserID;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_friend_detail);

		friendUserID = getIntent().getIntExtra("friendUserID", -1);
		
		setupDialogActionBar();
		setupLayouts();
		loadData();
		setupClickListeners();
	}

	private void setupClickListeners()
	{
		message.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v) 
			{
				Intent intent = new Intent(FriendDetailActivity.this, DialogActivity.class);
				intent.putExtra("reveiewer", friendUserID);
				startActivity(intent);
			}
		});
	}
	
	private FriendUser getFriendByID(int id)
	{
		for (int i = 0; i < ConstantValues.user.getFriendList().size(); i++)
		{
			if (ConstantValues.user.getFriendList().get(i).getID() == id)
				return ConstantValues.user.getFriendList().get(i);
		}
		return null;
	}
	
	private void loadData()
	{
		FriendUser friend = getFriendByID(friendUserID);
		
		portrait.setImageBitmap(friend.getPortraitBmp());
		name.setText(friend.getAlias() == null ? friend.getNickName() : friend.getAlias() + "(" + friend.getNickName() + ")");
		joinDate.setText("加入时间小于 1  个月");
		sexIcon.setImageDrawable(friend.getSex().equalsIgnoreCase("male") 
				? getResources().getDrawable(R.drawable.yg_appkefu_ic_sex_male)
				: getResources().getDrawable(R.drawable.yg_appkefu_ic_sex_female));
		region.setText(friend.getHometown());
		
		birthday.setText(String.format("%s(%s)", friend.getBirthday(),Constellation.getConstellation(friend.getBirthday())));
		phoneNumber.setText(friend.getPhoneNumber());
	}
	
	private void setupLayouts()
	{
		portrait = (ImageView)findViewById(R.id.yg_activity_friend_detail_portrait);
		name = (TextView)findViewById(R.id.yg_activity_friend_detail_name);
		joinDate = (TextView)findViewById(R.id.yg_activity_friend_detail_join_date);
		sexIcon = (ImageView)findViewById(R.id.yg_activity_friend_detail_sex_icon);
		region = (TextView)findViewById(R.id.yg_activity_friend_detail_region);
		message = (Button)findViewById(R.id.yg_activity_friend_detail_message);
		birthday = (TextView)findViewById(R.id.yg_activity_friend_detail_birthday);
		phoneNumber = (TextView)findViewById(R.id.yg_activity_friend_detail_phonenumber);
	}
	
	private void setupDialogActionBar()
	{
		getActionBar().setBackgroundDrawable(new ColorDrawable(Color.rgb(0x1E, 0x90, 0xFF)));
		getActionBar().setDisplayShowHomeEnabled(false);
		getActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM); 
		getActionBar().setCustomView(R.layout.yg_friend_detail_actionbar);
		
		LinearLayout back = (LinearLayout)findViewById(R.id.yg_friend_detail_actionbar_back);
		back.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				FriendDetailActivity.this.finish();
			}
		});
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) 
	{
		return false;
		//getMenuInflater().inflate(R.menu.friend_detail, menu);
		//return true;
	}
}
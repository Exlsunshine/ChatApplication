package com.lj.setting.achievement;



import com.example.testmobiledatabase.R;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

public class LinearLayoutAchieveItem extends LinearLayout
{
	private ImageView gMedalImage = null;
	private TextView gTitleText = null;
	private TextView gDesText = null;
	private ImageView gStarImage = null;
	private ProgressBar gProgressBar = null;
	private TextView gProgressText = null;
	
	private final int[] STAR_IMAGE_ID = {R.drawable.lj_setting_achieve_star_zero, R.drawable.lj_setting_achieve_star_one, R.drawable.lj_setting_achieve_star_two, R.drawable.lj_setting_achieve_star_three};
	private final int[] MEDAL_IMAGE_ID = {R.drawable.lj_setting_achieve_medal_zero, R.drawable.lj_setting_achieve_medal_one, R.drawable.lj_setting_achieve_medal_two, R.drawable.lj_setting_achieve_medal_three};
	
	public LinearLayoutAchieveItem(Context context) 
	{
		super(context);
		init(context);
	}
	
	public LinearLayoutAchieveItem(Context context, AttributeSet attrs) 
	{
		super(context, attrs);
		init(context);
	}
	
	private void init(Context context)
	{
		LayoutInflater.from(context).inflate(R.layout.lj_setting_achieve_item, this, true);
		
		gMedalImage = (ImageView)findViewById(R.id.lj_setting_achieve_item_medal);
		gTitleText = (TextView)findViewById(R.id.lj_setting_achieve_item_title);
		gDesText = (TextView)findViewById(R.id.lj_setting_achieve_item_des);
		gStarImage = (ImageView)findViewById(R.id.lj_setting_achieve_item_star);
		gProgressBar = (ProgressBar)findViewById(R.id.lj_setting_achieve_item_progressbar);
		gProgressText = (TextView)findViewById(R.id.lj_setting_achieve_item_progress);
	} 
	
	public void setAchieveProgress(int progress)
	{
		String str = progress + "/30";
		gProgressText.setText(str);
		progress = progress > 30 ? 30 : progress;
		gProgressBar.setProgress(progress);
		int n = progress / 10;
		setAchieveStar(n);
	}
	
	public void setAchieveTitleDes(String title, String des)
	{
		gTitleText.setText(title);
		gDesText.setText(des);
	}
	
	private void setAchieveStar(int n)
	{
		gStarImage.setImageResource(STAR_IMAGE_ID[n]);
		gMedalImage.setImageResource(MEDAL_IMAGE_ID[n]);
	}
}

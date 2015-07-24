package com.lj.driftbottle.ui;

import com.example.testmobiledatabase.R;
import com.lj.driftbottle.logic.CommBottle;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class BottleHistoryText extends RelativeLayout
{
	private ImageLoader imageLoader = ImageLoader.getInstance();
	
	public BottleHistoryText(Context context) {
		super(context);
		init(context);
	}
	
	public BottleHistoryText(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}
	
	private void init(Context context)
	{
		LayoutInflater.from(context).inflate(R.layout.lj_driftbottle_history, this, true);
	}
	
	public void setInfo(CommBottle bottle)
	{
		setPortarit(bottle.getPortraitURL());
		setNickname(bottle.getNickname());
		setHometown(bottle.getHometown());
		setSex(bottle.getSex());
		setHistoryText(bottle.getHistoryText());
		if (bottle.getBottleRelationStatus() == CommBottle.BOTTLE_RELATION_STATUS_DELETE)
			((TextView)findViewById(R.id.lj_bottle_history_address)).setText("已被删除");
	}
	
	private void setHistoryText(String text)
	{
		text = text.subSequence(0, text.length() - 1).toString();
		TextView tv = (TextView)findViewById(R.id.lj_bottle_history_text);
		tv.setText(text);
	}
	
	private void setSex(String sex)
	{
		ImageView img = (ImageView)findViewById(R.id.lj_bottle_history_sex);
		if (sex.equals("male"))
			img.setImageResource(R.drawable.yg_appkefu_ic_sex_male);
		else if (sex.equals("female"))
			img.setImageResource(R.drawable.yg_appkefu_ic_sex_female);
		else
			img.setImageResource(R.drawable.lj_bottle_drift);
	}
	
	private void setHometown(String hometown)
	{
		if (!hometown.equals(""))
			((TextView)findViewById(R.id.lj_bottle_history_address)).setText(hometown);
		else
			((TextView)findViewById(R.id.lj_bottle_history_address)).setText("正在漂流......");
	}
	
	private void setNickname(String nickname)
	{
		((TextView)findViewById(R.id.lj_bottle_history_name)).setText(nickname);
	}
	
	private void setPortarit(String url)
	{
		DisplayImageOptions options = new DisplayImageOptions.Builder()
			.showImageOnLoading(R.drawable.tp_loading_picture)
			.showImageOnFail(R.drawable.tp_loading_failed)
			.cacheInMemory(true).cacheOnDisk(true)
			.bitmapConfig(Bitmap.Config.RGB_565).build();
		ImageView img = (ImageView)findViewById(R.id.lj_bottle_history_portrait);
		imageLoader.displayImage(url, img, options);
	}
}

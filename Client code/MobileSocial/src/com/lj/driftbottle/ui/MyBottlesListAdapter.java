package com.lj.driftbottle.ui;

import java.util.List;

import com.example.testmobiledatabase.R;
import com.lj.driftbottle.logic.CommBottle;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.tp.ui.ImageZoomInActivity;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView.FindListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class MyBottlesListAdapter extends BaseAdapter
{
	private final int LAST_TEXT_LENGTH = 10;
	private Activity context = null;
	private List<CommBottle> list = null;
	private ImageLoader imageLoader = ImageLoader.getInstance();

	public MyBottlesListAdapter(Activity context, List<CommBottle> list) 
	{
		this.context = context;
		this.list = list;
	}
	
	@Override
	public int getCount() 
	{
		return list.size();
	}

	@Override
	public Object getItem(int position) 
	{
		return list.get(position);
	}

	@Override
	public long getItemId(int position)
	{
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		LayoutInflater inflater = context.getLayoutInflater();
		View itemView = inflater.inflate(R.layout.lj_my_bottles_list_item, null);  
		CommBottle bottle = list.get(position);
		
		ImageView img = (ImageView) itemView.findViewById(R.id.lj_bottle_item_img);
		DisplayImageOptions options = new DisplayImageOptions.Builder()
			.showImageOnLoading(R.drawable.tp_loading_picture)
			.showImageOnFail(R.drawable.tp_loading_failed)
			.cacheInMemory(true).cacheOnDisk(true)
			.bitmapConfig(Bitmap.Config.RGB_565).build();
		imageLoader.displayImage(bottle.getPortraitURL(), img, options);
		
		TextView addressView = (TextView) itemView.findViewById(R.id.lj_bottle_item_address);
		String hometown = null;
		if (bottle.getBottleStatus() == CommBottle.BOTTLE_STATUS_UNPICKED)
			hometown = "正在漂流......";
		else if (bottle.getBottleRelationStatus() == CommBottle.BOTTLE_RELATION_STATUS_DELETE)
			hometown = "被删除";
		else
			hometown = bottle.getHometown();
			
		addressView.setText(hometown);
		TextView historyTextView = (TextView) itemView.findViewById(R.id.lj_bottle_item_text);
		String text = bottle.getLastText();
		if (text.length() >= LAST_TEXT_LENGTH)
			text = text.substring(0, LAST_TEXT_LENGTH) + "......";
		historyTextView.setText(text);
		
		ImageView sexImg = (ImageView) itemView.findViewById(R.id.lj_bottle_item_sex);
		if (bottle.getSex().equals("male"))
			sexImg.setImageResource(R.drawable.yg_appkefu_ic_sex_male);
		else if (bottle.getSex().equals("female"))
			sexImg.setImageResource(R.drawable.yg_appkefu_ic_sex_female);
		else
			sexImg.setImageResource(R.drawable.lj_bottle_drift);
		return itemView;
	}

}

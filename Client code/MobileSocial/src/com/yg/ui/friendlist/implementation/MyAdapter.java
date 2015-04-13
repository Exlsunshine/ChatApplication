package com.yg.ui.friendlist.implementation;

import java.util.ArrayList;
import java.util.List;

import com.example.testmobiledatabase.R;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.TranslateAnimation;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MyAdapter extends BaseAdapter
{
	private static final String DEBUG_TAG = "MyAdapter______";
	private int firstVisibleItem;
	private int visibleItemCount;
	
	private List<String> list = null;
	private Context mContext;
	private LayoutInflater inflater = null;
	private List<Bitmap> list3 = new ArrayList<Bitmap>();
	private int viewWidth = 0;

	public MyAdapter(Context context, List<String> list, List<Bitmap> list3)
	{
		this.mContext = context;
		this.list = list;
		this.list3 = list3;
		this.inflater = LayoutInflater.from(this.mContext);
		
		this.mContext.registerReceiver(broadcastReceiver, intentFilter());
	}

	@Override
	public int getCount()
	{
		return this.list.size();
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

	public void remove(int position)
	{
		list.remove(position);
		list3.remove(position);
		notifyDataSetChanged();
	}

	public int viewWidth() 
	{
		return this.viewWidth;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent)
	{
		ViewHolder holder = null;

		if (convertView == null) 
		{
			holder = new ViewHolder();
			convertView = inflater.inflate(R.layout.yg_friendlist_item, null);
			holder.tvTitle = (TextView) convertView.findViewById(R.id.list_item);
			holder.btnIM = (ImageView) convertView.findViewById(R.id.picture);
			holder.btnDel = (ImageView) convertView.findViewById(R.id.button);
			holder.ll = (LinearLayout) convertView.findViewById(R.id.list1);
			viewWidth = holder.btnDel.getWidth();
			holder.ll.scrollTo(0, 0);
			convertView.setTag(holder);
		} 
		else

			holder = (ViewHolder) convertView.getTag();

		holder.tvTitle.setText(this.list.get(position));
		holder.btnIM.setImageBitmap(this.list3.get(position));

		
		
		int innerAnimationDuration = 100;
		if (position > firstVisibleItem || (position == 0 && firstVisibleItem == 0))
		{
			TranslateAnimation translate = new TranslateAnimation(-3000, 0, 0, 0);
			
			int delay = (position - firstVisibleItem - visibleItemCount + 3) * innerAnimationDuration;
			
			if (delay <= 0)
				delay = 800;
			else
				delay += 800;

			Log.i(DEBUG_TAG, "Delay is " + String.valueOf(delay) + "Position is " + String.valueOf(position - firstVisibleItem - visibleItemCount + 1));

			translate.setDuration(delay);
			convertView.startAnimation(translate);
		}
		else
		{
			TranslateAnimation translate = new TranslateAnimation(3000, 0, 0, 0);
			
			int delay = (firstVisibleItem - position) * innerAnimationDuration;
			
			if (delay <= 0)
				delay = 800;
			else
				delay += 800;

			Log.i(DEBUG_TAG, "Delay is " + String.valueOf(delay) + "Position is " + String.valueOf(position - firstVisibleItem - visibleItemCount + 1));

			translate.setDuration(delay);
			convertView.startAnimation(translate);
		}
		
		return convertView;
	}

	final static class ViewHolder
	{
		TextView tvTitle;
		ImageView btnIM;
		ImageView btnDel;
		LinearLayout ll;
	}
	
	private IntentFilter intentFilter()
	{
		IntentFilter filter = new IntentFilter();
		filter.addAction("firstVisibleItem");
		
		return filter;		
	}
	
	private BroadcastReceiver broadcastReceiver = new BroadcastReceiver()
	{
		@Override
		public void onReceive(Context context, Intent intent)
		{
			if ((intent.getAction().equals("firstVisibleItem")))
			{
				firstVisibleItem = intent.getIntExtra("firstVisibleItem", 0);
				visibleItemCount = intent.getIntExtra("visibleItemCount", 0);
			}
		}
	};
}
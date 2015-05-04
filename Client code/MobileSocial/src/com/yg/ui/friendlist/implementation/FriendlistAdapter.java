package com.yg.ui.friendlist.implementation;

import java.util.ArrayList;
import java.util.List;

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

import com.example.testmobiledatabase.R;

public class FriendlistAdapter extends BaseAdapter
{
	private static final String DEBUG_TAG = "FriendlistAdapter______";
	private int firstVisibleItem;
	private int visibleItemCount;
	
	private List<String> friendsName = null;
	private List<Bitmap> friendsPortrait = new ArrayList<Bitmap>();
	private Context context;
	private LayoutInflater inflater = null;
	private int viewWidth = 0;
	
	/**
	 * minimum milliseconds for the show up animation
	 */
	private final int MIN_ANIM_DURATION = 800;

	public FriendlistAdapter(Context context, List<String> friendsName, List<Bitmap> friendsPortrait)
	{
		this.context = context;
		this.friendsName = friendsName;
		this.friendsPortrait = friendsPortrait;
		this.inflater = LayoutInflater.from(this.context);
		
		this.context.registerReceiver(broadcastReceiver, intentFilter());
	}

	@Override
	public int getCount()
	{
		return this.friendsName.size();
	}

	@Override
	public Object getItem(int position)
	{
		return friendsName.get(position);
	}

	@Override
	public long getItemId(int position)
	{
		return position;
	}

	public void remove(int position)
	{
		friendsName.remove(position);
		friendsPortrait.remove(position);
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

		holder.tvTitle.setText(this.friendsName.get(position));
		holder.btnIM.setImageBitmap(this.friendsPortrait.get(position));

		int innerAnimationDuration = 80;
		if (position > firstVisibleItem || (position == 0 && firstVisibleItem == 0))
		{
			TranslateAnimation translate = new TranslateAnimation(-3000, 0, 0, 0);
			
			int delay = (position - firstVisibleItem - visibleItemCount + 3) * innerAnimationDuration;
			
			if (delay <= 0)
				delay = MIN_ANIM_DURATION;
			else
				delay += MIN_ANIM_DURATION;

			Log.i(DEBUG_TAG, "Delay is " + String.valueOf(delay) + "Position is " + String.valueOf(position - firstVisibleItem - visibleItemCount + 1));

			translate.setDuration(delay);
			convertView.startAnimation(translate);
		}
		else
		{
			TranslateAnimation translate = new TranslateAnimation(3000, 0, 0, 0);
			
			int delay = (firstVisibleItem - position) * innerAnimationDuration;
			
			if (delay <= 0)
				delay = MIN_ANIM_DURATION;
			else
				delay += MIN_ANIM_DURATION;

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
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
	private List<Boolean> friendsStarMark = new ArrayList<Boolean>();
	private Context context;
	private LayoutInflater inflater = null;
	private int viewWidth = 0;
	
	private boolean animationEnabled = true;
	
	/**
	 * minimum milliseconds for the show up animation
	 */
	private final int MIN_ANIM_DURATION = 800;

	public FriendlistAdapter(Context context, List<String> friendsName,
					List<Bitmap> friendsPortrait, List<Boolean> friendsStarMark)
	{
		this.context = context;
		this.friendsName = friendsName;
		this.friendsPortrait = friendsPortrait;
		this.friendsStarMark = friendsStarMark;
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
		friendsStarMark.remove(position);
		//notifyDataSetChanged();
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
			holder.star = (ImageView) convertView.findViewById(R.id.yg_friendlist_item_star);
			
			viewWidth = holder.btnDel.getWidth();
			holder.ll.scrollTo(0, 0);
			convertView.setTag(holder);
		} 
		else
			holder = (ViewHolder) convertView.getTag();

		holder.tvTitle.setText(friendsName.get(position));
		holder.btnIM.setImageBitmap(friendsPortrait.get(position));
		if (friendsStarMark.get(position))
			holder.star.setImageResource(R.drawable.yg_slide_yellow_star_yellow_icon);
		else
			holder.star.setImageResource(R.drawable.yg_slide_yellow_star_gray_icon);
		
		Log.i(DEBUG_TAG, "friendsStarMark.get(position) is " + friendsStarMark.get(position));
		
		if (animationEnabled)
		{
			int innerAnimationDuration = 80;
			if (position > firstVisibleItem || (position == 0 && firstVisibleItem == 0))
			{
				TranslateAnimation translate = new TranslateAnimation(-3000, 0, 0, 0);
				
				int delay = (position - firstVisibleItem - visibleItemCount + 3) * innerAnimationDuration;
				
				if (delay <= 0)
					delay = MIN_ANIM_DURATION;
				else
					delay += MIN_ANIM_DURATION;
				
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
	
				translate.setDuration(delay);
				convertView.startAnimation(translate);
			}
		}
		
		return convertView;
	}
	
	/**
	 * 开启或关闭动画效果
	 * @param enable
	 */
	public void enableAnimation(boolean enable)
	{
		Log.i(DEBUG_TAG, "Set animation as " + enable);
		animationEnabled = enable;
	}

	final static class ViewHolder
	{
		TextView tvTitle;
		ImageView btnIM;
		ImageView btnDel;
		LinearLayout ll;
		ImageView star;
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
package com.yg.ui.recentdialog.implementation;

import java.util.ArrayList;
import java.util.List;

import com.example.testmobiledatabase.R;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class RecentDialogAdapter extends BaseAdapter
{
	private List<String> names = null;
	private List<Bitmap> portraits = new ArrayList<Bitmap>();
	private Context mContext;
	private LayoutInflater inflater = null;
	private int viewWidth = 0;
	private List<String> messages = null;
	private List<String> dates = null;
	
	public RecentDialogAdapter(Context context, List<String> names, List<String> messages, List<String> dates, List<Bitmap> portraits)
	{
		this.mContext = context;
		this.names = names;
		this.messages = messages;
		this.dates = dates;
		this.portraits = portraits;
		inflater = LayoutInflater.from(this.mContext);
	}

	@Override
	public int getCount()
	{
		return this.names.size();
	}

	@Override
	public Object getItem(int position)
	{
		return names.get(position);
	}

	@Override
	public long getItemId(int position)
	{
		return position;
	}

	public void remove(int position)
	{
		names.remove(position);
		portraits.remove(position);
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
			convertView = inflater.inflate(R.layout.yg_recent_dialog_item, null);
			holder.tvFriendName = (TextView) convertView.findViewById(R.id.yg_recent_dialog_item_friend_name);
			holder.tvLastMsg = (TextView) convertView.findViewById(R.id.yg_recent_dialog_item_last_msg);
			holder.tvDate = (TextView) convertView.findViewById(R.id.yg_recent_dialog_item_date);
			holder.ivFriendPortrait = (ImageView) convertView.findViewById(R.id.yg_recent_dialog_item_portrait);
			holder.ivDelete = (ImageView) convertView.findViewById(R.id.yg_recent_dialog_item_delete);
			holder.ll = (LinearLayout) convertView.findViewById(R.id.yg_recent_dialog_item_linearlayout);
			viewWidth = holder.ivDelete.getWidth();
			holder.ll.scrollTo(0, 0);
			convertView.setTag(holder);
		} 
		else

			holder = (ViewHolder) convertView.getTag();

		holder.tvFriendName.setText(this.names.get(position));
		holder.tvLastMsg.setText(this.messages.get(position));
		holder.tvDate.setText(this.dates.get(position));
		holder.ivFriendPortrait.setImageBitmap(this.portraits.get(position));

		return convertView;
	}

	final static class ViewHolder
	{
		TextView tvFriendName;
		TextView tvLastMsg;
		TextView tvDate;
		ImageView ivFriendPortrait;
		ImageView ivDelete;
		LinearLayout ll;
	}
}
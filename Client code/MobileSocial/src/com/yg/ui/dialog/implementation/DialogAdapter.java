package com.yg.ui.dialog.implementation;

import java.util.List;

import com.example.testmobiledatabase.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class DialogAdapter extends BaseAdapter 
{
	private List<msgtext> list = null;
	private Context mContext;
	private LayoutInflater inflater = null;
	private String realSender = null;

	public DialogAdapter(Context context, List<msgtext> list, String realSender)
	{
		this.mContext = context;
		this.list = list;
		inflater = LayoutInflater.from(this.mContext);
		this.realSender = realSender;
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
		notifyDataSetChanged();
	}

	public void add(msgtext msg) 
	{
		list.add(msg);
		notifyDataSetChanged();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) 
	{
		ViewHolder holder = null;

		if (convertView == null) 
		{
			holder = new ViewHolder();
			convertView = inflater.inflate(R.layout.yg_dialoglist_item, null);
			holder.tvFriendText = (TextView) convertView.findViewById(R.id.yg_dialoglist_item_friend_text);
			holder.tvMyText = (TextView) convertView.findViewById(R.id.yg_dialoglist_item_my_text);
			holder.ivFriendPortrait = (ImageView) convertView.findViewById(R.id.yg_dialoglist_item_friend_portrait);
			holder.ivMyPortrait = (ImageView) convertView.findViewById(R.id.yg_dialoglist_item_my_portrait);
			holder.rlFriendLayout = (RelativeLayout) convertView.findViewById(R.id.yg_dialoglist_item_friend_layout);
			holder.rlMyLayout = (RelativeLayout) convertView.findViewById(R.id.yg_dialoglist_item_my_layout);
			holder.tvTime = (TextView) convertView.findViewById(R.id.yg_dialoglist_item_time);
			convertView.setTag(holder);
		} 
		else
			holder = (ViewHolder) convertView.getTag();

		holder.rlMyLayout.setVisibility(View.VISIBLE);
		holder.rlFriendLayout.setVisibility(View.VISIBLE);
		holder.tvTime.setVisibility(View.VISIBLE);

		if (realSender.equals(list.get(position).getreceiver())) 
		{
			holder.rlFriendLayout.setVisibility(View.GONE);
			holder.tvMyText.setText("  " + this.list.get(position).getmsg().toString() + "  ");
			holder.ivMyPortrait.setImageBitmap(this.list.get(position).getsenderView());
			holder.tvTime.setText(this.list.get(position).gettime().toString());
		} 
		else if (realSender.equals(list.get(position).getsender()))
		{
			holder.rlMyLayout.setVisibility(View.GONE);
			holder.tvFriendText.setText("  " + this.list.get(position).getmsg().toString() + "  ");
			holder.ivFriendPortrait.setImageBitmap(this.list.get(position).getreceiverView());
			holder.tvTime.setText(this.list.get(position).gettime().toString());
		} 
		else
		{
			holder.rlMyLayout.setVisibility(View.GONE);
			holder.rlFriendLayout.setVisibility(View.GONE);
			holder.tvTime.setVisibility(View.GONE);
		}

		return convertView;
	}

	final static class ViewHolder 
	{
		TextView tvFriendText, tvMyText;
		ImageView ivFriendImage, ivMyImage;
		ImageView ivFriendAudio, ivMyAudio;
		ImageView ivFriendPortrait, ivMyPortrait;
		RelativeLayout rlFriendLayout, rlMyLayout;
		TextView tvTime;
	}
}
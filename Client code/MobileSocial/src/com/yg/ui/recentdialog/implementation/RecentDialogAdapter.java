package com.yg.ui.recentdialog.implementation;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.text.SpannableString;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.testmobiledatabase.R;
import com.readystatesoftware.viewbadger.BadgeView;
import com.yg.commons.ConstantValues;
import com.yg.emoji.ParseEmojiMsgUtil;

public class RecentDialogAdapter extends BaseAdapter
{
	private static final String DEBUG_TAG = "RecentDialogAdapter______";
	private List<String> names = null;
	private List<Bitmap> portraits = new ArrayList<Bitmap>();
	private Context mContext;
	private LayoutInflater inflater = null;
	private int viewWidth = 0;
	private List<String> messages = null;
	private List<String> dates = null;
	private List<Integer> ids = null;
	private List<Integer> unreadAmount = null;
	
	public RecentDialogAdapter(Context context, List<String> names, List<String> messages, List<String> dates, List<Bitmap> portraits, List<Integer> ids, List<Integer> unreadAmount)
	{
		this.mContext = context;
		this.names = names;
		this.messages = messages;
		this.dates = dates;
		this.portraits = portraits;
		this.ids = ids;
		this.inflater = LayoutInflater.from(this.mContext);
		this.unreadAmount = unreadAmount;
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
		return ids.get(position);
	}

	public void remove(int position)
	{
		names.remove(position);
		portraits.remove(position);
		messages.remove(position);
		dates.remove(position);
		ids.remove(position);
		unreadAmount.remove(position);
		notifyDataSetChanged();
	}

	public int viewWidth() 
	{
		return this.viewWidth;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent)
	{
		if (convertView == null) 
		{
			final ViewHolder holder = new ViewHolder();
			convertView = inflater.inflate(R.layout.yg_recent_dialog_item, null);
			holder.tvFriendName = (TextView) convertView.findViewById(R.id.yg_recent_dialog_item_friend_name);
			holder.tvLastMsg = (TextView) convertView.findViewById(R.id.yg_recent_dialog_item_last_msg);
			holder.tvDate = (TextView) convertView.findViewById(R.id.yg_recent_dialog_item_date);
			holder.ivFriendPortrait = (ImageView) convertView.findViewById(R.id.yg_recent_dialog_item_portrait);
			holder.ivDelete = (ImageView) convertView.findViewById(R.id.yg_recent_dialog_item_delete);
			holder.ll = (LinearLayout) convertView.findViewById(R.id.yg_recent_dialog_item_linearlayout);
			viewWidth = holder.ivDelete.getWidth();
			holder.ll.scrollTo(0, 0);
			
			RelativeLayout target = (RelativeLayout) convertView.findViewById(R.id.yg_recent_dialog_item_outter_layout);
			holder.badge = new BadgeView(mContext, target);
			
			convertView.setTag(holder);
			
			SpannableString spannableString = ParseEmojiMsgUtil.getExpressionString(mContext, this.messages.get(position));
			
			holder.tvFriendName.setText(this.names.get(position));
			holder.tvLastMsg.setText(spannableString);
			holder.tvDate.setText(this.dates.get(position));
			holder.ivFriendPortrait.setImageBitmap(this.portraits.get(position));
			
			if (unreadAmount.get(position) > 0)
			{
				holder.badge.setAlpha(1);
				holder.badge.setText(String.valueOf(unreadAmount.get(position)));
				holder.badge.setTextColor(Color.WHITE);
				holder.badge.setBadgeBackgroundColor(Color.RED);
				holder.badge.setBadgePosition(BadgeView.POSITION_BOTTOM_RIGHT);
				holder.badge.show();
				holder.badge.setOnClickListener(new OnClickListener()
				{
					@Override
					public void onClick(View v) 
					{
						holder.badge.hide(true);
						
						Intent intent = new Intent(ConstantValues.InstructionCode.CLEAR_MESSAGE_RED_DOT);
						intent.putExtra("friendUserID", ids.get(position));
						mContext.sendBroadcast(intent);
					}
				});
			}
			else
				holder.badge.hide(true);
		} 
		else
		{
			final ViewHolder holder2 = (ViewHolder) convertView.getTag();

			SpannableString spannableString = ParseEmojiMsgUtil.getExpressionString(mContext, this.messages.get(position));
			
			holder2.tvFriendName.setText(this.names.get(position));
			holder2.tvLastMsg.setText(spannableString);
			holder2.tvDate.setText(this.dates.get(position));
			holder2.ivFriendPortrait.setImageBitmap(this.portraits.get(position));
			
			if (unreadAmount.get(position) > 0)
			{
				holder2.badge.setAlpha(1);
				holder2.badge.setText(String.valueOf(unreadAmount.get(position)));
				holder2.badge.setTextColor(Color.WHITE);
				holder2.badge.setBadgeBackgroundColor(Color.RED);
				holder2.badge.setBadgePosition(BadgeView.POSITION_BOTTOM_RIGHT);
				holder2.badge.show();
				holder2.badge.setOnClickListener(new OnClickListener()
				{
					@Override
					public void onClick(View v) 
					{
						holder2.badge.hide(true);
						
						Intent intent = new Intent(ConstantValues.InstructionCode.CLEAR_MESSAGE_RED_DOT);
						intent.putExtra("friendUserID", ids.get(position));
						mContext.sendBroadcast(intent);
					}
				});
			}
			else
				holder2.badge.hide(true);
		}
		
		return convertView;
	}

	final static class ViewHolder
	{
		BadgeView badge;
		TextView tvFriendName;
		TextView tvLastMsg;
		TextView tvDate;
		ImageView ivFriendPortrait;
		ImageView ivDelete;
		LinearLayout ll;
	}
}
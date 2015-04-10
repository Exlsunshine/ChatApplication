package com.yg.ui.dialog.implementation;

import java.util.ArrayList;
import com.example.testmobiledatabase.R;
import com.yg.commons.ConstantValues;
import com.yg.message.AbstractMessage;
import com.yg.message.TextMessage;
import com.yg.user.FriendUser;

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
	private Context context;
	private ArrayList<AbstractMessage> messages;
	
	public DialogAdapter(Context context, ArrayList<AbstractMessage> messages)
	{
		this.context = context;
		this.messages = messages;
	}
	
	@Override
	public int getCount() 
	{
		return messages.size();
	}

	@Override
	public Object getItem(int position) {
		return messages.get(position);
	}

	@Override
	public long getItemId(int position) 
	{
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		ViewHolder holder = null;
		
		if (convertView == null)
		{
			holder = new ViewHolder();
			LayoutInflater inflater = LayoutInflater.from(context);
			convertView = inflater.inflate(R.layout.yg_dialoglist_item, null);
			
			holder.tvFriendText = (TextView) convertView.findViewById(R.id.yg_dialoglist_item_friend_text);
			holder.tvMyText = (TextView) convertView.findViewById(R.id.yg_dialoglist_item_my_text);
			holder.ivFriendImage = (ImageView) convertView.findViewById(R.id.yg_dialoglist_item_friend_image);
			holder.ivMyImage = (ImageView) convertView.findViewById(R.id.yg_dialoglist_item_my_image);
			holder.ivFriendAudio = (ImageView) convertView.findViewById(R.id.yg_dialoglist_item_friend_audio);
			holder.ivMyAudio = (ImageView) convertView.findViewById(R.id.yg_dialoglist_item_my_audio);
			holder.ivFriendPortrait = (ImageView) convertView.findViewById(R.id.yg_dialoglist_item_friend_portrait);
			holder.ivMyPortrait = (ImageView) convertView.findViewById(R.id.yg_dialoglist_item_my_portrait);
			holder.rlFriendLayout = (RelativeLayout) convertView.findViewById(R.id.yg_dialoglist_item_friend_layout);
			holder.rlMyLayout = (RelativeLayout) convertView.findViewById(R.id.yg_dialoglist_item_my_layout);
			holder.tvTime = (TextView) convertView.findViewById(R.id.yg_dialoglist_item_time);
			convertView.setTag(holder);
		}
		else
			holder = (ViewHolder) convertView.getTag();

		holder.tvTime.setVisibility(View.VISIBLE);
		holder.tvTime.setText(messages.get(position).getDate());
		
		if (messages.get(position).getFromUserID() == ConstantValues.user.getID())
		{
			holder.rlMyLayout.setVisibility(View.VISIBLE);
			holder.rlFriendLayout.setVisibility(View.GONE);
			
			switch (messages.get(position).getMessageType())
			{
			case ConstantValues.InstructionCode.MESSAGE_TYPE_AUDIO:
				holder.ivMyAudio.setVisibility(View.VISIBLE);
				holder.ivMyImage.setVisibility(View.GONE);
				holder.tvMyText.setVisibility(View.GONE);
				break;
			case ConstantValues.InstructionCode.MESSAGE_TYPE_IMAGE:
				holder.ivMyAudio.setVisibility(View.GONE);
				holder.ivMyImage.setVisibility(View.VISIBLE);
				holder.tvMyText.setVisibility(View.GONE);
				break;
			case ConstantValues.InstructionCode.MESSAGE_TYPE_TEXT:
				holder.ivMyAudio.setVisibility(View.GONE);
				holder.ivMyImage.setVisibility(View.GONE);
				holder.tvMyText.setVisibility(View.VISIBLE);
				holder.tvMyText.setText(((TextMessage)messages.get(position)).getText());
				holder.ivMyPortrait.setImageBitmap(ConstantValues.user.getPortraitBmp());
				break;
			default:
				break;
			}
		}
		else
		{
			holder.rlMyLayout.setVisibility(View.GONE);
			holder.rlFriendLayout.setVisibility(View.VISIBLE);
			
			switch (messages.get(position).getMessageType())
			{
			case ConstantValues.InstructionCode.MESSAGE_TYPE_AUDIO:
				holder.ivFriendAudio.setVisibility(View.VISIBLE);
				holder.ivFriendImage.setVisibility(View.GONE);
				holder.tvFriendText.setVisibility(View.GONE);
				break;
			case ConstantValues.InstructionCode.MESSAGE_TYPE_IMAGE:
				holder.ivFriendAudio.setVisibility(View.GONE);
				holder.ivFriendImage.setVisibility(View.VISIBLE);
				holder.tvFriendText.setVisibility(View.GONE);
				break;
			case ConstantValues.InstructionCode.MESSAGE_TYPE_TEXT:
				holder.ivFriendAudio.setVisibility(View.GONE);
				holder.ivFriendImage.setVisibility(View.GONE);
				holder.tvFriendText.setVisibility(View.VISIBLE);
				
				holder.tvFriendText.setText(((TextMessage)messages.get(position)).getText());
				holder.ivFriendPortrait.setImageBitmap(getFriendByID(messages.get(position).getFromUserID()).getPortraitBmp());
				break;
			default:
				break;
			}
		}
		
		return convertView;
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
	
	private static class ViewHolder 
	{
		TextView tvFriendText, tvMyText;
		ImageView ivFriendImage, ivMyImage;
		ImageView ivFriendAudio, ivMyAudio;
		ImageView ivFriendPortrait, ivMyPortrait;
		RelativeLayout rlFriendLayout, rlMyLayout;
		TextView tvTime;
	}
}
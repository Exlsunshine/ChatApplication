package com.yg.ui.dialog.implementation;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.AsyncTask;
import android.text.SpannableString;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.testmobiledatabase.R;
import com.lj.translator.Translator;
import com.lj.translator.TranslatorActivity;
import com.tp.ui.ImageZoomInActivity;
import com.tp.ui.MyselfPostActivity;
import com.yg.commons.CommonUtil;
import com.yg.commons.ConstantValues;
import com.yg.emoji.ParseEmojiMsgUtil;
import com.yg.image.preview.ImagePreviewManager;
import com.yg.message.AbstractMessage;
import com.yg.message.AudioMessage;
import com.yg.message.ImageMessage;
import com.yg.message.TextMessage;
import com.yg.user.FriendUser;

public class DialogAdapter extends BaseAdapter 
{
	private static final String DEBUG_TAG = "DialogAdapter______";
	private Context context;
	private ArrayList<AbstractMessage> messages;
	private ImagePreviewManager imagePreviewManager;
	private HashMap<Integer, GetDureationTast> animManager;
	
	public DialogAdapter(Context context, ArrayList<AbstractMessage> messages)
	{
		this.context = context;
		this.messages = messages;
		this.imagePreviewManager = new ImagePreviewManager(context);
		this.animManager = new HashMap<Integer, DialogAdapter.GetDureationTast>();
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
	public View getView(final int position, View convertView, ViewGroup parent)
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
			holder.tvFriendAudioLength = (TextView) convertView.findViewById(R.id.yg_dialoglist_item_friend_audio_length);
			holder.tvMyAudioLength = (TextView) convertView.findViewById(R.id.yg_dialoglist_item_my_audio_length);
			holder.ivFriendPortrait = (ImageView) convertView.findViewById(R.id.yg_dialoglist_item_friend_portrait);
			holder.ivMyPortrait = (ImageView) convertView.findViewById(R.id.yg_dialoglist_item_my_portrait);
			holder.rlFriendLayout = (RelativeLayout) convertView.findViewById(R.id.yg_dialoglist_item_friend_layout);
			holder.rlMyLayout = (RelativeLayout) convertView.findViewById(R.id.yg_dialoglist_item_my_layout);
			holder.tvTime = (TextView) convertView.findViewById(R.id.yg_dialoglist_item_time);
			convertView.setTag(holder);
		}
		else
			holder = (ViewHolder) convertView.getTag();

		setTime(holder, position);
		
		if (messages.get(position).getFromUserID() == ConstantValues.user.getID())
		{
			holder.rlMyLayout.setVisibility(View.VISIBLE);
			holder.rlFriendLayout.setVisibility(View.GONE);
			holder.ivMyPortrait.setImageBitmap(ConstantValues.user.getPortraitBmp());
			
			holder.ivMyPortrait.setOnClickListener(new OnClickListener()
			{
				@Override
				public void onClick(View v)
				{
					Intent intent = new Intent(context, MyselfPostActivity.class);
					context.startActivity(intent);
				}
			});
			
			switch (messages.get(position).getMessageType())
			{
			case ConstantValues.InstructionCode.MESSAGE_TYPE_AUDIO:
				holder.ivMyAudio.setVisibility(View.VISIBLE);
				holder.tvMyAudioLength.setVisibility(View.VISIBLE);
				holder.ivMyImage.setVisibility(View.GONE);
				holder.tvMyText.setVisibility(View.GONE);
				holder.tvMyAudioLength.setText(String.format("%d''", ((AudioMessage)messages.get(position)).getDuration(context)));
				
				holder.ivMyAudio.setOnClickListener(new OnClickListener()
				{
					@Override
					public void onClick(final View v) 
					{
						if (animManager.containsKey(position))
						{
							Log.i(DEBUG_TAG, "Aborting..." + position);
							animManager.get(position).abort();
						}
						else
						{
							GetDureationTast durationTask = new GetDureationTast(v, position, 
									R.anim.yg_dialog_activity_my_voice_playing_anim,
									R.drawable.yg_dialog_activity_my_voice_norm);
							cleanAllAudio();
							animManager.put(position, durationTask);
							durationTask.execute(v, position);
						}
					}
				});
				
				break;
			case ConstantValues.InstructionCode.MESSAGE_TYPE_IMAGE:
				holder.ivMyAudio.setVisibility(View.GONE);
				holder.tvMyAudioLength.setVisibility(View.GONE);
				holder.ivMyImage.setVisibility(View.VISIBLE);
				holder.tvMyText.setVisibility(View.GONE);
				
				holder.ivMyImage.setImageBitmap(((ImageMessage)messages.get(position)).getImage());
				holder.ivMyImage.setOnClickListener(new OnClickListener()
				{
					@Override
					public void onClick(View v)
					{
						Intent intent = new Intent(context, ImageZoomInActivity.class);
						intent.putExtra("Path", "file://" + ((ImageMessage)messages.get(position)).getContent());
						context.startActivity(intent);
					}
				});
				
				break;
			case ConstantValues.InstructionCode.MESSAGE_TYPE_TEXT:
				holder.ivMyAudio.setVisibility(View.GONE);
				holder.tvMyAudioLength.setVisibility(View.GONE);
				holder.ivMyImage.setVisibility(View.GONE);
				holder.tvMyText.setVisibility(View.VISIBLE);
				
				SpannableString spannableString = ParseEmojiMsgUtil.getExpressionString(context, ((TextMessage)messages.get(position)).getText());
				
				holder.tvMyText.setText(spannableString);
				final String text = eliminateEmoji(holder.tvMyText.getText().toString());
				holder.tvMyText.setOnClickListener(new OnClickListener()
				{
					@Override
					public void onClick(View arg0)
					{
						if (text.length() == 0)
							return;
						Intent intent = new Intent();
						intent.putExtra(TranslatorActivity.TEXT, text);
						intent.putExtra(TranslatorActivity.TOLANGUAGE, Translator.AUTO);
						intent.setClass(context, TranslatorActivity.class);
						context.startActivity(intent);
					}
				});
				break;
			default:
				break;
			}
		}
		else
		{
			holder.rlMyLayout.setVisibility(View.GONE);
			holder.rlFriendLayout.setVisibility(View.VISIBLE);
			holder.ivFriendPortrait.setImageBitmap(getFriendByID(messages.get(position).getFromUserID()).getPortraitBmp());
			
			imagePreviewManager.push(holder.ivFriendPortrait.getId(), holder.ivFriendPortrait);
			
			switch (messages.get(position).getMessageType())
			{
			case ConstantValues.InstructionCode.MESSAGE_TYPE_AUDIO:
				holder.ivFriendAudio.setVisibility(View.VISIBLE);
				holder.tvFriendAudioLength.setVisibility(View.VISIBLE);
				holder.ivFriendImage.setVisibility(View.GONE);
				holder.tvFriendText.setVisibility(View.GONE);
				
				holder.ivFriendAudio.setOnClickListener(new OnClickListener()
				{
					@Override
					public void onClick(final View v) 
					{
						if (animManager.containsKey(position))
						{
							Log.i(DEBUG_TAG, "Aborting..." + position);
							animManager.get(position).abort();
						}
						else
						{
							GetDureationTast durationTask = new GetDureationTast(v, position, 
									R.anim.yg_dialog_activity_friend_voice_playing_anim,
									R.drawable.yg_dialog_activity_friend_voice_norm);
							cleanAllAudio();
							animManager.put(position, durationTask);
							durationTask.execute(v, position);
						}
					}
				});
				
				holder.tvFriendAudioLength.setText(String.format("%d''", 
						((AudioMessage)messages.get(position)).getDuration(context)));
				break;
			case ConstantValues.InstructionCode.MESSAGE_TYPE_IMAGE:
				holder.ivFriendAudio.setVisibility(View.GONE);
				holder.tvFriendAudioLength.setVisibility(View.GONE);
				holder.ivFriendImage.setVisibility(View.VISIBLE);
				holder.tvFriendText.setVisibility(View.GONE);
				
				holder.ivFriendImage.setImageBitmap(((ImageMessage)messages.get(position)).getImage());
				
				holder.ivFriendImage.setOnClickListener(new OnClickListener()
				{
					@Override
					public void onClick(View v)
					{
						Intent intent = new Intent(context, ImageZoomInActivity.class);
						intent.putExtra("Path", "file://" + ((ImageMessage)messages.get(position)).getContent());
						context.startActivity(intent);
					}
				});
				break;
			case ConstantValues.InstructionCode.MESSAGE_TYPE_TEXT:
				holder.ivFriendAudio.setVisibility(View.GONE);
				holder.tvFriendAudioLength.setVisibility(View.GONE);
				holder.ivFriendImage.setVisibility(View.GONE);
				holder.tvFriendText.setVisibility(View.VISIBLE);
				
				SpannableString spannableString = ParseEmojiMsgUtil.getExpressionString(context, ((TextMessage)messages.get(position)).getText());
				
				holder.tvFriendText.setText(spannableString);
				final String text = eliminateEmoji(holder.tvFriendText.getText().toString());
				holder.tvFriendText.setOnClickListener(new OnClickListener()
				{
					@Override
					public void onClick(View arg0)
					{
						if (text.length() == 0)
							return;
						Intent intent = new Intent();
						intent.putExtra(TranslatorActivity.TEXT, text);
						intent.putExtra(TranslatorActivity.TOLANGUAGE, Translator.AUTO);
						intent.setClass(context, TranslatorActivity.class);
						context.startActivity(intent);
					}
				});
				break;
			default:
				break;
			}
		}
		
		return convertView;
	}
	
	private String eliminateEmoji(String text)
	{
		String reg = "\\[e\\]\\S{5}\\[/e\\]";
		return text.replaceAll(reg, "");
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
	
	/**
	 * 设置是否显示聊天的时间（5分钟之内的消息不显示）
	 * @param holder
	 * @param position
	 */
	private void setTime(ViewHolder holder, int position)
	{
		holder.tvTime.setVisibility(View.GONE);
		if (position >= 1)
		{
			if (!DateUtil.lessThan5Mins(messages.get(position).getDate(), messages.get(position - 1).getDate()))
			{
				holder.tvTime.setVisibility(View.VISIBLE);
				holder.tvTime.setText(DateUtil.getSuggestion(CommonUtil.now(), messages.get(position).getDate()));
			}
		}
		else
		{
			holder.tvTime.setVisibility(View.VISIBLE);
			holder.tvTime.setText(messages.get(position).getDate().substring(0, messages.get(position).getDate().length() - 9));
		}
	}
	
	private static class ViewHolder 
	{
		TextView tvFriendText, tvMyText;
		ImageView ivFriendImage, ivMyImage;
		ImageView ivFriendAudio, ivMyAudio;
		TextView tvFriendAudioLength, tvMyAudioLength;
		ImageView ivFriendPortrait, ivMyPortrait;
		RelativeLayout rlFriendLayout, rlMyLayout;
		TextView tvTime;
	}
	
	public void cleanAllAudio()
	{
		for (Integer key : animManager.keySet())
		{
			try
			{
				animManager.get(key).abort();
			}catch (Exception e)
			{
				e.getStackTrace();
			}
		}
	}
	
	private class GetDureationTast extends AsyncTask<Object, String, Void>
	{
		private ImageView imageView;
		private int position;
		private AnimationDrawable frameAnimation = null;
		private int animationID;
		private int defaultBackgroundID;
		
		public GetDureationTast(View view, int position, int animationID, int defaultBackgroundID)
		{
			this.position = position;
			this.imageView = (ImageView) view;
			this.animationID = animationID;
			this.defaultBackgroundID = defaultBackgroundID;
		}
		
		@Override
		protected void onPreExecute() 
		{
			super.onPreExecute();
			
			imageView.setBackgroundResource(animationID);
		}
		
		@Override
		protected Void doInBackground(Object... params) 
		{
			return null;
		}
		
		@Override
		protected void onPostExecute(Void result) 
		{
			super.onPostExecute(result);
			
			frameAnimation = (AnimationDrawable) imageView.getBackground();
			((AudioMessage)messages.get(position)).play(context, new OnPlayCompletedListener()
		    {
				@Override
				public void onPlayCompleted()
				{
					frameAnimation.stop();
				    imageView.setBackgroundResource(defaultBackgroundID);
				    animManager.remove(position);
				}
			});
			frameAnimation.start();
		}
		
		public void abort()
		{
			Log.i(DEBUG_TAG, "onAbort() " + position);
			((AudioMessage)messages.get(position)).stop();
			frameAnimation.stop();
		    imageView.setBackgroundResource(defaultBackgroundID);
		    animManager.remove(position);
		}
	}
}
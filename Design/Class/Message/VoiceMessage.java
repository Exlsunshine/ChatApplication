package com.Message;

import com.doc.User;

/**
 * 语音类型的消息，继承自{@link AbstractMessage}：<br>
 * 需要将{@link AbstractMessage}中的方法重写
 * @author EXLsunshine
 *
 */
class VoiceMessage extends AbstractMessage
{
	Audio voice;

	/**
	 * 播放此条消息中的语音
	 */
	public void play();
	
	/**
	 * 停止播放此条消息中的语音
	 */
	public void stop();

	@Override
	public User getFromUser() { return fromUser; }

	@Override
	public User getToUser() { return toUser; }

	@Override
	public Object getContent()
	{
		return null;
	}

	@Override
	public int getMsgType() { return ConstantValues.InstructionCode.MESSAGE_TYPE_AUDIO; }

	@Override
	public String getDate() { return date; }

	@Override
	public boolean hasBeenRead() { return isRead; }

	@Override
	public void setAsRead() { this.isRead = true; }
}
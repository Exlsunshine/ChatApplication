package com.Message;

import com.doc.User;

/**
 * �������͵���Ϣ���̳���{@link AbstractMessage}��<br>
 * ��Ҫ��{@link AbstractMessage}�еķ�����д
 * @author EXLsunshine
 *
 */
class VoiceMessage extends AbstractMessage
{
	Audio voice;

	/**
	 * ���Ŵ�����Ϣ�е�����
	 */
	public void play();
	
	/**
	 * ֹͣ���Ŵ�����Ϣ�е�����
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
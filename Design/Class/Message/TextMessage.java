package com.Message;

import com.doc.User;

/**
 * �������͵���Ϣ���̳���{@link AbstractMessage}��<br>
 * ��Ҫ��{@link AbstractMessage}�еķ�����д
 * @author EXLsunshine
 *
 */
class TextMessage extends AbstractMessage
{
	String text;

	/**
	 * ���ƴ�����Ϣ�е����ֵ�������
	 */
	public void copy();

	@Override
	public User getFromUser() { return fromUser; }

	@Override
	public User getToUser() { return toUser; }

	@Override
	public String getContent() { return text; }

	@Override
	public int getMsgType() { return ConstantValues.InstructionCode.MESSAGE_TYPE_TEXT; }

	@Override
	public String getDate() { return date; }

	@Override
	public boolean hasBeenRead() { return isRead; }

	@Override
	public void setAsRead() { this.isRead = true; }
}

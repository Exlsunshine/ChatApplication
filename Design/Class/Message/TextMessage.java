package com.Message;

import com.doc.User;

/**
 * 文字类型的消息，继承自{@link AbstractMessage}：<br>
 * 需要将{@link AbstractMessage}中的方法重写
 * @author EXLsunshine
 *
 */
class TextMessage extends AbstractMessage
{
	String text;

	/**
	 * 复制此条消息中的文字到剪贴板
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

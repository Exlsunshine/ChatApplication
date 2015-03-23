package com.message;

import com.commons.ConstantValues;

/**
 * 文字类型的消息，继承自{@link AbstractMessage}：<br>
 * 需要将{@link AbstractMessage}中的方法重写
 * @author EXLsunshine
 *
 */
public class TextMessage extends AbstractMessage
{
	private String text;

	public TextMessage(int msgID, int fromID, int toID, byte [] content, String date, boolean isRead)
	{
		this.fromUserID = fromID;
		this.toUserID = toID;
		this.date = date;
		this.type = ConstantValues.InstructionCode.MESSAGE_TYPE_TEXT;
		this.isRead = isRead;
		this.id = msgID;
		this.text = ConvertUtil.bytes2String(content);
	}
	
	public TextMessage(int fromID, int toID, byte [] content, String date, boolean isRead)
	{
		this.fromUserID = fromID;
		this.toUserID = toID;
		this.date = date;
		this.type = ConstantValues.InstructionCode.MESSAGE_TYPE_TEXT;
		this.isRead = isRead;
		this.id = -1;
		this.text = ConvertUtil.bytes2String(content);
	}
	
	public TextMessage(int fromID, int toID, String content, String date, boolean isRead)
	{
		this.fromUserID = fromID;
		this.toUserID = toID;
		this.date = date;
		this.type = ConstantValues.InstructionCode.MESSAGE_TYPE_TEXT;
		this.isRead = isRead;
		this.id = -1;
		this.text = content;
	}
	
	/**
	 * 复制此条消息中的文字到剪贴板
	 */
	public void copy() {
	}

	public String getText() { return text; }
	
	@Override
	public int getFromUserID() { return fromUserID; }

	@Override
	public int getToUserID() { return toUserID; }

	@Override
	public byte [] getContent() { return ConvertUtil.string2Bytes(text); }

	@Override
	public int getMessageType() { return ConstantValues.InstructionCode.MESSAGE_TYPE_TEXT; }

	@Override
	public String getDate() { return date; }

	@Override
	public boolean hasBeenRead() { return isRead; }

	@Override
	public void setAsRead() { this.isRead = true; }
	
	@Override
	public int getID() { return id; }
	
	@Override
	public void setID(int id) { this.id = id; }
}
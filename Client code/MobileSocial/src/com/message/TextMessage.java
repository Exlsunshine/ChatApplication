package com.message;

import com.commons.ConstantValues;

/**
 * �������͵���Ϣ���̳���{@link AbstractMessage}��<br>
 * ��Ҫ��{@link AbstractMessage}�еķ�����д
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
	 * ���ƴ�����Ϣ�е����ֵ�������
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
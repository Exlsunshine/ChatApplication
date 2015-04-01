package com.yg.message;

import com.yg.commons.ConstantValues;

import android.graphics.Bitmap;

/**
 * ͼƬ���͵���Ϣ���̳���{@link AbstractMessage}��<br>
 * ��Ҫ��{@link AbstractMessage}�еķ�����д
 * @author EXLsunshine
 *
 */
public class ImageMessage extends AbstractMessage
{
	Bitmap image;

	public ImageMessage(int msgID, int fromID, int toID, byte [] content, String date, boolean isRead)
	{
		this.fromUserID = fromID;
		this.toUserID = toID;
		this.date = date;
		this.type = ConstantValues.InstructionCode.MESSAGE_TYPE_IMAGE;
		this.isRead = isRead;
		this.id = msgID;
		this.image = ConvertUtil.bytes2Bitmap(content);
	}
	
	public ImageMessage(int fromID, int toID, byte [] content, String date, boolean isRead)
	{
		this.fromUserID = fromID;
		this.toUserID = toID;
		this.date = date;
		this.type = ConstantValues.InstructionCode.MESSAGE_TYPE_IMAGE;
		this.isRead = isRead;
		this.id = -1;
		this.image = ConvertUtil.bytes2Bitmap(content);
	}
	
	public ImageMessage(int fromID, int toID, Bitmap bitmap, String date, boolean isRead)
	{
		this.fromUserID = fromID;
		this.toUserID = toID;
		this.date = date;
		this.type = ConstantValues.InstructionCode.MESSAGE_TYPE_IMAGE;
		this.isRead = isRead;
		this.id = -1;
		this.image = bitmap;
	}
	
	/**
	 * ���������Ϣ�е�ͼƬ���ֻ�
	 */
	public void saveImage()
	{
	}
	
	public Bitmap getImage() { return image; }

	@Override
	public int getFromUserID() { return fromUserID; }

	@Override
	public int getToUserID() { return toUserID; }

	@Override
	public byte [] getContent() { return ConvertUtil.bitmap2Bytes(image); }

	@Override
	public int getMessageType() { return ConstantValues.InstructionCode.MESSAGE_TYPE_IMAGE; }

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
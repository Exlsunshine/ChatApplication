package com.Message;

import com.doc.User;

import android.graphics.Bitmap;

/**
 * ͼƬ���͵���Ϣ���̳���{@link AbstractMessage}��<br>
 * ��Ҫ��{@link AbstractMessage}�еķ�����д
 * @author EXLsunshine
 *
 */
public class ImageMessage extends AbstractMessage
{
	Bitmap picture;

	public ImageMessage(User from, User to, Bitmap picture, String date)
	{
		this.fromUser = from;
		this.toUser = to;
		this.picture = picture;
		this.date = date;
	}
	
	/**
	 * ���������Ϣ�е�ͼƬ���ֻ�
	 */
	public void savePicture();

	@Override
	public User getFromUser() { return fromUser; }

	@Override
	public User getToUser() { return toUser; }

	@Override
	public Bitmap getContent() { return picture; }

	@Override
	public int getMsgType() { return ConstantValues.InstructionCode.MESSAGE_TYPE_IMAGE; }

	@Override
	public String getDate() { return date; }

	@Override
	public boolean hasBeenRead() { return isRead; }

	@Override
	public void setAsRead() { this.isRead = true; }
}
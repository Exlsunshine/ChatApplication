package com.Message;

import com.doc.User;

import android.graphics.Bitmap;

/**
 * 图片类型的消息，继承自{@link AbstractMessage}：<br>
 * 需要将{@link AbstractMessage}中的方法重写
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
	 * 保存此条消息中的图片到手机
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
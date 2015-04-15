package com.yg.message;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import android.graphics.Bitmap;

import com.yg.commons.ConstantValues;

/**
 * 图片类型的消息，继承自{@link AbstractMessage}：<br>
 * 需要将{@link AbstractMessage}中的方法重写
 * @author EXLsunshine
 *
 */
public class ImageMessage extends AbstractMessage
{
	private Bitmap image;
	private String imagePath = null;
	
	public ImageMessage(int msgID, int fromID, int toID, String path, String date, boolean isRead)
	{
		this.fromUserID = fromID;
		this.toUserID = toID;
		this.date = date;
		this.type = ConstantValues.InstructionCode.MESSAGE_TYPE_IMAGE;
		this.isRead = isRead;
		this.id = msgID;
		this.imagePath = path;
		
		image = ConvertUtil.loadBitmap(imagePath);
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
		
		imagePath = FileNameGenerator.getFileName("/MobileSocial/image/", fromID, toID, "jpg");
		saveBmpFile();
	}
	
	private void saveBmpFile()
	{
		File imgFile = new File(imagePath);
		
		/*if (imgFile.exists() && imgFile.isFile())
			imgFile.delete();*/

		imgFile.getParentFile().mkdirs();
		
		if (!imgFile.exists())
		{
			try
			{
				//imgFile.mkdirs();
				imgFile.createNewFile();
				FileOutputStream out = new FileOutputStream(imagePath);
				image.compress(Bitmap.CompressFormat.PNG, 100, out);
				out.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * 保存此条消息中的图片到手机
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
	public String getContent() { return imagePath; }

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
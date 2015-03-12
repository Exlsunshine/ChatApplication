package com.Message;

/**
 * 图片类型的消息，继承自{@link AbstractMessage}：<br>
 * 需要将{@link AbstractMessage}中的方法重写
 * @author EXLsunshine
 *
 */
class ImageMessage extends AbstractMessage
{
	Bitmap picture;

	/**
	 * 保存此条消息中的图片到手机
	 */
	public void downloadPicture();
}

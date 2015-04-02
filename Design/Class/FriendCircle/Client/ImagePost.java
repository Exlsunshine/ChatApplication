package com.friendcircle;

import android.graphics.Bitmap;

class ImagePost extends AbstractPost
{
	/**
	 * 当前ImagePost的图像内容
	 * 类型暂定
	 */
	private Bitmap image;

	/**
	 * 新建ImagePost时调用<br>
	 * <b>用法参看AbstractPost<br>
	 * @see AbstractPost
	 * @param postUserID
	 * @param postDate
	 * @param image
	 * @param location
	 */
	public ImagePost(int postUserID, String postDate, Bitmap image, String location)
	{
		super(0, null, null, null);
	}

	/**
	 * 从服务器获取已存在的ImagePost时调用<br>
	 * <b>用法参看AbstractPost<br>
	 * @see AbstractPost
	 * @param postID
	 * @param postUserID
	 * @param postDate
	 * @param imgPath
	 * @param location
	 */
	public ImagePost(int postID, int postUserID, String postDate, String imgPath, String location)
	{
		super(0, 0, 0, null, null, null);
	}

	public Bitmap getImage()
	{
		return null;
	}

	/**
	 * 将当前ImagePost中的Image保存到指定路径
	 * @param savePath 保存路径
	 */
	public void downloadImage(String savePath)
	{
	}

	@Override
	public int getPostType()
	{
		return 0;
	}
}
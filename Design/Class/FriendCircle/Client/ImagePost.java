package com.friendcircle;

import android.graphics.Bitmap;

class ImagePost extends AbstractPost
{
	/**
	 * ��ǰImagePost��ͼ������
	 * �����ݶ�
	 */
	private Bitmap image;

	/**
	 * �½�ImagePostʱ����<br>
	 * <b>�÷��ο�AbstractPost<br>
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
	 * �ӷ�������ȡ�Ѵ��ڵ�ImagePostʱ����<br>
	 * <b>�÷��ο�AbstractPost<br>
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
	 * ����ǰImagePost�е�Image���浽ָ��·��
	 * @param savePath ����·��
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
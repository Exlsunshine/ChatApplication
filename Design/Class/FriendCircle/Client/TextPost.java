package com.friendcircle;

class TextPost extends AbstractPost
{
	/**
	 * ��ǰTextPost������
	 */
	private String text;

	/**
	 * �½�TextPostʱ����<br>
	 * <b>�÷��ο�AbstractPost<br>
	 * @see AbstractPost
	 * @param postUserID
	 * @param postDate
	 * @param text
	 * @param location
	 */
	public TextPost(int postUserID, String postDate, String text, String location)
	{
		super(0, null, null, null);
	}

	/**
	 * �ӷ�������ȡ�Ѵ��ڵ�TextPostʱ����<br>
	 * <b>�÷��ο�AbstractPost<br>
	 * @see AbstractPost
	 * @param postID
	 * @param postUserID
	 * @param postDate
	 * @param text
	 * @param location
	 */
	public TextPost(int postID, int postUserID, String postDate, String text, String location)
	{
		super(0, 0, 0, null, null, null);
	}

	public String getText()
	{
		return null;
	}

	@Override
	public int getPostType() 
	{
		return 0;
	}
}
package com.friendcircle;

class TextPost extends AbstractPost
{
	/**
	 * 当前TextPost的内容
	 */
	private String text;

	/**
	 * 新建TextPost时调用<br>
	 * <b>用法参看AbstractPost<br>
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
	 * 从服务器获取已存在的TextPost时调用<br>
	 * <b>用法参看AbstractPost<br>
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
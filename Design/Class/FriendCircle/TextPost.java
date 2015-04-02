class TextPost extends AbstractPost
{
	/**
	 * 当前TextPost的内容
	 */
	private String text;

	/**
	 * 新建TextPost时调用<br>
	 * @see AbstractPost
	 * @param postUserID
	 * @param postDate
	 * @param text
	 * @param location
	 */
	public TextPost(int postUserID, String postDate, String text, String location)
	{
		super(-1, null, null, null);
	}

	/**
	 * 从服务器获取已存在的TextPost时调用<br>
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

	/**
	 * 将当前TextPost的内容复制到剪贴板中将
	 */
	public void copyToClipboard()
	{
	}

	@Override
	public int getPostType() {
		// TODO Auto-generated method stub
		return 0;
	}
}
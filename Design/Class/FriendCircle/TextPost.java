class TextPost extends AbstractPost
{
	/**
	 * ��ǰTextPost������
	 */
	private String text;

	/**
	 * �½�TextPostʱ����<br>
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
	 * �ӷ�������ȡ�Ѵ��ڵ�TextPostʱ����<br>
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
	 * ����ǰTextPost�����ݸ��Ƶ��������н�
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
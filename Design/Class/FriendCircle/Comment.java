public class Comment
{
	/**
	 * ��ǰ���۵�ID
	 */
	private int commentID;
	
	/**
	 * ��ǰ����������Post ID
	 */
	private int postID;
	
	/**
	 * ��ǰ����������Post��User��ID
	 */
	private int postUserID;
	
	/**
	 * ����ǰ���۵�User��ID
	 */
	private int commentUserID;
	
	/**
	 * ��ǰ��������
	 */
	private String comment;
	
	/**
	 * ��ǰ���۷��������
	 */
	private String commentDate;

	/**
	 * �ӷ�������ȡ���е�Commentʱ���ô˹��캯��
	 * @param commentID
	 * @param postID
	 * @param postUserID
	 * @param commentUserID
	 * @param comment
	 * @param commentDate
	 */
	public Comment(int commentID, int postID, int postUserID, int commentUserID, String comment, String commentDate)
	{}

	/**
	 * �û�������Commentʱ�����ô˹��캯����<br>
	 * �ù��캯��Ӧ��ɵĹ��ܣ�<br>
	 * 1���ڱ��ع���һ��Comment<br>
	 * 2�����������Comment�Զ�������������<br>
	 * 3���������ɹ����ȡ��ID���õ����ص�Comment��
	 * @param postID
	 * @param postUserID
	 * @param commentUserID
	 * @param comment
	 * @param commentDate
	 */
	public Comment(int postID, int postUserID, int commentUserID, String comment, String commentDate)
	{}
	
	/**
	 * ����ǰ���۷�����������<br>
	 * �ú���ֻӦ����{@link #Comment(int, int, int, String, String)}�����е���
	 * @return ��ǰ���۷������������󷵻ص�commentID<br>
	 * ����0xffff��ʾ����ʧ��
	 */
	private int publish()
	{
		return 0;
	}
	
	/**
	 * ����{@link #publish()}���ص�commentID
	 */
	private void setCommentID()
	{}
	
	public int getCommentID()
	{
		return 0;}
	
	public int getPostID()
	{
		return 0;}
	
	public int getPostUserID()
	{
		return 0;}
	
	public int getCommentUserID()
	{
		return 0;}
	
	public String getComment() {
		return null;
	}
	
	public String getCommentDate()
	{
		return null;
	}
}
public class Comment
{
	/**
	 * 当前评论的ID
	 */
	private int commentID;
	
	/**
	 * 当前评论所属的Post ID
	 */
	private int postID;
	
	/**
	 * 当前评论所属的Post的User的ID
	 */
	private int postUserID;
	
	/**
	 * 发起当前评论的User的ID
	 */
	private int commentUserID;
	
	/**
	 * 当前评论内容
	 */
	private String comment;
	
	/**
	 * 当前评论发起的日期
	 */
	private String commentDate;

	/**
	 * 从服务器获取已有的Comment时调用此构造函数
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
	 * 用户发布新Comment时，调用此构造函数。<br>
	 * 该构造函数应完成的功能：<br>
	 * 1、在本地构造一条Comment<br>
	 * 2、将构造完的Comment自动发布到服务器<br>
	 * 3、将发布成功后获取的ID设置到本地的Comment中
	 * @param postID
	 * @param postUserID
	 * @param commentUserID
	 * @param comment
	 * @param commentDate
	 */
	public Comment(int postID, int postUserID, int commentUserID, String comment, String commentDate)
	{}
	
	/**
	 * 将当前评论发布至服务器<br>
	 * 该函数只应该在{@link #Comment(int, int, int, String, String)}函数中调用
	 * @return 当前评论发布到服务器后返回的commentID<br>
	 * 返回0xffff表示发布失败
	 */
	private int publish()
	{
		return 0;
	}
	
	/**
	 * 设置{@link #publish()}返回的commentID
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
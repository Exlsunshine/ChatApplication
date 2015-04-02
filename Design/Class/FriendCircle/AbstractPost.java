import java.util.ArrayList;

public abstract class AbstractPost
{
	/**
	 * 当前Post ID
	 */
	private int postID;
	/**
	 * 当前Post发布者的User ID
	 */
	private int postUserID;
	/**
	 * 当前Post被赞数量
	 */
	private int likedNumber;
	/**
	 * 当前Post的发布时间
	 */
	private String postDate;
	/**
	 * 当前Post的内容
	 */
	private byte [] content;
	/**
	 * 当前Post的评论集合
	 */
	private ArrayList<Comment> comments;

	/**
	 * 发布当前Post的地理位置(如：北京，上海...)<br>
	 * 默认为null
	 */
	private String location;
	
	/**
	 * 当前Post的类型
	 */
	private int postType;
	
	/**
	 * 用户发布新Post时调用此构造函数<br>
	 * 该构造函数应完成的功能：<br>
	 * 1、在本地构造一条Post<br>
	 * 2、将构造完的Post自动发布到服务器<br>
	 * 3、将发布成功后获取的ID设置到本地的Post中
	 * @param postUserID
	 * @param postDate
	 * @param content
	 * @param location
	 */
	AbstractPost(int postUserID, String postDate, byte [] content, String location)
	{
	}

	/**
	 * 从服务器获取已有的Post时调用此构造函数<br>
	 * 构造完成后，从服务器获取评论列表{@link #getCommentsFromServer(int)}
	 * @param postID
	 * @param postUserID
	 * @param likedNumber
	 * @param postDate
	 * @param content
	 * @param location
	 */
	AbstractPost(int postID, int postUserID, int likedNumber, String postDate, byte [] content, String location)
	{
	}
	
	public int getPostID() {
		return 0;
	}

	public int getPostUserID() {
		return 0;
	}

	public int getLikedNumber() {
		return 0;
	}

	public String getPostDate() {
		return null;
	}

	public byte [] getContent() {
		return null;
	}
	
	public String getLocation() {
		return null;
	}
	
	public abstract int getPostType();

	public ArrayList<Comment> getComments() {
		return null;
	}
	
	/**
	 * 用户点赞后调用此函数，被赞数+1
	 */
	public void increaseLikedNumber() {
	}

	/**
	 * 用户撤销点赞后调用此函数，被赞数-1
	 */
	public void decreaseLikedNumber() {
	}

	/**
	 * 向当前Post添加一条Comment
	 * @param comment 被添加的Comment
	 */
	public void appendComment(Comment comment) {
	}

	/**
	 * 根据给定commentID，从当前Post中删除该Comment
	 * @param commentID 被删除的comment对应的commentID 
	 */
	public void deleteCommentByID(int commentID) {
	}
	
	/**
	 * 从服务器获取当前Post的Comment集合
	 * @param postID 当前Post的postID
	 */
	private void getCommentsFromServer(int postID) {
	}
}

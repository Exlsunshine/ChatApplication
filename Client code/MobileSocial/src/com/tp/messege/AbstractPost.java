package com.tp.messege;

import java.util.ArrayList;
import java.util.HashMap;

import com.yg.user.PackString;
import com.yg.user.WebServiceAPI;



import android.util.Log;


public abstract class AbstractPost
{
	/**
	 * 当前Post的ID
	 */
	protected int postID;
	
	/**
	 * 当前Post发布者的UserID
	 */
	protected int postUserID;
	
	/**
	 * 当前Post被赞数量
	 */
	protected int likedNumber;
	
	/**
	 * 当前Post的发布时间
	 */
	protected String postDate;
	
	/**
	 * 当前Post的内容
	 */
	protected Object content;
	
	/**
	 * 当前Post的评论集合
	 */
	protected ArrayList<Comment> comments = new ArrayList<Comment>();

	/**
	 * 发布当前Post的地理位置(如：北京，上海...)<br>
	 * <b>默认为null<br>
	 */
	protected String location;
	
	/**
	 * 当前Post的类型<br>
	 * 1表示文字类型Post<br>
	 * 2表示图片类型Post
	 */
	protected int postType;
	
	/**
	 * 当前用户性别
	 * male 男
	 * female 女
	 */
	protected String sex;
	/**
	 * 调用的webservice的package名和class名
	 */
	final String packageName = "FriendCircleServer.tp.com";
	final String className = "FriendCircleHandler";
	
	
	/**
	 * 用户发布新Post时调用此构造函数<br>
	 * 该构造函数应完成的功能：<br>
	 * 1、在本地构造一条Post<br>
	 * 2、将构造完的Post自动发布到服务器<br>
	 * 3、将发布成功后获取的ID设置到本地的Post中
	 * @param postUserID Post所属的User的ID
	 * @param postDate Post发布的日期
	 * @param content Post的内容
	 * @param location 发布Post时所在的地点
	 */
	public AbstractPost(int postUserID, String postDate, Object content, String location, String sex)
	{
		this.postUserID = postUserID;
		this.postDate = postDate;
		this.content = content;
		this.location = location;
		this.sex = sex;
	}
	
	protected abstract int publish();
	
	protected abstract void setPostID(int id);

	/**
	 * 从服务器获取已有的Post时调用此构造函数<br>
	 * 构造完成后，从服务器获取评论列表{@link #getCommentsFromServer(int)}
	 * @param postID Post的ID
	 * @param postUserID Post所属的User的ID
	 * @param likedNumber Post被赞数
	 * @param postDate Post发布的日期
	 * @param content Post的内容
	 * @param location 发布Post时所在的地点
	 */
	public AbstractPost(int postID, int postUserID, int likedNumber, String postDate, Object content, String location, String sex)
	{
		this.postID = postID;
		this.postUserID = postUserID;
		this.likedNumber = likedNumber;
		this.postDate = postDate;
		this.content = content;
		this.location = location;
		this.sex = sex;
		new Thread()
	        {
	        	public void run() 
	        	{
	        		try
	        		{
	        			getCommentsFromServer();
	        			Log.d("AbstractPost", "服务器构造函数");
	        		}
	        		catch (Exception e)
	                {
	        			e.printStackTrace();
	                }
	        	};
	        }.start();
	}
	
	public AbstractPost()
	{
		
	}
	
	/**********************								***********************/
	/**********************			以下是本地操作			***********************/
	/**********************								***********************/
	public int getPostID()
	{
		return this.postID;
	}

	public int getPostUserID()
	{
		return this.postUserID;
	}

	public int getLikedNumber()
	{
		return this.likedNumber;
	}

	public String getPostDate()
	{
		return this.postDate;
	}

	public Object getContent() 
	{
		return this.content;
	}
	
	public String getLocation()
	{
		return this.location;
	}
	
	public String getSex()
	{
		return this.sex;
	}

	/**
	 * 获取当前Post的类型
	 * @see postType
	 */
	public abstract int getPostType();
	
	public ArrayList<Comment> getComments()
	{
		return this.comments;
	}
	
	public int getcommentsize()
	{
		return comments.size();
	}
	/**
	 * 用户点赞或取消点赞后调用此函数，被赞数+1或-1
         * @param userID 用户ID 
	 * @return 更新后的点赞数
	 */
	public int modifyLikedNumber(int userID) 
	{
		int result = modifyLikedNumberAtServer(postID, userID);
		this.likedNumber += result;
		return result;
	}
	
	/**
	 * 向当前Post添加一条Comment
	 * @param comment 被添加的Comment
	 */
	public void appendComment(Comment comment)
	{
		comments.add(comment);
	}
	
	/**
	 * 根据给定commentID，从当前Post中删除该Comment
	 * @param commentID 被删除的comment对应的commentID 
	 */
	public void deleteCommentByID(int commentID) 
	{
		 for (int i = 0; i < comments.size(); i++) 
		 {
			  if (comments.get(i).getCommentID() == commentID)
			  {
				  comments.remove(i);
				  break;
			  }
		 }
	}

	/**********************								***********************/
	/**********************			以上是本地操作			***********************/
	/**********************								***********************/
	
	
	/**********************								***********************/
	/**********************		以下是与服务器交互的操作		***********************/
	/**********************								***********************/
	/**
	 * 根据给定的postID在服务器端增加1次被赞数
	 * @param postID 给定的postID(次参数应为this.postID)
         * @param userID 点赞用户的ID
	 * @return -1表示取消点赞成功<br>
	 * 1表示点赞成功
	 */
	protected int modifyLikedNumberAtServer(int postID, int userID)
	{
		WebServiceAPI wsApi;
		Object []value = {this.postID, userID}; 
		String []para = {"postID", "userID"};
		wsApi = new WebServiceAPI(packageName, className);
		Object s = wsApi.callFuntion("modifyLikedNumber", para ,value);
		int result = Integer.parseInt(s.toString());
		return result;
	}
	
	/**
	 * 在服务器端,删除指定的postID中的一条Comment
	 * @param postID 指定的postID(此参数应为this.postID)
	 * @param commentID 要删除的Comment对应的commentID
	 */
	protected void deleteCommentByIDAtServer(int postID, int commentID)
	{
		WebServiceAPI wsApi;
		String packageName = "FriendCircleServer.tp.com";
		String className = "FriendCircleHandler";
		Object []value = {postID,commentID}; 
		String []para = {"postID","commentID"};
		wsApi = new WebServiceAPI(packageName, className);
		Object s = wsApi.callFuntion("deleteComment", para ,value);
	}
	
	/**
	 * 从服务器获取当前Post的Comment集合
	 * @param postID 当前Post的postID(此参数应为this.postID)
	 */
	protected void getCommentsFromServer(int postID) 
	{
		WebServiceAPI wsApi;
		String packageName = "FriendCircleServer.tp.com";
		String className = "FriendCircleHandler";
		Object []value = {postID}; 
		String []para = {"postID"};
		wsApi = new WebServiceAPI(packageName, className);
		Object s = wsApi.callFuntion("getComments", para ,value);
		Log.d("getCommentsFromServerdebug_______", postID + "");
		if (s == null)
			return;
		PackString jsonString = new PackString(s.toString());
		ArrayList<HashMap<String, Object>> commentResult = jsonString.jsonString2Arrylist("commentsFromServer");
		if (commentResult == null)
			return;
		for (int i = 0; i < commentResult.size(); i++)
		{
			int commentID = Integer.parseInt(commentResult.get(i).get("id").toString());
			int postUserID = Integer.parseInt(commentResult.get(i).get("post_user_id").toString());
			int commentUserID = Integer.parseInt(commentResult.get(i).get("comment_user_id").toString());
			String comment = commentResult.get(i).get("comment").toString();
			String commentDate = commentResult.get(i).get("comment_date").toString();
			String sex = commentResult.get(i).get("sex").toString();
			Comment comm = new Comment(commentID, postID, postUserID, commentUserID, comment, commentDate, sex);
			appendComment(comm);
		}
	}
	/**
	 * 从服务器获取当前用户Post的Comment集合
	 * @param postID 当前Post的postID(此参数应为this.postID)
	 */
	protected void getCommentsFromServer() 
	{
		WebServiceAPI wsApi;
		String packageName = "FriendCircleServer.tp.com";
		String className = "FriendCircleHandler";
		Object []value = {postID}; 
		String []para = {"postID"};
		wsApi = new WebServiceAPI(packageName, className);
		Object s = wsApi.callFuntion("getComments", para ,value);
		Log.d("getCommentsFromServerdebug_______", postID + "");
		if (s == null)
			return;
		PackString jsonString = new PackString(s.toString());
		ArrayList<HashMap<String, Object>> commentResult = jsonString.jsonString2Arrylist("commentsFromServer");
		for (int i = 0; i < commentResult.size(); i++)
		{
			int commentID = Integer.parseInt(commentResult.get(i).get("id").toString());
			int postUserID = Integer.parseInt(commentResult.get(i).get("post_user_id").toString());
			int commentUserID = Integer.parseInt(commentResult.get(i).get("comment_user_id").toString());
			String comment = commentResult.get(i).get("comment").toString();
			String commentDate = commentResult.get(i).get("comment_date").toString();
			String sex = commentResult.get(i).get("sex").toString();
			Comment comm = new Comment(commentID, postID, postUserID, commentUserID, comment, commentDate, sex);
			appendComment(comm);
			Log.d("getCommentsFromServer", comments.size() + "");
		}
	}
	/**********************								***********************/
	/**********************		以上是与服务器交互的操作		***********************/
	/**********************								***********************/
}
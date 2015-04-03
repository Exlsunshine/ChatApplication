package com.friendcircle;

import java.util.ArrayList;

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
	protected byte [] content;
	
	/**
	 * 当前Post的评论集合
	 */
	protected ArrayList<Comment> comments;

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
	public AbstractPost(int postUserID, String postDate, byte [] content, String location)
	{
	}

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
	public AbstractPost(int postID, int postUserID, int likedNumber, String postDate, byte [] content, String location)
	{
	}
	
	/**********************								***********************/
	/**********************			以下是本地操作			***********************/
	/**********************								***********************/
	public int getPostID()
	{
		return 0;
	}

	public int getPostUserID()
	{
		return 0;
	}

	public int getLikedNumber()
	{
		return 0;
	}

	public String getPostDate()
	{
		return null;
	}

	public byte [] getContent() 
	{
		return null;
	}
	
	public String getLocation()
	{
		return null;
	}

	/**
	 * 获取当前Post的类型
	 * @see postType
	 */
	public abstract int getPostType();
	
	public ArrayList<Comment> getComments()
	{
		return null;
	}
	
	/**
	 * 用户点赞后调用此函数，被赞数+1
	 */
	public void increaseLikedNumber() 
	{
	}
	
	/**
	 * 用户撤销点赞后调用此函数，被赞数-1
	 */
	public void decreaseLikedNumber()
	{
	}
	
	/**
	 * 向当前Post添加一条Comment
	 * @param comment 被添加的Comment
	 */
	public void appendComment(Comment comment)
	{
	}
	
	/**
	 * 根据给定commentID，从当前Post中删除该Comment
	 * @param commentID 被删除的comment对应的commentID 
	 */
	public void deleteCommentByID(int commentID) 
	{
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
	 */
	protected void increaseLikedNumberAtServer(int postID)
	{
	}
	
	/**
	 * 根据给定的postID在服务器端减少1次被赞数
	 * @param postID 给定的postID(此参数应为this.postID)
	 */
	protected void decreaseLikedNumberAtServer(int postID)
	{
	}
	
	/**
	 * 在服务器端向指定的postID追加一条Comment
	 * @param postID 指定的postID(此参数应为this.postID)
	 * @param comment 要追加的Comment
	 */
	protected void appendCommentAtServer(int postID, Comment comment)
	{
	}
	
	/**
	 * 在服务器端,删除指定的postID中的一条Comment
	 * @param postID 指定的postID(此参数应为this.postID)
	 * @param commentID 要删除的Comment对应的commentID
	 */
	protected void deleteCommentByIDAtServer(int postID, int commentID)
	{
	}
	
	/**
	 * 从服务器获取当前Post的Comment集合
	 * @param postID 当前Post的postID(此参数应为this.postID)
	 */
	protected void getCommentsFromServer(int postID) 
	{
	}
	/**********************								***********************/
	/**********************		以上是与服务器交互的操作		***********************/
	/**********************								***********************/
}
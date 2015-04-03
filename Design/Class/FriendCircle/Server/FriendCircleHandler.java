package com.friendcircle;

import com.example.test.SQLServerEnd;

public class FriendCircleHandler
{
	private SQLServerEnd postDataTB = null;
	private SQLServerEnd commentDataTB = null;
	
	private void initPostDataTB()
	{
	}
	
	private void initCommentDataTB()
	{
	}
	
	/************************								***************************/
	/************************			以下是要发布的接口		***************************/
	/************************								***************************/
	/**
	 * 将客户端传来的Comment存入服务器的数据库中
	 * @param commentJson 从客户端传来的JSON格式的Comment信息
	 * @return 将成功存入服务器的Comment对应的commentID返回给客户端<br>
	 * null表示出现异常
	 */
	public String publishComment(String commentJson)
	{
		return null;
	}
	
	/**
	 * 将客户端传来的Post存入服务器的数据库中
	 * @param postJson 从客户端传来的JSON格式的Post信息
	 * @return 将成功存入服务器的Post对应的postID返回给客户端
	 * null表示出现异常
	 */
	public String publishPost(String postJson)
	{
		return null;
	}
	
	/**
	 * 根据给定的postID获取其对应的所有Comment
	 * @param postID 指定的postID
	 * @return JSON格式的Comments信息<br>
	 * null表示出现异常
	 */
	public String getCommentsByPostID(int postID)
	{
		return null;
	}
	
	/**
	 * 根据给定的userID，从服务器获取其好友们10条最新的Post<br>
	 * <b>注：是从指定的postID下一条Post开始的最近10条，即返回的postID均应该大于当前指定的postID</b>
	 * 如果postID = -1 则意味着不做筛选，仅选择好友们最新的10条Post
	 * @param userID
	 * @param postID 
	 * @return JSON格式的Post信息<br>
	 * null表示出现异常
	 */
	public String getPostsByUserID(int userID, int postID)
	{
		return null;
	}
	
	/**
	 * 给指定的Post赞的数量+1
	 * @param postID 指定的Post对应的postID
	 * @return 操作结果的状态<br>
	 * 0表示成功<br>
	 * 1表示失败
	 */
	public int increaseLikeByPostID(int postID)
	{
		return 0;
	}
	
	/**
	 * 给指定的Post赞的数量-1
	 * @param postID 指定的Post对应的postID
	 * @return 操作结果的状态<br>
	 * 0表示成功<br>
	 * 1表示失败
	 */
	public int decreaseLikeByPostID(int postID)
	{
		return 0;
	}
	
	/**
	 * 将一条Comment插入到一条Post中
	 * @param postID 要被插入的Post对应的postID
	 * @param comment 要追加的Comment
	 * @return 操作结果的状态<br>
	 * 0表示成功<br>
	 * 1表示失败
	 */
	public int appendComment(int postID, Comment comment)
	{
		return 0;
	}
	
	/**
	 * 删除Post中指定的一条Comment
	 * @param postID 要删除的Comment所在的Post的postID
	 * @param commentID 要删除的Comment的commentID
	 * @return 操作结果的状态<br>
	 * 0表示成功<br>
	 * 1表示失败
	 */
	public int deleteComment(int postID, int commentID)
	{
		return 0;
	}
	/************************									**************************/
	/************************			以上是要发布的接口			**************************/
	/************************									**************************/
}
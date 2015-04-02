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
	
	/********		要发布的接口		***********/
	/**
	 * 将客户端传来的Comment存入服务器的数据库中
	 * @param commentJson 从客户端传来的JSON格式的Comment信息
	 * @return 将commentJson参数原封不动，再次返回给客户端<br>
	 * null表示服务器保存时出现异常
	 */
	public String publishComment(String commentJson)
	{
		return null;
	}
	
	/**
	 * 根据给定的postID获取其对应的所有Comment
	 * @param postID 指定的postID
	 * @return JSON格式的Comments信息
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
	 * @return JSON格式的Post信息
	 */
	public String getPostsByUserID(int userID, int postID)
	{
		return null;
	}
}
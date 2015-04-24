package com.tp.messege;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONException;

import com.yg.user.PackString;
import com.yg.user.WebServiceAPI;



public class Comment
{
	/**
	 * 当前评论的ID
	 */
	private int commentID;
	
	/**
	 * 当前评论所属的Post的ID
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
	
	private String sex;
	
	public Comment()
	{
		
	}
	
	/**
	 * 从服务器获取已有的Comment时调用此构造函数
	 * @param commentID 评论ID
	 * @param postID 评论所属的Post的ID
	 * @param postUserID 评论所属的Post的User的ID
	 * @param commentUserID 发起评论的User的ID
	 * @param comment 评论内容
	 * @param commentDate 评论发起的日期
	 */
	public Comment(int commentID, int postID, int postUserID, int commentUserID, String comment, String commentDate, String sex)
	{
		this.commentID = commentID;
		this.postID = postID;
		this.postUserID = postUserID;
		this.commentUserID = commentUserID;
		this.comment = comment;
		this.commentDate = commentDate;
		this.sex = sex;
	}

	/**
	 * 用户发布新Comment时，调用此构造函数<br>
	 * 该构造函数应完成的功能：<br>
	 * 1、在本地构造一条Comment<br>
	 * 2、将构造完的Comment自动发布到服务器<br>
	 * 3、将发布成功后获取的ID设置到本地的Comment中
	 * @param postID 评论所属的Post的ID
	 * @param postUserID 评论所属的Post的User的ID
	 * @param commentUserID 发起评论的User的ID
	 * @param comment 评论内容
	 * @param commentDate 评论发起的日期
	 */
	public Comment(int postID, int postUserID, int commentUserID, String comment, String commentDate, String sex)
	{
		this.postID = postID;
		this.postUserID = postUserID;
		this.commentUserID = commentUserID;
		this.comment = comment;
		this.commentDate = commentDate;
		this.sex = sex;
		publish();
	}
	
	/**
	 * 将当前评论发布至服务器<br>
	 * 该函数只应该在{@link #Comment(int, int, int, String, String)}函数中调用
	 * @return 当前评论发布到服务器后返回的commentID<br>
	 * 返回0xffff表示发布失败
	 */
	private int publish()
	{
		int ret = 0xffff;
		try 
		{
			WebServiceAPI wsApi;
			String packageName = "FriendCircleServer.tp.com";
			String className = "FriendCircleHandler";
			
			ArrayList<HashMap<String, Object>> items = new ArrayList<HashMap<String, Object>>();
			HashMap<String, Object> item = new HashMap<String, Object>();
			item.put("post_user_id", Integer.toString(postUserID));
			item.put("post_id", Integer.toString(postID));
			item.put("comment_user_id", Integer.toString(commentUserID));
			item.put("comment", comment);
			item.put("comment_date", commentDate);
			item.put("sex", sex);
			items.add(item);
			
			String ps = PackString.arrylist2JsonString("comments", items);
			Object []value = {ps}; 
			String []para = {"commentJson"}; 
			wsApi = new WebServiceAPI(packageName, className);
			Object s = wsApi.callFuntion("publishComment", para ,value);
			System.out.print(s.toString());
			if (Integer.valueOf(s.toString()) == -1)
				ret = 0xffff;
			else
			{
				setCommentID(Integer.valueOf(s.toString()));
				ret = Integer.valueOf(s.toString());
			}
		} 
		catch (JSONException e) 
		{
			e.printStackTrace();
		}
		return ret;
	}
	
	/**
	 * 设置{@link #publish()}返回的commentID
	 */
	private void setCommentID(int CommentIDRtn)
	{
		System.out.print(CommentIDRtn + "_____________________");
		commentID = CommentIDRtn;
	}
	
	public int getCommentID()
	{
		return commentID;
	}
	
	public int getPostID()
	{
		return postID;
	}
	
	public int getPostUserID()
	{
		return postUserID;
	}
	
	public int getCommentUserID()
	{
		return commentUserID;
	}
	
	public String getComment() 
	{
		return comment;
	}
	
	public String getCommentDate()
	{
		return commentDate;
	}
	
	public String getSex()
	{
		return sex;
	}
	public static void main(String []args)
	{
		Comment comm = new Comment(22,22,22,"123","1234-1-1", "female");
	}
}
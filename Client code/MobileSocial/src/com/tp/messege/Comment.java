package com.tp.messege;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONException;

import com.yg.user.PackString;
import com.yg.user.WebServiceAPI;



public class Comment
{
	/**
	 * ��ǰ���۵�ID
	 */
	private int commentID;
	
	/**
	 * ��ǰ����������Post��ID
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
	
	private String sex;
	
	public Comment()
	{
		
	}
	
	/**
	 * �ӷ�������ȡ���е�Commentʱ���ô˹��캯��
	 * @param commentID ����ID
	 * @param postID ����������Post��ID
	 * @param postUserID ����������Post��User��ID
	 * @param commentUserID �������۵�User��ID
	 * @param comment ��������
	 * @param commentDate ���۷��������
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
	 * �û�������Commentʱ�����ô˹��캯��<br>
	 * �ù��캯��Ӧ��ɵĹ��ܣ�<br>
	 * 1���ڱ��ع���һ��Comment<br>
	 * 2�����������Comment�Զ�������������<br>
	 * 3���������ɹ����ȡ��ID���õ����ص�Comment��
	 * @param postID ����������Post��ID
	 * @param postUserID ����������Post��User��ID
	 * @param commentUserID �������۵�User��ID
	 * @param comment ��������
	 * @param commentDate ���۷��������
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
	 * ����ǰ���۷�����������<br>
	 * �ú���ֻӦ����{@link #Comment(int, int, int, String, String)}�����е���
	 * @return ��ǰ���۷������������󷵻ص�commentID<br>
	 * ����0xffff��ʾ����ʧ��
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
	 * ����{@link #publish()}���ص�commentID
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
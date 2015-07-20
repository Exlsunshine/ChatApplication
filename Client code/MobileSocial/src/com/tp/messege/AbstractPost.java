package com.tp.messege;

import java.util.ArrayList;
import java.util.HashMap;

import com.yg.user.PackString;
import com.yg.user.WebServiceAPI;



import android.util.Log;


public abstract class AbstractPost
{
	/**
	 * ��ǰPost��ID
	 */
	protected int postID;
	
	/**
	 * ��ǰPost�����ߵ�UserID
	 */
	protected int postUserID;
	
	/**
	 * ��ǰPost��������
	 */
	protected int likedNumber;
	
	/**
	 * ��ǰPost�ķ���ʱ��
	 */
	protected String postDate;
	
	/**
	 * ��ǰPost������
	 */
	protected Object content;
	
	/**
	 * ��ǰPost�����ۼ���
	 */
	protected ArrayList<Comment> comments = new ArrayList<Comment>();

	/**
	 * ������ǰPost�ĵ���λ��(�磺�������Ϻ�...)<br>
	 * <b>Ĭ��Ϊnull<br>
	 */
	protected String location;
	
	/**
	 * ��ǰPost������<br>
	 * 1��ʾ��������Post<br>
	 * 2��ʾͼƬ����Post
	 */
	protected int postType;
	
	/**
	 * ��ǰ�û��Ա�
	 * male ��
	 * female Ů
	 */
	protected String sex;
	/**
	 * ���õ�webservice��package����class��
	 */
	final String packageName = "FriendCircleServer.tp.com";
	final String className = "FriendCircleHandler";
	
	
	/**
	 * �û�������Postʱ���ô˹��캯��<br>
	 * �ù��캯��Ӧ��ɵĹ��ܣ�<br>
	 * 1���ڱ��ع���һ��Post<br>
	 * 2�����������Post�Զ�������������<br>
	 * 3���������ɹ����ȡ��ID���õ����ص�Post��
	 * @param postUserID Post������User��ID
	 * @param postDate Post����������
	 * @param content Post������
	 * @param location ����Postʱ���ڵĵص�
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
	 * �ӷ�������ȡ���е�Postʱ���ô˹��캯��<br>
	 * ������ɺ󣬴ӷ�������ȡ�����б�{@link #getCommentsFromServer(int)}
	 * @param postID Post��ID
	 * @param postUserID Post������User��ID
	 * @param likedNumber Post������
	 * @param postDate Post����������
	 * @param content Post������
	 * @param location ����Postʱ���ڵĵص�
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
	        			Log.d("AbstractPost", "���������캯��");
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
	/**********************			�����Ǳ��ز���			***********************/
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
	 * ��ȡ��ǰPost������
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
	 * �û����޻�ȡ�����޺���ô˺�����������+1��-1
         * @param userID �û�ID 
	 * @return ���º�ĵ�����
	 */
	public int modifyLikedNumber(int userID) 
	{
		int result = modifyLikedNumberAtServer(postID, userID);
		this.likedNumber += result;
		return result;
	}
	
	/**
	 * ��ǰPost���һ��Comment
	 * @param comment ����ӵ�Comment
	 */
	public void appendComment(Comment comment)
	{
		comments.add(comment);
	}
	
	/**
	 * ���ݸ���commentID���ӵ�ǰPost��ɾ����Comment
	 * @param commentID ��ɾ����comment��Ӧ��commentID 
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
	/**********************			�����Ǳ��ز���			***********************/
	/**********************								***********************/
	
	
	/**********************								***********************/
	/**********************		������������������Ĳ���		***********************/
	/**********************								***********************/
	/**
	 * ���ݸ�����postID�ڷ�����������1�α�����
	 * @param postID ������postID(�β���ӦΪthis.postID)
         * @param userID �����û���ID
	 * @return -1��ʾȡ�����޳ɹ�<br>
	 * 1��ʾ���޳ɹ�
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
	 * �ڷ�������,ɾ��ָ����postID�е�һ��Comment
	 * @param postID ָ����postID(�˲���ӦΪthis.postID)
	 * @param commentID Ҫɾ����Comment��Ӧ��commentID
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
	 * �ӷ�������ȡ��ǰPost��Comment����
	 * @param postID ��ǰPost��postID(�˲���ӦΪthis.postID)
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
	 * �ӷ�������ȡ��ǰ�û�Post��Comment����
	 * @param postID ��ǰPost��postID(�˲���ӦΪthis.postID)
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
	/**********************		������������������Ĳ���		***********************/
	/**********************								***********************/
}
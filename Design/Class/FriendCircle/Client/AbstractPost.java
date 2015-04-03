package com.friendcircle;

import java.util.ArrayList;

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
	protected byte [] content;
	
	/**
	 * ��ǰPost�����ۼ���
	 */
	protected ArrayList<Comment> comments;

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
	public AbstractPost(int postUserID, String postDate, byte [] content, String location)
	{
	}

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
	public AbstractPost(int postID, int postUserID, int likedNumber, String postDate, byte [] content, String location)
	{
	}
	
	/**********************								***********************/
	/**********************			�����Ǳ��ز���			***********************/
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
	 * ��ȡ��ǰPost������
	 * @see postType
	 */
	public abstract int getPostType();
	
	public ArrayList<Comment> getComments()
	{
		return null;
	}
	
	/**
	 * �û����޺���ô˺�����������+1
	 */
	public void increaseLikedNumber() 
	{
	}
	
	/**
	 * �û��������޺���ô˺�����������-1
	 */
	public void decreaseLikedNumber()
	{
	}
	
	/**
	 * ��ǰPost���һ��Comment
	 * @param comment ����ӵ�Comment
	 */
	public void appendComment(Comment comment)
	{
	}
	
	/**
	 * ���ݸ���commentID���ӵ�ǰPost��ɾ����Comment
	 * @param commentID ��ɾ����comment��Ӧ��commentID 
	 */
	public void deleteCommentByID(int commentID) 
	{
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
	 */
	protected void increaseLikedNumberAtServer(int postID)
	{
	}
	
	/**
	 * ���ݸ�����postID�ڷ������˼���1�α�����
	 * @param postID ������postID(�˲���ӦΪthis.postID)
	 */
	protected void decreaseLikedNumberAtServer(int postID)
	{
	}
	
	/**
	 * �ڷ���������ָ����postID׷��һ��Comment
	 * @param postID ָ����postID(�˲���ӦΪthis.postID)
	 * @param comment Ҫ׷�ӵ�Comment
	 */
	protected void appendCommentAtServer(int postID, Comment comment)
	{
	}
	
	/**
	 * �ڷ�������,ɾ��ָ����postID�е�һ��Comment
	 * @param postID ָ����postID(�˲���ӦΪthis.postID)
	 * @param commentID Ҫɾ����Comment��Ӧ��commentID
	 */
	protected void deleteCommentByIDAtServer(int postID, int commentID)
	{
	}
	
	/**
	 * �ӷ�������ȡ��ǰPost��Comment����
	 * @param postID ��ǰPost��postID(�˲���ӦΪthis.postID)
	 */
	protected void getCommentsFromServer(int postID) 
	{
	}
	/**********************								***********************/
	/**********************		������������������Ĳ���		***********************/
	/**********************								***********************/
}
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
	/************************			������Ҫ�����Ľӿ�		***************************/
	/************************								***************************/
	/**
	 * ���ͻ��˴�����Comment��������������ݿ���
	 * @param commentJson �ӿͻ��˴�����JSON��ʽ��Comment��Ϣ
	 * @return ���ɹ������������Comment��Ӧ��commentID���ظ��ͻ���<br>
	 * null��ʾ�����쳣
	 */
	public String publishComment(String commentJson)
	{
		return null;
	}
	
	/**
	 * ���ͻ��˴�����Post��������������ݿ���
	 * @param postJson �ӿͻ��˴�����JSON��ʽ��Post��Ϣ
	 * @return ���ɹ������������Post��Ӧ��postID���ظ��ͻ���
	 * null��ʾ�����쳣
	 */
	public String publishPost(String postJson)
	{
		return null;
	}
	
	/**
	 * ���ݸ�����postID��ȡ���Ӧ������Comment
	 * @param postID ָ����postID
	 * @return JSON��ʽ��Comments��Ϣ<br>
	 * null��ʾ�����쳣
	 */
	public String getCommentsByPostID(int postID)
	{
		return null;
	}
	
	/**
	 * ���ݸ�����userID���ӷ�������ȡ�������10�����µ�Post<br>
	 * <b>ע���Ǵ�ָ����postID��һ��Post��ʼ�����10���������ص�postID��Ӧ�ô��ڵ�ǰָ����postID</b>
	 * ���postID = -1 ����ζ�Ų���ɸѡ����ѡ����������µ�10��Post
	 * @param userID
	 * @param postID 
	 * @return JSON��ʽ��Post��Ϣ<br>
	 * null��ʾ�����쳣
	 */
	public String getPostsByUserID(int userID, int postID)
	{
		return null;
	}
	
	/**
	 * ��ָ����Post�޵�����+1
	 * @param postID ָ����Post��Ӧ��postID
	 * @return ���������״̬<br>
	 * 0��ʾ�ɹ�<br>
	 * 1��ʾʧ��
	 */
	public int increaseLikeByPostID(int postID)
	{
		return 0;
	}
	
	/**
	 * ��ָ����Post�޵�����-1
	 * @param postID ָ����Post��Ӧ��postID
	 * @return ���������״̬<br>
	 * 0��ʾ�ɹ�<br>
	 * 1��ʾʧ��
	 */
	public int decreaseLikeByPostID(int postID)
	{
		return 0;
	}
	
	/**
	 * ��һ��Comment���뵽һ��Post��
	 * @param postID Ҫ�������Post��Ӧ��postID
	 * @param comment Ҫ׷�ӵ�Comment
	 * @return ���������״̬<br>
	 * 0��ʾ�ɹ�<br>
	 * 1��ʾʧ��
	 */
	public int appendComment(int postID, Comment comment)
	{
		return 0;
	}
	
	/**
	 * ɾ��Post��ָ����һ��Comment
	 * @param postID Ҫɾ����Comment���ڵ�Post��postID
	 * @param commentID Ҫɾ����Comment��commentID
	 * @return ���������״̬<br>
	 * 0��ʾ�ɹ�<br>
	 * 1��ʾʧ��
	 */
	public int deleteComment(int postID, int commentID)
	{
		return 0;
	}
	/************************									**************************/
	/************************			������Ҫ�����Ľӿ�			**************************/
	/************************									**************************/
}
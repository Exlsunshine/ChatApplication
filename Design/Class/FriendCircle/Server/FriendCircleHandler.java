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
	
	/********		Ҫ�����Ľӿ�		***********/
	/**
	 * ���ͻ��˴�����Comment��������������ݿ���
	 * @param commentJson �ӿͻ��˴�����JSON��ʽ��Comment��Ϣ
	 * @return ��commentJson����ԭ�ⲻ�����ٴη��ظ��ͻ���<br>
	 * null��ʾ����������ʱ�����쳣
	 */
	public String publishComment(String commentJson)
	{
		return null;
	}
	
	/**
	 * ���ݸ�����postID��ȡ���Ӧ������Comment
	 * @param postID ָ����postID
	 * @return JSON��ʽ��Comments��Ϣ
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
	 * @return JSON��ʽ��Post��Ϣ
	 */
	public String getPostsByUserID(int userID, int postID)
	{
		return null;
	}
}
package com.message;

import java.util.ArrayList;

import com.user.User;


/**
 * �Ի��ࣺ���ڱ���͹��������û��以�෢�͵���Ϣ<br>
 * һ���Ի��������û�A���û�B��AB֮�以�෢�͵���Ϣ��¼<br>
 * @see {@link AbstractMessage}
 * @author EXLsunshine
 *
 */
public class Dialog
{
	private ArrayList<AbstractMessage> dialogHistory;
	private User currentUser;
	private User another;

	public Dialog(User currentUser, User another) {
	}
	
	/**
	 * ��õ�ǰ�û�������ǰ���ڵ�½״̬������ʹ�ø�������û���
	 * @return ��ǰ�û�
	 */
	public User getCurrentUser() {
		return null;
	}
	
	/**
	 * ��ô����Ի�����һ���û�
	 * @return ��һ���û�
	 */
	public User getAnotherUser() {
		return null;
	}
	
	/**
	 * ��ô����Ի���������ʷ��Ϣ
	 * @return �����Ի����е���ʷ��Ϣ
	 */
	public ArrayList<AbstractMessage> getDialogHistory() {
		return null;
	}
	
	/**
	 * ��ô����Ի������һ����Ϣ
	 * @return �����Ի������һ����Ϣ
	 */
	public AbstractMessage getLastMessage() {
		return null;
	}
	
	/**
	 * ������Ի���ĩβ���һ����Ϣ
	 * @param message ����ӵ���Ϣ
	 */
	public void appendMessage(AbstractMessage message) {
	}
	
	/**
	 * �������Ի��е�msg������Ϣת����other����û�
	 * @param msg ��ת������Ϣ
	 * @param other Ŀ���û�
	 */
	public void forwardTo(AbstractMessage msg, User other) {
	}
}
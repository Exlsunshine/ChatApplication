package com.Message;

import com.doc.User;


/**
 * �Ի��ࣺ���ڱ���͹��������û��以�෢�͵���Ϣ<br>
 * һ���Ի��������û�A���û�B��AB֮�以�෢�͵���Ϣ��¼<br>
 * @see {@link AbstractMessage}
 * @author EXLsunshine
 *
 */
class Dialog
{
	private ArrayList<AbstractMessage> dialogHistory;
	private User currentUser;
	private User another;

	public Dialog(User currentUser, User another);
	
	/**
	 * ��õ�ǰ�û�������ǰ���ڵ�½״̬������ʹ�ø�������û���
	 * @return ��ǰ�û�
	 */
	public User getCurrentUser();
	
	/**
	 * ��ô����Ի�����һ���û�
	 * @return ��һ���û�
	 */
	public User getAnotherUser();
	
	/**
	 * ��ô����Ի���������ʷ��Ϣ
	 * @return �����Ի����е���ʷ��Ϣ
	 */
	public ArrayList<AbstractMessage> getDialogHistory();
	
	/**
	 * ��ô����Ի������һ����Ϣ
	 * @return �����Ի������һ����Ϣ
	 */
	public AbstractMessage getLastMessage();
	
	/**
	 * ������Ի���ĩβ���һ����Ϣ
	 * @param message ����ӵ���Ϣ
	 */
	public void appendMessage(AbstractMessage message);
	
	/**
	 * �������Ի��е�msg������Ϣת����other����û�
	 * @param msg ��ת������Ϣ
	 * @param other Ŀ���û�
	 */
	public void forwardTo(AbstractMessage msg, User other);
}
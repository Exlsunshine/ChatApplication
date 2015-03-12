package com.Message;

import com.doc.User;

/**
 * 	�������Ϣ�ࣺһ��AbstractMessage���͵Ķ������һ����Ϣ<br>
 *	һ����Ϣ�����������û��������û�������ʱ�䡢��Ϣ�������Ƿ��Ѷ�
 * @author EXLsunshine<br>
 */
public abstract class AbstractMessage
{
	protected User fromUser;
	protected User toUser;
	protected String date;
	protected int type;
	protected boolean isRead;
	
	/**
	 * ��ȡ������Ϣ�ķ�����
	 * @return ��Ϣ�ķ�����
	 */
	public abstract User getFromUser();

	/**
	 * {@link MapActivity}
	 * ��ȡ������Ϣ�Ľ�����
	 * @return ��Ϣ�Ľ�����
	 */
	public abstract User getToUser();
	
	/**
	 * ��ȡ������Ϣ������
	 * @return ��Ϣ������
	 */
	public abstract Object getContent();
	
	/**
	 * ��ȡ������Ϣ������
	 * @return ��Ϣ������<br>
	 * MESSAGE_TYPE_TEXT��������Ϣ����������<br>
	 * MESSAGE_TYPE_AUDIO��������Ϣ����������<br>
	 * MESSAGE_TYPE_IMAGE��������Ϣ��ͼƬ����<br>
	 * @see ConstantValues.InstructionCode
	 */
	public abstract int getMsgType();

	/**
	 * ��ȡ������Ϣ�Ľ���ʱ��
	 * @return ��Ϣ�Ľ���ʱ��
	 */
	public abstract String getDate();
	
	/**
	 * ��������Ϣ�Ƿ��ѱ��������Ķ�
	 * @return true �Ѷ�<br>
	 * false δ��
	 */
	public abstract boolean hasBeenRead();
	
	/**
	 * ��Ǵ�����ϢΪ�Ѷ�
	 */
	public abstract void setAsRead();
}
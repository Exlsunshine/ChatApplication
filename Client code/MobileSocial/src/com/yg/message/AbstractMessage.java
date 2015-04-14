package com.yg.message;

import com.yg.commons.ConstantValues;

/**
 * 	�������Ϣ�ࣺһ��AbstractMessage���͵Ķ������һ����Ϣ<br>
 *	һ����Ϣ�����������û��������û�������ʱ�䡢��Ϣ�������Ƿ��Ѷ�
 * @author EXLsunshine<br>
 */
public abstract class AbstractMessage
{
	protected int fromUserID;
	protected int toUserID;
	protected String date;
	protected int type;
	protected boolean isRead;
	protected int id;
	
	/**
	 * ��ȡ������Ϣ�ķ�����
	 * @return ��Ϣ�ķ�����
	 */
	public abstract int getFromUserID();

	/**
	 * {@link MapActivity}
	 * ��ȡ������Ϣ�Ľ�����
	 * @return ��Ϣ�Ľ�����
	 */
	public abstract int getToUserID();
	
	/**
	 * ��ȡ������Ϣ������<br>
	 * ������ı���Ϣ��ֱ�ӷ����ı�<br>
	 * �����ͼƬ��Ϣ�򷵻�ͼƬ���·��<br>
	 * �����������Ϣ�򷵻��������·��
	 * @return ��Ϣ���ݻ��ļ�·��
	 */
	public abstract String getContent();
	
	/**
	 * ��ȡ������Ϣ������
	 * @return ��Ϣ������<br>
	 * MESSAGE_TYPE_TEXT��������Ϣ����������<br>
	 * MESSAGE_TYPE_AUDIO��������Ϣ����������<br>
	 * MESSAGE_TYPE_IMAGE��������Ϣ��ͼƬ����<br>
	 * @see ConstantValues.InstructionCode
	 */
	public abstract int getMessageType();

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
	
	/**
	 * ��ȡ������Ϣ��ID
	 * @return ��ϢID
	 */
	public abstract int getID();
	
	public abstract void setID(int id);
}
package com.yg.dialog;

import java.util.ArrayList;
import java.util.Collections;

import com.yg.message.AbstractMessage;
import com.yg.message.DatabaseHandler;

import android.content.Context;

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
	private int currentUserID;
	private int anotherUserID;
	private Context context;

	public Dialog(int currentUserID, int anotherUserID, Context context)
	{
		this.currentUserID = currentUserID;
		this.anotherUserID = anotherUserID;
		this.context = context;
		
		//���ر������ݿ��е������¼
		DatabaseHandler db = new DatabaseHandler(context);
		this.dialogHistory = db.getAllMsg(currentUserID, anotherUserID);
		db.close();
		
		//�������¼��ʱ������
		Collections.sort(dialogHistory, new SortByDate());
	}
	
	/**
	 * ��ô����Ի��ܹ�������{@link AbstractMessage}�������������Ի�����Ϣ��
	 * @return ��Ϣ����
	 */
	public int getMessageNum() { return dialogHistory.size(); }
	
	/**
	 * ��õ�ǰ�û���ID������ǰ���ڵ�½״̬������ʹ�ø�������û���
	 * @return ��ǰ�û���ID
	 */
	public int getCurrentUserID() { return currentUserID; }
	
	/**
	 * ��ô����Ի�����һ���û���ID
	 * @return ��һ���û���ID
	 */
	public int getAnotherUserID() { return anotherUserID; }
	
	/**
	 * ��ô����Ի���������ʷ��Ϣ
	 * @return �����Ի����е���ʷ��Ϣ
	 */
	public ArrayList<AbstractMessage> getDialogHistory() { return dialogHistory; }
	
	/**
	 * ��ô����Ի������һ����Ϣ
	 * @return �����Ի������һ����Ϣ
	 */
	public AbstractMessage getLastMessage()
	{
		return dialogHistory.get(dialogHistory.size() - 1);
	}
	
	/**
	 * ������Ի���ĩβ���һ����Ϣ
	 * @param message ����ӵ���Ϣ
	 */
	public void appendMessage(AbstractMessage message) 
	{
		//����Ϣ���뵽�����¼��
		dialogHistory.add(message);
		
		//����Ϣ���뱾���������ݿ�
		DatabaseHandler db = new DatabaseHandler(context);
		db.insertMessage(message);
		db.close();
	}
	
	/**
	 * �������Ի��е�msg������Ϣת����otherUserID��Ӧ���û�
	 * @param msg ��ת������Ϣ
	 * @param otherUserID Ŀ���û�ID
	 */
	public void forwardTo(AbstractMessage msg, int otherUserID) {
	}
}
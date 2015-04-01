package com.yg.dialog;

import java.util.ArrayList;
import java.util.Collections;

import com.yg.message.AbstractMessage;
import com.yg.message.DatabaseHandler;

import android.content.Context;

/**
 * 对话类：用于保存和管理两个用户间互相发送的消息<br>
 * 一个对话包含：用户A、用户B、AB之间互相发送的消息记录<br>
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
		
		//加载本地数据库中的聊天记录
		DatabaseHandler db = new DatabaseHandler(context);
		this.dialogHistory = db.getAllMsg(currentUserID, anotherUserID);
		db.close();
		
		//将聊天记录按时间排序
		Collections.sort(dialogHistory, new SortByDate());
	}
	
	/**
	 * 获得此条对话总共包含的{@link AbstractMessage}数量，即词条对话的消息数
	 * @return 消息数量
	 */
	public int getMessageNum() { return dialogHistory.size(); }
	
	/**
	 * 获得当前用户的ID（即当前处于登陆状态、正在使用该软件的用户）
	 * @return 当前用户的ID
	 */
	public int getCurrentUserID() { return currentUserID; }
	
	/**
	 * 获得此条对话的另一个用户的ID
	 * @return 另一个用户的ID
	 */
	public int getAnotherUserID() { return anotherUserID; }
	
	/**
	 * 获得此条对话的所有历史消息
	 * @return 此条对话所有的历史消息
	 */
	public ArrayList<AbstractMessage> getDialogHistory() { return dialogHistory; }
	
	/**
	 * 获得此条对话的最后一条消息
	 * @return 此条对话的最后一条消息
	 */
	public AbstractMessage getLastMessage()
	{
		return dialogHistory.get(dialogHistory.size() - 1);
	}
	
	/**
	 * 向此条对话的末尾添加一条消息
	 * @param message 待添加的消息
	 */
	public void appendMessage(AbstractMessage message) 
	{
		//将消息插入到聊天记录中
		dialogHistory.add(message);
		
		//将消息存入本地聊天数据库
		DatabaseHandler db = new DatabaseHandler(context);
		db.insertMessage(message);
		db.close();
	}
	
	/**
	 * 将此条对话中的msg这条消息转发给otherUserID对应的用户
	 * @param msg 待转发的消息
	 * @param otherUserID 目标用户ID
	 */
	public void forwardTo(AbstractMessage msg, int otherUserID) {
	}
}
package com.Message;

import com.doc.User;

/**
 * 对话类：用于保存和管理两个用户间互相发送的消息<br>
 * 一个对话包含：用户A、用户B、AB之间互相发送的消息记录<br>
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
	 * 获得当前用户（即当前处于登陆状态、正在使用该软件的用户）
	 * @return 当前用户
	 */
	public User getCurrentUser();
	
	/**
	 * 获得此条对话的另一个用户
	 * @return 另一个用户
	 */
	public User getAnotherUser();
	
	/**
	 * 获得此条对话的所有历史消息
	 * @return 此条对话所有的历史消息
	 */
	public ArrayList<AbstractMessage> getDialogHistory();
	
	/**
	 * 获得此条对话的最后一条消息
	 * @return 此条对话的最后一条消息
	 */
	public AbstractMessage getLastMessage();
	
	/**
	 * 向此条对话的末尾添加一条消息
	 * @param message 待添加的消息
	 */
	public void appendMessage(AbstractMessage message);
	
	/**
	 * 将此条对话中的msg这条消息转发给other这个用户
	 * @param msg 待转发的消息
	 * @param other 目标用户
	 */
	public void forwardTo(AbstractMessage msg, User other);
}
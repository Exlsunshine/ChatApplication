package com.database;

/**
 * 	抽象的消息类：一个AbstractMessage类型的对象代表一条消息<br>
 *	一条消息包含：发起用户、接收用户、发送时间、消息类型与是否已读
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
	 * 获取此条消息的发送者
	 * @return 消息的发送者
	 */
	public abstract int getFromUserID();

	/**
	 * {@link MapActivity}
	 * 获取此条消息的接收者
	 * @return 消息的接收者
	 */
	public abstract int getToUserID();
	
	/**
	 * 获取此条消息的内容
	 * @return 消息的内容
	 */
	public abstract byte [] getContent();
	
	/**
	 * 获取此条消息的类型
	 * @return 消息的类型<br>
	 * MESSAGE_TYPE_TEXT：此条消息是文字类型<br>
	 * MESSAGE_TYPE_AUDIO：此条消息是语音类型<br>
	 * MESSAGE_TYPE_IMAGE：此条消息是图片类型<br>
	 * @see ConstantValues.InstructionCode
	 */
	public abstract int getMessageType();

	/**
	 * 获取此条消息的接收时间
	 * @return 消息的接收时间
	 */
	public abstract String getDate();
	
	/**
	 * 检查此条消息是否已被接收者阅读
	 * @return true 已读<br>
	 * false 未读
	 */
	public abstract boolean hasBeenRead();
	
	/**
	 * 标记此条消息为已读
	 */
	public abstract void setAsRead();
	
	/**
	 * 获取此条消息的ID
	 * @return 消息ID
	 */
	public abstract int getID();
	
	public abstract void setID(int id);
}
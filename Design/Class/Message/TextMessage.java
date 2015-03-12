package com.Message;

/**
 * 文字类型的消息，继承自{@link AbstractMessage}：<br>
 * 需要将{@link AbstractMessage}中的方法重写
 * @author EXLsunshine
 *
 */
class TextMessage extends AbstractMessage
{
	String text;

	/**
	 * 复制此条消息中的文字
	 */
	public void copy();
}

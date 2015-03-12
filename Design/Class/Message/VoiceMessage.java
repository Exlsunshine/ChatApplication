package com.Message;

/**
 * 语音类型的消息，继承自{@link AbstractMessage}：<br>
 * 需要将{@link AbstractMessage}中的方法重写
 * @author EXLsunshine
 *
 */
class VoiceMessage extends AbstractMessage
{
	Audio voice;

	/**
	 * 播放此条消息中的语音
	 */
	public void play();
	
	/**
	 * 停止播放此条消息中的语音
	 */
	public void stop();
}
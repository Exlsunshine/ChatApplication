package com.game;

/**
 * 打地鼠游戏类：<br>
 * 继承自{@link AbstractGame}
 * @author EXLsunshine
 *
 */
class WhacAMoleGame implements AbstractGame
{
	private long elapse;
	private long maxTime;
	private int [] board;
	private int score;
	private int targetScore;

	/**
	 * 初始化一个WhacAMoleGame，并将其通关的目标分数设置为targetScore
	 * @param targetScore 通关所需的目标分数
	 */
	public WhacAMoleGame(int targetScore);
	
	/**
	 * 随机生成下个地鼠出来的位置（0-8之一的一个整数）<br>
	 * 0 1 2<br>
	 * 3 4 5<br>
	 * 6 7 8<br>
	 * @return
	 */
	public int randomPosition();
	
	/**
	 * 移除index位置的地鼠
	 * @param index
	 */
	public void removeMole(int index);
	
	/**
	 * 打中地鼠后加一次分数
	 */
	public void addScore();
	
	/**
	 * 获得当前分数
	 * @return
	 */
	public int getScore();
}
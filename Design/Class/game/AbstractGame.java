package com.game;

import com.doc.ConstantValues;

/**
 * 游戏的抽象类
 */
public class AbstractGame
{
	private int gameType;
	
	/**
	 * 重新开始游戏
	 */
	public void restart();
	
	/**
	 * 放弃并退出游戏
	 */
	public void abort();
	
	/**
	 * 查询是否已经胜利
	 * @return true 已经胜利<br>
	 * false 尚未胜利
	 */
	public boolean isWin();
	
	/**
	 * 查询是否超过规定的游戏时间
	 * @return true 已超过<br>
	 * false 未超过
	 */
	public boolean isTimeout();
	
	/**
	 * 获取当前游戏对象的游戏类型
	 * @return 游戏类型<br>
	 * GAME_TYPE_EIGHTPUZZLE<br>
	 * GAME_TYPE_QUIZ<br>
	 * GAME_TYPE_WHAC
	 * @see ConstantValues.InstructionCode
	 */
	public int getGameType();
}
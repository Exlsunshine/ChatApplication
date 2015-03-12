package com.game;

import com.doc.ConstantValues;

/**
 * 华容道游戏类：<br>
 * 继承自{@link AbstractGame}
 * @author EXLsunshine
 *
 */
public class EightPuzzleGame extends AbstractGame
{
	private long steps;
	private long elapse;
	private long maxTime;
	private int [][] board;

	/**
	 * 随机的生成一个3x3的华容道棋盘
	 */
	private void generateBoard();
	
	/**
	 * 检查当前棋盘是否和棋(即无解)
	 * @return true 无解<br>
	 * false 有解
	 */
	private boolean isTie();
	
	/**
	 * 向上移动棋盘
	 */
	private void moveUp();
	
	/**
	 * 向下移动棋盘
	 */
	private void moveDown();
	
	/**
	 * 向左移动棋盘
	 */
	private void moveLeft();
	
	/**
	 * 向右移动棋盘
	 */
	private void moveRight();

	/**
	 * 对棋盘进行移动操作
	 * @param direction <br>
	 * DIRECTION_UP 上<br>
	 * DIRECTION_DOWN 下<br>
	 * DIRECTION_LEFT 左<br>
	 * DIRECTION_RIGHT 右<br>
	 * @see ConstantValues.InstructionCode
	 * 
	 */
	public void execute(int direction);
	
	/**
	 * 获得当前的棋盘
	 * @return 当前的棋盘
	 */
	public int [][] getCurrentBoard();	
}

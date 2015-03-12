package com.game;

import com.doc.ConstantValues;

/**
 * ���ݵ���Ϸ�ࣺ<br>
 * �̳���{@link AbstractGame}
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
	 * ���������һ��3x3�Ļ��ݵ�����
	 */
	private void generateBoard();
	
	/**
	 * ��鵱ǰ�����Ƿ����(���޽�)
	 * @return true �޽�<br>
	 * false �н�
	 */
	private boolean isTie();
	
	/**
	 * �����ƶ�����
	 */
	private void moveUp();
	
	/**
	 * �����ƶ�����
	 */
	private void moveDown();
	
	/**
	 * �����ƶ�����
	 */
	private void moveLeft();
	
	/**
	 * �����ƶ�����
	 */
	private void moveRight();

	/**
	 * �����̽����ƶ�����
	 * @param direction <br>
	 * DIRECTION_UP ��<br>
	 * DIRECTION_DOWN ��<br>
	 * DIRECTION_LEFT ��<br>
	 * DIRECTION_RIGHT ��<br>
	 * @see ConstantValues.InstructionCode
	 * 
	 */
	public void execute(int direction);
	
	/**
	 * ��õ�ǰ������
	 * @return ��ǰ������
	 */
	public int [][] getCurrentBoard();	
}

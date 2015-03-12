package com.game;

import com.doc.ConstantValues;

/**
 * ��Ϸ�ĳ�����
 */
public class AbstractGame
{
	private int gameType;
	
	/**
	 * ���¿�ʼ��Ϸ
	 */
	public void restart();
	
	/**
	 * �������˳���Ϸ
	 */
	public void abort();
	
	/**
	 * ��ѯ�Ƿ��Ѿ�ʤ��
	 * @return true �Ѿ�ʤ��<br>
	 * false ��δʤ��
	 */
	public boolean isWin();
	
	/**
	 * ��ѯ�Ƿ񳬹��涨����Ϸʱ��
	 * @return true �ѳ���<br>
	 * false δ����
	 */
	public boolean isTimeout();
	
	/**
	 * ��ȡ��ǰ��Ϸ�������Ϸ����
	 * @return ��Ϸ����<br>
	 * GAME_TYPE_EIGHTPUZZLE<br>
	 * GAME_TYPE_QUIZ<br>
	 * GAME_TYPE_WHAC
	 * @see ConstantValues.InstructionCode
	 */
	public int getGameType();
}
package com.game;

/**
 * �������Ϸ�ࣺ<br>
 * �̳���{@link AbstractGame}
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
	 * ��ʼ��һ��WhacAMoleGame��������ͨ�ص�Ŀ���������ΪtargetScore
	 * @param targetScore ͨ�������Ŀ�����
	 */
	public WhacAMoleGame(int targetScore);
	
	/**
	 * ��������¸����������λ�ã�0-8֮һ��һ��������<br>
	 * 0 1 2<br>
	 * 3 4 5<br>
	 * 6 7 8<br>
	 * @return
	 */
	public int randomPosition();
	
	/**
	 * �Ƴ�indexλ�õĵ���
	 * @param index
	 */
	public void removeMole(int index);
	
	/**
	 * ���е�����һ�η���
	 */
	public void addScore();
	
	/**
	 * ��õ�ǰ����
	 * @return
	 */
	public int getScore();
}
package com.game;

import com.doc.ArrayList;
import com.doc.QuizItem;

/**
 * ���ﴳ����Ϸ�ࣺ�̳���{@link AbstractGame}<br>
 * һ��������Ϸ�а�����һ����������б�
 * @author EXLsunshine
 *
 */
class QuizGame implements AbstractGame
{
	private long elapse;
	private long maxTime;
	
	/**
	 * ��ǰ���ڳ����ƽ�������Ӧ�����б��е�����
	 */
	private int quizIndex;

	/**
	 * ��ʼ�����ﴳ����Ϸ�࣬�������е���������б�����Ϊdata
	 * @param data
	 */
	public QuizGame(ArrayList<QuizItem> data);
	
	/**
	 * ��õ�ǰ���ڳ����ƽ���������ʾ
	 * @return �������ʾ
	 */
	public String getCurrentHint();
	
	/**
	 * ��鵱ǰ���ڳ����ƽ������Ĵ��Ƿ�ΪuserAnswer
	 * @param userAnswer ��Ҫ���ԵĴ�
	 * @return true ��ȷ<br>
	 * false ����
	 */
	public boolean checkAnswer(String userAnswer);	
}

/**
 * �����ࣺ<br>
 * һ��������������һ����ʾ��һ����
 * @author EXLsunshine
 *
 */
class QuizItem
{
	private String hint;
	private String answer;

	/**
	 * ��ʼ����������󣬽���ʾ����Ϊhint��������Ϊanswer
	 * @param hint �������ʾ
	 * @param answer ����Ĵ�
	 */
	public QuizItem(String hint, String answer);
	
	/**
	 * ��ȡ�����������ʾ
	 * @return �������ʾ
	 */
	public String getHint();
	
	/**
	 * ��ȡ��������Ĵ�
	 * @return ����Ĵ�
	 */
	public String getAnswer();
}

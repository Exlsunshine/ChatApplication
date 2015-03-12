package com.game;

import com.doc.ArrayList;
import com.doc.QuizItem;

/**
 * 谜语闯关游戏类：继承自{@link AbstractGame}<br>
 * 一个谜语游戏中包含：一个谜语对象列表
 * @author EXLsunshine
 *
 */
class QuizGame implements AbstractGame
{
	private long elapse;
	private long maxTime;
	
	/**
	 * 当前正在尝试破解的谜语对应的在列表中的索引
	 */
	private int quizIndex;

	/**
	 * 初始化谜语闯关游戏类，并将其中的谜语对象列表设置为data
	 * @param data
	 */
	public QuizGame(ArrayList<QuizItem> data);
	
	/**
	 * 获得当前正在尝试破解的谜语的提示
	 * @return 谜语的提示
	 */
	public String getCurrentHint();
	
	/**
	 * 检查当前正在尝试破解的谜语的答案是否为userAnswer
	 * @param userAnswer 想要尝试的答案
	 * @return true 正确<br>
	 * false 错误
	 */
	public boolean checkAnswer(String userAnswer);	
}

/**
 * 谜语类：<br>
 * 一个谜语对象包含：一个提示和一个答案
 * @author EXLsunshine
 *
 */
class QuizItem
{
	private String hint;
	private String answer;

	/**
	 * 初始化谜语类对象，将提示设置为hint，答案设置为answer
	 * @param hint 谜语的提示
	 * @param answer 谜语的答案
	 */
	public QuizItem(String hint, String answer);
	
	/**
	 * 获取此条谜语的提示
	 * @return 谜语的提示
	 */
	public String getHint();
	
	/**
	 * 获取此条谜语的答案
	 * @return 谜语的答案
	 */
	public String getAnswer();
}

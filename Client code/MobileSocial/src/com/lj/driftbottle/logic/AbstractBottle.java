package com.lj.driftbottle.logic;
 

/**
 * Bottle base class
 * @author admin
 *
 */
public abstract class AbstractBottle 
{
	//json object key string
	public static final String HISTORY_TEXT = "historyText";
	public static final String APPEND_TEXT = "appendText";
	
	private String historyText = "";
	private String appendText = "";
	
	public AbstractBottle() 
	{
	}
	
	/**
	 * constructor<br>
	 * @param historyText history text string
	 */
	public AbstractBottle(String historyText)
	{
		this.historyText = historyText.replaceAll("<br>", "\n");
	}
	
	/**
	 * append new text
	 * @param appendText appended text
	 */
	public void appentText(String appendText, int userID)
	{
		appendText = "u" + userID + ":" + appendText;
		this.appendText = appendText.replaceAll("\r\n", "").replaceAll("\n", "") + "<br>";
	}
	
	/**
	 * update history text<br>
	 * call this function after throw or reply bottle
	 */
	public void updateHistoryText()
	{
		historyText += appendText.replaceAll("<br>", "\n");
		appendText = "";
	}
	
	public String getAppendText()
	{
		return appendText;
	}
	
	public String getHistoryText()
	{
		return historyText;
	}
	
	public abstract String toJsonString();
}

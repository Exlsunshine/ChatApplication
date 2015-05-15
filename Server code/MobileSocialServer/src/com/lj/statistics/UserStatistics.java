package com.lj.statistics;

import java.util.ArrayList;
import java.util.HashMap;



import com.commonapi.ConstantValues;
import com.commonapi.PackString;
import com.database.SQLServerEnd;

public class UserStatistics 
{
	private final String DATABASE_NAME = "JMMSRDB";
	private final String TABLE_NAME = "user_statistics";
	
	private final String[] COLUMN_NAME_LIST = {"friends_num", "shake_num", "game_challenged_success", "game_challenged_fail",
			                                   "game_challeng_success", "game_challeng_fail", "voice_num"};
	
	private String joinConditionStatement(String [] condition, String [] value, String seperator, String operator)
	{
		String sentence = "";
		
		for (int i = 0; i < condition.length - 1; i++)
			sentence += condition[i] + " " + operator + " "  + value[i]  + " " + seperator + " ";

		sentence += condition[condition.length - 1]  + " = "  + value[condition.length - 1];
		return sentence;
	}
	
	private SQLServerEnd sql = new SQLServerEnd(DATABASE_NAME, TABLE_NAME);
	
	public void createNewUser(int userID)
	{
		String[] column = {"user_id"};
		String[] value = {String.valueOf(userID)};
		sql.insert(column, value);
	}
	
	public void increaseStatistic(int userID, int type)
	{
		String[] updateCol = {COLUMN_NAME_LIST[type]};
		String[] updateVal = {COLUMN_NAME_LIST[type] + "+1"};
		String[] condition = {"user_id"};
		String[] conditionVal = {String.valueOf(userID)};
		String str = "UPDATE " + TABLE_NAME + " SET "
				+ joinConditionStatement(updateCol, updateVal, ",", "=")
				+ " WHERE " + joinConditionStatement(condition, conditionVal, "and", "=");
		sql.excecuteRawQuery(str);
	}
	
	public String getStatistics(int userID) throws Exception
	{
		String[] query = {"friends_num", "shake_num", "game_challenged_success", "game_challenged_fail",
                "game_challeng_success", "game_challeng_fail", "voice_num", "register_time"};
		String[] condition = {"user_id"};
		String[] conditionVal = {String.valueOf(userID)};
		ArrayList<HashMap<String, String>> result = sql.select(query, condition, conditionVal);
		String str = PackString.arrylist2JsonString("statistics", result, 0);
		return str;
	}
	
	public void increaseGameChallengFail(int challengUserID, int challengedUserID)
	{
		increaseStatistic(challengedUserID, ConstantValues.InstructionCode.STATISTICS_GAME_CHALLENGED_FAIL_TYPE);
		increaseStatistic(challengUserID, ConstantValues.InstructionCode.STATISTICS_GAME_CHALLENG_FAIL_TYPE);
	}
}

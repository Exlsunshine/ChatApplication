package com.lj.gameSettingPackage;

import java.util.ArrayList;
import java.util.HashMap;

import com.commonapi.ConstantValues;
import com.database.SQLServerEnd;


public class GameMole 
{
	private final String DATABASE_NAME = "JMMSRDB";
	private final String TABLE_NAME = "game_whackmole_setting";

	private SQLServerEnd sql = new SQLServerEnd(DATABASE_NAME, TABLE_NAME);
	public int uploadScore(int userID, int score)
	{
		String[] condition = {"user_id"};
		String[] conditionVal = {String.valueOf(userID)};
		boolean result = sql.isConditionExist(condition, conditionVal);
        if (result)
        	sql.delete(condition, conditionVal);
        
        String[] column = {"user_id", "score"};
        String[] value = {String.valueOf(userID), String.valueOf(score)};
        sql.insert(column, value);
        return ConstantValues.InstructionCode.SUCCESS;
	}
	
	public String getMoleScore(int userID)
	{
		String[] query = {"score"};
		String[] condition = {"user_id"};
		String[] conditionVal = {String.valueOf(userID)};
		ArrayList<HashMap<String, String>> map = sql.select(query, condition, conditionVal);
		if (map.size() == 0)
			return "0";
		return map.get(0).get("score").toString();
	}
}

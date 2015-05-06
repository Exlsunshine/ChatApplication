package com.lj.gamePackage;

import java.util.ArrayList;
import java.util.HashMap;

import com.commonapi.ConstantValues;
import com.database.SQLServerEnd;


public class GameSetting 
{
	//数据库名
//	private final String DATABASE_NAME = "StrangerDB";
	private final String DATABASE_NAME = "JMMSRDB";
	//表名
	private final String TABLE_NAME = "user_settings";

	private SQLServerEnd sql = new SQLServerEnd(DATABASE_NAME, TABLE_NAME);
	
	public int checkSetGame(int userId)
	{
        String[] condition = {"user_id"};
        String[] conditionVal = {String.valueOf(userId)};
        boolean result = sql.isConditionExist(condition, conditionVal);
        if (result)
        	return ConstantValues.InstructionCode.GAME_HAS_SET;
        else
        	return ConstantValues.InstructionCode.GAME_NOT_SET;
	}
	
	public int getSetGameType(int userId)
	{
		String[] query = {"game_type"};
		String[] condition = {"user_id"};
        String[] conditionVal = {String.valueOf(userId)};
        ArrayList<HashMap<String, String>> result = sql.select(query, condition, conditionVal);
        if (result.size() == 0)
        	return ConstantValues.InstructionCode.GAME_NOT_SET;
        else
        	return Integer.valueOf(result.get(0).get("game_type").toString());
	}
	
	public void setGameType(int userID, int gameType)
	{
		String[] condition = {"user_id"};
        String[] conditionVal = {String.valueOf(userID)};
        boolean result = sql.isConditionExist(condition, conditionVal);
        if (result)
        {
        	String[] updateCol = {"game_type"};
        	String[] updateVal = {String.valueOf(gameType)};
        	sql.update(updateCol, updateVal, condition, conditionVal);
        }
        else
        {
        	String[] column = {"game_type"};
        	String[] val = {String.valueOf(gameType)};
        	sql.insert(column, val);
        }
	}
}

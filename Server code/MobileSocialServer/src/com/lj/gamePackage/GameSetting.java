package com.lj.gamePackage;

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
}

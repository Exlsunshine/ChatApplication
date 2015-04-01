package com.lj.shake;

import com.yg.commons.CommonUtil;
import com.yg.commons.ConstantValues;
import com.yg.user.WebServiceAPI;




public class WebGameSetting 
{
	private WebServiceAPI webserviceGame = new WebServiceAPI(ConstantValues.InstructionCode.PACKAGE_GAME, "GameSetting");
	
	public int checkSetGame(int userId)
	{
		String[] name = {"userId"};
		Object[] values = {userId};
		Object result = webserviceGame.callFuntion("checkSetGame", name, values);
		if (CommonUtil.isNetWorkError(result))
			return ConstantValues.InstructionCode.ERROR_NETWORK;
		else
			return Integer.parseInt(result.toString());
	}
}

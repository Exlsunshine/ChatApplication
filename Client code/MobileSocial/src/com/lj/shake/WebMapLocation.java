package com.lj.shake;

import java.util.ArrayList;
import java.util.HashMap;

import com.yg.commons.CommonUtil;
import com.yg.commons.ConstantValues;
import com.yg.user.PackString;
import com.yg.user.WebServiceAPI;

public class WebMapLocation 
{

	private WebServiceAPI webserviceGame = new WebServiceAPI(ConstantValues.InstructionCode.PACKAGE_GAME, "ShakeLocation");
	
	public int uploadLocation(int userId, float longitude, float latitude)
	{
		String[] name = {"userId", "longitude", "latitude"};
		Object[] values = {userId, String.valueOf(longitude), String.valueOf(latitude)};
		Object result = webserviceGame.callFuntion("uploadLocation", name, values);
		if (CommonUtil.isNetWorkError(result))
			return ConstantValues.InstructionCode.ERROR_NETWORK;
		return ConstantValues.InstructionCode.SUCCESS;
	}
	
	public ArrayList<UserShakeData> getNearbyUser(int userId)
	{
		ArrayList<UserShakeData> userShakeDataList = new ArrayList<UserShakeData>();
		String[] name = {"userId"};
		Object[] values = {userId};
		Object result = webserviceGame.callFuntion("getNearbyUser", name, values);
		
		if (CommonUtil.isNetWorkError(result))
			return null;
		if (result.toString().equals("null"))
			return userShakeDataList;
		PackString ps = new PackString(result.toString());
		ArrayList<HashMap<String, Object>> map_NearbyUser = ps.jsonString2Arrylist("nearbyusers");
		for (int i =0; i < map_NearbyUser.size(); i++) 
		{
			HashMap<String, Object> m = map_NearbyUser.get(i);
			float longitude = Float.parseFloat((String)m.get("longitude"));
			float latitude = Float.parseFloat((String)m.get("latitude"));
			int userid = Integer.parseInt(m.get("user_id").toString());
			String nickname = m.get("nick_name").toString();
			int gametype = Integer.parseInt(m.get("game_type").toString());
			String sex = m.get("sex").toString();
			UserShakeData item = new UserShakeData(userid, nickname, longitude, latitude, gametype, sex);
			userShakeDataList.add(item);
		}
		return userShakeDataList;
	}
}

package com.network;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONException;

import com.database.SQLServerEnd;
import com.json.process.PackString;
import com.mail.SendMailDemo;
import com.util.HometownHandler;
import com.util.PortraitTransmit;

public class NetworkHandler
{
	private SQLServerEnd userBasicInfoTB = null;
	private SQLServerEnd userRelationshipTB = null;
	
	private void initUserBasicInfoTB()
	{
		if (userBasicInfoTB == null)
			userBasicInfoTB = new SQLServerEnd("JMMSRDB", "user_basic_info");
	}
	
	private void initUserRelationshipTB()
	{
		if (userRelationshipTB == null)
			userRelationshipTB = new SQLServerEnd("JMMSRDB", "user_relationship");
	}
	
	public int setPassword(int userID, String password)
	{
		initUserBasicInfoTB();
		
		String [] updateCol = {"login_pwd"};
		String [] updateVal = { password };
		String [] condition = { "id" };
		String [] conditionVal = { String.valueOf(userID) };
		
		int errorCode = userBasicInfoTB.update(updateCol, updateVal, condition, conditionVal);
		
		if (errorCode == 0)
			System.out.println("setPassword success.");
		else
			System.out.println("setPassword failed.");
		
		return errorCode;
	}
	
	public int setPortrait(int userID, String portrait)
	{
		PortraitTransmit pt = new PortraitTransmit(userID, portrait);
		String portraitPath = pt.getSavedImgPath();
		
		if (portraitPath == null)
		{
			System.out.println("setPortrait failed.");
			return 4;
		}
		else
		{
			initUserBasicInfoTB();
			
			String [] updateCol = {"portrait_path"};
			String [] updateVal = { portraitPath };
			String [] condition = { "id" };
			String [] conditionVal = { String.valueOf(userID) };
			
			int errorCode = userBasicInfoTB.update(updateCol, updateVal, condition, conditionVal);
			
			if (errorCode == 0)
				System.out.println("setPortrait success.");
			else
				System.out.println("setPortrait failed.");
			
			return errorCode;
		}
	} 
	
	public int setNickName(int userID, String nickName) 
	{
		initUserBasicInfoTB();
		
		String [] updateCol = {"nick_name"};
		String [] updateVal = { nickName };
		String [] condition = { "id" };
		String [] conditionVal = { String.valueOf(userID) };
		
		int errorCode = userBasicInfoTB.update(updateCol, updateVal, condition, conditionVal);
		
		if (errorCode == 0)
			System.out.println("setNickName success.");
		else
			System.out.println("setNickName failed.");
		
		return errorCode;
	}
	
	public int setEmail(int userID, String email) 
	{
		initUserBasicInfoTB();
		
		String [] updateCol = {"email"};
		String [] updateVal = { email };
		String [] condition = { "id" };
		String [] conditionVal = { String.valueOf(userID) };
		
		int errorCode = userBasicInfoTB.update(updateCol, updateVal, condition, conditionVal);
		
		if (errorCode == 0)
			System.out.println("setEmail success.");
		else
			System.out.println("setEmail failed.");
		
		return errorCode;
	}
	
	public int setSex(int userID, String sex) 
	{
		initUserBasicInfoTB();
		
		String [] updateCol = {"sex"};
		String [] updateVal = { sex };
		String [] condition = { "id" };
		String [] conditionVal = { String.valueOf(userID) };
		
		int errorCode = userBasicInfoTB.update(updateCol, updateVal, condition, conditionVal);
		
		if (errorCode == 0)
			System.out.println("setSex success.");
		else
			System.out.println("setSex failed.");
		
		return errorCode;
	}
	
	public int setBirthday(int userID, String birthday) 
	{
		initUserBasicInfoTB();
		
		String [] updateCol = {"birthday"};
		String [] updateVal = { birthday };
		String [] condition = { "id" };
		String [] conditionVal = { String.valueOf(userID) };
		
		int errorCode = userBasicInfoTB.update(updateCol, updateVal, condition, conditionVal);
		
		if (errorCode == 0)
			System.out.println("setBirthday success.");
		else
			System.out.println("setBirthday failed.");
		
		return errorCode;
	}
	
	public int setHometown(int userID, int hometownID) 
	{
		initUserBasicInfoTB();
		
		String [] updateCol = {"hometown_id"};
		String [] updateVal = { String.valueOf(hometownID) };
		String [] condition = { "id" };
		String [] conditionVal = { String.valueOf(userID) };
		
		int errorCode = userBasicInfoTB.update(updateCol, updateVal, condition, conditionVal);
		
		if (errorCode == 0)
			System.out.println("setHometown success.");
		else
			System.out.println("setHometown failed.");
		
		return errorCode;
	}
	
	public int setPhoneNumber(int userID, String phoneNumber) 
	{
		initUserBasicInfoTB();
		
		String [] updateCol = {"phone_number"};
		String [] updateVal = { phoneNumber };
		String [] condition = { "id" };
		String [] conditionVal = { String.valueOf(userID) };
		
		int errorCode = userBasicInfoTB.update(updateCol, updateVal, condition, conditionVal);
		
		if (errorCode == 0)
			System.out.println("setPhoneNumber success.");
		else
			System.out.println("setPhoneNumber failed.");
		
		return errorCode;
	}
	
	public int setAlias(int userID, int anotherUserID, String alias)
	{
		initUserRelationshipTB();
		
		String [] updateCol = {"alias"};
		String [] updateVal = { alias };
		String [] condition = { "first_userid", "second_userid"};
		String [] conditionVal = { String.valueOf(userID), String.valueOf(anotherUserID) };
		
		int errorCode = userRelationshipTB.update(updateCol, updateVal, condition, conditionVal);
		
		if (errorCode == 0)
			System.out.println("setAlias success.");
		else
			System.out.println("setAlias failed.");
		
		return errorCode;
	}
	
	public int moveFriendToGroup(int userID, int anotherUserID, String groupName)
	{
		initUserRelationshipTB();
		
		String [] updateCol = {"group_name"};
		String [] updateVal = { groupName };
		String [] condition = { "first_userid", "second_userid"};
		String [] conditionVal = { String.valueOf(userID), String.valueOf(anotherUserID) };
		
		int errorCode = userRelationshipTB.update(updateCol, updateVal, condition, conditionVal);
		
		if (errorCode == 0)
			System.out.println("moveFriendToGroup success.");
		else
			System.out.println("moveFriendToGroup failed.");
		
		return errorCode;
	}
	
	public int setAsCloseFriend(int userID, int anotherUserID, boolean enable)
	{
		initUserRelationshipTB();
		
		String [] updateCol = {"close_friend_flag"};
		String [] updateVal = { String.valueOf(enable ? 1 : 0) };
		String [] condition = { "first_userid", "second_userid"};
		String [] conditionVal = { String.valueOf(userID), String.valueOf(anotherUserID) };
		
		int errorCode = userRelationshipTB.update(updateCol, updateVal, condition, conditionVal);
		
		if (errorCode == 0)
			System.out.println("setAsCloseFriend success.");
		else
			System.out.println("setAsCloseFriend failed.");
		
		return errorCode;
	}
	
	public int blockUser(int userID, int anotherUserID)
	{
		return moveFriendToGroup(userID, anotherUserID, "BLACK_LIST");
	}
	
	public int deleteUser(int userID, int anotherUserID)
	{
		initUserRelationshipTB();
		
		String [] condition = { "first_userid", "second_userid"};
		String [] conditionVal = { String.valueOf(userID), String.valueOf(anotherUserID) };
		
		int errorCode1 = userRelationshipTB.delete(condition, conditionVal);
		
		conditionVal = new String [] { String.valueOf(anotherUserID), String.valueOf(userID) };
		int errorCode2 = userRelationshipTB.delete(condition, conditionVal);
		
		if (errorCode1 == 0 && errorCode2 == 0)
			System.out.println("deleteUser success.");
		else
			System.out.println("deleteUser failed.");
		
		return errorCode1 == 0? errorCode2 : errorCode1;
	}
	
	public String getFriendList(int userID)
	{
		initUserBasicInfoTB();
		
		/*
		 
		select a.login_account, a.nick_name, a.email, a.sex, a.birthday, a.portrait_path, a.hometown_id, a.phone_number, b.second_userid, b.group_name, b.alias, b.close_friend_flag
		from user_basic_info as a,	(
								select second_userid, group_name, alias, close_friend_flag
								from user_relationship
								where first_userid = userID
							)as b
		where a.id = b.second_userid
		
		
		select login_account, nick_name, email, sex, birthday, portrait_path, hometown_id, phone_number, group_name, alias, close_friend_flag
		from user_basic_info
		inner join 	(
						select second_userid, group_name, alias, close_friend_flag
						from user_relationship
						where first_userid = 4
					)as b
		on user_basic_info.id = b.second_userid
		
		select user_basic_info.id, login_account, nick_name, email, sex, birthday, portrait_path, city, province, phone_number, group_name, alias, close_friend_flag
		from user_basic_info
			inner join ( select second_userid, group_name, alias, close_friend_flag from user_relationship where first_userid = 4 )as b
				on user_basic_info.id = b.second_userid
			inner join ( select * from hometown_data )as c
				on user_basic_info.hometown_id = c.id
		 
		 * 
		String sql = 
				  "select id, login_account, nick_name, email, sex, birthday, portrait_path, hometown_id, phone_number, group_name, alias, close_friend_flag "
				+ "from user_basic_info "
				+ "inner join ("
								+ "select second_userid, group_name, alias, close_friend_flag "
								+ "from user_relationship "
								+ "where first_userid = " + String.valueOf(userID) + " "
							+ ")as b "
				+ "on user_basic_info.id = b.second_userid ";
		
		String sql = "select user_basic_info.id as user_id, login_account, nick_name, email, sex, birthday, portrait_path, city as hometown_city, province as hometown_province, phone_number, group_name, alias, close_friend_flag "
				+ "from user_basic_info "
				+ "inner join ( select second_userid, group_name, alias, close_friend_flag from user_relationship where first_userid = " + String.valueOf(userID) + " )as b "
				+ "on user_basic_info.id = b.second_userid "
				+ "inner join ( select * from hometown_data )as c "
				+ "on user_basic_info.hometown_id = c.id ";
		*/
		
		String sql = "select user_basic_info.id as user_id, login_account, nick_name, email, sex, birthday, portrait_path, hometown, phone_number, group_name, alias, close_friend_flag "
				+ "from user_basic_info "
				+ "inner join ( select second_userid, group_name, alias, close_friend_flag from user_relationship where first_userid = " + String.valueOf(userID) + " )as b "
				+ "on user_basic_info.id = b.second_userid ";
		
		String[] query = {"user_id", "login_account", "nick_name", "email", "sex", "birthday", "portrait_path", "phone_number", "hometown", "group_name", "alias", "close_friend_flag"};
		ArrayList<HashMap<String, String>> result = userBasicInfoTB.excecuteRawQuery(sql, query);
		
		if (result != null)
		{
			for (int i = 0; i < result.size(); i++)
			{
				try {
					String img = ImageTransmit.image2String(result.get(i).get("portrait_path"));
					result.get(i).remove("portrait_path");
					result.get(i).put("portrait", img);
					
					
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		else
			System.out.println("getFriendList failed.");
		
		String str = null;
		try 
		{
			str = PackString.arrylist2JsonString("friends_list", result, 0);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		return str;
	}
	
	/**
	 * 验证用户的身份
	 * @param loginAccount 登陆账号
	 * @param password 登陆密码
	 * @return 用户信息(Json格式)<br>
	 * null表示身份认证失败或数据有误
	 */
	public String validateIdentity(String loginAccount, String password)
	{
		initUserBasicInfoTB();
		
		String[] query = {"id", "login_account", "nick_name", "email", "portrait_path", "sex", "birthday", "phone_number", "hometown"};
		
		ArrayList<HashMap<String, String>> list = userBasicInfoTB.select(query , new String [] {"login_account" , "login_pwd"},  new String [] {loginAccount, password});
		if (list.size() == 0)
			return null;
		else
		{
			try 
			{
				String portrait;
				portrait = ImageTransmit.image2String(list.get(0).get("portrait_path"));list.get(0).remove("portrait_path");
				list.get(0).remove("portrait_path");
				list.get(0).put("portrait", portrait);
				System.out.println(list.get(0).get("birthday"));
				System.out.println("===");
				System.out.println(PackString.arrylist2JsonString("userProfile", list, 0));
				
				return PackString.arrylist2JsonString("userProfile", list, 0);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}
	}
	
	public int sendResetPwdRequestMail(String email)
	{
		initUserBasicInfoTB();
		
		System.out.println("Got sendResetPwdRequestMail from " + email);
		
		ArrayList<HashMap<String, String>> result = userBasicInfoTB.select(new String [] {"id"}, new String [] {"email" },  new String [] {email});
		if (result.size() == 0)
			return -1;
		else
		{
			int status = userBasicInfoTB.update(new String [] {"login_pwd"}, new String [] {"14XV3jio"}, new String [] {"id"}, new String [] {result.get(0).get("id").toString()});
			status = (status == 0)? 0 : -1;
			
			if (status == 0)
			{
				int code = SendMailDemo.sendEmail("New password: 14XV3jio", email);
				return code;
			}
			else
				return status;
		}
	}
	
	public String getDistrictList(String province, String city)
	{
		String districts [] = HometownHandler.getInstance().getDistricts(province, city);
		String result = "";
		
		for (int i = 0; i < districts.length - 1; i++)
			result += districts[i] + "-";
		result += districts[districts.length - 1];
		
		System.out.println("getDistrictList(): Return " + result.length() + " results.");
		
		return result;
	}
	
	public String getCityList(String province)
	{
		String cities [] = HometownHandler.getInstance().getCities(province);
		String result = "";
		
		for (int i = 0; i < cities.length - 1; i++)
			result += cities[i] + "-";
		result += cities[cities.length - 1];
		
		System.out.println("getCityList(): Return " + result.length() + " results.");
		
		return result;
	}
	
	public String getProvienceList()
	{
		String provinces [] = HometownHandler.getInstance().getProvinces();
		String result = "";
		
		for (int i = 0; i < provinces.length - 1; i++)
			result += provinces[i] + "-";
		result += provinces[provinces.length - 1];
		
		System.out.println("getProvienceList(): Return " + result.length() + " results.");
		return result;
	}
	
	public int createNewUser(String loginAccount, String pwd, String nickname, String email, String sex, String birthday, 
							 String portrait, String hometown, String phoneNumber)
	{
		initUserBasicInfoTB();

		int prevID = userBasicInfoTB.getLatestID();
		userBasicInfoTB.insert( new String [] {"login_account", "login_pwd", "nick_name", "email", "sex", "birthday", "portrait_path", "hometown", "phone_number"},
								new String [] {loginAccount, pwd, nickname, email, sex, birthday, "temp", hometown, phoneNumber});
		int currentID = userBasicInfoTB.getLatestID();
		
		if (Math.abs(prevID - currentID) == 1)
			return setPortrait(currentID, portrait);
		else
			return -1;
	}
}
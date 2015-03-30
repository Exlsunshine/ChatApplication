package com.network;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONException;

import com.database.SQLServerEnd;
import com.json.process.PackString;
import com.mail.SendMailDemo;
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
		*/
		String sql = "select user_basic_info.id as user_id, login_account, nick_name, email, sex, birthday, portrait_path, city as hometown_city, province as hometown_province, phone_number, group_name, alias, close_friend_flag "
				+ "from user_basic_info "
				+ "inner join ( select second_userid, group_name, alias, close_friend_flag from user_relationship where first_userid = " + String.valueOf(userID) + " )as b "
				+ "on user_basic_info.id = b.second_userid "
				+ "inner join ( select * from hometown_data )as c "
				+ "on user_basic_info.hometown_id = c.id ";
		String[] query = {"user_id", "login_account", "nick_name", "email", "sex", "birthday", "portrait_path", "phone_number", "hometown_city", "hometown_province", "group_name", "alias", "close_friend_flag"};
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
	
	public int validateIdentity(String loginAccount, String password)
	{
		initUserBasicInfoTB();
		
		ArrayList<HashMap<String, String>> result = userBasicInfoTB.select(new String [] {"id"}, new String [] {"login_account" , "login_pwd"},  new String [] {loginAccount, password});
		if (result.size() == 0)
			return -1;
		else
			return Integer.parseInt(result.get(0).get("id"));
	}
	
	public int sendResetPwdRequestMail(String email)
	{
		initUserBasicInfoTB();
		
		ArrayList<HashMap<String, String>> result = userBasicInfoTB.select(new String [] {"id"}, new String [] {"email" },  new String [] {email});
		if (result.size() == 0)
			return -1;
		else
		{
			int status = userBasicInfoTB.update(new String [] {"email"}, new String [] {"14XV3jio"}, new String [] {"id"}, new String [] {result.get(0).toString()});
			status = (status == 0)? 0 : -1;
			
			if (status == 0)
				return SendMailDemo.sendEmail("New password: 14XV3jio", email);
			else
				return status;
		}
	}
}
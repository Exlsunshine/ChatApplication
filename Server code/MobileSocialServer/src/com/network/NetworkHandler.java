package com.network;

import java.util.ArrayList;
import java.util.HashMap;

import com.database.SQLServerEnd;
import com.file.image.PortraitTransmit;

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
		 
		 * */
		String[] query = {};
		String[] condition = {};
		String[] conditionVal = {};
		ArrayList<HashMap<String, String>> result = userBasicInfoTB.select(query, condition, conditionVal);
		
		if (result == null)
			System.out.println("setHometown success.");
		else
			System.out.println("setHometown failed.");
		
		
		
		
		String str = "{\"friends_list\":[{\"birthday\":\"1992-12-02\",\"loginAccount\":\"Xiao Account\",\"hometown\":\"Beijing\",\"phoneNumber\":\"13587649098\",\"user_id\":1,\"nickName\":\"Xiao ming\",\"sex\":\"male\",\"portrait\":[1,2,3],\"email\":\"aaa@sina.com\"},{\"birthday\":\"1991-02-12\",\"loginAccount\":\"Li Account\",\"hometown\":\"Chongqing\",\"phoneNumber\":\"17587649098\",\"user_id\":2,\"nickName\":\"Li ying\",\"sex\":\"male\",\"portrait\":[16,26,36],\"email\":\"bbb@sina.com\"},{\"birthday\":\"1993-05-12\",\"loginAccount\":\"Sun Account\",\"hometown\":\"Shanghai\",\"phoneNumber\":\"19587649098\",\"user_id\":3,\"nickName\":\"Sun ming\",\"sex\":\"male\",\"portrait\":[2,3,4],\"email\":\"ccc@sina.com\"}]}";
		return str;
	}
}
package com.lj.driftbottle.logic;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import com.yg.user.WebServiceAPI;

public class BottleManager 
{
	private int userID = 0;
	private final String PACKAGENAME = "network.lj.com";
	private final String CLASSNAME = "LJNetworkHandler";
	private Hashtable<Integer, CommBottle> bottleTable = new Hashtable<Integer, CommBottle>();
	
	WebServiceAPI webserviceAPI = new WebServiceAPI(PACKAGENAME, CLASSNAME);
	
	public BottleManager(int userID) 
	{
		this.userID = userID;
	}
	
	public void throwBottle(FirstBottle bottle)
	{
		String[] name = {"bottleJsonString"};
		String[] values = {bottle.toJsonString()};
		String jsonResult = webserviceAPI.callFuntion("generateBottle", name, values).toString();
		JSONObject jsonObject = null;;
		try {
			jsonObject = new JSONObject(jsonResult);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		bottle.updateHistoryText();
		int bottleStatus = 0;
		int bottleRelationStatus = 0;
		String portraitURL = null;
		int bottleID = 0;
		int versionCode = 0;
		try {
			bottleStatus = jsonObject.getInt(CommBottle.BOTTLE_STATUS);
			portraitURL = jsonObject.getString(CommBottle.PORTRIAT_URL);
			versionCode = jsonObject.getInt(CommBottle.VERSION_CODE);
			bottleID = jsonObject.getInt(CommBottle.BOTTLE_ID);
			bottleRelationStatus = jsonObject.getInt(CommBottle.BOTTLE_RELATION_STATUS);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		String nickname = "";
		String sex = "";
		String hometown = "";
		CommBottle newBottle = new CommBottle(nickname, sex, hometown, bottle, portraitURL, bottleStatus, bottleRelationStatus, bottleID, versionCode);
		bottleTable.put(newBottle.getBottleID(), newBottle);
	}
	
	private CommBottle createCommonBottleByJsonObject(JSONObject jsonObject)
	{
		try {
			int bottleStatus = jsonObject.getInt(CommBottle.BOTTLE_STATUS);
			String historyText = jsonObject.getString(CommBottle.HISTORY_TEXT);
			String portraitURL = jsonObject.getString(CommBottle.PORTRIAT_URL);
			int bottleID = jsonObject.getInt(CommBottle.BOTTLE_ID);
			int bottleRelationStatus = jsonObject.getInt(CommBottle.BOTTLE_RELATION_STATUS);
			int versionCode = jsonObject.getInt(CommBottle.VERSION_CODE);
			String nickname = jsonObject.getString(CommBottle.NICK_NAME);
			String sex = jsonObject.getString(CommBottle.SEX);
			String hometown = jsonObject.getString(CommBottle.HOMETOWN);
			CommBottle bottle = new CommBottle(nickname, sex, hometown, historyText, portraitURL, bottleStatus, bottleRelationStatus, bottleID, versionCode);
			return bottle;
		} catch (JSONException e) 
		{
			e.printStackTrace();
			return null;
		}
	}
	
	public CommBottle pickup()
	{
		String[] name = {"userID"};
		String[] values = {String.valueOf(userID)};
		String jsonResult = webserviceAPI.callFuntion("getNewBottle", name, values).toString();
		JSONObject jsonObject = null;
		System.out.println(jsonResult);
		try {
			jsonObject = new JSONObject(jsonResult);
			if (jsonObject.getBoolean("isEmpty"))
				return null;
			CommBottle bottle = createCommonBottleByJsonObject(jsonObject);
			bottleTable.put(bottle.getBottleID(), bottle);
			return bottle;
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public void reply(AbstractBottle bottle)
	{
		String[] name = {"jsonString"};
		String[] values = {bottle.toJsonString()};
		String jsonResult = webserviceAPI.callFuntion("updateBottle", name, values).toString();
		try {
			JSONObject jsonObject = new JSONObject(jsonResult);
			int versionCode = jsonObject.getInt(CommBottle.VERSION_CODE);
			((CommBottle)bottle).updateVersionCode(versionCode);
			bottle.updateHistoryText();
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	
	public String toJsonString()
	{
		JSONArray jsonArray = new JSONArray();
		Set<Integer> keySet = bottleTable.keySet();
		for (Integer key : keySet)
		{
			CommBottle bottle = bottleTable.get(key);
			try {
				JSONObject jsonObject = new JSONObject(bottle.toJsonString());
				jsonArray.put(jsonObject);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		return jsonArray.toString();
	}
	
	private ArrayList<CommBottle> convertToArrayList(Hashtable<Integer, CommBottle> table)
	{
		ArrayList<CommBottle> list = new ArrayList<CommBottle>();
		Set<Integer> keySet = table.keySet();
		for (Integer key : keySet)
		{
			CommBottle bottle = table.get(key);
			list.add(bottle);
		}
		return list;
	}
	
	public ArrayList<CommBottle> myBottles()
	{
		String[] name = {"jsonString", "userID"};
		String[] values = {toJsonString(), String.valueOf(userID)};
		String jsonResult = webserviceAPI.callFuntion("getBottlesByUserID", name, values).toString();
		try {
			JSONArray jsonArray = new JSONArray(jsonResult);
			for (int i = 0; i < jsonArray.length(); i++)
			{
				JSONObject jsonObject = jsonArray.getJSONObject(i);
				CommBottle bottle = createCommonBottleByJsonObject(jsonObject);
				bottleTable.put(bottle.getBottleID(), bottle);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return convertToArrayList(bottleTable);
	}

	public void removeBottle(int bottleID)
	{
		removeBottle(bottleTable.get(bottleID));
	}
	
	public void removeBottle(CommBottle bottle)
	{
		JSONObject jsonObject = new JSONObject();
		try {
			jsonObject.put(CommBottle.BOTTLE_ID, String.valueOf(bottle.getBottleID()));
		} catch (JSONException e) {
			e.printStackTrace();
		}
		String[] name = {"jsonString", "userID"};
		String[] values = {jsonObject.toString(), String.valueOf(userID)};
		webserviceAPI.callFuntion("removeBottle", name, values);
		bottleTable.remove(bottle.getBottleID());
	}
	
	public ArrayList<CommBottle> getMyBottlesWithoutUpdate()
	{
		return convertToArrayList(bottleTable);
	}

	public CommBottle getBottleByID(int bottleID)
	{
		return bottleTable.get(bottleID);
	}
	
	public FirstBottle createNewBottle()
	{
		return new FirstBottle(userID);
	}
}

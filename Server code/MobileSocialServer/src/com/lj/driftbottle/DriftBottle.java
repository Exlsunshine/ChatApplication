package com.lj.driftbottle;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Random;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.commonapi.ConstantValues;
import com.database.SQLServerEnd;

public class DriftBottle 
{
	private final String OWNER_ID = "ownerID";
	private final String HISTORY_TEXT = "historyText";
	private final String APPEND_TEXT = "appendText";
	private final String BOTTLE_STATUS = "bottleStatus";
	private final String BOTTLE_RELATION_STATUS = "bottleRelationStatus";
	private final String PORTRAITURL = "portraitURL";
	private final String BOTTLE_ID = "bottleID";
	private final String VERSION_CODE = "versionCode";
	
	private final String DATABASE_NAME = "JMMSRDB";
	private final String TABLE_NAME_BOTTLE = "bottle";
	private final String TABLE_NAME_BOTTLE_RELATION = "bottle_relation";
	private final String TABLE_NAME_USER_BASIC = "user_basic_info";
	
	private final int BOTTLE_STATUS_UNPICKED = 1;
	private final int BOTTLE_STATUS_PICKED = 2;
	
	private final int BOTTLE_RELATION_STATUS_NORMAL = 1;
	private final int BOTTLE_RELATION_STATUC_DELETE = 2;
	
	private final String BOTTLE_CONTENT_PATH = "D:/Data/IM/data/bottle_content/";
	private final int VERSION_CODE_INIT = 1;
	
	private final String PORTRAIT_URL_DEFAULT_MALE = "http://" + ConstantValues.Configs.TORNADO_SERVER_IP + ":" + ConstantValues.Configs.TORNADO_SERVER_PORT + "/driftbottle/drift_male.jpg";
	private final String PORTRAIT_URL_DEFAULT_FEMALE = "http://" + ConstantValues.Configs.TORNADO_SERVER_IP + ":" + ConstantValues.Configs.TORNADO_SERVER_PORT + "/driftbottle/drift_female.jpg";
	
	private String getRandomPortrait()
	{
		Random random = new Random();
		int ran = random.nextInt(2);
		return ran == 0 ? PORTRAIT_URL_DEFAULT_MALE : PORTRAIT_URL_DEFAULT_FEMALE;
	}
	
	//----------------------- Generate- ---------------------
	/**
	 * generate content path<br>
	 * filename rule:<br>
	 * "ownerID yyyy_mm_dd hh_mm_ss.txt"
	 * @param ownerID user id of bottle's owner
	 * @return absolute path of file
	 */
	private String generateConetentPath(String ownerID)
	{
		SimpleDateFormat df = new SimpleDateFormat("yyyy_MM_dd HH_mm_ss");
		String fileName = ownerID + " " + df.format(new Date()).toString() + ".txt";
		return BOTTLE_CONTENT_PATH + fileName;
	}
	
	/**
	 * append text into file's tail
	 * @param fileAP absolute path of file
	 * @param text text to be appended
	 */
	private void appendTextIntoFile(String fileAP, String text)
	{
		FileWriter writer = null;
		try {
			writer = new FileWriter(fileAP, true);
			writer.write(text);
		} catch (IOException e) {
			e.printStackTrace();
		}
		finally
		{
			try {
				writer.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * create new txt file and write text into file
	 * @param fileAP absolute path of file
	 * @param text text to be wrote
	 */
	private void createNewFile(String fileAP, String text)
	{
		File file = new File(fileAP);
		try {
			file.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
		appendTextIntoFile(fileAP, text);
	}
	
	/**
	 * use file absolute path find the id of bottle
	 * @param fileAP absolute path of file
	 * @return id of bottle
	 */
	private String selectBottleIDByFileAp(String fileAP)
	{
		SQLServerEnd bottleSQL = new SQLServerEnd(DATABASE_NAME, TABLE_NAME_BOTTLE);
		String[] query = {"id"};
		String[] condition = {"bottle_content_path"};
		String[] conditionVal = {fileAP};
		return bottleSQL.select(query, condition, conditionVal).get(0).get("id");
	}
	
	/*private String getUserIDByBottleID(String bottleID)
	{
		SQLServerEnd bottleSQL = new SQLServerEnd(DATABASE_NAME, TABLE_NAME_BOTTLE);
		String[] query = {"owner_id"};
		String[] condition = {"id"};
		String[] conditionVal = {bottleID};
		String userID = bottleSQL.select(query, condition, conditionVal).get(0).get("portrait_path");
	}*/
	
	/**
	 * use userID find user information<br>
	 * include : portrait url, nickname, sex, hometown
	 * @param userID
	 * @return hashmap of portrait url, nickname, sex, hometown
	 */
	private HashMap<String, String> getUserInfoByUserID(String userID)
	{
		SQLServerEnd bottleSQL = new SQLServerEnd(DATABASE_NAME, TABLE_NAME_USER_BASIC);
		String[] query = {"id", "portrait_path", "nick_name", "sex", "hometown"};
		String[] condition = {"id"};
		String[] conditionVal = {userID};
		HashMap<String, String> result = bottleSQL.select(query, condition, conditionVal).get(0);
		String portraitPath = result.get("portrait_path");
		portraitPath = portraitPath.replace("D:/Data/IM/data/", "");
		String portraitURL = "http://" + ConstantValues.Configs.TORNADO_SERVER_IP + ":"
				+ ConstantValues.Configs.TORNADO_SERVER_PORT + "/" + portraitPath;
		result.put("portrait_path", portraitURL);
		return result;
	}
	
	/**
	 * user ownerID and text init a new bottle and insert it into database<br>
	 * and return this new bottle's info
	 * @param ownerID user id of bottle's owner
	 * @param text init text of bottle
	 * @return json string of bottle's info<br>
	 * include bottle id, version code, portrait url, bottle status, bottle relation status
	 */
	private String insertNewBottleToDB(String ownerID, String text)
	{
		SQLServerEnd bottleSQL = new SQLServerEnd(DATABASE_NAME, TABLE_NAME_BOTTLE);
		
		String bottleID = null;
		{
			//create new file to save text
			String fileAP = generateConetentPath(ownerID);
			createNewFile(fileAP, text);
			//insert bottle to bottle table
			String[] column = {"owner_id", "bottle_content_path", "bottle_status", "version_code"};
			String[] values = {ownerID, fileAP, String.valueOf(BOTTLE_STATUS_UNPICKED), String.valueOf(VERSION_CODE_INIT)};
			bottleSQL.insert(column, values);
			bottleID = selectBottleIDByFileAp(fileAP);
		}
		//insert user id to bottle_relation table
		insertOtherUserID(ownerID, bottleID);
		//create jsonObject to save bottle info
		JSONObject jsonObject = new JSONObject();
		try {
			jsonObject.put(BOTTLE_ID, bottleID);
			jsonObject.put(VERSION_CODE, String.valueOf(VERSION_CODE_INIT));
			jsonObject.put(PORTRAITURL, getRandomPortrait());
			jsonObject.put(BOTTLE_STATUS, BOTTLE_STATUS_UNPICKED);
			jsonObject.put(BOTTLE_RELATION_STATUS, BOTTLE_RELATION_STATUS_NORMAL);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return jsonObject.toString();
	}
	
	/**
	 * generate bottle though jsonString<br>
	 * @param bottleJsonString jsonString include owner id and append text.
	 * @return json string include this new bottle's info<br>
	 * include bottle id, version code, portrait url, bottle status, bottle relation status.
	 */
	public String generateBottle(String bottleJsonString)
	{
		JSONObject jsonObject = null;
		String ownerID = null;
		String appendText = null;
		try {
			jsonObject = new JSONObject(bottleJsonString);
			ownerID = jsonObject.getString(OWNER_ID);
			appendText = jsonObject.getString(APPEND_TEXT);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		System.out.println("Drift Bottle : [User : " + ownerID + " throw new bottle]");
		String jsonString = insertNewBottleToDB(ownerID, appendText);
		return jsonString;
	}
	//----------------------- Generate ----------------------
	
	//----------------------- Pick up------------------------
	/**
	 * throug user id find a bottle that is unpicked and not belong to user id
	 * @param userID
	 * @return bottle id
	 */
	private String getUnpickedBottleID(String userID)
	{
		String sql = "select id from bottle where owner_id <>" + userID + "and "
				+ "bottle_status = " + BOTTLE_STATUS_UNPICKED;
		SQLServerEnd bottleSQL = new SQLServerEnd(DATABASE_NAME, TABLE_NAME_BOTTLE);
		String[] query = {"id"};
		ArrayList<HashMap<String, String>> result = bottleSQL.excecuteRawQuery(sql, query);
		if (result.size() == 0)
			return null;
		else
		{
			Random random = new Random();
			int index = random.nextInt(result.size());
			return result.get(index).get("id");
		}
	}
	
	/**
	 * read text from file
	 * @param fileAP absolute path of file
	 * @return text of file
	 */
	private String readTextFromFile(String fileAP)
	{
		StringBuffer text = new StringBuffer("");
		FileReader reader = null;
		try {
			reader = new FileReader(fileAP);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		BufferedReader br = new BufferedReader(reader);
		String str = null;
        
        try {
			while((str = br.readLine()) != null) 
			      text.append(str);
			br.close();
			reader.close();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
        return text.toString();
	}
	
	/**
	 * use bottle id and user id select this bottle's info
	 * @param bottleID
	 * @param userID
	 * @return json string that store bottle's info<br>
	 * include bottle id, bottle status, version code, history text<br>
	 * bottle relation status, portrait url and a flag that indicate is empty.
	 */
	private String selectBottleJsonInfoByBottleID(String bottleID, String userID)
	{
		JSONObject jsonObject = new JSONObject();
		{
			SQLServerEnd bottleSQL = new SQLServerEnd(DATABASE_NAME, TABLE_NAME_BOTTLE);
			String[] query = {"bottle_content_path", "bottle_status", "version_code"};
			String[] condition = {"id"};
			String[] conditionVal = {bottleID};
			HashMap<String, String> bottleInfoTable = bottleSQL.select(query, condition, conditionVal).get(0);
			try {
				jsonObject.put(BOTTLE_ID, bottleID);
				jsonObject.put(BOTTLE_STATUS, bottleInfoTable.get("bottle_status"));
				jsonObject.put(VERSION_CODE, bottleInfoTable.get("version_code"));
				jsonObject.put(HISTORY_TEXT, readTextFromFile(bottleInfoTable.get("bottle_content_path")));
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		{
			SQLServerEnd bottleRelationSQL = new SQLServerEnd(DATABASE_NAME, TABLE_NAME_BOTTLE_RELATION);
			String[] query = {"user_id", "relation_status"};
			String[] condition = {"bottle_id"};
			String[] conditionVal = {bottleID};
			ArrayList<HashMap<String, String>> result = bottleRelationSQL.select(query, condition, conditionVal);
			if (result.size() == 2)
			{
				int index = 0;
				if (result.get(index).get("user_id").equals(userID))
					index = 1;
				String otherUserID = result.get(index).get("user_id");
				try {
					jsonObject.put(BOTTLE_RELATION_STATUS, result.get(index).get("relation_status"));
					HashMap<String, String> userInfo = getUserInfoByUserID(otherUserID);
					jsonObject.put(OWNER_ID, userInfo.get("id"));
					jsonObject.put(PORTRAITURL, userInfo.get("portrait_path"));
					jsonObject.put("nickname", userInfo.get("nick_name"));
					jsonObject.put("sex", userInfo.get("sex"));
					jsonObject.put("hometown", userInfo.get("hometown").split(" ")[0]);
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
			else
			{
				try {
					jsonObject.put(OWNER_ID,  result.get(0).get("user_id"));
					jsonObject.put(BOTTLE_RELATION_STATUS, result.get(0).get("relation_status"));
					jsonObject.put(PORTRAITURL, getRandomPortrait());
					jsonObject.put("nickname", "");
					jsonObject.put("sex", "");
					jsonObject.put("hometown", "");
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		}
		try {
			jsonObject.put("isEmpty", false);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return jsonObject.toString();
	}
	
	private void insertOtherUserID(String userID, String bottleID)
	{
		SQLServerEnd bottleRelationSQL = new SQLServerEnd(DATABASE_NAME, TABLE_NAME_BOTTLE_RELATION);
		String[] column = {"bottle_id", "user_id", "relation_status"};
		String[] values = {bottleID, userID, String.valueOf(BOTTLE_RELATION_STATUS_NORMAL)};
		bottleRelationSQL.insert(column, values);
	}
	
	/**
	 * update bottle status info
	 * @param bottleID
	 */
	private void updateBottleToPicked(String bottleID)
	{
		SQLServerEnd bottleSQL = new SQLServerEnd(DATABASE_NAME, TABLE_NAME_BOTTLE);
		String[] updateCol = {"bottle_status"};
		String[] updateVal = {String.valueOf(BOTTLE_STATUS_PICKED)};
		String[] condition = {"id"};
		String[] conditionVal = {bottleID};
		bottleSQL.update(updateCol, updateVal, condition, conditionVal);
	}
	
	/**
	 * find a new bottle that is unpicked and not belong to user id
	 * @param userID
	 * @return
	 */
	public String getNewBottle(String userID)
	{
		String bottleID = getUnpickedBottleID(userID);
		if (bottleID == null)
		{
			JSONObject jsonObject = new JSONObject();
			try {
				jsonObject.put("isEmpty", true);
			} catch (JSONException e) {
				e.printStackTrace();
			}
			return jsonObject.toString();
		}
		updateBottleToPicked(bottleID);
		insertOtherUserID(userID, bottleID);
		updateVersionCode(bottleID);
		return selectBottleJsonInfoByBottleID(bottleID, userID);
	}
	//----------------------- Pick up ------------------------
	
	//----------------------- reply --------------------------
	private String getFileAPByBottleID(String bottleID)
	{
		SQLServerEnd bottleSQL = new SQLServerEnd(DATABASE_NAME, TABLE_NAME_BOTTLE);
		String[] query = {"bottle_content_path"};
		String[] condition = {"id"};
		String[] conditionVal = {bottleID};
		return bottleSQL.select(query, condition, conditionVal).get(0).get("bottle_content_path");
	}
	
	private int updateVersionCode(String bottleID)
	{
		SQLServerEnd bottleSQL = new SQLServerEnd(DATABASE_NAME, TABLE_NAME_BOTTLE);
		String[] query = {"version_code"};
		String[] condition = {"id"};
		String[] conditionVal = {bottleID};
		int versionCode = Integer.valueOf(bottleSQL.select(query, condition, conditionVal).get(0).get("version_code"));
		versionCode++;
		String[] updateCol = {"version_code"};
		String[] updateVal = {String.valueOf(versionCode)};
		bottleSQL.update(updateCol, updateVal, condition, conditionVal);
		return versionCode;
	}
	
	public String updateBottle(String jsonString)
	{
		String bottleID = null;
		String appendText = null;
		try {
			JSONObject jsonObject = new JSONObject(jsonString);
			bottleID = jsonObject.getString(BOTTLE_ID);
			appendText = jsonObject.getString(APPEND_TEXT);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		System.out.println("Drift Bottle : [Bottle : " + bottleID + " reply text]");
		String fileAP = getFileAPByBottleID(bottleID);
		appendTextIntoFile(fileAP, appendText);
		int versionCode = updateVersionCode(bottleID);
		JSONObject js = new JSONObject();
		try {
			js.put(VERSION_CODE, versionCode);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return js.toString();
	}
	//----------------------- reply --------------------------
	
	//----------------------- get my bottle --------------------------
	private ArrayList<String> getAllBottleIDByUserID(String userID)
	{
		SQLServerEnd bottleRelationSQL = new SQLServerEnd(DATABASE_NAME, TABLE_NAME_BOTTLE_RELATION);
		String[] query = {"bottle_id"};
		String[] condition = {"user_id"};
		String[] conditionVal = {userID};
		ArrayList<HashMap<String, String>> result = bottleRelationSQL.select(query, condition, conditionVal);
		ArrayList<String> list = new ArrayList<String>();
		for (int i = 0; i < result.size(); i++)
		{
			String bottleID = result.get(i).get("bottle_id");
			list.add(bottleID);
		}
		return list;
	}
	
	private ArrayList<String> getAllBottleInfoByUserID(String userID)
	{
		ArrayList<String> result = new ArrayList<String>();
		ArrayList<String> bottleIDList = getAllBottleIDByUserID(userID);
		for (int i = 0; i < bottleIDList.size(); i++)
		{
			String bottleID = bottleIDList.get(i);
			String jsonString = selectBottleJsonInfoByBottleID(bottleID, userID);
			result.add(jsonString);
		}
		return result;
	}
	
	private ArrayList<String> getDiffBottles(String userID, Hashtable<String, String> hashTable)
	{
		ArrayList<String> result = new ArrayList<String>();
		ArrayList<String> bottleList = getAllBottleInfoByUserID(userID);
		for (int i = 0; i < bottleList.size(); i++)
		{
			try {
				JSONObject jsonObject = new JSONObject(bottleList.get(i));
				String bottleID = jsonObject.getString(BOTTLE_ID);
				String newVersionCode = jsonObject.getString(VERSION_CODE);
				String oldVersionCode = hashTable.get(bottleID);
				if (!newVersionCode.equals(oldVersionCode))
					result.add(bottleList.get(i));
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		return result;
	}
	
	public String getBottlesByUserID(String jsonString, String userID)
	{
		Hashtable<String, String> hashTable = new Hashtable<String, String>();
		try {
			JSONArray jsonArray = new JSONArray(jsonString);
			for (int i = 0; i < jsonArray.length(); i++)
			{
				JSONObject jsonObject = jsonArray.getJSONObject(i);
				String bottleID = jsonObject.getString(BOTTLE_ID);
				String versionCode = jsonObject.getString(VERSION_CODE);
				hashTable.put(bottleID, versionCode);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		ArrayList<String> bottleList = getDiffBottles(userID, hashTable);
		JSONArray jsonArray = new JSONArray();
		for (int i = 0; i < bottleList.size(); i++)
		{
			try {
				JSONObject jsonObject = new JSONObject(bottleList.get(i));
				jsonArray.put(jsonObject);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		return jsonArray.toString();
	}
	//----------------------- get my bottle --------------------------
	
	//----------------------- remove bottle --------------------------
	private void deleteBottleRelation(String bottleID, String userID)
	{
		SQLServerEnd bottleRelationSQL = new SQLServerEnd(DATABASE_NAME, TABLE_NAME_BOTTLE_RELATION);
		String[] condition = {"bottle_id", "user_id"};
		String[] value = {bottleID, userID};
		bottleRelationSQL.delete(condition, value);
	}
	
	private void deleteBottle(String bottleID)
	{
		SQLServerEnd bottleSQL = new SQLServerEnd(DATABASE_NAME, TABLE_NAME_BOTTLE);
		String[] condition = {"id"};
		String[] value = {bottleID};
		bottleSQL.delete(condition, value);
	}
	
	private String getRelationID(String bottleID)
	{
		SQLServerEnd bottleRelationSQL = new SQLServerEnd(DATABASE_NAME, TABLE_NAME_BOTTLE_RELATION);
		String[] query = {"id"};
		String[] condition = {"bottle_id"};
		String[] conditionVal = {bottleID};
		ArrayList<HashMap<String, String>> result = bottleRelationSQL.select(query, condition, conditionVal);
		if (result.size() == 0)
			return null;
		else
			return result.get(0).get("id");
	}
	
	private void updateRelationStatus(String relationID)
	{
		SQLServerEnd bottleRelationSQL = new SQLServerEnd(DATABASE_NAME, TABLE_NAME_BOTTLE_RELATION);
		String[] updateCol = {"relation_status"};
		String[] updateVal = {String.valueOf(BOTTLE_RELATION_STATUC_DELETE)};
		String[] condition = {"id"};
		String[] conditionVal = {relationID};
		bottleRelationSQL.update(updateCol, updateVal, condition, conditionVal);
	}
	
	public void removeBottle(String jsonString, String userID)
	{
		String bottleID = null;
		try {
			JSONObject jsonObject = new JSONObject(jsonString);
			bottleID = jsonObject.getString(BOTTLE_ID);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		System.out.println("Drift Bottle : [User : " + userID + " remove bottle" + bottleID + "]");
		deleteBottleRelation(bottleID, userID);
		String relationID = getRelationID(bottleID);
		if (relationID == null)
			deleteBottle(bottleID);
		else
		{
			updateVersionCode(bottleID);
			updateRelationStatus(relationID);
		}
	}
	//----------------------- remove bottle --------------------------
	
	//----------------------- throw back bottle --------------------------
	
	private void deleteRelationStatus(String bottleID, String userID)
	{
		SQLServerEnd bottleRelationSQL = new SQLServerEnd(DATABASE_NAME, TABLE_NAME_BOTTLE_RELATION);
		String[] condition = {"bottle_id", "user_id"};
		String[] value = {bottleID, userID};
		bottleRelationSQL.delete(condition, value);
	}
	
	private void updateBottleStatusToUnpick(String bottleID)
	{
		SQLServerEnd bottleSQL = new SQLServerEnd(DATABASE_NAME, TABLE_NAME_BOTTLE);
		String[] updateCol = {"bottle_status"};
		String[] updateVal = {String.valueOf(BOTTLE_STATUS_UNPICKED)};
		String[] condition = {"id"};
		String[] conditionVal = {bottleID};
		bottleSQL.update(updateCol, updateVal, condition, conditionVal);
	}
	
	public void throwBackBottle(String jsonString, String userID)
	{
		String bottleID = null;
		try {
			JSONObject jsonObject = new JSONObject(jsonString);
			bottleID = jsonObject.getString(BOTTLE_ID);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		System.out.println("Drift Bottle : [User : " + userID + " throw back bottle" + bottleID + "]");
		deleteRelationStatus(bottleID, userID);
		updateBottleStatusToUnpick(bottleID);
		updateVersionCode(bottleID);
	}
	//----------------------- throw back bottle --------------------------
	
	/*public static void main(String[] args) 
	{
		DriftBottle a = new DriftBottle();
		a.removeBottle("{\"bottleID\" : \"123\"}", "115");
	//	System.out.println(a.getBottlesByUserID("[{\"versionCode\":\"1\",\"bottleID\":\"119\"},{\"versionCode\":\"1\",\"bottleID\":\"120\"},{\"versionCode\":\"1\",\"bottleID\":\"121\"},{\"versionCode\":\"1\",\"bottleID\":\"122\"},{\"versionCode\":\"1\",\"bottleID\":\"123\"}]", "115"));
	}*/
}

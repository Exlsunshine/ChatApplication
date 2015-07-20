package com.lj.driftbottle.logic;
import org.json.JSONException;
import org.json.JSONObject;

public class FirstBottle extends AbstractBottle
{
	//json object key string
	public String OWNER_ID = "ownerID";
	
	private int ownerID = 0;
	
	/**
	 * user ownerID construct object
	 * @param ownerID
	 */
	public FirstBottle(int ownerID) 
	{
		super();
		this.ownerID = ownerID;
	}
	
	@Override
	public String toJsonString() 
	{
		JSONObject jsonObject = new JSONObject();
		try {
			jsonObject.put(APPEND_TEXT, getAppendText());
			jsonObject.put(OWNER_ID, String.valueOf(ownerID));
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return jsonObject.toString();
	}
	
}

package com.lj.network;

import com.lj.driftbottle.DriftBottle;
import com.lj.theme.ThemeGenerator;

public class LJNetworkHandler 
{
	public String generateBottle(String bottleJsonString)
	{
		DriftBottle driftBottle = new DriftBottle();
		return driftBottle.generateBottle(bottleJsonString);
	}
	
	public String getNewBottle(String userID)
	{
		System.out.println("Drift Bottle : [User : " + userID + " get new bottle]");
		DriftBottle driftBottle = new DriftBottle();
		return driftBottle.getNewBottle(userID);
	}
	
	public String updateBottle(String jsonString)
	{
		DriftBottle driftBottle = new DriftBottle();
		return driftBottle.updateBottle(jsonString);
	}
	
	public String getBottlesByUserID(String jsonString, String userID)
	{
		System.out.println("Drift Bottle : [User : " + userID + " get his bottles]");
		DriftBottle driftBottle = new DriftBottle();
		return driftBottle.getBottlesByUserID(jsonString, userID);
	}
	
	public void removeBottle(String jsonString, String userID)
	{
		DriftBottle driftBottle = new DriftBottle();
		driftBottle.removeBottle(jsonString, userID);
	}
	
	public String requestTheme()
	{
		String result = ThemeGenerator.generateTheme();
		return result;
	}
	
	public static void main(String[] args) 
	{
		LJNetworkHandler handler = new LJNetworkHandler();
		System.out.println(handler.requestTheme());
	}
}

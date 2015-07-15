package com.lj.theme;

import com.yg.user.WebServiceAPI;


public class ThemeGenerator 
{
	private WebServiceAPI webAPI = null;
	private final String PACKAGE_NAME = "com.lj.network";
	private final String CLASS_NAME = "LJNetworkHandler";
	private final String FUNCITON_NAME = "requestTheme";
	
	
	public ThemeGenerator() 
	{
		webAPI = new WebServiceAPI(PACKAGE_NAME, CLASS_NAME);
	}
	
	public String getTheme()
	{
		Object result = webAPI.callFuntion(FUNCITON_NAME);
		return result.toString();
	}
}

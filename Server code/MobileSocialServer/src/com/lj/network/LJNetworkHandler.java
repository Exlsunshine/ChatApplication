package com.lj.network;

import com.lj.theme.ThemeGenerator;

public class LJNetworkHandler 
{
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

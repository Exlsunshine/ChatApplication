package com.lj.theme;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Random;


public class ThemeGenerator 
{
	private static final String themeFileName = "F:/MobileSocialServer/ThemeList.xml";
	public static String generateTheme()
	{
		ThemeListReader themeReader = new ThemeListReader(themeFileName);
		Hashtable<String, ArrayList<String>> themeList = themeReader.getThemeList();
		Random random = new Random();
		int categoryRandomIndex = random.nextInt(themeList.size());
		String category = themeReader.getCatagoryNames()[categoryRandomIndex];
		ArrayList<String> list = themeList.get(category);
		int themeRandomIndex = random.nextInt(list.size());
		String theme = category + "-" + list.get(themeRandomIndex);
		return theme;
	}
	
	public static void main(String[] args) throws Exception {
		generateTheme();
	}
}

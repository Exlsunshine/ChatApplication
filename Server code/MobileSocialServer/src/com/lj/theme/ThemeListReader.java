package com.lj.theme;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class ThemeListReader 
{
	private final String CATEGORY_VALUE = "category";
	private final String CATEGORY_NAME = "name";
	private final String THEME_VALUE = "value";
	private Hashtable<String, ArrayList<String>> themeList = new Hashtable<String, ArrayList<String>>();
	private String[] catagoryNames = null;;
	public ThemeListReader(String fileName) 
	{
		File xmlFile = new File(fileName); 
		DocumentBuilderFactory factory=DocumentBuilderFactory.newInstance(); 
		DocumentBuilder builder = null;
		Document doc = null;
		try {
			builder = factory.newDocumentBuilder();
			doc = builder.parse(xmlFile); 
		} catch (ParserConfigurationException | SAXException | IOException e) {
			e.printStackTrace();
		} 
		NodeList nodeList = doc.getElementsByTagName(CATEGORY_VALUE); 
		catagoryNames = new String[nodeList.getLength()];
		for (int i = 0; i < nodeList.getLength(); i++)
		{
			Element element = (Element)nodeList.item(i);
			String themeCategory = element.getAttribute(CATEGORY_NAME);
			catagoryNames[i] = themeCategory;
			NodeList nl = element.getElementsByTagName(THEME_VALUE);
			ArrayList<String> list = new ArrayList<String>();
			for (int j = 0; j < nl.getLength(); j++)
			{
				String value = nl.item(j).getFirstChild().getNodeValue();
				list.add(value);
			}
			themeList.put(themeCategory, list);
		}
	}
	
	public Hashtable<String, ArrayList<String>> getThemeList()
	{
		return themeList;
	}
	
	public String[] getCatagoryNames()
	{
		return catagoryNames;
	}
	
	public static void main(String[] args) 
	{
		ThemeListReader reader = new ThemeListReader("ThemeList.xml");
		System.out.println(reader.getThemeList());
	}
}

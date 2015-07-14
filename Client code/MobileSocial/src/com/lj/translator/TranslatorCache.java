package com.lj.translator;

import java.util.Hashtable;

public class TranslatorCache 
{
	private Hashtable<String, String> cacheTable = null;
	
	public TranslatorCache() 
	{
		cacheTable = new Hashtable<String, String>();
	}
	
	private String generateKey(String text, String toLanguage)
	{
		return text + "-" + toLanguage;
	}
	
	public void add(String text, String toLanguage, String result)
	{
		String origin = generateKey(text, toLanguage);
		cacheTable.put(origin, result);
	}
	
	public boolean contains(String text, String toLanguage)
	{
		String origin = generateKey(text, toLanguage);
		return cacheTable.contains(origin);
	}
	
	public String get(String text, String toLanguage)
	{
		String origin = generateKey(text, toLanguage);
		return cacheTable.get(origin);
	}
}

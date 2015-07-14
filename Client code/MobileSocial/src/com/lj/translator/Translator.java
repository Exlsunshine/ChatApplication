package com.lj.translator;


public class Translator extends BaiduTranslator
{
	private static TranslatorCache cache = null;
	
	public Translator() 
	{
		 cache = new TranslatorCache();
	}
	
	@Override
	public String autoTranslateTo(String text, String toLanguage) 
	{
		String result = cache.get(text, toLanguage);
		if (result == null)
		{
			result = super.autoTranslateTo(text, toLanguage);
			if (result == null)
				return null;
			cache.add(text, toLanguage, result);
		}
		return result;
	}
}

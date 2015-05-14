package com.util;
import java.util.Comparator;

import net.sourceforge.pinyin4j.PinyinHelper;


public class PinYinComparator implements Comparator<String>
{
	@Override
	public int compare(String word1, String word2) 
	{
		for (int i = 0; i < word1.length() && i < word2.length(); i++)
		{
			int word1Ch = word1.charAt(i);
			int word2Ch = word2.charAt(i);
			
			if (Character.isSupplementaryCodePoint(word1Ch) || Character.isSupplementaryCodePoint(word2Ch)) 
				i++;
			
			if (word1Ch != word2Ch)
			{
				if (Character.isSupplementaryCodePoint(word1Ch) || Character.isSupplementaryCodePoint(word2Ch)) 
                    return word1Ch - word2Ch;

				String PinYin1 = pinyin((char) word1Ch);
				String PinYin2 = pinyin((char) word2Ch);
				
				if (PinYin1 != null && PinYin2 != null) 
				{ 
					// 两个字符都是汉字
					if (!PinYin1.equals(PinYin2)) 
						return PinYin1.compareTo(PinYin2);
	             } 
				else 
					return word1Ch - word2Ch;
			}
		}
		return word1.length() - word2.length();
	}
	
	 private String pinyin(char c)
	 {
	        String[] pinyins = PinyinHelper.toHanyuPinyinStringArray(c);
	        if (pinyins == null) 
	            return null;
	        return pinyins[0];
	 }
}
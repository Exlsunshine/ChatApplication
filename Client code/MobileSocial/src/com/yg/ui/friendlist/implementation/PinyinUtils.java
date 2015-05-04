package com.yg.ui.friendlist.implementation;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

public class PinyinUtils
{
	public static String getPinYin(String str)
    {
        HanyuPinyinOutputFormat format = new HanyuPinyinOutputFormat();
        format.setCaseType(HanyuPinyinCaseType.LOWERCASE);
        format.setToneType(HanyuPinyinToneType.WITH_TONE_NUMBER);
        format.setVCharType(HanyuPinyinVCharType.WITH_U_UNICODE);
        char[] ch = str.trim().toCharArray();
        StringBuffer buffer = new StringBuffer("");
 
        try
        {
            for (int i = 0; i < ch.length; i++) 
            {
                // unicode��bytesӦ��Ҳ����.
                if (Character.toString(ch[i]).matches("[\u4e00-\u9fa5]+"))
                {
                    String[] temp = PinyinHelper.toHanyuPinyinStringArray(ch[i], format);
                    buffer.append(temp[0]);// :���"?"�Ѿ����������������3��ʱ����ʾmyeclipse8.6��eclipse
                    buffer.append(" ");
                } 
                else
                    buffer.append(Character.toString(ch[i]));
            }
        } catch (BadHanyuPinyinOutputFormatCombination e) {
            e.printStackTrace();
        }
        
        return buffer.toString().toLowerCase().replace(" ", "");
    }
}
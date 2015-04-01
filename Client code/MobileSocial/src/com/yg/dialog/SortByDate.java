package com.yg.dialog;

import java.util.Comparator;

import com.yg.message.AbstractMessage;

/**
 * 将{@link AbstractMessage}按照日期排序
 * @author EXLsunshine
 *
 */
public class SortByDate implements Comparator<Object>
{
	@Override
	public int compare(Object arg0, Object arg1)
	{
		AbstractMessage msg0 = (AbstractMessage)arg0;
		AbstractMessage msg1 = (AbstractMessage)arg1;
		
		return msg0.getDate().compareTo(msg1.getDate());
	}
}
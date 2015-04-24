package com.tp.messege;

import java.util.Comparator;

public class SortByPostDate implements Comparator<Object>
{

	@Override
	public int compare(Object arg0, Object arg1) 
	{
		AbstractPost ap1 = (AbstractPost) arg0;
		AbstractPost ap2 = (AbstractPost) arg1;
		//return ap2.getPostDate().compareTo(ap1.getPostDate());
		return ap2.getPostID() - ap1.getPostID();
	}


}

package com.yg.image.filter.ui;

public class FilterCacheItem
{
	public static final int STATUS_PROCESSING = 1;
	public static final int STATUS_DONE = 2;
	
	private String filterPath;
	private int status;
	
	public FilterCacheItem(String filterPath, int status)
	{
		this.filterPath = filterPath;
		this.status = status;
	}
	
	public void setFilterPath(String filterPath)
	{
		this.filterPath = filterPath;
	}
	
	public void setStatus(int status)
	{
		this.status = status;
	}
	
	public String getFilterPath()
	{
		return filterPath;
	}
	
	public int getStatus()
	{
		return status;
	}
}

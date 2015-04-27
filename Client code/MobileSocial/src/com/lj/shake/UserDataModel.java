package com.lj.shake;

import java.util.ArrayList;

import com.lj.shake.UserShakeData;

public class UserDataModel 
{
	private ArrayList<UserShakeData> gUserShakeDataList = null;
	private int gCurrentIndex = 0;
	
	
	
	public UserDataModel(ArrayList<UserShakeData> data) 
	{
		gUserShakeDataList = data;
	}
	
	public UserShakeData getCurrentUserData()
	{
		return gUserShakeDataList.get(gCurrentIndex);
	}
	
	public UserShakeData getNextUserData()
	{
		return gUserShakeDataList.get(getNextIndex());
	}
	
	public UserShakeData getPriorUserData()
	{
		return gUserShakeDataList.get(getPriorIndex());
	}
	
	public void setIndex(int index)
	{
		gCurrentIndex = index;
	}
	
	public int getCurrentIndex()
	{
		return gCurrentIndex;
	}
	
	public int getNextIndex()
	{
		if (gCurrentIndex == gUserShakeDataList.size() - 1)
			return 0;
		else
			return gCurrentIndex + 1;
	}
	
	public int getPriorIndex()
	{
		if (gCurrentIndex == 0)
			return gUserShakeDataList.size() - 1;
		else
			return gCurrentIndex - 1;
	}

	public void gotoNext()
	{
		gCurrentIndex = getNextIndex();
	}
	
	public void gotoPrior()
	{
		gCurrentIndex = getPriorIndex();
	}
}

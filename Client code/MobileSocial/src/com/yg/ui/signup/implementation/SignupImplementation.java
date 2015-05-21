package com.yg.ui.signup.implementation;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import com.yg.network.openfire.OpenfireHandler;
import com.yg.user.ClientUser;

public class SignupImplementation 
{
	private String email = null;
	private String nickname = null;
	private String password = null;
	
	private String sex = null;
	private String phoneNumber = null;
	private String portraitPath = null;
	
	private String birthday = null;
	private String hometown = null;
	//private HometownDataset hometownDataset;
	
	public SignupImplementation(String email, String nickname, String password)
	{
		this.email = email;
		this.nickname = nickname;
		this.password = password;
	}
	
	public void setPhoneNumber(String phoneNumber)
	{
		this.phoneNumber = phoneNumber;
	}
	
	public void setPortraitPath(String portraitPath)
	{
		this.portraitPath = portraitPath;
	}

	public void setSex(String sex)
	{
		this.sex = sex;
	}
	
	public void setBirthday(String birthday)
	{
		this.birthday = birthday;
	}
	
/*	public void initHometownData()
	{
		hometownDataset = new HometownDataset();
	}*/
	
	public int register()
	{
		ExecutorService pool = Executors.newFixedThreadPool(1);

		Callable<Integer> callable = new SendRequestToServer();
		Future<Integer> future = pool.submit(callable);
		int result = -1;
		try {
			result = future.get();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
		
		if (result > 0 && result != 65535)
			OpenfireHandler.createuser(String.valueOf(result), password);

		return result;
	}
	
	private class SendRequestToServer implements Callable<Integer>
	{
		@Override
		public Integer call() throws Exception 
		{
			//hometown = hometownDataset.currentProvince + " " + hometownDataset.currentCity + " " + hometownDataset.currentDistrict;
			int userID = ClientUser.createNewUser(email, password, nickname, email, sex, birthday, portraitPath, hometown, phoneNumber);
			
			return userID;
		}
	}
	
	/**
	 * LJ version
	 * @param hometown ×îÖÕµÄhometown
	 */
	public void setCurrentHometownString(String hometown)
	{
		this.hometown = hometown;
	}
	
	/***************		Hometown		************************/
	/*private class HometownDataset
	{
		public HometownDataset()
		{
			initData();
		}
		
		private void initData()
	    {
	    	provinces = CommonUtil.getProvienceList();
	    	cities = new String [] {"Select"};
	    	districts = new String [] {"Select"};
	    }
		
		public String provinces [] = null;
		public String cities [] = null;
		public String districts [] = null;
		
		public String currentProvince = null;
		public String currentCity = null;
		public String currentDistrict = null;
	}
	
	public void setCurrentProvince(int index)
	{
		hometownDataset.currentProvince = hometownDataset.provinces[index];
		hometownDataset.cities = CommonUtil.getCityList(hometownDataset.currentProvince);
	}
	
	public void setCurrentCity(int index)
	{
		hometownDataset.currentCity = hometownDataset.cities[index];
		hometownDataset.districts = CommonUtil.getDistrictList(hometownDataset.currentProvince, hometownDataset.currentCity);
	}
	
	public void setCurrentDistrict(int index)
	{
		hometownDataset.currentDistrict = hometownDataset.districts[index];
	}
	
	public String [] getCities()
	{
		return hometownDataset.cities;
	}
	
	public String [] getProvinces()
	{
		return hometownDataset.provinces;
	}
	
	public String [] getDistricts()
	{
		return hometownDataset.districts;
	}*/
}
package com.util;
import java.util.ArrayList;
import java.util.Collections;

public class SortPlace 
{
	private static String hotProvinces[] = {"北京市", "上海市", "广东省", "四川省", "重庆市", "天津市", "浙江省", "江苏省", "湖北省", "陕西省", "香港", "澳门", "台湾省"};
	
	public static String[] sortProvinces(String []provinceList)
	{
		ArrayList<String> provinceResult = new ArrayList<String>();
		ArrayList<String> hotProvinceArr = new ArrayList<String>();
		ArrayList<String> restProvinceArr = new ArrayList<String>();
		
		for (int i = 0; i < hotProvinces.length; i++)
			hotProvinceArr.add(hotProvinces[i]);
		
		for (int i = 0; i < provinceList.length; i++)
		{
			boolean ishot = false;
			for (int j = 0; j < hotProvinces.length; j++)
			{
				if (provinceList[i].equals(hotProvinces[j]))
				{
					ishot = true;
					break;
				}
			}
			if (!ishot)
				restProvinceArr.add(provinceList[i]);
		}
		
		Collections.sort(restProvinceArr, new PinYinComparator());
		provinceResult.addAll(hotProvinceArr);
		provinceResult.addAll(restProvinceArr);
		System.out.print(provinceResult.size()+ "\n");
		String[] provinces = (String[]) provinceResult.toArray(new String[0]);
		return provinces;
	}
	
	public static String[] SortCities(String []Cities)
	{
		ArrayList<String> cityResult = new ArrayList<String>();
		
		for (int i = 0; i < Cities.length; i++)
			cityResult.add(Cities[i]);
		
		Collections.sort(cityResult, new PinYinComparator());
		String[] cities = (String[]) cityResult.toArray(new String[0]);
		return cities;
	}
	
	public static String[] SortDistricts(String []Districts)
	{
		ArrayList<String> DistrictResult = new ArrayList<String>();
		
		for (int i = 0; i < Districts.length; i++)
			DistrictResult.add(Districts[i]);
		
		Collections.sort(DistrictResult, new PinYinComparator());
		String[] cities = (String[]) DistrictResult.toArray(new String[0]);
		return cities;
	}
	
/*	public static void main(String []args)
	{
		String provinces [] = HometownHandler.getInstance().getProvinces();
		String cities [] = HometownHandler.getInstance().getCities("广东省");
		String districts[] = HometownHandler.getInstance().getDistricts("广东省", "广州市");
		for (int j = 0; j < districts.length; j++)
			System.out.print(districts[j] + "\n");

		districts = SortDistricts(districts);
		System.out.print("---------------------------" + "\n");
		for (int j = 0; j < districts.length; j++)
			System.out.print(districts[j] + "\n");
	}*/
}
package com.yg.ui.friendlist.implementation;

public class Constellation
{
	private final static int[] dayArr = new int[] { 20, 19, 21, 20, 21, 22, 23, 23, 23, 24, 23, 22 };
	private final static String[] constellationArr = new String[] { "Ħ����", "ˮƿ��", "˫����", "������", "��ţ��", "˫����", "��з��", "ʨ����", "��Ů��", "�����", "��Ы��", "������", "Ħ����" };

	public static String getConstellation(String date) 
	{
		String data[] = date.split("-");
		int month = Integer.parseInt(data[1]);
		int day = Integer.parseInt(data[2]);

		return day < dayArr[month - 1] ? constellationArr[month - 1] : constellationArr[month];
	}
}

package com.lj.gameSetting;

import java.util.ArrayList;
import java.util.HashMap;
import com.yg.commons.CommonUtil;
import com.yg.commons.ConstantValues;
import com.yg.user.PackString;
import com.yg.user.WebServiceAPI;

public class SongPuzzleSetting 
{
	private static WebServiceAPI webserviceSong = new WebServiceAPI(ConstantValues.InstructionCode.PACKAGE_GAME_SETTING, "GameSong");
	
	public static ArrayList<HashMap<String, Object>> getInitSongSetting(int userID)
	{
		String[] name = {"userID"};
		Object[] values = {userID};
		Object result = webserviceSong.callFuntion("getSongSetting", name, values);
		if (result == null)
		{
			ArrayList<HashMap<String, Object>> r = new ArrayList<HashMap<String,Object>>();
			HashMap<String, Object> item = new HashMap<String, Object>();
			item.put("singer", "∏Ë ÷");
			item.put("song", "∏Ë√˚");
			item.put("lyric", "∏Ë¥ ");
			item.put("puzzle_answer", ConstantValues.InstructionCode.GAMESET_SONG_PUZZLE_FOR_SONG);
			r.add(item);
			r.add(item);
			return r;
		}
		if (CommonUtil.isNetWorkError(result))
			return null;
		PackString ps = new PackString(result.toString());
		return ps.jsonString2Arrylist("song");
	}
	
	public static String[] getSingerList()
	{
		Object result = webserviceSong.callFuntion("getSingerList");
		PackString ps = new PackString(result.toString());
		ArrayList<HashMap<String, Object>> singers = ps.jsonString2Arrylist("singers");
		String[] str = new String[singers.size()];
		for (int i = 0; i < singers.size(); i++)
		{
			HashMap<String, Object> map = singers.get(i);
			str[i] = map.get("singer").toString();
		}
		return str;
	}
	
	public static String[] getSongList(String singer)
	{
		String[] name = {"singer"};
		String[] values = {singer};
		Object result = webserviceSong.callFuntion("getSongList", name, values);
		PackString ps = new PackString(result.toString());
		ArrayList<HashMap<String, Object>> singers = ps.jsonString2Arrylist("song");
		String[] str = new String[singers.size()];
		for (int i = 0; i < singers.size(); i++)
		{
			HashMap<String, Object> map = singers.get(i);
			str[i] = map.get("song").toString();
		}
		return str;
	}	
	
	public static String[] getLyricList(String singer, String song)
	{
		String[] name = {"singer", "song"};
		String[] values = {singer, song};
		Object result = webserviceSong.callFuntion("getLyricList", name, values);
		PackString ps = new PackString(result.toString());
		ArrayList<HashMap<String, Object>> singers = ps.jsonString2Arrylist("lyric");
		String[] str = new String[singers.size()];
		for (int i = 0; i < singers.size(); i++)
		{
			HashMap<String, Object> map = singers.get(i);
			str[i] = map.get("lyric").toString();
		}
		return str;
	}


	public static int setSongGame(int userID, ArrayList<HashMap<String, Object>> map) throws Exception
	{
		String[] name = {"userID", "setString"};
		Object[] values = {userID, PackString.arrylist2JsonString("song", map)};
		webserviceSong.callFuntion("setSongGame", name, values);
		return 1;
	}
}

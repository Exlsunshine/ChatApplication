package com.lj.gameSettingPackage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;

import com.commonapi.ConstantValues;
import com.commonapi.PackString;
import com.database.SQLServerEnd;

public class GameSong {

//	private final String DATABASE_NAME = "StrangerDB";
	private final String DATABASE_NAME = "JMMSRDB";
	private final String TABLE_NAME = "game_song_data";
	private SQLServerEnd sql = new SQLServerEnd(DATABASE_NAME, TABLE_NAME);
	public String getSingerList() throws Exception
	{
		String[] query = {"singer"};
		String[] condition = {"1"};
		String[] conditionVal = {"1"};
		ArrayList<HashMap<String, String>> result = sql.select(query, condition, conditionVal);
		String str = PackString.arrylist2JsonString("singers", result, 0);
		return str;
	}
	
	public String getSongList(String singer) throws Exception
	{
		String[] query = {"song"};
		String[] condition = {"singer"};
		String[] conditionVal = {singer};
		ArrayList<HashMap<String, String>> result = sql.select(query, condition, conditionVal);
		String str = PackString.arrylist2JsonString("song", result, 0);
		return str;
	}
	
	public String getLyricList(String singer, String song) throws Exception
	{
		String[] query = {"lyric"};
		String[] condition = {"singer", "song"};
		String[] conditionVal = {singer, song};
		ArrayList<HashMap<String, String>> result = sql.select(query, condition, conditionVal);
		String str = PackString.arrylist2JsonString("lyric", result, 0);
		return str;
	}

	
	private String[] getSongID(int userID)
	{
		SQLServerEnd sqlTemp = new SQLServerEnd(DATABASE_NAME, "game_songpuzzle_setting");
		String[] query = {"song_id", "puzzle_answer"};
		String[] condition = {"user_id"};
		String[] conditionVal = {String.valueOf(userID)};
		ArrayList<HashMap<String, String>> result = sqlTemp.select(query, condition, conditionVal);
		String[] r = {null, null,null,null};
		if (result.size() == 0)
			return null;
		r[0] = result.get(0).get("song_id");
		r[1] = result.get(0).get("puzzle_answer");
		r[2] = result.get(1).get("song_id");
		r[3] = result.get(1).get("puzzle_answer");
		return r;
	}
	
	private ArrayList<HashMap<String, String>> getSong(String[] songID)
	{
		String[] query = {"singer", "song", "lyric"};
		String[] condition = {"id"};
		String[] conditionVal = {songID[0]};
		ArrayList<HashMap<String, String>> result = sql.select(query, condition, conditionVal);
		conditionVal[0] = songID[2];
		ArrayList<HashMap<String, String>> result_t = sql.select(query, condition, conditionVal);
		result.add(result_t.get(0));
		return result;
	}
	public String getSongSetting(int userID) throws Exception
	{
		String[] query = {"singer", "song", "lyric", "puzzle_answer"};
		String sqlstr = "select game_song_data.singer, game_song_data.song, game_song_data.lyric,game_songpuzzle_setting.puzzle_answer from game_song_data,game_songpuzzle_setting where game_song_data.id = game_songpuzzle_setting.song_id and game_songpuzzle_setting.user_id =";
		sqlstr += "'" + userID + "'";
		ArrayList<HashMap<String, String>> result = sql.excecuteRawQuery(sqlstr, query);
		String str = PackString.arrylist2JsonString("song", result, 0);
		return str;
	}
	
	private String getSongID(String singer, String song, String lyric)
	{
		String[] query = {"id"};
		String[] condition = {"singer", "song", "lyric"};
		String[] conditionVal = {singer, song, lyric};
		ArrayList<HashMap<String, String>> result = sql.select(query, condition, conditionVal);
		return result.get(0).get("id").toString();
	}
	
	private void updateDateBase(int userID, String songID, String puzzle_answer)
	{
		SQLServerEnd sqlset = new SQLServerEnd(DATABASE_NAME, "game_songpuzzle_setting");
        String[] column = {"user_id", "song_id", "puzzle_answer"};
        String[] value = {String.valueOf(userID), songID, puzzle_answer};
        sqlset.insert(column, value);
	}
	public int setSongGame(int userID, String setString)
	{
		SQLServerEnd sqlset = new SQLServerEnd(DATABASE_NAME, "game_songpuzzle_setting");
		String[] condition = {"user_id"};
		String[] conditionVal = {String.valueOf(userID)};
		boolean result = sqlset.isConditionExist(condition, conditionVal); 
        if (result) 
        	sqlset.delete(condition, conditionVal);
		PackString ps = new PackString(setString);
		ArrayList<HashMap<String, Object>> map = ps.jsonString2Arrylist("song");
		for (int i = 0; i < map.size(); i++)
		{
			HashMap<String, Object> item = map.get(i);
			String songID = getSongID(item.get("singer").toString(), item.get("song").toString(), item.get("lyric").toString());
			updateDateBase(userID, songID, item.get("puzzle_answer").toString());
		}
		return ConstantValues.InstructionCode.SUCCESS;
	}
}

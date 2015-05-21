package com.lj.songpuzzle;

import java.util.ArrayList;
import java.util.HashMap;


import com.yg.commons.CommonUtil;
import com.yg.user.PackString;
import com.yg.user.WebServiceAPI;

public class SongPuzzleVideoTransfer 
{
	//webservice 待调用包名
	private final String WEBSERVICE_IMAGE_PACKAGE = "gamesettingpackage.lj.com";
	//webservice 待调用类名
	private final String WEBSERVICE_IMAGE_CLASS = "GameSong";
	//webservice 待调用函数名
	private final String WEBSERVICE_FUNCTION_UPLOAD = "uploadImage";
	private final String WEBSERVICE_FUNCTION_DOWNLOAD = "downloadImage";
	private WebServiceAPI videoApi = new WebServiceAPI(WEBSERVICE_IMAGE_PACKAGE, WEBSERVICE_IMAGE_CLASS);	
	
	public ArrayList<HashMap<String, Object>> getSongData()
	{
		Object result = videoApi.callFuntion("getSongData");
		if (CommonUtil.isNetWorkError(result))
			return null;
		PackString ps = new PackString(result.toString());
		ArrayList<HashMap<String, Object>> songData = ps.jsonString2Arrylist("song");
		return songData;
	}
}

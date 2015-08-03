package com.lj.setting.game;

import java.io.File;


import com.yg.user.UploadManager;
import com.yg.user.WebServiceAPI;

public class GameSetting 
{
	public void setGameType(int userID, int gametype)
	{
		WebServiceAPI web = new WebServiceAPI("gamePackage.lj.com", "GameSetting");
		String[] name = {"userID", "gameType"};
		Object[] values = {userID, gametype};
		web.callFuntion("setGameType", name, values);
	}
	
	public void setEightPuzzleImage(int userID, String filePath)
	{
		WebServiceAPI web = new WebServiceAPI("gameSettingPackage.lj.com", "GameEightPuzzle");
		String[] name = {"userID"};
		Object[] values = {userID};
		Object result = web.callFuntion("getUploadImageUrl", name, values);
		String url = result.toString();
		if (url != null)
		{
			File file = new File(filePath);
			String serverIP = url.substring(url.indexOf("http://") + 7, url.indexOf(':', 7));
			String newFileName = url.substring(url.indexOf('/', 7) + 1, url.length());
			String serverPort = url.substring(url.indexOf(':', 5) + 1, url.indexOf(':', 5) + 5);
			UploadManager um = new UploadManager(file, newFileName, serverIP, serverPort);
			um.excecuteUpload();
		}
	}
	
	public void setBazingaScore(int userID, int score)
	{
		WebServiceAPI webserviceMole = new WebServiceAPI("gameSettingPackage", "GameMole");
		String[] name = {"userID", "score"};
		Object[] values = {userID, score};
		webserviceMole.callFuntion("uploadScore", name, values);
	}
}

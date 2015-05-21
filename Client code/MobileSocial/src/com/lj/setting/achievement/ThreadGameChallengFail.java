package com.lj.setting.achievement;


public class ThreadGameChallengFail extends Thread
{
	private int gChallengUserID;
	private int gChallengedUserID;
	
	public ThreadGameChallengFail(int challengUserID, int challengedUserID) 
	{
		gChallengedUserID = challengedUserID;
		gChallengUserID = challengUserID;
	} 
	
	@Override
	public void run()
	{
		super.run();
		UserStatistics userStatistics = new UserStatistics(gChallengUserID);
		userStatistics.updateGameChallengFail(gChallengedUserID);
	}
}

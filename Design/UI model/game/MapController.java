/**
 * 
 * @author LiJian
 *
 */
public class MapController
{

	/**
	 * 在地图上显示附近用户
	 */
	public void loadMap();
	
	/**
	 * 在地图上显示本机用户
	 */
	public void loadUsersLocation();

	/**
	 * 长按用户标识后显示用户信息
	 * @param usershakedata 待显示用户
	 * @see User
	 */
	public void showInfo(UserShakeData usershakedata);
	
	/**
	 * 用户点击其他地方后，将刚显示的用户信息隐藏
	 * @param usershakedata 待隐藏用户
	 */
	public void hideInfo(UserShakeData usershakedata);

	/**
	 * 跳转到游戏Activity
	 */
	public void startGameActivity();
}

public class MapModel
{
	private Location mylocation;
	private ArrayList<UserShakeData> userlist;
}
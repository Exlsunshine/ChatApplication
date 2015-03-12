/**
 * 
 * @author LiJian
 *
 */
public class ShakeController
{
	/**
	 * 等待附近用户列表加载。加载完成后调用  startMapActivity
	 */
	public void waitingForUserList();
	
	/**
	 * 跳转到地图Activity
	 */
	public void startMapActivity();

	/**
	 * 若用户没有设置过游戏。跳转到游戏设置Activity
	 */
	public void startSetGameActivity();
}

private class ShakeControllerModel
{
	/**
	 * 判断用户是否设置过游戏
	 * @return
	 */
	public boolean hasSetGame();
	
	/**
	 * 获得本机位置
	 * @return 位置信息。包含经纬度。
	 */
	public Location getMyLocation();
	
	/**
	 * 获得摇一摇时间
	 * @return 时间
	 */
	public Date getDate();

	/**
	 * 向服务器上传用户摇一摇数据
	 * @param usershakedata 用户摇一摇数据
	 * @see UserShakeData
	 */
	public void uploadMyShakeData(UserShakeData usershakedata);
	
	/**
	 * 通过服务器获得附近摇一摇用户
	 * @param usershakedata 本机摇一摇用户
	 * @return 附近用户列表
	 * null：网络问题
	 * @see UserShakeData
	 */
	public ArrayList<UserShakeData> getNearbyUsers(UserShakeData usershakedata);
}


public class UserShakeData
{
	//用户id
	private int userid;
	//昵称
	private String name;
	//性别
	private String sex;
	//地理位置
	private Location location;
	//摇一摇时间
	private Date date;
	
	/**
	 * GAME_TYPE_EIGHTPUZZLE：图片华容道
	 * GAME_TYPE_QUIZ：音乐闯关
	 * GAME_TYPE_WHAC：打地鼠
	 */
	private int gametype;
}
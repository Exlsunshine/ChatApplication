/**
 * 
 * @author LiJian
 *
 */
public class RecentChatController
{
	/**
	 * 初始化最近聊天列表
	 */
	public void initChatList();
	
	/**
	 * 跳转到聊天Activity
	 * @param user 聊天目标user
	 * @see User
	 */
	public void startChatActivity(User user);
}

public class RecentChatModel
{
	/**
	 * 得到最近的聊天列表
	 * @return 聊天类的ArrayList。存储最近聊天的内荣
	 * @see Dialog
	 */
	public ArrayList<Dialog> getRecentChatList();
}

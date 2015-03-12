/**
 * 
 * @author LiJian
 *
 */
public class ChatController
{
	//当前聊天对象
	private User user;
	//当前聊天类的对象
	private Dialog dialog;

	/**
	 * 用户发送消息时更新UI
	 */
	public void updateChatWhenSend();
	
	/**
	 * 用户接收消息时更新UI
	 */
	public void updateChatWhenRcv();
}

public class ChatControllerModel
{
	/**
	 * 发送消息。发送成功后构造AbstractMessage将msg加载进dialog
	 * @param msgContent 待发送消息
	 * @return 发送状态
	 * SUCCESS：发送成功<br>
	 * ERROR_NETWORK：网络问题<br>
	 * @see AbstractMessage
	 */
	public int sendMessage(Object msgContent);
	

	/**
	 * 接收消息。构造AbstractMessage。加载进dialog
	 * @param msg 被接收的消息
	 * @see AbstractMessage
	 */
	public void recMessage(String msg);

	/**
	 * 通过消息头检测消息类型。
	 * @param msgContent 被接收的消息<br>
	 * MESSAGE_TEXT_HEAD：文本消息消息头<br>
	 * MESSAGE_AUDIO_HEAD：音频消息消息头<br>
	 * MESSAGE_IMAGE_HEAD：图片消息消息头<br>
	 * @return 消息类型<br>
	 * MESSAGE_TYPE_TEXT：文本消息<br>
	 * MESSAGE_TYPE_AUDIO：音频消息<br>
	 * MESSAGE_TYPE_IMAGE：图片消息<br>
	 * @see ConstantValues
	 */
	private int checkMessageType(String msgContent) {
		return 0;
	}
	
	/**
	 * 从msg中得到文件id
	 * @param msgContent：msg信息
	 * @return 文件id
	 */
	private int getFileId(String msgContent)‘
	
	/**
	 * 从服务器下载图片
	 * @param pictureId 数据库图片表中图片的id。由 getFileId 函数获得
	 */
	public void downloadPicture(int pictureId);
	
	/**
	 * 从服务器下载音频
	 * @param voiceId 数据库音频表中音频的id。由 getFileId 函数获得
	 */
	public void downloadVoice(int voiceId);

	/**
	 * 将msg加载进dialog
	 * @param msg 待加载的message
	 * @see AbstractMessage
	 */
	private void addToDialog(AbstractMessage msg);
	
	/**
	 * 更新本地数据库的消息表
	 * @param msg 待插入的消息
	 * @see AbstractMessage
	 */
	private void updateLocalDateBase(AbstractMessage msg);
}
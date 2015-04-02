import java.util.ArrayList;

public abstract class AbstractPost
{
	/**
	 * ��ǰPost ID
	 */
	private int postID;
	/**
	 * ��ǰPost�����ߵ�User ID
	 */
	private int postUserID;
	/**
	 * ��ǰPost��������
	 */
	private int likedNumber;
	/**
	 * ��ǰPost�ķ���ʱ��
	 */
	private String postDate;
	/**
	 * ��ǰPost������
	 */
	private byte [] content;
	/**
	 * ��ǰPost�����ۼ���
	 */
	private ArrayList<Comment> comments;

	/**
	 * ������ǰPost�ĵ���λ��(�磺�������Ϻ�...)<br>
	 * Ĭ��Ϊnull
	 */
	private String location;
	
	/**
	 * ��ǰPost������
	 */
	private int postType;
	
	/**
	 * �û�������Postʱ���ô˹��캯��<br>
	 * �ù��캯��Ӧ��ɵĹ��ܣ�<br>
	 * 1���ڱ��ع���һ��Post<br>
	 * 2�����������Post�Զ�������������<br>
	 * 3���������ɹ����ȡ��ID���õ����ص�Post��
	 * @param postUserID
	 * @param postDate
	 * @param content
	 * @param location
	 */
	AbstractPost(int postUserID, String postDate, byte [] content, String location)
	{
	}

	/**
	 * �ӷ�������ȡ���е�Postʱ���ô˹��캯��<br>
	 * ������ɺ󣬴ӷ�������ȡ�����б�{@link #getCommentsFromServer(int)}
	 * @param postID
	 * @param postUserID
	 * @param likedNumber
	 * @param postDate
	 * @param content
	 * @param location
	 */
	AbstractPost(int postID, int postUserID, int likedNumber, String postDate, byte [] content, String location)
	{
	}
	
	public int getPostID() {
		return 0;
	}

	public int getPostUserID() {
		return 0;
	}

	public int getLikedNumber() {
		return 0;
	}

	public String getPostDate() {
		return null;
	}

	public byte [] getContent() {
		return null;
	}
	
	public String getLocation() {
		return null;
	}
	
	public abstract int getPostType();

	public ArrayList<Comment> getComments() {
		return null;
	}
	
	/**
	 * �û����޺���ô˺�����������+1
	 */
	public void increaseLikedNumber() {
	}

	/**
	 * �û��������޺���ô˺�����������-1
	 */
	public void decreaseLikedNumber() {
	}

	/**
	 * ��ǰPost���һ��Comment
	 * @param comment ����ӵ�Comment
	 */
	public void appendComment(Comment comment) {
	}

	/**
	 * ���ݸ���commentID���ӵ�ǰPost��ɾ����Comment
	 * @param commentID ��ɾ����comment��Ӧ��commentID 
	 */
	public void deleteCommentByID(int commentID) {
	}
	
	/**
	 * �ӷ�������ȡ��ǰPost��Comment����
	 * @param postID ��ǰPost��postID
	 */
	private void getCommentsFromServer(int postID) {
	}
}

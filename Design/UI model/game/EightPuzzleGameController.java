/**
 * 
 * @author LiJian
 *
 */
public class EightPuzzleGameController
{
	private int step;
	private int elapse;
	//原始图像
	private Bitmap bitmap;
	//拆分后图像。View为自定义控件
	private View[] pictures;

	/**
	 * 初始化游戏平台，完成分割图像，创建puzzle，更新ui
	 */
	public void initPlatform();
	
	/**
	 * 根据移动状态反馈给用户：移动动画；不可移警告
	 * @param movestatus ：移动状态<br>
	 * DIRECTION_UP：上移<br>
	 * DIRECTION_DOWN：下移<br>
	 * DIRECTION_LEFT：左移<br>
	 * DIRECTION_RIGH：右移<br>
	 */
	public void movePuzzle(int movestatus);
	
	/**
	 * 获胜后调用。赢的动画
	 */
	public void winAction();
	/**
	 * 根据步数更新UI
	 */
	public void stepIncrement();
}

public class EightPuzzleGameModel
{

	/**
	 * 判断当前矩阵可不可移
	 * @param direction 移动方向<br>
	 * DIRECTION_UP：上移<br>
	 * DIRECTION_DOWN：下移<br>
	 * DIRECTION_LEFT：左移<br>
	 * DIRECTION_RIGH：右移<br>
	 * @return 是否可移
	 * true：可移
	 * false：不可移
	 */
	private boolean canMove(int direction);

	private int[][] board;
	public void generateBoard();
	private boolean isTie();
	public boolean isWin();

	private boolean moveUp();
	private boolean moveDown();
	private boolean moveLeft();
	private boolean moveRight();

	public int execute(int direction);

	public int [][] getCurrentBoard();	
}

public class View
{
	private Bitmap bitmap;

	public void moveAction;
}
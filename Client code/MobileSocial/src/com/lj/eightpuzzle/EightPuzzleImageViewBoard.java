package com.lj.eightpuzzle;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Bitmap.Config;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

public class EightPuzzleImageViewBoard 
{
	private Bitmap eightPuzzleAnswerImage;
	private final float GAUSSION_VALUE_ONE = 5;
	private Bitmap imageGaussianOne;
	private Bitmap imageGaussianTwo;
	private ImageView[] gImageViewBoard;
	private EightPuzzleBoard gGameBoard;
	private Context context;
	
	private Bitmap createGaussianImage(Bitmap bitmap, float value)
	{
	
		Bitmap outBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Config.ARGB_8888);  
//		Bitmap outBitmap = Bitmap.createBitmap(bitmap);
		RenderScript rs = RenderScript.create(context);
		ScriptIntrinsicBlur blurScript = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs));  
		Allocation allIn = Allocation.createFromBitmap(rs, bitmap);  
        Allocation allOut = Allocation.createFromBitmap(rs, outBitmap);  
        blurScript.setRadius(value);
        blurScript.setInput(allIn);  
        blurScript.forEach(allOut); 
        allOut.copyTo(outBitmap);  
    //    bitmap.recycle();  
        rs.destroy();  
		return outBitmap;
	}
	
	private ImageView[] createImageViewBoard(ImageView[] imageViewBoard, Bitmap bitmap, int[] board)
	{
		int size = bitmap.getWidth();
		int single = size / 3;
		for (int i = 0; i < board.length; i++)
		{
			Bitmap b = Bitmap.createBitmap(bitmap, board[i] % 3 * single, board[i] / 3 * single, single, single);
			imageViewBoard[i].setImageBitmap(b);
		}
		imageViewBoard[gGameBoard.getBlankIndex()].setVisibility(View.INVISIBLE);
		return imageViewBoard;
	}
	
	public EightPuzzleImageViewBoard(ImageView[] imageViewBoard, Bitmap bitmap, Context context) 
	{
		this.context = context;
		gGameBoard = new EightPuzzleBoard();
		eightPuzzleAnswerImage = bitmap;
		gImageViewBoard = imageViewBoard;
		setGaussian();
	//	gImageViewBoard = createImageViewBoard(imageViewBoard, eightPuzzleAnswerImage, gGameBoard.getGameBoard());
	}
	
	public boolean isWin()
	{
		return gGameBoard.isWin();
	}
	
	public void excMove(int direction)
	{
		int moveIndex = gGameBoard.getMoveIndex(direction);
		int blankIndex = gGameBoard.getBlankIndex();
		gGameBoard.excMove(moveIndex);
		
		Drawable temp = gImageViewBoard[moveIndex].getDrawable();
		gImageViewBoard[moveIndex].setImageDrawable(gImageViewBoard[blankIndex].getDrawable());
		gImageViewBoard[blankIndex].setImageDrawable(temp);
		gImageViewBoard[blankIndex].setVisibility(View.VISIBLE);
		gImageViewBoard[moveIndex].setVisibility(View.INVISIBLE);
		setGaussian();
	}
	
	public float getImageViewSize()
	{
		return gImageViewBoard[1].getX() - gImageViewBoard[0].getX();
	}
	
	public ImageView getCurrentMoveImageView(int direction)
	{
		int moveIndex = gGameBoard.getMoveIndex(direction);
		return gImageViewBoard[moveIndex];
	}
	
	public int getMoveIndex(int direction)
	{
		return gGameBoard.getMoveIndex(direction);
	}
	
	private void setGaussian()
	{
		int n = 9;
		int[] board = gGameBoard.getGameBoard();
		for (int i = 0; i < board.length; i++)
			if (board[i] == i)
				n--;
		n = n * 2;
		if (n != 0)
			gImageViewBoard = createImageViewBoard(gImageViewBoard, createGaussianImage(eightPuzzleAnswerImage, n), gGameBoard.getGameBoard());
		else
			gImageViewBoard = createImageViewBoard(gImageViewBoard, eightPuzzleAnswerImage, gGameBoard.getGameBoard());
	}
	
	public void restart()
	{
		for (int i = 0; i < gImageViewBoard.length; i++)
			gImageViewBoard[i].setVisibility(View.VISIBLE);
		gGameBoard = new EightPuzzleBoard();
		setGaussian();
	}
	
	public void showHint()
	{
		int[] board = gGameBoard.getGameBoard();
		for (int i = 0; i < board.length; i++)
		{
	//		gImageViewBoard[i].set
		}
	}
	
}

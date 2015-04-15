package com.lj.eightpuzzle;

import java.util.Random;

import com.yg.commons.ConstantValues;

import android.widget.ImageView;

public class EightPuzzleBoard 
{
	private int[] gGameBoard;
	private int gBlankIndex;
	
	private final int RANDOM_STEP = 10;
	
	private void createGameBoard()
	{
		gGameBoard = new int[9];
		for (int i = 0; i < gGameBoard.length; i++)
			gGameBoard[i] = i;
		Random random = new Random();
		for (int i = 0; i < RANDOM_STEP; i++)
		{
			int direction = random.nextInt(4);
			int moveIndex = getMoveIndex(direction);
			if (moveIndex != -1)
				excMove(moveIndex);
		}
	}
	
	public EightPuzzleBoard() 
	{
		gBlankIndex = 8;
		createGameBoard();
	}

	public int getMoveIndex(int direction)
	{
		int result = 0;
		switch (direction)
		{
		case ConstantValues.InstructionCode.DIRECTION_UP:
			if (gBlankIndex / 3 == 2)
				result = -1;
			else
				result = gBlankIndex + 3;
			break;
		case ConstantValues.InstructionCode.DIRECTION_DOWN:
			if (gBlankIndex / 3 == 0)
				result = -1;
			else
				result = gBlankIndex - 3;
			break;
		case ConstantValues.InstructionCode.DIRECTION_LEFT:
			if (gBlankIndex % 3 == 2)
				result = -1;
			else
				result = gBlankIndex + 1;
			break;
		case ConstantValues.InstructionCode.DIRECTION_RIGHT:
			if (gBlankIndex % 3 == 0)
				result = -1;
			else
				result = gBlankIndex - 1;
			break;
		}
		return result;
	}
	
	public void excMove(int moveIndex)
	{
		int t = gGameBoard[moveIndex];
		gGameBoard[moveIndex] = gGameBoard[gBlankIndex];
		gGameBoard[gBlankIndex] = t;
		gBlankIndex = moveIndex;
	}
	
	public boolean isWin()
	{
		for (int i = 0; i < gGameBoard.length; i++)
			if (gGameBoard[i] != i)
				return false;
		return true;
	}

	public int[] getGameBoard()
	{
		return gGameBoard;
	}
	
	public int getBlankIndex()
	{
		return gBlankIndex;
	}
}

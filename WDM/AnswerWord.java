import java.awt.Color;
import java.awt.Graphics;

/*
 * Created on June 10, 2004
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */

/**
 * @author marie
 *
 * To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
public class AnswerWord extends Word
{
	private static final Color[] wordColors = {Color.black, new Color(0,125,0), Color.blue, Color.red};
	private boolean found = false;
	int x;
	int y;
	private static final int leftMARGIN = 30;
	private static final int topMARGIN = 320;
	private static final int rowMARGIN = 20;
	private static final int colMARGIN = 110;
	private static final int WORDSPERCOLUMN = 12;
	
	public AnswerWord(Word w, int index)
	{
		this.word = w.getWord();
		this.points = w.getPoints();
		found = false;
		int col = (int) index / WORDSPERCOLUMN;
		int row =  index % WORDSPERCOLUMN;
		x = leftMARGIN + (col * colMARGIN);
		y = topMARGIN + (row * rowMARGIN);
//		System.out.println("AnswerWord " + word + ", index " + index + ", points " + points + ", col " + col + ", row " + row + ", x " + x + ", y " + y);
		found = false;
	}
	public void draw(Graphics og, int gameStage)
	{
		og.setColor(wordColors[this.getWord().length() - 3]);
		og.drawString("*", x - 12, y);
		if (found)
		{
			og.setColor(new Color(0,125,0));
		}
		else if (gameStage == 2)
		{	
			og.setColor(new Color(200,0,0));
		}	
		else
		{
			og.setColor(ToMarUtils.toMarBackground);
		}	
		og.drawString(word, x, y);
	}
	public boolean isFound()
	{
		return found;
	}

	public void setFound(boolean found)
	{
		this.found = found;
	}
	public int getX()
	{
		return x;
	}

	public void setX(int x)
	{
		this.x = x;
	}

	public int getY()
	{
		return y;
	}

	public void setY(int y)
	{
		this.y = y;
	}
}

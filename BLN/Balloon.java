import java.awt.Color;
import java.awt.Graphics;
import java.awt.Polygon;

public class Balloon
{
	private int color;
	private int state;
	private int row;
	private int col;
	private int rowOrig;
	private int colOrig;
	static final int WIDTH = 27;
	static final int HEIGHT = 33;
	static final int MARGIN = 50;
	static final int TAB = 5;
	private static int xFactor;
	public static final int READY = 0;
	public static final int SELECTED = 1;
	public static final int POPPED = 2;
	private static final Color ready[] = {tmColors.DARKRED,
			tmColors.DARKGREEN,tmColors.MAGENTA,tmColors.DARKBLUE,
			tmColors.ORANGE};
	private static final Color selected[] = {tmColors.LIGHTPINK,
			tmColors.LIGHTGREEN,tmColors.LIGHTMAGENTA,tmColors.LIGHTBLUE,
			tmColors.YELLOW};
	
	public Balloon(int r, int c)
	{
		rowOrig = row = r;
		colOrig = col = c;
		color = getRnd(BLN.NUMBEROFCOLORS);
		state = READY;
	}
	public int getOrigRow()
	{
		return rowOrig;
	}
	public int getOrigCol()
	{
		return colOrig;
	}	
	public void setRow(int r)
	{
		row = r;
	}
	public void setCol(int c)
	{
		col = c;
	}
	public int getRow()
	{
		return row;
	}
	public int getCol()
	{
		return col;
	}	
	public int getRnd(int value)
    {
        return (int)(Math.random() * value);
    }    
	
	public int getColor()
	{
		return color;
	}	
	
	public int getState()
	{
		return state;
	}	
	public void setState(int s)
	{
		state = s;
	}	
	public void draw(Graphics og)
	{
		if (this.getState() == Balloon.READY)
		{	
			og.setColor(ready[this.getColor()]);	
		}
		else if (this.getState() == Balloon.SELECTED)
		{	
			og.setColor(selected[this.getColor()]);	
		}
		else
		{
			System.out.println("Major error! r = " + this.getRow() +
					" c = " + this.getCol() +
					" state = " + this.getState());
		}
		int midpoint = (this.getCol() * (WIDTH + 1)) + (WIDTH/2) + MARGIN + xFactor;
		int bottom = this.getRow() * (HEIGHT + TAB)+ HEIGHT;
		int xs[] = {midpoint, midpoint - 2, midpoint + 2};
		int ys[] = {bottom - 2, bottom + TAB - 1, bottom + TAB - 1};
		Polygon poly = new Polygon(xs, ys, 3);
		og.fillOval(this.getCol() * (WIDTH + 1) + MARGIN + xFactor,
				this.getRow() * (HEIGHT + TAB),	WIDTH, HEIGHT);
		og.fillPolygon(poly);
		og.setColor(Color.white);
		og.fillRect(midpoint+5, bottom - (HEIGHT - 10), 2, 3);
	}
	/**
	 * @return Returns the xFactor.
	 */
	public static int getXFactor()
	{
		return xFactor;
	}

	/**
	 * @param factor The xFactor to set.
	 */
	public static void setXFactor(int factor)
	{
		xFactor = factor;
	}

}

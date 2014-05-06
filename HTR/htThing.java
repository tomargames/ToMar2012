/* 
	base class for You, Good guys, Bad guys, and Bombs
*/

import java.awt.*;

public class htThing
{
	int row;
	int column;
	Color color;
	
	public htThing()
	{
	}	
	
	public void draw(Graphics g)
	{
		g.setColor(color);
		g.fillRoundRect(column * HTR.cWidth + 2, row * HTR.cHeight + 2, HTR.cWidth - 2, HTR.cHeight - 2,20,20);
	}	
	
	public int getRow()
	{
		return row;
	}
	
	public int getColumn()
	{
		return column;
	}
	
	public void setRow(int r)
	{
		row = r;
	}
	
	public void setColumn(int c)
	{
		column = c;
	}
	
	public void setColor(Color c)
	{
		color = c;
	}
	
	public boolean hit(int r, int c)
	{
		if (row == r && column == c)
		{
			return true;
		}
		return false;
	}	
}

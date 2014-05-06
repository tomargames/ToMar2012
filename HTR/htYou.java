import java.awt.*;



public class htYou extends htThing
{
	private int direction;
	public static final int NOTMOVING = 0;
	public static final int NORTH = 1;
	public static final int EAST = 2;
	public static final int SOUTH = 3;
	public static final int WEST = 4;

	public htYou()
	{
		direction = NOTMOVING;
		row = 15;
		column = 22;
		color = HTR.colorYou;
	}	
	public boolean move()
	{	
		if (direction == NORTH)
		{
			if (row > 0)
			{
				row -= 1;
			}
			else
			{
				return false;
			}	
		}
		else if (direction == SOUTH)
		{
			if (row < HTR.numRows - 1)
			{
				row += 1;
			}
			else
			{
				return false;
			}	
		}
		else if (direction == WEST)
		{
			if (column > 0)
			{
				column -= 1;
			}
			else
			{
				return false;
			}	
		}
		else if (direction == EAST)
		{
			if (column < HTR.numCols - 1)
			{
				column += 1;
			}
			else
			{
				return false;
			}	
		}	
		return true;
	}	
	
	public void setDirection(int dir)
	{
		direction = dir;
	}
	
	public int getDirection()
	{
		return direction;
	}	
	
	public void drawBig(Graphics g)
	{
		g.setColor(color);
		g.fillRoundRect(column * HTR.cWidth + 2, row * HTR.cHeight + 2, HTR.cWidth + 2, HTR.cHeight + 2,20,20);
	}	
		
}

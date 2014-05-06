import java.awt.Color;
import java.awt.Graphics;

/*
 * Created on Feb 13, 2005
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
public class Thing
{
	private int location;
	private Color color;
	public static final int NOTMOVING = 0;
	public static final int NORTH = 1;
	public static final int EAST = 2;
	public static final int SOUTH = 3;
	public static final int WEST = 4;
	
	public Thing(int location, Color color)
	{
		this.setLocation(location);
		this.color = color; 
	}
	/**
	 * @return Returns the location.
	 */
	public int getLocation()
	{
		return location;
	}

	/**
	 * @param location The location to set.
	 */
	public void setLocation(int location)
	{
		this.location = location;
	}
	public void move(int dir)
	{	
		if (dir == NORTH)
		{
			moveNorth();
		}	
		else if (dir == SOUTH)
		{
			moveSouth();
		}
		else if (dir == WEST)
		{
			moveWest();
		}	
		else if (dir == EAST)
		{
			moveEast();
		}
	}
	private void moveEast()
	{
		if (getCol() < WDQ.getNumCols() - 1)
		{	
			if (WDQ.getAllCells()[location].getVert() == false)
			{	
				location += 1;
			}	
		}		
	}	
	private void moveWest()
	{
		if (getCol() > 0)
		{
			if (WDQ.getAllCells()[location - 1].getVert() == false)
			{	
				location -= 1;
			}	
		}
	}	
	private void moveSouth()
	{
		if (getRow() < WDQ.getNumRows() - 1)
		{
			if (WDQ.getAllCells()[location].getHoriz() == false)
			{	
				location += WDQ.getNumCols();
			}	
		}
	}
	private void moveNorth()
	{
		if (getRow() > 0)
		{
			if (WDQ.getAllCells()[location - WDQ.getNumCols()].getHoriz() == false)
			{	
				location -= WDQ.getNumCols();
			}	
		}
	}	
	public int getRow()
	{
		return  location / WDQ.getNumCols();
	}
	public int getCol()
	{				   
		return location % WDQ.getNumCols();
	}
	
	/**
	 * @return Returns the color.
	 */
	public Color getColor()
	{
		return color;
	}

	public void draw(Graphics og)
	{
		int x = getCol() * WDQ.cWIDTH + WDQ.MARGIN;
		int y = getRow() * WDQ.cHEIGHT;
		og.setColor(this.getColor());
		og.fillRoundRect(x + 2, y + 2, WDQ.cWIDTH - 2, WDQ.cHEIGHT - 2,20,20);
		og.setColor(Color.black);
		og.drawRoundRect(x + 2, y + 2, WDQ.cWIDTH - 2, WDQ.cHEIGHT - 2,20,20);
	}
}

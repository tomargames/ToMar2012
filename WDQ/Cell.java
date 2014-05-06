import java.awt.Graphics;
import java.util.Vector;

public class Cell
{
	private Vector pathVector;
	private boolean vert;
	private boolean horiz;
	private int state;
	public static final int FREE = 0;

	public Cell(int num)
	{
		pathVector = new Vector();
		pathVector.addElement(new Integer(num));
		vert = true;
		horiz = true;
		state = FREE;
	}	
	public void setState(int st)
	{
		state = st;
	}
	public int getState()
	{
		return state;
	}	
	public Vector getPathVector()
	{
		return pathVector;
	}
	public void setVert(boolean v)
	{
		vert = v;
	}
	public void setHoriz(boolean h)
	{
		horiz = h;
	}	
	public boolean getVert()
	{
		return vert;
	}
	public boolean getHoriz()
	{
		return horiz;
	}	
	public void draw(Graphics og, int r, int c)
	{
		int x = c * WDQ.cWIDTH + WDQ.MARGIN;
		int y = r * WDQ.cHEIGHT;
		if (getState() > 0)
		{	
			og.setColor(tmColors.PURPLE);
			og.fillRect(x + 1, y + 1, WDQ.cWIDTH - 1, WDQ.cHEIGHT - 1);
		}
		og.setColor(WDQ.fgColor);
		if (getHoriz())
		{
			og.drawLine(x, y + WDQ.cHEIGHT, x + WDQ.cWIDTH, y + WDQ.cHEIGHT);
		}
		if (getVert())
		{
			og.drawLine(x + WDQ.cWIDTH, y, x + WDQ.cWIDTH, y + WDQ.cHEIGHT);
		}
		if (c == 0)
		{
			og.drawLine(x, y, x, y + WDQ.cHEIGHT);
		}
		if (r == 0)
		{
			og.drawLine(x, y, x + WDQ.cWIDTH, y);
		}
	}
}

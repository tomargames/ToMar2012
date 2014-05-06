import java.awt.Graphics;

/*
 * Created on Apr 9, 2005
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
public class Anchor
{
	String label;
	int x;
	int y;
	
	public Anchor(String label, int x, int y)
	{
		this.label = label;
		this.x = x;
		this.y = y;
	}
	public void draw(Graphics og)
	{
		og.setFont(AAA.LETTERFONT);
		og.drawString(label, x + getXFactor(label), y + 26);
	}	
	/**
	 * @return Returns the label.
	 */
	public String getLabel()
	{
		return label;
	}
	public static int getXFactor(String l)
	{
		if ("W".indexOf(l) > -1)
		{
			return 5;
		}
		else if ("JI?".indexOf(l) > -1)
		{
			return 12;
		}
		else if ("ELYTP".indexOf(l) > -1)
		{
			return 10;
		}
		else if ("AR".indexOf(l) > -1)
		{
			return 9;
		}
		return 8;
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
	public void setLabel(String label)
	{
		this.label = label;
	}
}

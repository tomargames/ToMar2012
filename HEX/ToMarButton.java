import java.awt.Font;
import java.awt.Graphics;
import java.awt.Color;

/*
 * Created on Mar 20, 2005
 */

/**
 * @author marie
 * VERSION 1.01
 * 1.01 - fix fonts to be compatible with jvm 1.6 (7/08)
 * 1.1 - incorporates ToMarUtils 1.1
 * 1.2 - adds disabled mode -- you can turn it off, and drawing turns it back on
 */
public class ToMarButton
{
	protected int x;
	protected int y;
	protected int xLabel = 10;
	protected int yLabel = 25;
	protected int width;
	protected int height = 40;
	protected String label;
	protected Font font;
	protected Color bgColor = tmColors.ORANGE;
	protected Color fgColor = tmColors.BLACK;
	protected boolean enabled = true;
	
	public ToMarButton(){}
	
	public ToMarButton (int x, int y, int width, String label)
	{
		this.x = x;
		this.y = y;
		this.width = width;
		this.label = label;
		font = new Font("Verdana", Font.PLAIN, 16);
	}
	public boolean clicked(int x, int y)
	{
		if (x > this.x && 
			x < this.x + width && 
			y > this.y && 
			y < this.y + height)
		{
			if (enabled)
			{	
				return true;
			}		
		}
		return false;
	}
	public void draw(Graphics og)
	{
		enabled = true;
		og.setColor(bgColor);
		og.fillRoundRect(x, y, width, height, 5, 5);
		og.setColor(fgColor);
		og.drawRoundRect(x, y, width, height, 5, 5);
		og.setFont(font);
		og.drawString(label, x + xLabel, y + yLabel);
	}	
	
	/**
	 * @return Returns the height.
	 */
	public int getHeight()
	{
		return height;
	}

	/**
	 * @param height The height to set.
	 */
	public void setHeight(int height)
	{
		this.height = height;
	}

	/**
	 * @return Returns the label.
	 */
	public String getLabel()
	{
		return label;
	}

	/**
	 * @param label The label to set.
	 */
	public void setLabel(String label)
	{
		this.label = label;
	}

	/**
	 * @return Returns the width.
	 */
	public int getWidth()
	{
		return width;
	}

	/**
	 * @param width The width to set.
	 */
	public void setWidth(int width)
	{
		this.width = width;
	}

	/**
	 * @return Returns the x.
	 */
	public int getX()
	{
		return x;
	}

	/**
	 * @param x The x to set.
	 */
	public void setX(int x)
	{
		this.x = x;
	}

	/**
	 * @return Returns the y.
	 */
	public int getY()
	{
		return y;
	}

	/**
	 * @param y The y to set.
	 */
	public void setY(int y)
	{
		this.y = y;
	}
	public Font getFont()
	{
		return font;
	}
	public void setFont(Font font)
	{
		this.font = font;
	}

	public int getXLabel()
	{
		return xLabel;
	}

	public void setXLabel(int label)
	{
		xLabel = label;
	}

	public int getYLabel()
	{
		return yLabel;
	}

	public void setYLabel(int label)
	{
		yLabel = label;
	}
	public Color getBgColor()
	{
		return bgColor;
	}

	public void setBgColor(Color bgColor)
	{
		this.bgColor = bgColor;
	}

	public Color getFgColor()
	{
		return fgColor;
	}

	public void setFgColor(Color fgColor)
	{
		this.fgColor = fgColor;
	}

	public boolean isEnabled()
	{
		return enabled;
	}

	public void setEnabled(boolean enabled)
	{
		this.enabled = enabled;
	}
}

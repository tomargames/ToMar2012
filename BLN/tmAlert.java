import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

/*
 * Created on Mar 22, 2005
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
public class tmAlert 
{
	public static final int CANCELLED = 0;
	public static final int VALID = 1;
	public static final int INVALID = 2;
	public static final int GARBAGE = 3;
	protected int x = 450;
	protected int y = 399;
	protected int width = 350;
	protected int height = 100;
	protected String message;
	protected Font font;
	protected tmButton submitButton;
	
	public tmAlert(String message)
	{
		this.message = message;
		submitButton = new tmButton(x + 130, y + 60, tmColors.LIGHTGRAY, 60, 100, "Okay");
		submitButton.setHeight(35);
		font = new Font("Verdana", Font.BOLD, 12);
	}
	public int processMouseInput(int x, int y)
	{
		if (getSubmitButton().clicked(x, y))
		{
			return VALID;
		}
		return GARBAGE;
	}

	public void draw(Graphics og)
	{
		og.setColor(new Color(222,180,180));
		og.fillRoundRect(x, y, width, height, 5, 5);
		og.setColor(new Color(0,0,0));
		og.drawRoundRect(x, y, width, height, 5, 5);
		og.setFont(font);
		og.drawString(message, x + 5, y + 15);
		submitButton.draw(og);
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
	 * @return Returns the message.
	 */
	public String getMessage()
	{
		return message;
	}

	/**
	 * @param message The message to set.
	 */
	public void setMessage(String message)
	{
		this.message = message;
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

	/**
	 * @return Returns the font.
	 */
	public Font getFont()
	{
		return font;
	}

	/**
	 * @param font The font to set.
	 */
	public void setFont(Font font)
	{
		this.font = font;
	}

	/**
	 * @return Returns the submitButton.
	 */
	public tmButton getSubmitButton()
	{
		return submitButton;
	}

	/**
	 * @param submitButton The submitButton to set.
	 */
	public void setSubmitButton(tmButton submitButton)
	{
		this.submitButton = submitButton;
	}

}

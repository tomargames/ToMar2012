import java.awt.Color;
import java.awt.Graphics;

/*
 * Created on Jan 23, 2005
 *
 */

public class Ball
{
	private int speed = 0;
	private int size = 0;
	private int x = 0;
	private int y = 50;
	private int dx = 0;
	private int dy = 0;
	private Color color;
	private int pointValue = 0;
	private static int redCount = 0;

	public Ball(boolean good)
	{
		this.x = ToMarUtils.getRnd(DBL.WIDTH - 50) + 20;
		this.speed = ToMarUtils.getRnd(3) + 1;
		this.size = ToMarUtils.getRnd(10) + 10;
		this.dx = ToMarUtils.getRnd(4) + 1;
		this.dy = ToMarUtils.getRnd(3) + 1;
		if (ToMarUtils.getRnd(2) == 0)
		{
			dx *= -1;
		}
		if (good)
		{
			this.color = tmColors.DARKGREEN;
			pointValue = redCount * speed * (25 - size);
		}
		else
		{
			redCount += 1;
			pointValue = 0;
			this.color = tmColors.RED;
		}
		this.speed = (this.speed > redCount) ? redCount : speed;
	}
	
	public boolean hit(int x, int y)
	{
		if (x >= this.x && x <= this.x + size)
		{
			if (y >= this.y && y <= this.y + size)
			{
				return true;	
			}
		}
		return false;
	}
	public void draw(Graphics g)
	{
		g.setColor(color);
		g.fillRoundRect(x, y, size, size, 50, 50);
	}	
	
	public void move()
	{
		x += dx * speed;
		y += dy * speed;
		if (x < 0 || x + size > DBL.WIDTH)
		{
			dx *= -1;
			x = (x < 0) ? 0 : DBL.WIDTH - size; 
		}
		else if (y < 50 || y + size > DBL.HEIGHT)
		{
			dy *= -1;
			y = (y < 50) ? 50 : DBL.HEIGHT - size; 
		}
	}
	/**
	 * @return Returns the size.
	 */
	public int getSize()
	{
		return size;
	}

	/**
	 * @param size The size to set.
	 */
	public void setSize(int size)
	{
		this.size = size;
	}

	/**
	 * @return Returns the speed.
	 */
	public int getSpeed()
	{
		return speed;
	}

	/**
	 * @param speed The speed to set.
	 */
	public void setSpeed(int speed)
	{
		this.speed = speed;
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

	/**
	 * @return Returns the dx.
	 */
	public int getDx()
	{
		return dx;
	}

	/**
	 * @param dx The dx to set.
	 */
	public void setDx(int dx)
	{
		this.dx = dx;
	}

	/**
	 * @return Returns the dy.
	 */
	public int getDy()
	{
		return dy;
	}

	/**
	 * @param dy The dy to set.
	 */
	public void setDy(int dy)
	{
		this.dy = dy;
	}

	/**
	 * @return Returns the color.
	 */
	public Color getColor()
	{
		return color;
	}

	/**
	 * @param color The color to set.
	 */
	public void setColor(Color color)
	{
		this.color = color;
	}

	/**
	 * @return Returns the pointValue.
	 */
	public int getPointValue()
	{
		return pointValue;
	}

	/**
	 * @param pointValue The pointValue to set.
	 */
	public void setPointValue(int pointValue)
	{
		this.pointValue = pointValue;
	}

	/**
	 * @param redCount The counter to set.
	 */
	public static void resetCounter()
	{
		Ball.redCount = 0;
	}

}

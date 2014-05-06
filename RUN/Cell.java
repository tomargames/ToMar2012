import java.awt.*;

public class Cell
{
	private boolean completed;
	private int row;
	private int column;
	private Rune rune;
	

	public static final int MARGIN = 50;
	public static final int SIZE = 65;
	
	public Cell( int row, int column)
	{
		this.completed = false;
		this.row = row;
		this.column = column;
		this.rune = null;
	}

	public boolean isMatch(Rune r)
	{
		return this.rune.isMatch(r);
	}
	public Rune getRune()
	{
		return this.rune;
	}
	public boolean isCompleted()
	{
		return this.completed;
	}	
	
	public void setRune(Rune rune)
	{
		this.rune = rune;
	}

	public void completed()
	{
		this.rune = null;
		this.completed = true;
	}	
	
	public void draw(Graphics g)
	{
		int x = MARGIN + (column * SIZE);
		int y = MARGIN + (row * SIZE);
		if (this.isCompleted())
		{	
			g.setColor(tmColors.PALEGRAY);
		}
		else
		{	
			g.setColor(tmColors.CREAM);
		}
		g.fillRect(x, y, SIZE, SIZE);
		g.setColor(Color.black);
		g.drawRect(x, y, SIZE, SIZE);
		if (this.rune != null)
		{
			rune.draw(g, x, y);
		}	
	}	
	public boolean isClicked(int x, int y)
	{
		int startx = MARGIN + (column * SIZE);
		int starty = MARGIN + (row * SIZE);
		int endx = MARGIN + ((column + 1) * SIZE);
		int endy = MARGIN + ((row + 1) * SIZE);
		if (x >= startx && x <= endx && y >= starty && y <= endy)
		{
			return true;
		}
		return false;
	}	
}

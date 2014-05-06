import java.awt.*;

public class bdLetter
{
	private String letter;
	private boolean bonus;
	private static String univ = "CCDDDGGHHLLLNNNNRRRRSSSSTTTTBFJKMPQVWXZAAAAAEEEEEEIIIOOOUY";
	private int row;
	private int column;
	private boolean selected;

	public bdLetter(int row, int column)
	{
		this.row = row;
		this.column = column;
		int rnd = (int)(Math.random()* univ.length());
		this.setLetter(univ.substring(rnd, rnd + 1));
		if (this.getLetter().equals("Q"))
		{
			this.setLetter("Qu");
		}	
		;
		bonus = ((int)(Math.random() * 8) == 0) ? true : false;
		selected = false;
	}
	public void setRow(int r)
	{
		this.row = r;
	}	
	public void setSelected(boolean selected)
	{
		this.selected = selected;
	}
	public boolean isSelected()
	{
		return this.selected;
	}	
	public void setLetter(String s)
	{
		this.letter = s;
	}
	public String getLetter()
	{
		return this.letter;
	}
	
	public void setBonus(boolean b)
	{
		this.bonus = b;
	}
	public boolean getBonus()
	{
		return this.bonus;
	}
	public int getColumn()
	{
		return column;
	}
	public boolean isOdd()
	{
		return (column % 2 == 0) ? false : true;
	}
	public boolean isTouching(int row, int column)
	{
		if (this.column == column)
		{
			if (row == this.row - 1 || row == this.row + 1)
			{
				return true;
			}
		}	
		else if (column == this.column - 1 || column == this.column + 1)
		{
			if (row == this.row)
			{
				return true;
			}	
			if (this.isOdd())
			{
				if (row == this.row + 1)
				{
					return true;
				}	
			}	
			else
			{
				if (row == this.row - 1)
				{
					return true;
				}	
			}	
		}
		return false;
	}	
	public boolean isWithin(int x, int y)
	{
		int firstY = BND.margin + ((BND.maxSize - this.row) * BND.side * 8);
		int lastY = BND.margin + ((BND.maxSize - this.row + 1) * BND.side * 8);
		int firstX = BND.margin + ((BND.maxSize - this.column) * BND.side * 8 ) + BND.side * 3;
		int lastX = BND.margin + ((BND.maxSize - this.column + 1) * BND.side * 8);
		if (this.isOdd())
		{
			firstY -= BND.side * 4;
			lastY -= BND.side * 4;
		}	
		if (x >= firstX && x <= lastX && y >= firstY && y <= lastY)
		{
			return true;
		}
		return false;
	}	
	public void draw(Graphics g)
	{
		int firstY = BND.margin + ((BND.maxSize - this.row) * BND.side * 8);
		int firstX = BND.margin + ((BND.maxSize - this.column) * BND.side * 8);
		if (this.isOdd())
		{
			firstY -= BND.side * 4;
		}	
		int y[] = {firstY, 
				   firstY, 
				   firstY + BND.side * 4,  
				   firstY + BND.side * 8,  
				   firstY + BND.side * 8, 
				   firstY + BND.side * 4};
		int x[] = {firstX + BND.side * 3,
				   firstX + BND.side * 8,
				   firstX + BND.side * 11,
				   firstX + BND.side * 8,
				   firstX + BND.side * 3,
				   firstX};
		int colorIndex = (this.getBonus()) ? 3 : 1;
		if (this.isSelected())
		{	
			colorIndex += 1;
		}
		g.setColor(BND.colors[colorIndex]);
		g.fillPolygon(x, y, 6);
		g.setColor(BND.colors[5]);
		g.drawPolygon(x, y, 6);
		int xc = firstX + BND.side * 4;
		if (this.getLetter().equals("I"))
		{
			xc += 4;
		}	
		else if (this.getLetter().equals("Qu"))
		{
			xc -= 4;
		}	
		g.drawString(this.letter, xc + 1, firstY + BND.side * 6 - 3);
	}	
}

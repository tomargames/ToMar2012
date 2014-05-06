import java.awt.*;

public class Rune
{
	private int type;
	private int color;
	
	public static final int NUMBEROFCOLORS = 8;     
	public static final int NUMBEROFTYPES = 13;          // including bomb/wild
	public static final int BOMB = 13;
	public static final int WILD = 14;
	
	public static Color[] colors = {tmColors.RED,
		tmColors.OLIVE, tmColors.ORANGE,
		tmColors.PLUM,tmColors.MAGENTA, tmColors.GOLD,
		tmColors.GRAY,  tmColors.PURPLE, tmColors.BLACK};
	public static String[] runes = {"SPEC","ç","€","‡","¥","§","Þ","†","¤","±","º","¿","£","BOMB","WILD"};
	
	public Rune(int level)
	{
		int numberOfTypes = (level < 4) ? 8 : (level < 8) ? 9 : (level < 12) ? 10 : (level < 16) ? 11 : 12;
		int numberOfColors = (level < 6) ? 5 : (level < 10) ? 6 : (level < 14) ? 7 : 8;
		type = (int) (Math.random() * numberOfTypes);
		if (type == 0)
		{
			type = (int) (Math.random() * 2) + 13;
			color = NUMBEROFCOLORS;
		}
		else
		{	
			color = (int) (Math.random() * numberOfColors);
		}	
	}	

	public Rune()
	{
		this.type = WILD;
		this.color = NUMBEROFCOLORS;
	}

	public boolean isWild()
	{
		if (type == WILD)
		{
			return true;
		}
		return false;
	}
	
	public boolean isBomb()
	{
		if (type == BOMB)
		{
			return true;
		}
		return false;
	}
	
	public boolean isMatch(Rune r)
	{
		if (this.isWild() || r.isWild())
		{
			return true;
		}	
		else if (r.getType() == this.type || r.getColor() == this.color)
		{
			return true;
		}
		return false;
	}	
	
	public int getType()
	{
		return this.type;
	}
	
	public int getColor()
	{
		return this.color;
	}
	public void draw(Graphics g, int x, int y)
	{
		int fontWeight = (type < NUMBEROFTYPES) ? Font.PLAIN : Font.ITALIC;
		int fudgeX = (type < NUMBEROFTYPES) ? Cell.SIZE/4 : Cell.SIZE/8;
		int fudgeY = (type < NUMBEROFTYPES) ? (int)(Cell.SIZE/1.25) : (int)(Cell.SIZE/1.6);
		int size = (type < NUMBEROFTYPES) ? 48 : 16;
        g.setFont(new Font("Verdana",fontWeight,size));
		g.setColor(colors[color]);
		g.drawString(runes[type], x + fudgeX, y + fudgeY);
	}	
}

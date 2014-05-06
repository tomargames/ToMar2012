import java.awt.*;

public class WasteCan extends tmButton
{
	private int numberOfRunes;
	private static final int EMPTY = 0;
	private static int FULL = 3;
	private static String[] label = {"Empty", "1", "2", "Full"};
	
	public WasteCan(int level)
	{
		super(RUN.RIGHTCOLUMN, 100, tmColors.BLACK, 120, 80,"");
		numberOfRunes = 0;
		if (level == 18 || level == 22)
		{
			this.shrink();
		}	
	}
	
	public boolean add()
	{
		if (numberOfRunes == FULL)
		{
			return false;
		}	
		numberOfRunes += 1;
		return true;
	}
	public void subtract()
	{
		if (numberOfRunes > EMPTY)
		{
			numberOfRunes -= 1;
		}
	}
	
	public void shrink()
	{
		FULL -= 1;
		label[FULL] = "Full";	
	}	
	
	public void draw(Graphics g)
	{
		g.setColor(tmColors.DARKGRAY);
		g.fillRoundRect(x, width, y, height, 20, 20);
		g.setColor(tmColors.PEACH);
		g.drawRoundRect(x, width, y, height, 20, 20);
        g.setFont(new Font("Verdana", Font.PLAIN, 24));
		g.drawString(label[numberOfRunes], x + 15, y + height - 10);
        g.setFont(new Font("Verdana", Font.PLAIN, 16));
		g.setColor(Color.black);
		g.drawString("WasteCan", RUN.RIGHTCOLUMN, 230);
	}	
}

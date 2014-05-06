import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.util.Vector;

public class Letter extends ToMarButton
{
	private static final int SIZE = 60;
	private static final int SPACING = 2;
	public static final int TMARGIN = 150;
	public static final int RMARGIN = 200;
	private int index;
	private boolean selected;

	public void log()
	{
		WDM.log("Letter " + this.getLabel() + ", index is " + index);
	}
	public Letter (int index, String letter)
	{
		super (RMARGIN + (index * (SIZE + SPACING)), TMARGIN, SIZE, "");
		this.setHeight(SIZE);
		font = new Font("Verdana", Font.PLAIN, 36);
		label = letter;
		this.index = index;
		xLabel = 19;
		yLabel = 42;
		selected = false;
	}
	public void draw(Graphics og)
	{
		if (!selected)
		{	
			og.setColor(new Color(245,222,179));
			og.fillRoundRect(x, y, width, height, 5, 5);
			og.setColor(fgColor);
			og.drawRoundRect(x, y, width, height, 5, 5);
			og.setFont(font);
			int fudgeFactor = ("W".equals(label)) ? (-5) : ("I".equals(label)) ? 5 : 0;
			og.drawString(label, x + xLabel + fudgeFactor, y + yLabel);
		}
	}
	public boolean isLetter(String l)
	{
		if (this.getLabel().equalsIgnoreCase(l))
		{
			return true;
		}
		return false;
	}
	public boolean selectLetter()
	{
		// returns true if successfully selected; false if already selected
		if (!selected)
		{
			selected = true;
			return true;
		}
		return false;
	}
	public void putBack()
	{
		selected = false;
	}
}

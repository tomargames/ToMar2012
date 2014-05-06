import java.awt.*;
public class Slot extends ToMarButton
{
	public static final int size = 75;
	public static final int margin = 40;
	public static final int NOTUSED = 0;
	public static final int HIDING = 1;
	public static final int SHOWING = 2;
	public static final int ERROR = 3;
	public static final int REVEALING = 4;
	private int status;
//	private int value;
	private int index;
	private static Slot[] slots;
	private static Color[] colors = {tmColors.DARKMAGENTA, tmColors.RED, tmColors.BLACK}; 
	
	public String toString()
	{
		return "Slot: index = " + index + ", value = " + label + ", status = " + status;
	}
	public static void init()
	{
		slots = new Slot[HEX.MAXLEVEL];
		for (int i = 0; i < HEX.MAXLEVEL; i++)
		{
			slots[i] = new Slot(i);
		}
	}
	public Slot(int index) 
	{
		super(margin/2 * (index % 6) + margin + size * (index % 6), 
						margin/2 * ((int) index / 6) + margin + size * ((int) index / 6) - 35, size, "");
		this.setHeight(size);
		this.index = index;
		font = new Font("Verdana", Font.BOLD, 48);
	}
	
	public void draw(Graphics og)
	{
		if (status > NOTUSED)
		{
			og.setColor(tmColors.PALECYAN);
			og.fillRoundRect(x, y, width, height, 5, 5);
			og.setColor(tmColors.BLACK);
			og.drawRoundRect(x, y, width, height, 5, 5);
			if (status > HIDING)
			{	
				og.setFont(font);
				og.setColor(colors[status - 2]);
				int xFactor = 20;
				if ("W".equalsIgnoreCase(label))
				{	
					xFactor = 14;
				}
				else if ("I".equalsIgnoreCase(label))
				{	
					xFactor = 22;
				}
				else if ("M".equalsIgnoreCase(label))
				{	
					xFactor = 17;
				}
				og.drawString(label, x + xFactor, y + 55);
			}
		}	
	}
	public static Slot[] getSlots()
	{
		return slots;
	}
	public static void setSlots(Slot[] slots)
	{
		Slot.slots = slots;
	}
	public int getStatus()
	{
		return status;
	}
	public void setStatus(int status)
	{
		this.status = status;
	}
	public int getIndex()
	{
		return index;
	}
	public void setIndex(int index)
	{
		this.index = index;
	}	
	
}	

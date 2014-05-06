import java.awt.Graphics;
import java.util.Vector;

/*
 * Created on Apr 9, 2005
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */

/**
 * @author marie
 *
 */
public class Letter extends ToMarButton
{
	private static Vector letters;
	private boolean selected;
	private static int[] xCoordinates;
	private static int[] yCoordinates;
	private static Vector availableSlots;	
	
	public Letter(String label)
	{
		super(1, 1, AAA.SIZE, label);
		selected = false;
		letters.addElement(this);
		font = AAA.LETTERFONT;
		this.height = AAA.SIZE;
	}
	public static Vector getLetters()
	{
		return letters;
	}
	public static void refreshLetters()
	{
		letters = new Vector();
	}
	public static void removeLetter(Letter letter)
	{
		for (int i = 0; i < letters.size(); i++)
		{
			if (letter.equals(letters.elementAt(i)))
			{
				letters.setElementAt(null, i);
				availableSlots.addElement(new Integer(i));
			}
		}
	}
	public static void setUpLevel(int level)
	{
		xCoordinates = new int[level * (Word.MAXWORDSIZE - Word.MAXANCHORS)];
		yCoordinates = new int[level * (Word.MAXWORDSIZE - Word.MAXANCHORS)];
		availableSlots = new Vector();
		int counter = 0;
		// there will be TOTALROWS in each of 2 columns
		for (int i = 0; i < level; i++) 
		{
			for (int j = 0; j < Word.MAXWORDSIZE - Word.MAXANCHORS; j++)
			{
				if (i < AAA.TOTALROWS)
				{
					xCoordinates[counter] = (int)((AAA.COLUMNMARGIN + AAA.SIZE) * (Word.MAXWORDSIZE + 1.5 + j));
				}
				else
				{
					xCoordinates[counter] = (int)((AAA.COLUMNMARGIN + AAA.SIZE) * ((Word.MAXWORDSIZE + 1.5 + j) + (Word.MAXWORDSIZE - Word.MAXANCHORS)));
				}
				yCoordinates[counter++] = (AAA.TOPMARGIN + (AAA.ROWMARGIN + AAA.SIZE) * (i % AAA.TOTALROWS));
			}
		}
		mixLetters();
	}
	public static void putLetterBack(Letter letter)
	{
		// get index of first available slot
		int idx = ((Integer) availableSlots.elementAt(0)).intValue();
		availableSlots.removeElementAt(0);
		letter.setX(xCoordinates[idx]);
		letter.setY(yCoordinates[idx]);
		letters.setElementAt(letter, idx);
	}
	public static void mixLetters()
	{
		Vector temp = letters;
		letters = new Vector();
		int counter = 0;
		while (temp.size() > 0)
		{
			int r = ToMarUtils.getRnd(temp.size());
			Letter l = (Letter) temp.elementAt(r);
			if (l != null)
			{
				l.setX(xCoordinates[counter]);
				l.setY(yCoordinates[counter++]);
				letters.addElement(l);
			}
			temp.removeElementAt(r);
		}
		availableSlots = new Vector();
		for (int i = letters.size(); i < xCoordinates.length; i++)
		{
			letters.addElement(null);
			availableSlots.addElement(new Integer(i));
		}	
	}
	public void draw(Graphics og, boolean placed)
	{
		if (selected)
		{
			og.setColor(tmColors.LIGHTYELLOW);
		}
		else if (placed)
		{
			og.setColor(tmColors.PALEBLUE);
		}
		else
		{
			og.setColor(tmColors.LIGHTPINK);
		}
		og.fillRoundRect(x, y, width, height,AAA.ROUNDER,AAA.ROUNDER);
		og.setColor(tmColors.BLACK);
		og.drawRoundRect(x, y, width, height,AAA.ROUNDER,AAA.ROUNDER);
		og.setFont(font);
		og.drawString(label, x + Anchor.getXFactor(label), y + 26);
	}	
	/**
	 * @return Returns the selected.
	 */
	public boolean isSelected()
	{
		return selected;
	}

	/**
	 * @param selected The selected to set.
	 */
	public void setSelected(boolean selected)
	{
		this.selected = selected;
	}

}

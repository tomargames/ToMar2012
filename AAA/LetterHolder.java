import java.awt.Graphics;
/*
 * Created on Apr 9, 2005
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */

/**
 * @author marie
 */
public class LetterHolder extends ToMarButton
{
	private Letter letter;
	public static final String INIT = "?";
	public LetterHolder (int x, int y)
	{
		super(x, y, AAA.SIZE, INIT);
		font = AAA.LETTERFONT;
		this.height = AAA.SIZE;
		this.letter = null;
	}
	public void draw(Graphics og)
	{	
		if (this.letter == null)
		{
			og.setColor(tmColors.OFFWHITE);
			og.fillRoundRect(x, y, width, height, AAA.ROUNDER, AAA.ROUNDER);
			og.setColor(tmColors.BLACK);
			og.drawRoundRect(x, y, width, height,AAA.ROUNDER,AAA.ROUNDER);
			og.setFont(font);
			og.drawString(label, x + Anchor.getXFactor(label), y + 25);
		}
		else
		{
			this.letter.draw(og, true);
		}
	}	
	/**
	 * @return Returns the letter.
	 */
	public Letter getLetter()
	{
		return letter;
	}

	/**
	 * @param letter The letter to set.
	 */
	public void setLetter(Letter letter)
	{
		this.letter = letter;
		if (letter != null)
		{
			this.letter.setX(this.getX());
			this.letter.setY(this.getY());
		}
	}
}

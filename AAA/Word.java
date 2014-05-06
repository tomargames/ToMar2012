import java.awt.Font;
import java.awt.Graphics;

public class Word
{
	private Object[] elements;
	private String originalWord;
	private int index;
	private int baseX;
	private int yCoordinate;
	public static final int MAXWORDSIZE = 6;
	public static final int MAXANCHORS = 3;
	private static final int numberOfAnchors[] = {2, 2, 3};
	
	public Word (int index, String originalWord)
	{
		int[] xCoordinates = new int[MAXWORDSIZE];
		this.index = index;
		yCoordinate = AAA.TOPMARGIN + (AAA.ROWMARGIN + AAA.SIZE) * (index % AAA.TOTALROWS);
		if (index < AAA.TOTALROWS)
		{
			baseX = 0;
		}
		else
		{
			// need to account for the first column of words + 2 columns of letters
			baseX = AAA.NUMBERMARGIN + 
			(AAA.COLUMNMARGIN + AAA.SIZE) * (MAXWORDSIZE + 1 + 2 * (MAXWORDSIZE - MAXANCHORS));
		}
		// set x and y coordinates for letter positions in word
		for (int i = 0; i < MAXWORDSIZE; i++) 
		{
			xCoordinates[i] = baseX + AAA.NUMBERMARGIN +(AAA.COLUMNMARGIN + AAA.SIZE) * i;
//			Anchors.log("Word: xCoordinate is " + xCoordinates[i] + " for  word " + index + " letter " + i);
		}
		this.originalWord = originalWord;
		elements = new Object[originalWord.length()];
		// pick the anchor positions
		String pickString = "012345".substring(0, originalWord.length());
		for (int i = 0; i < numberOfAnchors[originalWord.length() - 4]; i++)
		{
			int rnd = ToMarUtils.getRnd(pickString.length());
			int a = (new Integer(pickString.substring(rnd, rnd + 1))).intValue();
			pickString = pickString.substring(0, rnd) + pickString.substring(rnd + 1);
			elements[a] = new Anchor(originalWord.substring(a, a + 1), xCoordinates[a], yCoordinate);
		}
		// generate letterHolders and unusedLetters for the rest
		for (int i = 0; i < pickString.length(); i++)
		{
			int a = (new Integer(pickString.substring(i, i + 1))).intValue();
			elements[a] = new LetterHolder(xCoordinates[a], yCoordinate);
			new Letter(originalWord.substring(a, a + 1));
		}
	}
	/**
	 * @return Returns the elements.
	 */
	public Object[] getElements()
	{
		return elements;
	}
	public String getValue()
	{
		String returnWord = "";
		for (int i = 0; i < elements.length; i++)
		{
			if (elements[i].getClass().getName().equals("Anchor"))
			{
				returnWord += ((Anchor) elements[i]).getLabel();
			}
			else if (((LetterHolder) elements[i]).getLetter() != null)
			{
				returnWord += ((LetterHolder) elements[i]).getLetter().getLabel();
			}
			else
			{
				returnWord += ((LetterHolder) elements[i]).getLabel();
			}
		}	
		return returnWord;
	}	
		/**
	 * @return Returns the originalWord.
	 */
	public void showOriginalWord()
	{
		for (int i = 0; i < originalWord.length(); i++)
		{
			if (elements[i].getClass().getName().equals("LetterHolder"))
			{
				((LetterHolder) elements[i]).setLabel(originalWord.substring(i, i+1));
				((LetterHolder) elements[i]).setLetter(null);
			}	
		}
	}
	public void drawOriginalWord(Graphics og)
	{
//		int top = elements.length - 1;
		og.setFont(new Font("Verdana", Font.BOLD, 14));
		og.setColor(tmColors.DARKBLUE);
//		int y = (elements[top].getClass().getName().equals("Anchor")) ? ((Anchor) elements[top]).getY() + (AAA.SIZE/2) : ((LetterHolder) elements[top]).getY() + (AAA.SIZE/2) + 9;
 		og.drawString(originalWord, baseX + ((MAXWORDSIZE + 1) * AAA.SIZE) , yCoordinate + (int)(AAA.SIZE * .75)  );
	}
	public void draw(Graphics og)
	{
		og.setFont(new Font("Verdana", Font.PLAIN, 10));
		og.drawString("" + (index + 1), baseX, yCoordinate + 20);
	
		for (int i = 0; i < elements.length; i++)
		{
			if (elements[i].getClass().getName().equals("Anchor"))
			{
				((Anchor) elements[i]).draw(og);
			}
			else 
			{
				((LetterHolder) elements[i]).draw(og);
			}
		}
	}
}


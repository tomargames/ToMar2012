import java.awt.Graphics;

public class Puzzle
{
	private Slot[] puzzle;
	private String[] values;
	private int currentIndex;
	public static final String stringValues = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
	public static final int ERROR = 0;
	public static final int NA = 1;
	public static final int CORRECT = 2;
	public static final int SOLVED = 3;

	public Puzzle(int size)
	{
		String indexString = stringValues;	// use to pick a letter to display in the puzzle
		String valueString = stringValues;	// use to pick a slot to display the letter in 
		/** pick size number of different slots (identified by index), store in puzzle
		 *  set each one to a value, and set its status to SHOWING  
		 */
		puzzle = new Slot[size];
		values = new String[size];
		for (int i = 0; i < size; i++)
		{
			int rnd = ToMarUtils.getRnd(indexString.length());
			int index = chrToInt(indexString.toString().substring(rnd, rnd+1));
//			HEX.log("i is " + i + ", rnd is " + rnd + ", index is " + index + ", size is " + size);
			puzzle[i] = Slot.getSlots()[index];
			indexString = removeChar(indexString,rnd);
			rnd = ToMarUtils.getRnd(valueString.length());
			puzzle[i].setLabel(valueString.toString().substring(rnd, rnd+1));
			valueString = removeChar(valueString,rnd);
			puzzle[i].setStatus(Slot.SHOWING);
			values[i] = puzzle[i].getLabel();
		}
		// sort the values in the puzzles
		ToMarUtils.arraySort(values);
		currentIndex = 0;
	}
	public int checkAnswer(int x, int y)
	{
		for (int i = 0; i < puzzle.length; i++)
		{
			if (puzzle[i].clicked(x, y))
			{
				if (puzzle[i].getLabel().equalsIgnoreCase(values[currentIndex]))
				{
					puzzle[i].setStatus(Slot.SHOWING);
					currentIndex += 1;
					return (currentIndex == puzzle.length) ? SOLVED : CORRECT;
				}
				else if (puzzle[i].getStatus() == Slot.HIDING)
				{
					puzzle[i].setStatus(Slot.ERROR);
					show();
					return ERROR;
				}
			}
		}
		return NA;
	}
	private void show()
	{
		for (int i = 0; i < puzzle.length; i++)
		{
			if (puzzle[i].getStatus() == Slot.HIDING)
			{	
				puzzle[i].setStatus(Slot.REVEALING);
			}	
		}
	}
	public void hide()
	{
		for (int i = 0; i < puzzle.length; i++)
		{
			puzzle[i].setStatus(Slot.HIDING);
		}
	}
	private String removeChar(String s, int index)
	{
		return (s.substring(0, index) + s.substring(index + 1));
	}
	public Slot[] getPuzzle()
	{
		return puzzle;
	}

	public void setPuzzle(Slot[] puzzle)
	{
		this.puzzle = puzzle;
	}
	
	public static int chrToInt(String chr)
	{
		try
		{
//			HEX.log("chr is " + chr + ", string is " + stringValues + ", index is " + stringValues.indexOf(chr));
			return stringValues.indexOf(chr);
		}
		catch (Exception e)
		{
			return 0;
		}
	}
	public static String intTochar(int i)
	{
		return stringValues.substring(i, i+1);
	}
	public void draw(Graphics og)
	{
		for (int i = 0; i < puzzle.length; i++)
		{
			puzzle[i].draw(og);
		}
	}
	public int getCurrentIndex()
	{
		return currentIndex;
	}
	public void setCurrentIndex(int currentIndex)
	{
		this.currentIndex = currentIndex;
	}
}

import java.awt.*;
import java.util.*;
public class Card
{
	public static final String GAMEOVER = "No more sets.";
	public static final int NOGOOD = 0;
	public static final int ALLSAME = 1;
	public static final int ALLDIFFERENT = 2;
	private static Color colors[] = {tmColors.DARKRED, tmColors.DARKGREEN, tmColors.PURPLE};
	private static Color selectColor = tmColors.LIGHTGRAY;
	private static Color normalColor = tmColors.CREAM;
	private static final int margin = 20;
	private static final int cardWidth = 80;
	private static final int cardHeight = 120;
	private int number;
	private int color;
	private int shading;
	private int shape;
	private int row;
	private int column;
	private boolean selected;
	private static Vector layout;
	private static Card[] cards;				// shuffled deck
	private static Card[] deck;					// unshuffled deck
	private static int numberSelected;
	private static int cardPointer;
	public final static int TOTALCARDS = 81;
	private static String message = "";
	private static int points = 0;
	private static int sets = 0;
	private static int[] hints;
	private static boolean[] hinted = {false, false};
	private static final int SORTPOINTS = 5;
	private static final int HINTPOINTS = 10;
	private static final int SETPOINTS = 30;
	private static final int DIFFICULTY = 10;
	public Card()
	{
		selected = false;
	}
	public boolean checkClick(int clickX, int clickY)
	{
		int x = 30 + column * (margin + cardWidth);
		int y = 30 + row * (margin + cardHeight);
		if (clickX >= x && clickX <= x + cardWidth
		&&  clickY >= y && clickY <= y + cardHeight)
		{
			selected = (selected == false) ? true : false;
			if (selected == true)
			{
				numberSelected += 1;
				if (numberSelected > 3)
				{
					selected = false;
					numberSelected = 3;
				}	
			}
			else
			{
				numberSelected -= 1;
			}
			return true;
		}
		return false;
	}	
	
	public static String checkSets()
	{	
		if (numberSelected == 3)
		{
			int[] selectedCards = new int[3];
			int idx = 0;
			for (int i = 0; i < layout.size(); i++)
			{
				if (Card.getCard(i).getSelected())
				{
//					System.out.println("Selected position " + i + " as " + idx);
					selectedCards[idx++] = i;
				}
			}
			int p = Card.goodSet(selectedCards[0], selectedCards[1], selectedCards[2], false);
			if (p > 0)
			{
				// add difficulty points for layout size
				p += DIFFICULTY * ToMarUtils.absoluteValue(layout.size() - 12);
				points += p;
				message = "Set: " + p + " points";
				sets += 1;
				for (int i = 0; i < 3; i++)
				{	
//					System.out.println("selectedCards[i] = " + selectedCards[i] + ", i = " + i);
					layout.removeElementAt(selectedCards[i] - i);
				}
				numberSelected = 0;
				if (Card.checkAndAdd() == 99)
				{
					message = GAMEOVER;
				}
				else
				{
					doCoordinates();
				}
			}
			else
			{
				message = "Invalid set";
			}
		}	
		else
		{
			message = "Selected: " + numberSelected;
		}	
		return message;
	}
	
	public static int getCardPointer()
	{
		return cardPointer;
	}	
	public void draw(Graphics g)
	{
		int x = 30 + column * (margin + cardWidth);
		int y = 30 + row * (margin + cardHeight);
		if (selected)
		{	
			g.setColor(selectColor);
		}
		else
		{	
			g.setColor(normalColor);
		}	
		g.fillRect(x, y, cardWidth, cardHeight);
		g.setColor(Color.black);
		g.drawRect(x, y, cardWidth, cardHeight);
		g.setColor(colors[this.color]);
		int yFactor = 0;
		if (this.number == 0)
		{
			yFactor = 25;
		}
		else if (this.number == 1)
		{
			yFactor = 12;
		}	
		for (int i = 1; i < this.number + 2; i++)
		{	
			if (shape == 0)
			{
				if (shading == 0)
				{	
					g.drawRect(x + 30, y + 25 * i + yFactor, 20, 20);
					g.drawRect(x + 30-1, y + 25 * i + yFactor-1, 22, 22);
				}
				else if (shading == 1)
				{	
					g.fillRect(x + 30, y + 25 * i + yFactor, 20, 20);
				}
				else 
				{	
					g.fillRect(x + 38, y + 25 * i + 8 + yFactor, 5, 5);
					g.drawRect(x + 30, y + 25 * i + yFactor, 20, 20);
					g.drawRect(x + 30-1, y + 25 * i + yFactor-1, 22, 22);
				}
			}
			else if (shape == 1)
			{
				if (shading == 0)
				{	
					g.drawRoundRect(x + 30, y + 25 * i + yFactor, 20, 20, 20, 20);
					g.drawRoundRect(x + 30-1, y + 25 * i + yFactor-1, 22, 22, 22, 22);
				}
				else if (shading == 1)
				{	
					g.fillRoundRect(x + 30, y + 25 * i + yFactor, 20, 20, 20, 20);
				}
				else 
				{	
					g.fillRect(x + 38, y + 25 * i + 8 + yFactor, 5, 5);
					g.drawRoundRect(x + 30, y + 25 * i + yFactor, 20, 20, 20, 20);
					g.drawRoundRect(x + 30-1, y + 25 * i + yFactor-1, 22, 22, 22, 22);
				}
			}
			else
			{
				int xs[] = {x + 40, x + 55, x + 25, x + 40};
				int ys[] = {y + 25 * i + yFactor, y + 25 * i + 20 + yFactor,
							y + 25 * i + 20 + yFactor, y + 25 * i + yFactor};
				int x2[] = {x + 40-1, x + 55-1, x + 25-1, x + 40-1};
				int y2[] = {y + 25 * i + yFactor-1, y + 25 * i + 20 + yFactor+1,
							y + 25 * i + 20 + yFactor+1, y + 25 * i + yFactor-1};
				if (shading == 0)
				{	
					g.drawPolygon(xs, ys, 4);
					g.drawPolygon(x2, y2, 4);
				}
				else if (shading == 1)
				{	
					g.fillPolygon(xs, ys, 4);
				}
				else 
				{	
					g.fillRect(x + 38, y + 25 * i + 10 + yFactor, 5, 5);
					g.drawPolygon(xs, ys, 4);
					g.drawPolygon(x2, y2, 4);
				}
			}	
		}
	}	
	public void setSelected(boolean selected)
	{
		this.selected = selected;
	}
	public boolean getSelected()
	{
		return this.selected;
	}	
	public void setNumber(int number)
	{
		this.number = number;
	}
	public void setColor(int color)
	{
		this.color = color;
	}
	public void setShading(int shading)
	{
		this.shading = shading;
	}
	public void setShape(int shape)
	{
		this.shape = shape;
	}
	public int getNumber()
	{
		return this.number;
	}
	public int getColor()
	{
		return this.color;
	}
	public int getShading()
	{
		return this.shading;
	}
	public int getShape()
	{
		return this.shape;
	}	
	public static void createDeck()
	{	
		cardPointer = points = sets = 0;
		deck = new Card[TOTALCARDS];
		// create the string of 81 cards to be shuffled
		for (int color = 0; color < 3; color++)
		{
			for (int shape = 0; shape < 3; shape++)
			{
				for (int shading = 0; shading < 3; shading++)
				{
					for (int number = 0; number < 3; number++)
					{
						deck[cardPointer] = new Card();
						deck[cardPointer].setColor(color);
						deck[cardPointer].setShape(shape);
						deck[cardPointer].setShading(shading);
						deck[cardPointer].setNumber(number);
						cardPointer += 1;
					}
				}
			}
		}	
	}	
	public static void createLayout()
	{
		cards = new Card[TOTALCARDS];
		String shuffler = "00010203040506070809101112131415161718192021222324252627282930" +
							"313233343536373839404142434445464748495051525354555657585960" +
							"6162636465666768697071727374757677787980";
		// shuffle the cards and put them in the cards array
		for (int i = 0; i < TOTALCARDS; i++)
		{
			int rnd = ToMarUtils.getRnd(TOTALCARDS - i) * 2;
			String cardString = shuffler.substring(rnd, rnd + 2);
			shuffler = shuffler.substring(0, rnd) + shuffler.substring(rnd + 2, shuffler.length());
			cards[i] = deck[Integer.parseInt(cardString)];
			cards[i].setSelected(false);
		}
		// put the first 12 cards in the layout
		layout = new Vector(12);
		for (cardPointer = 0; cardPointer < 12; cardPointer++)
		{
			layout.addElement(new Integer(cardPointer));
		}	
		hints = new int[2];
		Card.checkAndAdd();
		doCoordinates();
		numberSelected = 0;
		points = sets = 0;
	}
	public static Vector getLayout()
	{
		return layout;
	}
	public static int evaluate(int a, int b, int c, String methodName)
	{
		Card card1 = cards[Card.getIndex(a)];
		Card card2 = cards[Card.getIndex(b)];
		Card card3 = cards[Card.getIndex(c)];
		try
		{
			int value1 = ((Integer) card1.getClass().getMethod(methodName, null).invoke(card1, null)).intValue();
			int value2 = ((Integer) card2.getClass().getMethod(methodName, null).invoke(card2, null)).intValue();
			int value3 = ((Integer) card3.getClass().getMethod(methodName, null).invoke(card3, null)).intValue();
			if (value1 == value2 & value1 == value3)
			{
				return ALLSAME;
			}
			if (value1 != value2 & value1 != value3 & value2 != value3)
			{
				return ALLDIFFERENT;
			}
		}
		catch (Exception e)
		{
			System.out.println("ERROR!!: " + e);
		}
		return NOGOOD;
	}
	public static int goodSet(int a, int b, int c, boolean setHint)
	{
		int difficulty = 0;
		int passed = 0;
		// check shading
		int test = evaluate(a, b, c, "getShading");
		if (test > NOGOOD)
		{
			difficulty = (test == ALLDIFFERENT) ? difficulty + 1 : difficulty;
			passed += 1;
			test = evaluate(a, b, c, "getColor");
			if (test > NOGOOD)
			{
				difficulty = (test == ALLDIFFERENT) ? difficulty + 1 : difficulty;
				passed += 1;
				test = evaluate(a, b, c, "getNumber");
				if (test > NOGOOD)
				{
					difficulty = (test == ALLDIFFERENT) ? difficulty + 1 : difficulty;
					passed += 1;
					test = evaluate(a, b, c, "getShape");
					if (test > NOGOOD)
					{
						difficulty = (test == ALLDIFFERENT) ? difficulty + 1 : difficulty;
						passed += 1;
					}
				}
			}	
		}
		if (passed < 4)
		{
			return NOGOOD;
		}
		if (setHint)
		{	
			hints[0] = b;
			hints[1] = ToMarUtils.getRnd(2) == 0 ? a : c;
			hinted[0] = false;
			hinted[1] = false;
		}	
		return SETPOINTS + DIFFICULTY * (difficulty - 1);	
	}	
	public static int checkAndAdd()
	{
		boolean setFound = false;
		while (setFound == false)
		{	
			for (int i = 0; i < layout.size(); i++)
			{
				for (int j = i + 1; j < layout.size(); j++)
				{
					for (int k = j + 1; k < layout.size(); k++)
					{
						if (goodSet(i, j, k, true) > 0)
						{
							setFound = true;
							break;
						}
					}
					if (setFound == true) break;
				}	
				if (setFound == true) break;
			}
			if (setFound == false || layout.size() < 12)	
			{	// add 3 more cards if you can
				if (cardPointer < 80)
				{	
					layout.addElement(new Integer(cardPointer++));
					layout.addElement(new Integer(cardPointer++));
					layout.addElement(new Integer(cardPointer++));
				}
				else if (!setFound)
				{
					return 99;
				}	
			}	
		}	
		return cardPointer;
	}
	private static void doCoordinates()
	{
		int columns = Card.getLayout().size()/3;
		for (int i = 0; i < layout.size(); i++)
		{
			(Card.getCard(i)).setColumn(i % columns);
			(Card.getCard(i)).setRow(i / columns);
		}
	}	
	public static Card getCard(int idx)
	{
		return cards[getIndex(idx)];
	}	
	public static int getIndex(int idx)
	{
		return ((Integer) layout.elementAt(idx)).intValue();
	}
	public static int getPoints()
	{
		return points;
	}	
	public static void getHint()
	{	
		if (hinted[0] & hinted[1])
		{
			return;
		}	
		// clear all current selections
		for (int i = 0; i < layout.size(); i++)
		{
			Card.getCard(i).setSelected(false);
		}	
		points -= HINTPOINTS;
		if (Card.hinted[0])
		{
			Card.getCard(hints[0]).setSelected(true);
			Card.getCard(hints[1]).setSelected(true);
			Card.hinted[1] = true;
			numberSelected = 2;
		}
		else
		{
			Card.hinted[0] = true;
			Card.getCard(hints[0]).setSelected(true);
			numberSelected = 1;
		}	
	}	
	public static void addBonus(int bonus)
	{
		points += bonus;
	}	
	public static void sort(String methodName)
	{
		points -= SORTPOINTS;
		int[] sortedCounts = {0, 0, 0};
		Vector newLayout = new Vector();
		try
		{
			for (int n = 0; n < 3; n++)
			{
				for (int i = 0; i < layout.size(); i++)
				{
					if (((Integer) Card.getCard(i).getClass().getMethod(methodName, null).invoke(Card.getCard(i), null)).intValue() == n)
					{
	//					System.out.println("Adding position " + i);
						newLayout.addElement(new Integer(Card.getIndex(i)));
						cards[Card.getIndex(i)].setRow(n);
						cards[Card.getIndex(i)].setColumn(sortedCounts[n]++);
					}
				}
			}
		}
		catch (Exception e)
		{
			System.out.println("ERROR!!: " + e);
		}
		layout = newLayout;
		Card.checkAndAdd();
	}
	public int getColumn()
	{
		return column;
	}
	public void setColumn(int column)
	{
		this.column = column;
	}
	public int getRow()
	{
		return row;
	}
	public void setRow(int row)
	{
		this.row = row;
	}
	public static int getSets()
	{
		return sets;
	}
	public static void setSets(int sets)
	{
		Card.sets = sets;
	}
	public static void setLayout(Vector layout)
	{
		Card.layout = layout;
	}
}	

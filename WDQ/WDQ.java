/*
	A basic extension of the java.applet.Applet class
 */
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import java.net.*;

public class WDQ extends java.applet.Applet 
    implements KeyListener, MouseListener
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final int HORIZONTAL = 1;
	private static final int VERTICAL = 0;
	private static int numRows = 25;
	private static int numCols = 25;
	public static int cWIDTH = 17;
	public static int cHEIGHT = 17;
	private static int WIDTH = 800;
	private static int HEIGHT = 500;
	public static int MARGIN = 30;
	private static int points;
	private static final int numLetters = 6;
	private static final int numBandits = 5;
	private static int letterCount;
    private Image offscreenImg;
    private Graphics og;
	private int stage;
	private static final int NOTSTARTED = 0;
	private static final int INMAZE = 1;
	private static final int UNSCRAMBLING = 2;
	private static final int SOLVED = 3;
	private static final int GAMEOVER = 4;
	private String textMsg1;
	private String textMsg2;
	private static Cell[] allCells;
	public static Color fgColor;
	public static Color bgColor;
	private Vector words6;
	private String answerWord;
	private Thing you;
	private Thing[] bandits;
	private int timePassed;
	private int answerLetters[] = {0, 0, 0, 0, 0, 0};
	private int answerBlanks[] = {0, 0, 0, 0, 0, 0};
	private int boxX[] = {0, 0, 0, 0, 0, 0};
	private int boxSize = 25;
	private int leftMargin;
	private int banditCount;
	private String guessWord;
	private tmButton button;
	public int ENDOFMAZE = 0;
	private long startSec;
	private String message;
	
	public void init()
	{
		WIDTH = Integer.parseInt(this.getParameter("WIDTH"));
		HEIGHT = Integer.parseInt(this.getParameter("HEIGHT"));
		fgColor = tmColors.BLACK;
		bgColor = ToMarUtils.toMarBackground;
		this.addKeyListener(this);
		this.addMouseListener(this);
		this.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
		this.setBackground(bgColor);
		this.setSize(WIDTH,HEIGHT);
        offscreenImg = createImage(getSize().width, getSize().height);
        og = offscreenImg.getGraphics();
		//read the word files in
		String ServerPath = this.getDocumentBase().toString();
		ServerPath = ServerPath.substring(0,ServerPath.lastIndexOf("/"));
		ServerPath = ServerPath.substring(0,ServerPath.lastIndexOf("/") + 1) + "Words/";
		words6 = readFile(ServerPath + "words6.txt");
		stage = NOTSTARTED;
		leftMargin = numCols * cWIDTH + (5 * MARGIN);
		ENDOFMAZE = numCols * (numRows - 1);
		button = new tmButton(leftMargin, MARGIN + 300, tmColors.OLIVE, 120, 40, "Give Up"); 
		message = this.getParameter("message");
		message = (message == null) ? "Welcome to WordQuest!" : message;
		message += " Click to start...";
	}
    public void update(Graphics g)
    {
        og.setColor(bgColor);
        og.fillRect(0, 0, WIDTH, HEIGHT);
        paint(g);
    }
    public void giveUp()
	{
		if (stage < SOLVED)
		{
	    	for (int i = 0; i < numLetters; i++)
    		{	
    			answerBlanks[i] = (answerWord.charAt(i));
    		}
    		stage = GAMEOVER;
			textMsg1 = "There you go!";
			textMsg2 = "Click below to try again...";
			button.setLabel("Try again");
    		repaint();
		}
    }	
    
	private void doRound(int direction)
	{
//		ToMarUtils.log("in doRound, stage = " + stage);
		if (stage > NOTSTARTED && stage < SOLVED)
		{
			timePassed = (int)((new Date()).getTime() - startSec)/1000;
			if (stage == INMAZE)
			{	
				// move you
				you.move(direction);
				// see if you hit an answer letter
				if (allCells[you.getLocation()].getState() > 0)
				{
					answerLetters[letterCount++] = allCells[you.getLocation()].getState();
					allCells[you.getLocation()].setState(0);
					int p = (timePassed < 300) ? (300 - timePassed) : 10;
//					ToMarUtils.log("" + numLetters + "-letter game, points += " + p); 
					points += p;
				}	
				// move the bandits
				for (int b = 0; b < numBandits; b++)
				{
					bandits[b].move(ToMarUtils.getRnd(5));
					if (you.getLocation() == bandits[b].getLocation())
					{
						you.setLocation(0);
						textMsg1 = "A bandit got you!!!";
						textMsg2 = "";
						direction = Thing.NOTMOVING;
					}	
				}	
				if (you.getLocation() == ENDOFMAZE) 
				{
					if (letterCount == numLetters)
					{	
						int p = (timePassed < 300) ? (300 - timePassed) : 10;
//						ToMarUtils.log("" + numLetters + "-letter game, points += " + p + " out of maze"); 
						points += p;
						stage = UNSCRAMBLING;
						textMsg1 = "Now click on the letters in the";
						textMsg2 = "right order to make a word...";
						letterCount = 0;
					}
					else
					{
						textMsg1 = "You still need to collect more";
						textMsg2 = "letters before you can exit...";
					}	
				}	
			}
		}
		repaint();
	}
	private int getLocation(int r, int c)
	{
		return (r * numCols) + c;
	}
	public int getRow(int location)
	{
		return  location / getNumCols();
	}
	public int getCol(int location)
	{				   
		return location % getNumCols();
	}
	private void reInit()
	{
		you = new Thing(0, tmColors.GREEN);
		guessWord = "";
		answerWord = (String) words6.elementAt(ToMarUtils.getRnd(words6.size()));
		bandits = new Thing[numBandits];
		for (int i = 0; i < 6; i++)
		{
			answerLetters[i] = 0;
			answerBlanks[i] = 0;
			boxX[i] = leftMargin + (i * (boxSize + 3));
		}
		resetAllCells();
		String pickString = createPickString();
		while (pickString.length() > 0)
		{
			int i = ToMarUtils.getRnd(pickString.length()/3) * 3;
			int id1 = Integer.parseInt(pickString.substring(i, i + 3));
			int w = ToMarUtils.getRnd(2);
			pickString = pickString.substring(0, i) + pickString.substring(i + 3, pickString.length());
			int id2 = id1;
			if (!(getCol(id1) == numCols - 1 && getRow(id1) == numRows - 1))
			{
				if (getCol(id1) == numCols - 1)
				{	
					w = HORIZONTAL;
				}	
				else if (getRow(id1) == numRows - 1)
				{	
					w = VERTICAL;
				}	
				if (w == HORIZONTAL)
				{
					id2 = id1 + numCols;
					checkID(id1, id2);
					allCells[id1].setHoriz(false);	
				}	
				else
				{
					id2 = id1 + 1;
					checkID(id1, id2);
					allCells[id1].setVert(false);
				}
			}
		}	
		//open up exit
		allCells[numCols * (numRows - 1)].setHoriz(false);
		letterCount = 0;
		banditCount = 0;
		//open up 35 blocks, place letters, and bandits
		for (int i = 0; i < 35; i ++)
		{
			boolean opened = false;
			while(!opened)
			{
				int rr = ToMarUtils.getRnd(numRows - 3) + 2;
				int cc = ToMarUtils.getRnd(numCols - 3) + 2;
				int id = getLocation(rr, cc);
				if (i % 2 == 0)
				{
					if (allCells[id].getVert())
					{
						allCells[id].setVert(false);
						opened = true;
					}
				}
				else if (allCells[id].getHoriz())
				{
					allCells[id].setHoriz(false);
					opened = true;
				}
				if (letterCount < numLetters && allCells[id].getState() == Cell.FREE)
				{
					char ch = answerWord.charAt(letterCount);
					allCells[id].setState(ch);
					letterCount++;
				}
				else if (banditCount < numBandits)
				{
					bandits[banditCount++] = new Thing(id, tmColors.RED);
				}
			}
		}	
		points = 0;
		timePassed = 0;
		you.setLocation(0);
		stage = INMAZE;
		letterCount = 0;
		textMsg1 = "Use the arrow keys to move...";
		textMsg2 = "";
		repaint();
		startSec = (new Date()).getTime();
	}
	private void resetAllCells()
	{
		allCells = new Cell[numCols * numRows];
		for (int r = 0; r < numRows; r++)
		{
			for (int c = 0; c < numCols; c++)
			{
				allCells[getLocation(r, c)] = new Cell(getLocation(r, c));
			}	
		}
	}	
	private String createPickString()
	{
		String pickString = "000001002003004005006007008009";
		for (int i = 10; i < 100; i++)
		{
			pickString += "0" + i;
		}	
		for (int i = 100; i < numCols * numRows; i++)
		{
			pickString += "" + i;
		}
		return pickString;
	}	
	private void checkID(int id1, int id2)
	{
//		ToMarUtils.log("checkId, id1 = " + id1 + ", id2 = " + id2 + ", size is " + allCells[id2].getPathVector().size());
		// checks to see if there's a path between id1 and id2
		// add the elements of v2 to v1 - join the two vectors
		for (int i = 0; i < allCells[id2].getPathVector().size(); i++)
		{
			Integer I = (Integer) allCells[id2].getPathVector().elementAt(i);
			allCells[id1].getPathVector().addElement(I);
		}	
	}				
    public void paint(Graphics g)
    {
		if (stage > NOTSTARTED)
		{	
			// draw the cells
			for (int r = 0; r < numRows; r++)
			{
				for (int c = 0; c < numCols; c++)
				{
					allCells[getLocation(r, c)].draw(og, r, c);
				}
			}
			og.setFont(new Font("Verdana",Font.PLAIN,18));
			og.setColor(Color.blue);
			og.drawString("Time: " + timePassed + " seconds", leftMargin, MARGIN + 25);
			og.drawString("Points: " + points, leftMargin, MARGIN + 70);
			og.setFont(new Font("Verdana",Font.PLAIN,12));
			og.drawString("Exit",20, 445);
			// boxes to hold the letters
			for (int i = 0; i < numLetters; i++)
			{
				og.setColor(tmColors.LIGHTMAGENTA);
				og.fillRect(boxX[i], MARGIN + 100, boxSize, boxSize);
				og.setColor(Color.black);
				og.drawRect(boxX[i], MARGIN + 100, boxSize, boxSize);
				og.drawLine(boxX[i], MARGIN + 160, boxX[i] + boxSize, MARGIN + 160);
				if (answerLetters[i] > 0)
				{
					og.setFont(new Font("Verdana",Font.PLAIN,20));
					char ch = (char) answerLetters[i];
					og.drawString("" + ch, boxX[i] + 5, MARGIN + 120);
				}	
				if (answerBlanks[i] > 0)
				{
					og.setFont(new Font("Verdana",Font.PLAIN,20));
					char ch = (char) answerBlanks[i];
					og.drawString("" + ch, boxX[i] + 5, MARGIN + 158);
				}	
			}
			if (stage < UNSCRAMBLING)
			{
				you.draw(og);
				for (int i = 0; i < numBandits; i++)
				{
					bandits[i].draw(og);
				}		
			}	
			// message line
			og.setColor(Color.black);
			og.setFont(new Font("Verdana",Font.PLAIN,14));
			og.drawString(textMsg1,leftMargin, MARGIN + 200);
			og.drawString(textMsg2,leftMargin, MARGIN + 215);
			if (stage == SOLVED)
			{
				endGame();
			}
			else
			{	
				button.draw(og);
			}	
		}
		else
		{
			og.setColor(Color.black);
			og.setFont(new Font("Verdana",Font.BOLD,16));
			og.drawString(message, 20, 50);
			og.drawString("Each purple square is a letter to be collected on your way through the maze.", 20, 150);
			og.drawString("Don't let the red bandits catch you, though!", 20, 180);
			og.drawString("When you have all the letters, exit the maze, and then unscramble the word!", 20, 210);
		}
        g.drawImage(offscreenImg, 0, 0, this);
	}

    public void keyTyped(KeyEvent key)
	{
	}	
    public void keyReleased(KeyEvent key)
	{
	}	
    public void keyPressed(KeyEvent key)
    {   
    	if (stage == INMAZE)
    	{	
			textMsg1 = "Use the arrow keys to move...";
			textMsg2 = "";
	        switch (key.getKeyCode())
	        {
	            case 37:
					doRound(Thing.WEST);
	                break;
	            case 39:
					doRound(Thing.EAST);
	                break;
	            case 38:
					doRound(Thing.NORTH);
	                break;
	            case 40:
					doRound(Thing.SOUTH);
	        }
    	}    
    }
	public void mousePressed(MouseEvent e)
	{
        int x = e.getX();
	    int y = e.getY();
		if (stage == NOTSTARTED)
		{
			reInit();
		}
		else
		{
			if (button.clicked(x, y))
			{
				if (stage < SOLVED)
				{
					giveUp();
				}
				else
				{
					stage = NOTSTARTED;
					repaint();
				}
			}
			else if (stage == INMAZE)
			{
				textMsg1 = "Use arrow keys to move...";
				textMsg2 = "";
// the following code is a back door to the word unscrambling part			
//		for (int i = 0; i < numLetters; i++)
//			{	
//				answerLetters[i] = (int)(answerWord.charAt(i));
//			}	
//			stage = UNSCRAMBLING;
			}
			else if (stage == UNSCRAMBLING)
			{
				doRound(0);
		    	for (int i = 0; i < numLetters; i++)
				{	
		    		if (x > boxX[i] && 
						x < boxX[i] + boxSize &&
						y > MARGIN + 100 && 
						y < MARGIN + 100 + boxSize)
					{
		    			if (answerLetters[i] > 0)
						{	
							guessWord += (char) answerLetters[i];
							answerBlanks[letterCount++] = answerLetters[i];
							answerLetters[i] = 0;
							repaint();
						}
						break;
					}
				}	
				boolean wongame = false;
				if (letterCount == numLetters)
				{
					// check the word
					if (!guessWord.equalsIgnoreCase(answerWord))
					{
						wongame = findWord(words6, guessWord);
					}
					else
					{
						wongame = true;
					}
					if (!wongame)
					{
						// put the letters back
						for (int i = 0; i < numLetters; i++)
						{
							answerLetters[i] = answerBlanks[i];
							answerBlanks[i] = 0;
							letterCount = 0;
						}	
						textMsg1 = "Sorry, " + guessWord + " isn't it...";
						textMsg2 = "Try again!";
						guessWord = "";
					}
					else
					{
						int bonus = (timePassed < 300) ? (300 - timePassed) * 10 : 10; 
						points += bonus;
//						ToMarUtils.log("" + numLetters + "-letter game, bonus is " + bonus + ", total is " + points); 
						textMsg1 = "You got it! Your score is " + points + "!";
						textMsg2 = "Click on New Game to play again...";
						stage = SOLVED;
						repaint();
					}
				}	
			}
		}
	}
	public void endGame()
	{
		for (int i = 0; i < 99999; i++)
		{
			repaint();
			stage = GAMEOVER;
		}
		try
		{
			String encName = (this.getParameter("nm")).replaceAll(" ", "%20");
			String fwd = this.getParameter("site") + "WDQ?score=" + points + "&id=" + this.getParameter("id") + "&nm=" + encName + "&tsp=" + ToMarUtils.getDateTimeStamp();
			this.getAppletContext().showDocument(new URL(fwd));
		}
		catch(Exception ee)
		{
			log("Error 1: " + ee);
		}
	}
	public static void log(String s)
	{
		System.out.println(s);
	}
	private boolean findWord(Vector v, String gw)
	{
		for (int j = 0; j < v.size(); j++)
		{
			if (gw.equalsIgnoreCase((String) v.elementAt(j)))
			{
				return true;
			}
		}
		return false;
	}
	public void mouseReleased(MouseEvent e)
	{
	}	
	public void mouseEntered(MouseEvent e)
	{
	}	
	public void mouseExited(MouseEvent e)
	{
	}	
	public void mouseClicked(MouseEvent e)
	{
	}	
	private Vector readFile(String filename)
	{	
		Vector words = new Vector();
		String s = new String();
		try
		{
			URL url = new URL(filename);
			BufferedReader br = 
				new BufferedReader(new InputStreamReader(url.openStream()));
			while   (true)
			{
				s = br.readLine();
				if  (s == null)
				{
					break;
				}
				else
				{
//					log(s);
					words.addElement(s.toUpperCase());
				}
			}
			br.close();
		}	
		catch   (MalformedURLException me)
		{
			log("Malformed URL = " + me);
		}
		catch   (Exception e)
		{
			log("Exception = " + e);
		}
		return words;
	}
	/**
	 * @return Returns the numCols.
	 */
	public static int getNumCols()
	{
		return numCols;
	}

	/**
	 * @return Returns the allCells.
	 */
	public static Cell[] getAllCells()
	{
		return allCells;
	}

	/**
	 * @return Returns the numRows.
	 */
	public static int getNumRows()
	{
		return numRows;
	}

}

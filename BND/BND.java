import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;
import java.util.Vector;

public class BND extends java.applet.Applet
    implements MouseListener, Runnable 
{
	/**
	 *  a new game
	 */
	private static final long serialVersionUID = -51944739177789763L;
	public static Color[] colors = {ToMarUtils.toMarBackground,
									tmColors.PALECYAN,
									tmColors.PALEYELLOW,
									tmColors.PURPLE,
									tmColors.LIGHTYELLOW,
									tmColors.BLACK};
	public static int maxSize = 100;
	public static int margin = 30;
	public static int side = 5;
	public static int time = 2500;
	public static int pointsForPurpleLetter = 3;
	public static int WIDTH = 800;
	public static int HEIGHT = 500;
	public static final int COLUMNTWO = 620;
	public static final int NOTSTARTED = 0;
	public static final int PLAYING = 1;
	public static final int GAMEOVER = 2;
    Image offscreenImg;
    Graphics og;
	String word = "";
	String displayWord = "";
	String[] words;
	bdColumn[] columns;
	Vector selectedC;
	Vector selectedR;
	int points;
	Thread thread;
	int letterCount;
	int gameStage;
	boolean paused;
	tmButton submitButton;
	tmButton pauseButton;
	long startTime;
	String message;
	String encName;
    
    public void init()
    {
		encName = (this.getParameter("nm")).replaceAll(" ", "%20");
		WIDTH = Integer.parseInt(this.getParameter("WIDTH"));
		HEIGHT = Integer.parseInt(this.getParameter("HEIGHT"));
	    setSize(WIDTH,HEIGHT);
	    maxSize = Integer.parseInt(getParameter("SQUARE"));
	    margin = Integer.parseInt(getParameter("MARGIN"));
	    side = Integer.parseInt(getParameter("SIDE"));
	    time = Integer.parseInt(getParameter("TIME"));
		setBackground(colors[0]);
	    addMouseListener(this);
        offscreenImg = createImage(WIDTH, HEIGHT);
        og = offscreenImg.getGraphics();
		message = this.getParameter("message");
		message = (message == null) ? "Welcome to BoonDoggle!" : message;
		//read the word files in
        String ServerPath = this.getDocumentBase().toString();
		ServerPath = ServerPath.substring(0,ServerPath.lastIndexOf("/"));
		ServerPath = ServerPath.substring(0,ServerPath.lastIndexOf("/") + 1) + "Words/";
		Vector w = new Vector();
		readFile(ServerPath + "zynga.txt",w);
//		readFile(ServerPath + "words3.txt",w);
//		readFile(ServerPath + "words4.txt",w);
//		readFile(ServerPath + "words5.txt",w);
//		readFile(ServerPath + "words6.txt",w);
//		readFile(ServerPath + "wordsRest.txt",w);
		words = ToMarUtils.vectorToStringArray(w);
		columns = new bdColumn[maxSize];
		pauseButton = new tmButton(COLUMNTWO, 300, tmColors.GREEN, 80, 40, "Pause");
		submitButton = new tmButton(COLUMNTWO, 400, tmColors.PURPLE, 120, 40, "Submit Word");
		gameStage = NOTSTARTED;
    }
	
    public void reInit()
    {
		for (int i = 0; i < maxSize; i++)
		{
			columns[i] = new bdColumn(i);
		}	
		points = 0;
		clear();
		for (int i = 0; i < maxSize; i++)
		{
			for (int j = 0; j < 3; j++)
			{
				newLetter(i);
			}
		}	
		for (int i = 0; i < 5; i++)
		{
			newLetter();
		}	
		paused = false;
        gameStage = PLAYING;
		thread = new Thread(this);
		thread.start();
    }    
    private void newLetter()
	{
		while (true)
		{
			int rnd = ToMarUtils.getRnd(maxSize);
			if (!columns[rnd].isFull())
			{
				newLetter(rnd);
				break;
			}	
		}	
	}	
    private void newLetter(int col)
	{
		columns[col].addLetter();
	}	
    public void paint(Graphics g)
    {
        og.setFont(new Font("Verdana",Font.PLAIN,24));
        if  (gameStage == NOTSTARTED)
        {
	        og.setColor(colors[5]);
			og.drawString(message, 30, 2*20);
			og.drawString("Form words by clicking on each letter as you spell the words.", 30, 5*20);
			og.drawString("Word letters must be in a cluster of touching letters.", 30, 7*20);
			og.drawString("Each purple letter in the word multiplies the points by 3.", 30, 9*20);
			og.drawString("The game will end when the board is full.", 30, 11*20);
			og.drawString("Click anywhere in this window to start.", 30, 14*20);
        }        
        else
        {
        	if (gameStage == PLAYING)
        	{	
	        	pauseButton.setLabel(paused ? "Resume" : "Pause");
	        	pauseButton.draw(og);
	        	submitButton.draw(og);
        	}	
            //paint all the tiles
	        og.setColor(colors[5]);
	        og.setFont(new Font("Verdana", Font.PLAIN, 24));
	        og.drawString("Points: " + points + "    Word: " + displayWord, 25, 30);
			og.drawString("Letters: " + letterCount, COLUMNTWO, 80);
			og.drawString("Delay: " + time, COLUMNTWO, 120);
	        og.setFont(new Font("Verdana", Font.PLAIN, 20));
			for (int i = 0; i < maxSize; i++)
			{
				for (int j = 0; j < columns[i].size(); j++)
				{	
					((bdLetter) columns[i].elementAt(j)).draw(og);
				}
			}
        }        
        g.drawImage(offscreenImg, 0, 0, this);
    }
    public void update(Graphics g)
    {
        og.setColor(colors[0]);
        og.fillRect(0, 1, WIDTH, HEIGHT);
		paint(g);
    }
	public void submitWord()
	{
		if (word.length() < 3)
		{
			displayWord = "Words must be at least 3 letters long.";
			word = "";
		}
		else
		{	
			boolean found = false;
			word = word.toUpperCase();
			for (int i = 0; i < words.length; i++)
			{
				if (word.equals(words[i]))
				{
					int p = 0;
					found = true;
					for (int j = 0; j < word.length(); j++)
					{
						String ch = word.substring(j, j+1);
						if (ch.equals("X") || ch.equals("Q") || ch.equals("J"))
						{
							p += 3;
						}	
						else if (ch.equals("W")	||  ch.equals("V") || ch.equals("Z"))
						{
							p += 2;
						}	
						else
						{
							p += 1;
						}
					}
					p = dropOut(p);
					p = ToMarUtils.multiplyPoints(p, word.length());
					displayWord += " - " + p;
					word = "";
					points = points + p;
					break;
				}
			}
			if (!found)
			{	
				try
				{
					URL url = new URL(this.getParameter("site") + "Words/log.php?entry=" + word + "&source=BND&name=" + encName);
					this.getAppletContext().showDocument(url,"dummy");
				}
				catch(Exception ex)
				{
					log("BoonDoggle Error 2: " + ex);
				}
				displayWord = word + " is not in our dictionary!";
				word = "";
			}	
		}
		deselect();
		repaint();
	}	
    /*
     * Mouse methods
     */
    public void mousePressed(MouseEvent e) 
    {
        if  (gameStage == NOTSTARTED)
        {
            reInit();
	    }    
	    else if (gameStage == PLAYING)
	    {
            int x = e.getX();
            int y = e.getY();
            if (pauseButton.clicked(x, y))
            {
            	paused = (paused == false) ? true : false;
            }
            else if (submitButton.clicked(x, y))
			{
				submitWord();
            }	
			else if (!paused)
			{	
				for (int i = 0; i < maxSize; i++)
				{
					for (int j = 0; j < columns[i].size(); j++)
					{	
						bdLetter letter = (bdLetter) columns[i].elementAt(j);
						if ((letter).isWithin(x, y))
						{
							if (letter.isSelected())
							{
								deselect();
							}
							else
							{
								boolean found = false;
								if (selectedC.size() == 0)
								{	
									found = true;
								}
								else
								{	
									for (int k = 0; k < selectedC.size(); k++)
									{
										if (letter.isTouching(getRow(k),getCol(k)))
										{
											found = true;
											break;
										}	
									}
								}	
								if (!found)
								{	
									deselect();
								}		
								// add letter to selected letters
								if (selectedC.size() == 0)
								{	
									word = "";
								}
								selectedC.addElement(new Integer(i));
								selectedR.addElement(new Integer(j));
								letter.setSelected(true);
								word += letter.getLetter();
								displayWord = word;
							}
						}	
					}
				}
			}	
        }    
		repaint();
    }
	public void run()
	{
		startTime = ToMarUtils.startTimer();
		long prevSecs = ToMarUtils.getMilliSeconds(startTime);
		while (gameStage == PLAYING)
		{
			if (!paused)
			{	
				boolean emptySpace = false;
				int lCount = 0;
				for (int i = 0; i < maxSize; i++)
				{
					lCount += columns[i].size();
					if (!columns[i].isFull())
					{
						emptySpace = true;
//						break;
					}	
				}
				if (!emptySpace)
				{
					endGame();
				}	
				else
				{
					letterCount = lCount;
					long secs = ToMarUtils.getMilliSeconds(startTime);
					if (secs - prevSecs > time)
					{
						prevSecs = secs;
						time -= pointsForPurpleLetter;
						this.newLetter();
						repaint();
					}	
				}	
			}	
		}	
	}	
	public void deselect()
	{					 
		try
		{
			// deselect all selected letters
			for (int i = 0; i < selectedC.size(); i++)
			{
				((bdLetter) columns[getCol(i)].elementAt(getRow(i))).setSelected(false);
			}	
			clear();
		}
		catch (Exception e)
		{
			System.out.println("deselect: " + e);
		}	
	}
	public void clear()
	{
		selectedC = new Vector();
		selectedR = new Vector();
		word = "";
	}	
	public int getRow(int i)
	{
		return ((Integer)selectedR.elementAt(i)).intValue();
	}	
	public int getCol(int i)
	{
		return ((Integer)selectedC.elementAt(i)).intValue();
	}	
					  
	public int dropOut(int p)
	{					 
		try
		{
			while (selectedC.size() > 0)
			{	
				int highRow = 0;
				int highRowIndex = 0;
				for (int i = 0; i < selectedC.size(); i++)
				{
					if (getRow(i) > highRow)
					{
						highRowIndex = i;
						highRow = getRow(i);
					}
				}	
				if (columns[getCol(highRowIndex)].process(getRow(highRowIndex)))
				{
					p = ToMarUtils.multiplyPoints(p, pointsForPurpleLetter);
				}	
				selectedC.removeElementAt(highRowIndex);
				selectedR.removeElementAt(highRowIndex);
			}	
			selectedC = new Vector();
			selectedR = new Vector();
		}
		catch (Exception e)
		{
			System.out.println("dropOut size = " + selectedC.size() + " Error: " + e);
			clear();
		}	
		return p;
	}
	private void log(String s)
	{
		System.out.println(s);
	}
	public void endGame()
    {
		displayWord = "Game Over               ";
		gameStage = GAMEOVER;
		repaint();
   		try
   		{
			Thread.sleep(5000);
			String fwd = this.getParameter("site") + "BND?score=" + points + "&id=" + this.getParameter("id") + "&nm=" + encName + "&tsp=" + ToMarUtils.getDateTimeStamp();
   			this.getAppletContext().showDocument(new URL(fwd));
   		}
   		catch(Exception e)
   		{
   			log("Error 1: " + e);
   		}
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
	private void readFile(String filename, Vector w)
	{	
        String s = new String();
        try
        {
            URL url = new URL(filename);
		    BufferedReader br = 
				new BufferedReader(new InputStreamReader(url.openStream()));
            boolean eof = false;
            while   (eof == false)
            {
                s = br.readLine();
                if  (s == null)
                {
                    eof = true;
                }
                else
                {
                    w.addElement((s.toUpperCase()).trim());
                }
            }
            br.close();
		}	
		catch   (MalformedURLException me)
		{
            System.out.println("Malformed URL = " + me);
		}
		catch   (Exception e)
		{
            System.out.println("readFile Exception = " + e);
		}
	}
}

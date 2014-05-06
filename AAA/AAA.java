import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import java.net.*;

/*
 * Created on Apr 8, 2005
 * Updated March, 2011 for Facebook API 
 * Updated March, 2012 for PHP interface using Google authentication
 */
public class AAA extends java.applet.Applet	implements MouseListener, Runnable
{
	private static final long serialVersionUID = 1663107798626317480L;
	public static final int SIZE = 34;
	public static final Font LETTERFONT = new Font("Verdana", Font.PLAIN, 24);
	public static final int NUMBERMARGIN = 15;
	public static final int COLUMNMARGIN = 2;
	public static final int ROWMARGIN = 8;
	public static final int TOPMARGIN = 50;
	public static final int TOTALROWS = 10;
	public static final int MAXLEVEL = TOTALROWS * 2;
	public static final int ROUNDER = 10;
	public static final int WIDTH = 790; 
	public static final int HEIGHT = 520;
	private Color BGCOLOR = tmColors.CREAM;
	private Image offscreenImg;
	private Graphics og;
	private Word[] words;
	private Vector wordList;
	int points;
	int level;
	int displayLevel;
	int levelTime;
	int wordsFormed;
	int timeLeft = 0;
	int gameStage = 0;
	public static final int NOTSTARTED = 0;
	public static final int PLAYING = 1;
	public static final int BETWEENROUNDS = 2;
	public static final int GAMEOVER = 3;
	String message = "";
	ToMarButton clearButton;
	ToMarButton mixButton;
	ToMarButton giveUpButton;
	Letter selectedLetter;
	private Thread thread;
	private Color timeColor;
	long startSec;
		
	public static void log(String s)
	{
		System.out.println(ToMarUtils.getDateTimeStamp() + ": " + s);
	}	
	public void init()
	{
		this.setSize(WIDTH, HEIGHT);
		this.setBackground(ToMarUtils.toMarBackground);
		readWordFile();
		clearButton = new ToMarButton(710, 480, 60, "Clear");
		mixButton = new ToMarButton(610, 480, 50, "Mix");
		giveUpButton = new ToMarButton(410, 480, 120, "Give Up");
		clearButton.setHeight(34);
		mixButton.setHeight(34);
		giveUpButton.setHeight(34);
		clearButton.setBgColor(tmColors.CYAN);
		mixButton.setBgColor(tmColors.GREEN);
		giveUpButton.setBgColor(tmColors.DARKPINK);
		message = this.getParameter("message");
		message = (message == null) ? "Welcome to Anchors Away!" : message;
		this.addMouseListener(this);
        offscreenImg = createImage(WIDTH, HEIGHT);
        og = offscreenImg.getGraphics();
	}	
	private void readWordFile()
	{	
		String ServerPath = this.getDocumentBase().toString();
		ServerPath = ServerPath.substring(0,ServerPath.lastIndexOf("/"));
		ServerPath = ServerPath.substring(0,ServerPath.lastIndexOf("/") + 1) + "Words/";
		wordList = new Vector();
		for (int i = 4; i < 7; i++)
		{	
			String filename = ServerPath + "words" + i + ".txt";
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
						wordList.addElement(s.toUpperCase());
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
		}	
	}
	private void newLevel()
	{
		displayLevel += 1;
		level = (displayLevel > MAXLEVEL) ? MAXLEVEL : displayLevel;
		wordsFormed = 0;
		message = "";
		levelTime = 60 + ((level-1) * 17);
		Letter.refreshLetters();
		words = new Word[level];
		for (int i = 0; i < level; i++)
		{
			String wordString = (String) wordList.elementAt(ToMarUtils.getRnd(wordList.size()));
			words[i] = new Word(i, wordString);
		}	
		Letter.setUpLevel(level);
		ToMarUtils.startTimer();
		gameStage = PLAYING;
		timeLeft = levelTime;
		selectedLetter = null;
		giveUpButton.setLabel("Give Up");
		repaint();
		thread = new Thread(this);
		startSec = (new Date()).getTime();
		thread.start();
	}
	private void endGame()
	{
		try
		{
			Thread.sleep(1500);
			String encName = (this.getParameter("nm")).replaceAll(" ", "%20");
			String fwd = this.getParameter("site") + "AAA?score=" + points + "&id=" + this.getParameter("id") + "&nm=" + encName + "&tsp=" + ToMarUtils.getDateTimeStamp();
			this.getAppletContext().showDocument(new URL(fwd));
		}
		catch(Exception e)
		{
			log("Error 1: " + e);
		}
	}
	private void putBackAll()
	{
		for (int word = 0; word < level; word++)
		{
			for (int element = 0; element < words[word].getElements().length; element++)
			{
				if (words[word].getElements()[element].getClass().getName().equals("LetterHolder"))
				{
					LetterHolder holder = (LetterHolder) words[word].getElements()[element];
					if (holder.getLetter() != null)
					{	
						Letter.putLetterBack(holder.getLetter());
						holder.setLetter(null);
					}
				}	
			}
		}
		repaint();
	}	
	
	private boolean checkForWord(String temp)
	{											
		if (temp.indexOf(LetterHolder.INIT) == -1)
		{
			for (int j = 0; j < wordList.size(); j++)
			{
				if (temp.equals(wordList.elementAt(j)))
				{
					return true;
				}
			}
		}
		return false;
	}	
	
	public void mouseClicked(MouseEvent arg0)
	{
	}
	public void mousePressed(MouseEvent e)
	{
		int x = e.getX();
		int y = e.getY();
		if (gameStage == NOTSTARTED)
		{
			level = displayLevel = points = 0;
			newLevel();
		}
		else if  (gameStage == GAMEOVER)
		{
			endGame();
		}
		else if (gameStage == BETWEENROUNDS)
		{
			if (giveUpButton.clicked(x, y))
			{	
				newLevel();
			}	
		}
		else if (gameStage == PLAYING)
		{
			if (clearButton.clicked(x, y))
			{
				putBackAll();
			}
			else if (mixButton.clicked(x, y))
			{
				Letter.mixLetters();
				repaint();
			}
			else if (giveUpButton.clicked(x, y))
			{
				if (("Give Up".equals(giveUpButton.getLabel())))
				{
					giveUpButton.setLabel("Confirm Quit");
					repaint();
				}
				else if ("Confirm Quit".equals(giveUpButton.getLabel()))
				{
					levelTime = 0;
				}
			}
			else
			{	// this is looking at each letter in the letter pool
				giveUpButton.setLabel("Give Up");
				for (int letterIndex = 0; letterIndex < Letter.getLetters().size(); letterIndex++)
				{	// if the letter has already been placed, it will be null
					Letter targetLetter = (Letter) Letter.getLetters().elementAt(letterIndex);
					if (targetLetter != null && targetLetter.clicked(x, y))
					{	// if this letter was already selected, and you click again, deselect it
						if (targetLetter.isSelected())
						{
							deselect();
						}
						else
						{ 	// if another letter was already selected, deselect it
							if (selectedLetter != null)
							{
								deselect();
							}
							// now select this one
							selectedLetter = targetLetter;
							selectedLetter.setSelected(true);
						}
						repaint();
						return;
					}
				}
				wordsFormed = 0;
				// this is looking at each word on the word list
				for (int targetWord = 0; targetWord < level; targetWord++)
				{	// look at each element to see if it's a letterHolder
					for (int targetWordLetter = 0; targetWordLetter < words[targetWord].getElements().length; targetWordLetter++)
					{
						if (words[targetWord].getElements()[targetWordLetter].getClass().getName().equals("Anchor"))
						{
							continue;
						}	
						// for each letterHolder (non-anchor) within the word
						LetterHolder holder = (LetterHolder) words[targetWord].getElements()[targetWordLetter];
						if (holder.clicked(x, y))
						{	// you've clicked on a destination
							Letter currentLetter = holder.getLetter();
							if (currentLetter != null)
							{	//there's already a letter here - return it to the pool
								Letter.putLetterBack(currentLetter);
								holder.setLetter(null);
							}
							if (selectedLetter != null)
							{
								holder.setLetter(selectedLetter);
								Letter.removeLetter(selectedLetter);
								deselect();
							}
							repaint();
							break;
						}
					}
					if (checkForWord(words[targetWord].getValue()))
					{
						wordsFormed += 1;
//						log("word found = " + words[targetWord].getValue() + " making " + wordsFormed + " words");
					}	
				}
				if (wordsFormed == level)		// beat the level
				{
					points += (level * timeLeft);
					points += level * level * 50;	// 50 * level points for each word made
					gameStage = BETWEENROUNDS;
					giveUpButton.setLabel("Start Level " + (displayLevel + 1));
					message = "You've completed Level " + displayLevel + "!";
					repaint();
				}	
			}
		}
	}
	public void deselect()
	{
		selectedLetter.setSelected(false);
		selectedLetter = null;
	}
	public void mouseReleased(MouseEvent arg0)
	{
	}
	public void mouseEntered(MouseEvent arg0)
	{
	}
	public void mouseExited(MouseEvent arg0)
	{
	}	
	public void paint(Graphics g)
	{
		if (gameStage == NOTSTARTED)
		{
			int row = 20;
			og.setColor(tmColors.DARKRED);
			og.setFont(new Font("Verdana",Font.BOLD,18));
			og.drawString(message, 20, 1 * row);
			og.setFont(new Font("Verdana",Font.PLAIN,16));
			og.setColor(tmColors.BLACK);
			og.drawString("In each level, use the letters from the pool to fill", 20, 3*row);
			og.drawString("the blanks and form 4-, 5-, and 6-letter words.", 25, 4*row);
			og.setColor(tmColors.DARKGREEN);
			og.drawString("Click to start.", 20, 180);
		}
		else if (gameStage >= PLAYING)	
		{	
			giveUpButton.draw(og);
			og.setFont(new Font("Verdana",Font.PLAIN, 20));
			og.setColor(Color.black);
			og.drawString("Time: ", 10, 20);
			og.drawString("Level: " + displayLevel, 120, 20);
			og.drawString("Points: " + points, 240, 20);
			og.drawString("Words: " + wordsFormed, 400, 20);
			og.drawString(message, 520, 20);
			og.setColor(timeColor);
			og.drawString("" + timeLeft, 72, 20);
			og.setColor(Color.black);
			for (int i = 0; i < Letter.getLetters().size(); i++)
			{
				if (((Letter) Letter.getLetters().elementAt(i)) != null)
				{
					((Letter) Letter.getLetters().elementAt(i)).draw(og, false);
				}
			}
			for (int i = 0; i < level; i++)
			{
				words[i].draw(og);
			}
			if (gameStage > PLAYING)
			{
				for (int targetWord = 0; targetWord < level; targetWord++)
				{
					words[targetWord].drawOriginalWord(og);
				}
			}
			else
			{
				clearButton.draw(og);
				mixButton.draw(og);
			}
		}
		g.drawImage(offscreenImg, 0, 0, this);
	}
		
	public void update(Graphics g)
	{
		og.setColor(BGCOLOR);
		og.fillRect(0, 0, WIDTH, HEIGHT);
		paint(g);
	}
	public void run()
	{
		while (gameStage == PLAYING)
		{
    		int seconds = (int)((new Date()).getTime() - startSec)/1000;
			timeLeft = levelTime  - seconds;
			repaint();
			if (timeLeft < 0)
			{
				timeLeft = 0;
				for (int i = 0; i < level; i++)
				{
					if (checkForWord(words[i].getValue()))
					{
						points += 50 * level;
					}	
				}	
				gameStage = GAMEOVER;
				message = "Game Over...";
				repaint();
				try
				{
					Thread.sleep(1000 * displayLevel);
				}
				catch(Exception e){}
				endGame();
			}
			if (timeLeft < 11)
			{
				timeColor = Color.red;
			}
			else
			{
				timeColor = Color.black;
			}
		}
	}
}

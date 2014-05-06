import java.awt.*;
import java.awt.event.*;
import java.net.*;
import java.applet.*;


/**
 * This class reads PARAM tags from its HTML host page and sets
 * the color and label properties of the applet. Program execution
 * begins with the init() method. 
 */
public class HEX extends Applet
    implements MouseListener 
{
	private static final long serialVersionUID = -2609361118653956384L;
	private static final int READY = 0;		// "next puzzle" button, old puzzle if you had one
	private static final int VIEWING = 1;	// "ready to play" button, current puzzle
	private static final int GUESSING = 2;	// no button, empty puzzle
	private static final int GAMEOVER = 3;	// stats
	private static final int STARTLEVEL = 4;
	public static final int MAXLEVEL = 26;
    public static Color bgColor;
    private Color textColor;
    private Color infoColor;
    private static final int ROWSIZE = 20;
    private static final int LABELX = 620;
    private static final int INFOX = 720;
    private int points;
    private int level;
    private int stage;
    private int cumulativeWeight = 1;	// for each solve, add the current level of the puzzle into this
    private int strikes;				// add 1 each incorrect click, must be less than MAXSTRIKES
    private int puzzlesSolved;			// the raw number of puzzles solved, add 1 each time one is solved
    private int highestLevel;			// the highest level of puzzled solved so far
    Image offscreenImg;
    Graphics og;
    boolean started = false;
	String message;
	ToMarButton controlButton;
	String[] controlMessages = {"Next puzzle", "Ready", "ERROR", "Click"};
	Puzzle puzzle;
	
    public void init()
    {
	    setSize(Integer.parseInt(getParameter("WIDTH")),Integer.parseInt(getParameter("HEIGHT")));
	    Slot.init();
	    bgColor = tmColors.CREAM;
		textColor = tmColors.DARKBLUE;
		infoColor = tmColors.DARKGREEN;
		setBackground(bgColor);
	    addMouseListener(this);
        offscreenImg = createImage(getSize().width, getSize().height);
        og = offscreenImg.getGraphics();
        controlButton = new ToMarButton(LABELX, 400, 120, "");
		message = this.getParameter("message");
		message = (message == null) ? "Welcome to AlphabetMaze!" : message;
		points = puzzlesSolved = highestLevel = strikes = 0;
	}
	
    private void startLevel(int lev)
    {
		stage = READY;
		level = lev;
		repaint();
    }
    
    public void paint(Graphics g)
    {
        og.setFont(new Font("Verdana",Font.PLAIN,20));
        og.setColor(textColor);
        // print instructions if game hasn't started yet
        if (!started)
		{	
			og.drawString(message, 20, 3 * ROWSIZE);
			og.drawString("This game uses the alphabet to exercise your brain.", 20, 5 * ROWSIZE);
			og.drawString("Memorize each puzzle, so that when it disappears,", 20, 7 * ROWSIZE);
			og.drawString("you can recreate it in alphabetical order.", 30, 9 * ROWSIZE);
			og.drawString("Relax -- time is not a factor!", 20, 11 * ROWSIZE);
			og.drawString("Complete instructions are below.", 20, 15 * ROWSIZE);
			og.drawString("Click anywhere in this window to begin...", 20, 17 * ROWSIZE);
		}	
        else
        {
            og.setColor(textColor);
            og.drawString("Level:", LABELX, 2 * ROWSIZE);
            og.drawString("Strikes:", LABELX, 4 * ROWSIZE);
            og.drawString("Solved:", LABELX, 6 * ROWSIZE);
            og.drawString("Highest:", LABELX, 8 * ROWSIZE);
            og.drawString("Points:", LABELX, 10 * ROWSIZE);
     		og.setColor(infoColor);
            og.drawString("" + level, INFOX, 2 * ROWSIZE);
            og.drawString("" + strikes, INFOX, 4 * ROWSIZE);
            og.drawString("" + puzzlesSolved, INFOX, 6 * ROWSIZE);
            og.drawString("" + highestLevel, INFOX, 8 * ROWSIZE);
            og.drawString("" + points, INFOX, 10 * ROWSIZE);
			puzzle.draw(og);
			if (stage != GUESSING)
			{
				controlButton.setLabel(controlMessages[stage]);
				controlButton.draw(og);
				if (stage == GAMEOVER)
				{
		            og.drawString("Game Over.", LABELX, 12 * ROWSIZE);
		            og.drawString(" |", LABELX, 13 * ROWSIZE);
		            og.drawString(" |", LABELX, 14 * ROWSIZE);
		            og.drawString(" |", LABELX, 15 * ROWSIZE);
		            og.drawString(" |", LABELX, 16 * ROWSIZE);
		            og.drawString(" |", LABELX, 17 * ROWSIZE);
		            og.drawString(" |", LABELX, 18 * ROWSIZE);
		            og.drawString(" V", LABELX, 19 * ROWSIZE);
				}
			}	
        }        
        g.drawImage(offscreenImg, 0, 0, this);
    }
	
    public void update(Graphics g)
    {
        og.setColor(bgColor);
        og.fillRect(0, 1, Integer.parseInt(getParameter("WIDTH")),Integer.parseInt(getParameter("HEIGHT")));
        paint(g);
    }
    /*
     * Mouse methods
     */
    public void mousePressed(MouseEvent e) 
    {
        if  (!started)
        {
       		started = true;
       		level = STARTLEVEL;
    		puzzle = new Puzzle(level);
       		stage = VIEWING;
	    }    
	    else  
	    {
            int x = e.getX();
            int y = e.getY();
            if (controlButton.clicked(x, y))
            {
            	if (stage == GAMEOVER)
            	{
            		endGame();
            	}
            	else if (stage == READY)
            	{
            		puzzle = new Puzzle(level);
               	    stage = VIEWING;
            	}
            	else
            	{
            		puzzle.hide();
            		stage = GUESSING;
            	}
            }
            else if (stage == GUESSING)
            {
            	int result = puzzle.checkAnswer(x, y); 
            	if ( result == Puzzle.ERROR)
            	{
            		points += cumulativeWeight;
            		cumulativeWeight += puzzle.getCurrentIndex();
            		if (++strikes == 3)
            		{
            	        stage = GAMEOVER;
            		}
            		else
            		{
            			startLevel(level - 1);
            		}
            	}
            	else if (result == Puzzle.SOLVED)
            	{
            		points += level * cumulativeWeight;
            		cumulativeWeight += level;
            		puzzlesSolved += 1;
            		highestLevel = (level > highestLevel) ? level : highestLevel;
            		if (level == MAXLEVEL)
            		{
            			startLevel(level);
            		}
            		else
            		{	
            			startLevel(level + 1);
            		}	
            	}
            }
        }
        repaint();
    }

    public void endGame()
    {
		try
		{
			if (points > 0)
			{	
				String encName = (this.getParameter("nm")).replaceAll(" ", "%20");
				String fwd = this.getParameter("site") + "HEX?score=" + points + "&id=" + this.getParameter("id") + "&nm=" + encName + "&tsp=" + ToMarUtils.getDateTimeStamp();
				this.getAppletContext().showDocument(new URL(fwd));
			}
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
    public static void log(String message)
    {
    	System.out.println(message);
    }
}

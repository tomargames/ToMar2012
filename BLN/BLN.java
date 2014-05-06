/*
	A basic extension of the java.applet.Applet class
 */
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.net.*;

public class BLN extends java.applet.Applet 
    implements MouseListener, MouseMotionListener
{
	/**
	 *  
	 */
	private static final long serialVersionUID = -1296686598681274246L;
	public static final int NUMBEROFROWS = 10;
	public static final int NUMBEROFCOLUMNS = 15;
	public static final int NUMBEROFCOLORS = 5;
	public static final int COLUMNTWO = 600;
	public static int WIDTH = 800;
	public static int HEIGHT = 500;
	private int rightMost;
	private int points;
	private int moves;
	private int balloons;
	private int positions[] = new int[NUMBEROFCOLUMNS];
	private int highestScore;
	private int lowestCount;
	private Vector columns;
	private Vector selects;
	private Vector history;
    private Image offscreenImg;
    private Graphics og;
	private boolean started;
	private String message;
	private tmButton newGame;
	private tmButton backUp;
	private tmButton startOver;
	private boolean dialogInProgress;
	private tmDialog dialog;
	private int level = 1;
	private boolean popping = false;
	
	public void init()
	{
		this.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
		this.setBackground(ToMarUtils.toMarBackground);
		this.setSize(WIDTH, HEIGHT);
		this.addMouseListener(this);
		this.addMouseMotionListener(this);
		WIDTH = Integer.parseInt(this.getParameter("WIDTH"));
		HEIGHT = Integer.parseInt(this.getParameter("HEIGHT"));
        offscreenImg = createImage(getSize().width, getSize().height);
        og = offscreenImg.getGraphics();
		message = this.getParameter("message");
		message = (message == null) ? "Welcome to Ballooney!" : message;
		message += " Click on a balloon to activate the game...";
		newGame = new tmButton(60, 450, tmColors.GREEN, 200, 40, "New Puzzle");
		startOver = new tmButton(380, 450, tmColors.GREEN, 100, 40, "Start Over");
		backUp = new tmButton(580, 450, tmColors.GREEN, 100, 40, "Back Up");
		points = 0;
		reInit();
	}
    public void update(Graphics g)
    {
        og.setColor(ToMarUtils.toMarBackground);
        og.fillRect(0, 0, WIDTH, HEIGHT);
        paint(g);
    }  
	private void reInit()
	{
		started = false;
		columns = new Vector(NUMBEROFCOLUMNS);
		for (int c = 0; c < NUMBEROFCOLUMNS; c++)
		{
			BColumn col = new BColumn(c);
			columns.addElement(col);
			positions[c] = c;
		}
		selects = new Vector();
		setRightMost(NUMBEROFCOLUMNS - 1);
		balloons = lowestCount = NUMBEROFCOLUMNS * NUMBEROFROWS;
		history = new Vector();
		moves = 0;
		highestScore = 0;
		newGame.setLabel("New Puzzle");
		dialogInProgress = false;
	}	
    public void paint(Graphics g)
    {
    	int[] counter = {0, 0, 0, 0, 0};
    	String[] labels = {"Red", "Green", "Pink", "Blue", "Orange"};
		for (int c = 0; c <= rightMost; c++)
		{
			BColumn bcol = (BColumn) columns.elementAt(positions[c]);
			for (int r = 0; r <= bcol.getLowest(); r++)
			{
				(bcol.getBalloon(r)).draw(og);
				counter[bcol.getBalloon(r).getColor()]++;
			}
		}	
		og.setFont(new Font("Verdana",Font.BOLD, 18));
		og.setColor(Color.black);
		og.drawString("Selected: " + selects.size(), 0, 20);
		og.drawString("Moves: " + moves, 0, 80);
		og.drawString("Points: " + points, 0, 140);
		og.drawString("Bonus: " + getBonus(), 0, 170);
		og.drawString("Total: " + (points + getBonus()), 0, 200);
		og.drawString("High: " + getHighestScore(points + getBonus()), 0, 320);	
		og.drawString("Level: " + level, COLUMNTWO, 20);
		og.drawString("Balloons: " + balloons, COLUMNTWO, 80);
		for (int i = 0; i < 5; i++)
		{
			og.drawString(labels[i] + ": " + counter[i], COLUMNTWO, i * 30 + 140);
		}
		og.drawString("Lowest: " + getLowestCount(balloons), COLUMNTWO, 320);
        og.drawString(message, 50, 420);
		// three buttons
		newGame.draw(og);
		backUp.draw(og);
		startOver.draw(og);
        if (dialogInProgress)
		{
			dialog.draw(og);
		}
		g.drawImage(offscreenImg, 0, 0, this);
	}
    public void mousePressed(MouseEvent e)
	{
		message = "";
		// check for boxes
		if (dialogInProgress)
		{
			// don't take other input except the dialog
			int result = dialog.processMouseInput(e.getX(), e.getY());
			dialogInProgress = false;
			repaint();
			if (result != tmDialog.CANCELLED)
			{
				endGame();
			}
			return;
		}
		else if (newGame.clicked(e.getX(), e.getY()))
		{
			if (balloons < 150 || level > 1)
			{	
				newPuzzle();
			}
			else
			{	
				points = 0;
				reInit();
				started = true;
				repaint();
			}
			return;
		}
		else if (started && startOver.clicked(e.getX(), e.getY()))
		{
			this.startOver();
			newGame.setLabel("New Puzzle");
			repaint();
			return;	
		}
		else if (started && backUp.clicked(e.getX(), e.getY()))
		{
			this.backUp();
			repaint();
			return;	
		}
		if (started == false)
		{
			started = true;
			repaint();
			return;
		}	
		//pop whatever was in selects
		if (selects.size() == 0)
			return;
		popping = true;
		history.addElement(selects);
		moves += 1;
		int p = (selects.size() * selects.size() * level);
		points += p;
		message = "" + p + " points!";
		getHighestScore(points + getBonus());
		for (int i = 0; i < selects.size(); i++)
		{	
			balloons -= 1;
			Balloon b = (Balloon) selects.elementAt(i);
			b.setState(Balloon.POPPED);
			((BColumn) columns.elementAt(positions[b.getCol()])).reNumber();
		}	
		reNumber();
		newGame.setLabel("Give Up");
		popping = false;
	}
	private void emptySelects()
	{
		//deselect whatever was in selects
		for (int i = 0; i < selects.size(); i++)
		{	
			Balloon b = (Balloon) selects.elementAt(i);
			b.setState(Balloon.READY);
		}	
		selects = new Vector();
		repaint();
	}	
    public void mouseMoved(MouseEvent e)
	{
		if (started == false || popping)
			return;
		emptySelects();
		int c = ((e.getX() - 40)- Balloon.getXFactor())/(Balloon.WIDTH + 1);
		int r =  e.getY()/(Balloon.HEIGHT + Balloon.TAB);
		if (r < 0 || r > NUMBEROFROWS - 1 || c < 0 || c > NUMBEROFCOLUMNS - 1)
			return;
		Balloon b = getBalloon(r, c);
		if (b.getState() == Balloon.POPPED)
			return;
		addBalloon(b);
		// check the ones around the selected one
		int sels = 0;
//		System.out.println("Start select");
		while (selects.size() > sels)
		{
			Balloon b1  = (Balloon) selects.elementAt(sels);
			//get its position
			int r1 = b1.getRow();
			int c1 = b1.getCol();
//			System.out.println("row " + r1 + " col " + c1);
			//check north
			if (r1 > 0)
			{	
				Balloon b2 = getBalloon(r1-1, c1);
				if (b2.getColor() == b1.getColor()
					&& b2.getState() == Balloon.READY)
				{
					addBalloon(b2);
				}	
			}	
			//check south
			if (r1 < ((BColumn) columns.elementAt(positions[c1])).getLowest())
			{	
				Balloon b2 = getBalloon(r1 + 1, c1);
				if (b2.getColor() == b1.getColor()
					&& b2.getState() == Balloon.READY)
				{
					addBalloon(b2);
				}	
			}
			//check west
			if (c1 > 0
				&& (r1 <= ((BColumn) columns.elementAt(positions[c1-1])).getLowest()))
			{	
				Balloon b2 = getBalloon(r1, c1 - 1);
				if (b2.getColor() == b1.getColor()
					&& b2.getState() == Balloon.READY)
				{
					addBalloon(b2);
				}	
			}
			//check east
			if (c1 < this.rightMost
				&& (r1 <= ((BColumn) columns.elementAt(positions[c1+1])).getLowest()))
			{	
				Balloon b2 = getBalloon(r1, c1 + 1);
				if (b2.getColor() == b1.getColor()
					&& b2.getState() == Balloon.READY)
				{
					addBalloon(b2);
				}	
			}
			sels++;
		}
		if (selects.size() < 2)
		{
			b.setState(Balloon.READY);
			selects = new Vector();
		}
	}
	private void addBalloon(Balloon b)
	{
		b.setState(Balloon.SELECTED);
		selects.addElement(b);
	}	
	private Balloon getBalloon(int r, int c)
	{
		BColumn bcol = (BColumn) columns.elementAt(positions[c]);
		Balloon b = bcol.getBalloon(r);
		return b;
	}	
	public void mouseDragged(MouseEvent e)
	{
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
	public void startOver()
	{
		while (moves > 0)
		{	
			backUp();
		}	
	}	
	public void newPuzzle()
	{
		dialog = new tmDialog("You sure you want to abandon this puzzle?", 0, 0);
		dialogInProgress = true;
		repaint();
	}	
	private void log(String s)
	{
		System.out.println(s);
	}
	private int getBonus()
	{
		int base = 25 - balloons;
		if (base < 0)
		{
			return 0;
		}
		if (base < 10)
		{
			return base * 20 * level;
		}
		if (base < 20)
		{
			return base * 35 * level;
		}
		if (base < 25)
		{
			return base * 50 * level;
		}
		return 1500 * level;
	}
	public void endGame()
    {
		points += getBonus();
		message = "Your final score for that round was " + points + "."; 
   		try
   		{
   			String encName = (this.getParameter("nm")).replaceAll(" ", "%20");
			String fwd = this.getParameter("site") + "BLN?score=" + points + "&id=" + this.getParameter("id") + "&nm=" + encName + "&tsp=" + ToMarUtils.getDateTimeStamp();
   			this.getAppletContext().showDocument(new URL(fwd));
   		}
   		catch(Exception e)
   		{
   			log("Error 1: " + e);
   		}
    }
	public void backUp()
	{
		if (moves == 0)
			return;
		selects = (Vector) history.lastElement();
		moves -= 1;
		history.removeElementAt(moves);
		points -= (selects.size() * selects.size() * level);
		for (int i = 0; i < selects.size(); i++)
		{	
			balloons += 1;
			Balloon b = (Balloon) selects.elementAt(i);
			b.setState(Balloon.READY);
			((BColumn) columns.elementAt(b.getOrigCol())).reNumber();
		}	
		this.reNumber();
	}
	private void reNumber()
	{
//		System.out.println("Before renumber rightMost is " + this.rightMost);
		int pos = 0;
		for (int c = 0; c < NUMBEROFCOLUMNS; c++)
		{	
			BColumn bcol = (BColumn) columns.elementAt(c);
			if (bcol.isActive())
			{
//				System.out.println("Column " + c + " is " + pos);
				positions[pos] = c;
				bcol.setColumn(pos);
				pos++;
			}
		}
		setRightMost(pos - 1);
//		System.out.println("rightMost is " + rightMost + " xFactor is " + xFactor);
		selects = new Vector();
		if (balloons == 0)
		{	
			points += 2500 * level;
			level += 1;
			reInit();
		}
	}	
    public void mouseClicked(MouseEvent e) 
    {
    }
	
    public int getRnd(int value)
    {
        return (int)(Math.random() * value);
    }    
	/**
	 * @return Returns the rightMost.
	 */
	public int getRightMost()
	{
		return rightMost;
	}
	private int getHighestScore(int newScore)
	{
		if (newScore > highestScore)
		{
			highestScore = newScore;
		}
		return highestScore;
	}
	private int getLowestCount(int newScore)
	{
		if (newScore < lowestCount)
		{
			lowestCount = newScore;
		}
		return lowestCount;
	}

	/**
	 * @param rightMost The rightMost to set.
	 */
	public void setRightMost(int rightMost)
	{
		this.rightMost = rightMost;
		Balloon.setXFactor(100 + (NUMBEROFCOLUMNS - 1 - rightMost)*(Balloon.WIDTH + 1)/2);
	}

}

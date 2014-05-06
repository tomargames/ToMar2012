import java.awt.*;
import java.awt.event.*;
import java.net.*;
import java.applet.*;

public class RUN extends Applet
    implements MouseListener 
{
	private static final long serialVersionUID = 2922754750981919470L;
	static final int SIZE = 8;
	static final int BASE = 2;
	static final int LEVELBONUS = 1000;
	static final int ROWBONUS = 100;
	public static final int RIGHTCOLUMN = 620;
    Color bgColor;
    Color textColor;
    Image offscreenImg;
    Graphics og;
    boolean started = false;
	boolean betweenRounds = false;
	int points;
	int score;
	int level;
	Cell cell[][] = new Cell[SIZE][SIZE];
	String display = "";
	Rune nextRune;
	Toolkit toolkit = Toolkit.getDefaultToolkit();
	WasteCan wasteCan;
	String message;
    
	public static void log(String s)
	{
		System.out.println(ToMarUtils.getDateTimeStamp() + ": " + s);
	}
    public void init()
    {
	    setSize(800, 600);
		bgColor = ToMarUtils.toMarBackground;
		textColor = tmColors.DARKBLUE;
		setBackground(bgColor);
	    addMouseListener(this);
		message = this.getParameter("message");
		message = (message == null) ? "Welcome to Runes!" : message;
        offscreenImg = createImage(getSize().width, getSize().height);
        og = offscreenImg.getGraphics();
		nextRune = null;
	}
	
    public void reInit()
    {
		started = true;
		score = 0;
		level = 0;
		newLevel();
    }    
	
	public void newLevel()
	{
		level += 1;
		for (int i = 0; i < SIZE; i++)
		{
			for (int j = 0; j < SIZE; j++)
			{
				cell[i][j] = new Cell(i, j);
			}
		}	
		// place a wild card near the middle of the grid - row and column 3, 4, or 5
		int r = (int)(Math.random() * 3) + 3;
		int c = (int)(Math.random() * 3) + 3;
		cell[r][c].setRune(new Rune());
		wasteCan = new WasteCan(level);
		nextRune = new Rune(level);
		betweenRounds = true;
	}
    public void paint(Graphics g)
    {
        if  (started == false)
        {
			og.setFont(new Font("Verdana",Font.PLAIN,24));
	        og.setColor(textColor);
            // print instructions if game hasn't started yet
			if (score > 0)
			{
	            og.drawString("Game Over!", 200, 3*20);
	            og.drawString("You scored " + score + " points!", 150, 5*20);
			}	
			else
			{	
				og.drawString(message, 40, 2*20);
				og.drawString("Instructions are below.", 40, 5*20);
				og.drawString("Click anywhere in this window to play...", 40, 7*20);
			}	
        }        
        else
        {
            //paint all the tiles
			for (int i = 0; i < SIZE; i++)
			{
				for (int j = 0; j < SIZE; j++)
				{
					cell[i][j].draw(og);
				}
			}	
	        og.setColor(textColor);
	        og.setFont(new Font("Verdana",Font.PLAIN,20));
			og.drawString(display, 35, 30);
			// draw next rune
			if (nextRune != null)
			{
				nextRune.draw(og, RIGHTCOLUMN, 20);
				og.setFont(new Font("Verdana",Font.PLAIN,16));
				og.setColor(Color.black);
				og.drawString("Place this Rune...", RIGHTCOLUMN, 100);
			}	
			wasteCan.draw(og);
        }        
        g.drawImage(offscreenImg, 0, 0, this);
    }
	
    public void update(Graphics g)
    {
        og.setColor(bgColor);
        og.fillRect(0, 0, Integer.parseInt(getParameter("WIDTH")),Integer.parseInt(getParameter("HEIGHT")));
        paint(g);
    }
	
    /*
     * Mouse methods
     */
    public void mousePressed(MouseEvent e) 
    {
        if  (started == false)
        {
            reInit();
	    }    
	    else
	    {
            int x = e.getX();
            int y = e.getY();
			//check wasteCan
			betweenRounds = false;
			if (wasteCan.clicked(x, y))
			{
				boolean okay = wasteCan.add();
				if (!okay)
				{
					endGame();
				}	
				else
				{
					nextRune = new Rune(level);
				}	
			}
			else
			{	
				//check all the tiles
				for (int i = 0; i < SIZE; i++)
				{
					for (int j = 0; j < SIZE; j++)
					{
						if (cell[i][j].isClicked(x, y))
						{
							if (nextRune.isBomb())
							{
								if (cell[i][j].getRune() != null)
								{
									cell[i][j].setRune(null);
									wasteCan.subtract();
									nextRune = new Rune(level);
								}
								else
								{
									toolkit.beep();
								}
							}	
							else if (cell[i][j].getRune() == null)
							{
								points = 1;
								if (checkNorth(i,j))
								{
									if (checkSouth(i,j))
									{
										if (checkEast(i,j))
										{
											if (checkWest(i,j))
											{
												if (points > 1)
												{
													points *= level;
													wasteCan.subtract();
													cell[i][j].setRune(nextRune);
													if (levelCompleted(i, j))
													{
														points += LEVELBONUS * level;
														score = ToMarUtils.addToPoints(score, points);
														newLevel();
													}
													else
													{	
														score = ToMarUtils.addToPoints(score, points);
														nextRune = new Rune(level);
													}	
													if (ToMarUtils.isMaxPoints(score))
													{
														endGame();	
													}
													display ="Level: " + level + "     Points: " + points +  
															 "     Total: " + score;
												}
												else
												{
													toolkit.beep();
												}	
											}		
										}
									}
								}	
							}
							else
							{
								toolkit.beep();
							}	
							break;
						}
					}
				}
			}	
		}	
		repaint();
    }

	public boolean levelCompleted(int row, int col)
	{
		// first check the row to see if it was completed
		boolean rowCompleted = true;
		boolean columnCompleted = true;
		for (int i = 0; i < SIZE; i++)
		{
			if (cell[row][i].getRune() == null)
			{
				rowCompleted = false;
				break;
			}	
		}
		for (int i = 0; i < SIZE; i++)
		{
			if (cell[i][col].getRune() == null)
			{
				columnCompleted = false;
				break;
			}	
		}
		if (rowCompleted)
		{
			for (int i = 0; i < SIZE; i++)
			{
				cell[row][i].completed();
			}
			points += level * ROWBONUS;
		}
		if (columnCompleted)
		{
			for (int i = 0; i < SIZE; i++)
			{
				cell[i][col].completed();
			}
			points += level * ROWBONUS;
		}
		// now check for completed level - all cells INUSE or WASUSED
		boolean levelCompleted = true;
		for (int i = 0; i < SIZE; i++)
		{
			if (levelCompleted)
			{
				for (int j = 0; j < SIZE; j++)
				{
					if (!cell[i][j].isCompleted())
					{
						levelCompleted = false;
						break;
					}
				}
			}
		}
		return levelCompleted;
	}	
	public boolean checkNorth(int row, int col)
	{
		if (row == 0)
		{
			return true;
		}
		return checkCell(row - 1, col);
	}
	public boolean checkSouth(int row, int col)
	{
		if (row + 1 == SIZE)
		{
			return true;
		}
		return checkCell(row + 1, col);
	}
	public boolean checkEast(int row, int col)
	{
		if (col + 1 == SIZE)
		{
			return true;
		}
		return checkCell(row, col + 1);
	}
	public boolean checkWest(int row, int col)
	{
		if (col == 0)
		{
			return true;
		}
		return checkCell(row, col - 1);
	}
	public boolean checkCell(int r, int c)
	{
		if (cell[r][c].getRune() != null)
		{
			if (nextRune.isMatch(cell[r][c].getRune()))
			{
				points *= BASE;
				return true;
			}
			toolkit.beep();
			return false;
		}
		return true;
	}
    public void endGame()
    {
        started = false;
        repaint();
		try
		{
			Thread.sleep(1000);
			String encName = (this.getParameter("nm")).replaceAll(" ", "%20");
			String fwd = this.getParameter("site") + "RUN?score=" + score + "&id=" + this.getParameter("id") + "&nm=" + encName + "&tsp=" + ToMarUtils.getDateTimeStamp();
			this.getAppletContext().showDocument(new URL(fwd));
		}
		catch(Exception e)
		{
			log("Error 1: " + e);
		}
        repaint();
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

}

/*
	A basic extension of the java.applet.Applet class
*/
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.net.*;

public class HTR extends java.applet.Applet 
    implements Runnable , KeyListener, MouseListener
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 5387532993010411153L;
	public static int numRows;
	public static int numCols;
	public static int cWidth;
	public static int cHeight;
	public static int speed;
	public static int badness;
	public static htYou you;
	public static Color colorBad;
	public static Color colorYou;
	public static Color colorGood;
	public static Color colorBomb;
	public static Color colorThem;
	public static Color colorBG;
	private int points;
	private int pts;
	private static int WIDTH;
	private static int HEIGHT;
    private Image offscreenImg;
    private Graphics og;
	private boolean started;
	private boolean moved;
    private Thread thread;
    private String message;
	public static htGood good;
	public static Vector them;
	public static Vector badGuys;
	public static Vector bombs;
	private boolean takeFlag;
	
	public void init()
	{
		points = 0;
		numRows = 38;     //570
		numCols = 50;     //750
		cWidth = 15;
		cHeight = 15;
		speed = 90;
		badness = 3;
        colorBad = tmColors.RED;
		colorYou = tmColors.LIGHTBLUE;
		colorGood = tmColors.DARKGREEN;
		colorBomb = tmColors.PURPLE;
		colorThem = tmColors.CHARTREUSE;
		colorBG = tmColors.CREAM;
		this.addKeyListener(this);
		this.addMouseListener(this);
		this.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
		this.setBackground(colorBG);
		WIDTH = Integer.parseInt(this.getParameter("WIDTH"));
		HEIGHT = Integer.parseInt(this.getParameter("HEIGHT"));
		this.setSize(WIDTH, HEIGHT);
        offscreenImg = createImage(getSize().width, getSize().height);
        og = offscreenImg.getGraphics();
		message = this.getParameter("message");
		message = (message == null) ? "Welcome to HappyTrails!" : message;
		message += " Click to start...";
		started = false;
	}	
	
    public void update(Graphics g)
    {
        og.setColor(colorBG);
        og.fillRect(0, 0, WIDTH, HEIGHT);
        paint(g);
    }  
	private void log(String s)
	{
		System.out.println(s);
	}

	public void run()
	{
		while (started)
		{
			this.moveEverybody();
		}
		if (points == 0)
		{
			started = true;
			return;
		}
		try
		{
			Thread.sleep(3000);
			String encName = (this.getParameter("nm")).replaceAll(" ", "%20");
			String fwd = this.getParameter("site") + "HTR?score=" + points + "&id=" + this.getParameter("id") + "&nm=" + encName + "&tsp=" + ToMarUtils.getDateTimeStamp();
			this.getAppletContext().showDocument(new URL(fwd));
		}
		catch(Exception e)
		{
			log("Error 1: " + e);
		}
	}

	public void moveEverybody()
	{
		int previousRow = you.getRow();
		int previousCol = you.getColumn();
		started = you.move();
		for (int i = 0; i < them.size(); i++)
		{
			htThem follower = (htThem) them.elementAt(i);
			if (you.hit(follower.getRow(),follower.getColumn()))
			{
				started = false;
				break;
			}	
			int temp = follower.getRow();
			follower.setRow(previousRow);
			previousRow = temp;
			temp = follower.getColumn();
			follower.setColumn(previousCol);
			previousCol = temp;
		}	
		if (started && you.hit(good.getRow(),good.getColumn()) == true)
		{
			Date date = new Date();
			pts = (int) (date.getTime() - good.getStartTime())/1000;
			pts = (20 - pts < 5) ? 5 : 20 - pts;
			pts *= (them.size() + 1);
			pts *= (badGuys.size() + 1);//			System.out.println("Points = " + pts);
			points += pts;
			takeFlag = true;
			htThem follower = new htThem();
			follower.setRow(previousRow);
			follower.setColumn(previousCol);
			them.addElement(follower);
			good = new htGood();
			if (ToMarUtils.getRnd(badness) == 1)
			{
				badGuys.addElement(new htBadGuy());
				if (badGuys.size() % (badness * 2) == 0)
				{
					bombs.addElement(new htBomb());
				}	
			}	
		}	
		else
		{ 
			for (int i = 0; i < bombs.size(); i++)
			{
				htBomb bomb = (htBomb) bombs.elementAt(i);
				if (started && you.hit(bomb.getRow(), bomb.getColumn()))
				{
					them = new Vector();
					bombs.removeElementAt(i);
				}
			}
		}	
		if (started)
		{
			for (int i = 0; i < badGuys.size(); i++)
			{
				htBadGuy badGuy = (htBadGuy) badGuys.elementAt(i);
				if (you.hit(badGuy.getRow(), badGuy.getColumn()) == true)
				{
					started = false;
					break;
				}	
			}
		}	
		try
		{
			Thread.sleep(speed);
		}
		catch (Exception e) {}
		repaint();
		moved = true;
	}	
	private void drawText(Graphics og, Color color, String s, int r, int c)
	{   	
		og.setColor(color);
		og.drawString(s, r, c);
	}

    public void paint(Graphics g)
    {
		if (this.started)
		{	
			if (takeFlag)
			{
				you.drawBig(og);
				takeFlag = false;
			}
			else
			{	
				you.draw(og);
			}	
			for (int i = 0; i < them.size(); i++)
			{
				((htThem) them.elementAt(i)).draw(og);
			}	
			for (int i = 0; i < badGuys.size(); i++)
			{
				((htBadGuy) badGuys.elementAt(i)).draw(og);
			}	
			good.draw(og);
			for (int i = 0; i < bombs.size(); i++)
			{
				((htBomb) bombs.elementAt(i)).draw(og);
			}	
		}
		else
		{
			if (points > 0)
			{	
				drawText(og, colorBad, "*** GAME OVER ***       Score: " + points,30,410);
			}
			else
			{	
				og.setFont(new Font("Verdana",Font.PLAIN,24));
				drawText(og, colorBomb, message,30,40);
				og.setColor(colorYou);
				og.fillRoundRect(2 * cWidth + 2, 6 * cHeight + 2, cWidth - 2, cHeight - 2,20,20);
				drawText(og, colorYou, "is you.", 70, 105);
				og.setColor(colorGood);
				og.fillRoundRect(2 * cWidth + 2, 9 * cHeight + 2, cWidth - 2, cHeight - 2,20,20);
				drawText(og, colorGood, "are worth points if you hit them.", 70, 150);
				og.setColor(colorBad);
				og.fillRoundRect(2 * cWidth + 2, 12 * cHeight + 2, cWidth - 2, cHeight - 2,20,20);
				drawText(og, colorBad, "These and the walls are death if you hit them.", 70, 195);
				og.setColor(colorThem);
				og.fillRoundRect(2 * cWidth + 2, 15 * cHeight + 2, cWidth - 2, cHeight - 2,20,20);
				drawText(og, colorThem, "will follow you everywhere, but don't hit them!", 70, 240);
				og.setColor(colorBomb);
				og.fillRoundRect(2 * cWidth + 2, 18 * cHeight + 2, cWidth - 2, cHeight - 2,20,20);
				drawText(og, colorBomb, "will erase your trail.", 70, 285);
				drawText(og, tmColors.DARKGREEN, "Use the arrow keys to move around.",30,340);
				drawText(og, tmColors.DARKGREEN, "The longer your trail, the more points you score.",30,370);
			}
		}	
        og.setColor(colorBad);
        og.drawRect(0, 0, cWidth * numCols - 1, cHeight * numRows - 1);
        og.setColor(tmColors.DARKGREEN);
		og.setFont(new Font("Verdana",Font.BOLD,16));
        og.drawString("" + points, 755, 20);
		og.setFont(new Font("Verdana",Font.PLAIN,16));
        og.drawString("" + pts, 755, 60);
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
    	if (started)
		{
	    	if (!moved)
    		{
    			moveEverybody();
    		}
			switch (key.getKeyCode())
			{
			    case 37:
					if (!(you.getDirection() == htYou.EAST))
					{	
						you.setDirection(htYou.WEST);
						moved = false;
					}	
			        break;
			    case 39:
					if (!(you.getDirection() == htYou.WEST))
					{	
						you.setDirection(htYou.EAST);
						moved = false;
					}	
			        break;
			    case 38:
					if (!(you.getDirection() == htYou.SOUTH))
					{	
						you.setDirection(htYou.NORTH);
						moved = false;
					}	
			        break;
			    case 40:
					if (!(you.getDirection() == htYou.NORTH))
					{	
						you.setDirection(htYou.SOUTH);
						moved = false;
					}	
			}
		}	
    }
    public void startGame() 
    {
		if (!started)
		{	
			them = new Vector();
			badGuys = new Vector();
			bombs = new Vector();
			you = new htYou();
			good = new htGood();
			points = 0;
			started = true;
			takeFlag = false;
			thread = new Thread(this);
			thread.start();
		}	
    }
    public void mouseClicked(MouseEvent e) 
    {
		startGame();
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

    public void mousePressed(MouseEvent e) 
    {
		startGame();
    }
}

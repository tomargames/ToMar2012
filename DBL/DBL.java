/*
	A basic extension of the java.applet.Applet class
*/
import java.awt.*;
import java.awt.event.*;
import java.net.*;
import java.util.*;

public class DBL extends java.applet.Applet 
    implements Runnable, MouseListener, MouseMotionListener
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -3862900076951754137L;
	public static int WIDTH = 700;
	public static int HEIGHT = 450; 
	public static Color colorBG = ToMarUtils.toMarBackground;
	private int points;
    private Image offscreenImg;
    private Graphics og;
	private int gameStage;
    private Thread thread;
	private Vector goodBalls;
	private Vector badBalls;
	private int X;
	private int Y; 
	long goodTime = 1500;
	long badTime = 5000;
	long threshold = 100;
	int maxBad = 3;
	long startTime;
	String message;
	String encName;
	public static final int NOTSTARTED = 0;
	public static final int PLAYING = 1;
	public static final int GAMEOVER = 2;
	
	public void init()
	{
		encName = (this.getParameter("nm")).replaceAll(" ", "%20");
		WIDTH = Integer.parseInt(this.getParameter("WIDTH"));
		HEIGHT = Integer.parseInt(this.getParameter("HEIGHT"));
	    setSize(WIDTH,HEIGHT);
		points = 0;
		this.addMouseListener(this);
		this.addMouseMotionListener(this);	
		this.setCursor(new Cursor(Cursor.CROSSHAIR_CURSOR));
		this.setBackground(colorBG);
        offscreenImg = createImage(WIDTH,HEIGHT);
        og = offscreenImg.getGraphics();
		message = this.getParameter("message");
		message = (message == null) ? "Welcome to DodgeBall!" : message;
		gameStage = NOTSTARTED;
	}	
	
    public void update(Graphics g)
    {
        og.setColor(colorBG);
        og.fillRect(0, 0, WIDTH, HEIGHT);
        paint(g);
    }  

	public void run()
	{
		startTime = ToMarUtils.startTimer();
		long prevGood = ToMarUtils.getMilliSeconds(startTime);
		long prevBad = ToMarUtils.getMilliSeconds(startTime);
		badBalls.addElement(new Ball(false));
		goodBalls.addElement(new Ball(true));
		repaint();	
		while (gameStage == PLAYING)
		{
			long secs = ToMarUtils.getMilliSeconds(startTime);
			if ((secs - prevGood) > goodTime)
			{
				goodBalls.addElement(new Ball(true));
				prevGood = secs;
				goodTime = (goodTime > 1500) ? goodTime - 500 : goodTime + 5;
			}	
			if ((secs - prevBad) > badTime && badBalls.size() < maxBad)
			{
				badBalls.addElement(new Ball(false));
				prevBad = secs;
			}	
			for (int i = 0; i < goodBalls.size(); i++)
			{
				Ball ball = ((Ball) goodBalls.elementAt(i)); 
				ball.move();
				if (ball.hit(X,Y))
				{
					points += ball.getPointValue();
					goodBalls.removeElement(ball);
				} 
			}
			for (int i = 0; i < badBalls.size(); i++)
			{
				Ball ball = ((Ball) badBalls.elementAt(i)); 
				ball.move();
				if (ball.hit(X,Y))
				{
					gameStage = GAMEOVER;
					break;
				} 
			}
			if (points > threshold)
			{
				maxBad += 1;
				threshold = (threshold < 10000) ? threshold * 2 : threshold + 5000;
			}
			message = "Points: " + points + "    Green: " + goodBalls.size() + "    Red: " + badBalls.size();
			repaint();
			try
			{
				Thread.sleep(25);
			} 
			catch (InterruptedException e1)
			{
				e1.printStackTrace();
			}
		}
		try
		{
			Thread.sleep(2000);
			if (points > 0)
			{	
				String fwd = this.getParameter("site") + "DBL?score=" + points + "&id=" + this.getParameter("id") + "&nm=" + encName + "&tsp=" + ToMarUtils.getDateTimeStamp();
				this.getAppletContext().showDocument(new URL(fwd));
			}
			else
			{
				message = "Try again!";
				gameStage = NOTSTARTED;
				repaint();
			}
   		}
   		catch(Exception e)
   		{
   			log("Error 1: " + e);
   		}
	}
	public static void log(String s)
	{
		System.out.println(s);
	}
    public void paint(Graphics g)
    {
		og.setFont(new Font("Verdana",Font.PLAIN,24));
        og.setColor(tmColors.BLACK);
		og.drawString(message, 10,35);
        og.drawRect(0, 50, WIDTH - 1, HEIGHT - 51);
		if (gameStage == PLAYING)
		{
			for (int i = 0; i < goodBalls.size(); i++)
			{
				((Ball) goodBalls.elementAt(i)).draw(og);
			}
			for (int i = 0; i < badBalls.size(); i++)
			{
				((Ball) badBalls.elementAt(i)).draw(og);
			}
		}
		else if (gameStage == GAMEOVER)
		{
			og.drawString("*** GAME OVER ***       Score: " + points,80,410);
		}	
		else
		{
			og.drawString("Catch the green balls; avoid the red balls.", 30, 90);
			og.drawString("Click inside this box to start a new game...",30, 190);
		}	
        g.drawImage(offscreenImg, 0, 0, this);
	}
    public void mouseClicked(MouseEvent e) 
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

    public void mousePressed(MouseEvent e) 
    {
		if (gameStage == NOTSTARTED)
		{
			message = "";
			points = 0;
			goodBalls = new Vector();
			badBalls = new Vector();
			Ball.resetCounter();
			goodTime = 1000;
			badTime = 10000;
			threshold = 100;
			maxBad = 1;
			gameStage = PLAYING;
			thread = new Thread(this);
			thread.start();
		}
    }

	public void mouseDragged(MouseEvent arg0)
	{
	}

	public void mouseMoved(MouseEvent arg0)
	{
		X = arg0.getX();
		Y = arg0.getY();
	}
}

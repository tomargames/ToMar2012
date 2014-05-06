import java.awt.*;
import java.awt.event.*;
import java.net.*;
import java.util.Date;
import java.applet.*;

/*
 * Updated March, 2012 for PHP interface using Google authentication
 */
public class SDF extends Applet
    implements MouseListener, Runnable
{
	private static final long serialVersionUID = -2609361118653956384L;
	private String version = "2.51";
	private static final int MAXTIME = 600;
    public static Color bgColor;
    private Color textColor;
    private Color dataColor;
    Image offscreenImg;
    Graphics og;
    boolean started = false;
	LittleButton sortColorButton;
	LittleButton sortShadingButton;
	LittleButton hintButton;
	LittleButton sortNumberButton;
	LittleButton sortShapeButton;
	ToMarButton controlButton;
	int timeLeft = 0;
	String message = "";
	private Thread thread;
	private Color timeColor;
	long startSec;
	int points = 0;
	
	public void run()
	{
		while (started)
		{
    		int seconds = (int)((new Date()).getTime() - startSec)/1000;
			timeLeft = 180  - seconds;
			if (timeLeft < 0)
			{
				timeLeft = 0;
				message = "Game Over...";
				started = false;
				repaint();
				endGame();
			}
			if (timeLeft < 11)
			{
				timeColor = tmColors.RED;
			}
			else
			{
				timeColor = dataColor;
			}
			repaint();
		}
	}
    public void init()
    {
	    setSize(1200, 500);
	    bgColor = tmColors.CREAM;
		textColor = tmColors.DARKBLUE;
		dataColor = tmColors.PURPLE;
		setBackground(bgColor);
	    addMouseListener(this);
        offscreenImg = createImage(getSize().width, getSize().height);
        og = offscreenImg.getGraphics();
        hintButton = new LittleButton(20, 475, 20, "Get Hint");
        sortNumberButton = new LittleButton(140, 475, 20, "Sort Number");
        sortColorButton = new LittleButton(260, 475, 20, "Sort Color");
        sortShapeButton = new LittleButton(380, 475, 20, "Sort Shape");
        sortShadingButton = new LittleButton(500, 475, 20, "Sort Shading");
        controlButton = new ToMarButton(650, 0, 100, "Start Game");
        controlButton.setHeight(30);
        controlButton.setColor(tmColors.ORANGE);
		message = this.getParameter("message");
		message = (message == null) ? "Welcome to Same Difference!" : message;
        Card.createDeck();
	}
	
    public void reInit()
    {
		Card.createLayout();
		thread = new Thread(this);
		startSec = (new Date()).getTime();
		controlButton.setLabel("Start Over");
		started = true;
		thread.start();
    }    
    
	public static void log(String s)
	{
		System.out.println(ToMarUtils.getDateTimeStamp() + ": " + s);
	}	
    public void paint(Graphics g)
    {
        og.setFont(new Font("Verdana",Font.PLAIN,18));
        if  (!started)
        {
	        og.setColor(textColor);
			if (Card.getPoints() > 0)
			{
	            paintLayout(true);
			}	
			else
			{	
				og.drawString(message, 20, 2*20);
				og.drawString("Detailed instructions are below.", 20, 4*20);
				og.drawString("Each set is worth at least 30 points.", 20, 6*20);
				og.drawString("Sorts cost 5 points each.", 20, 8*20);
				og.drawString("Hints cost 10 points each.", 20, 10*20);
				og.drawString("You have 3 minutes to find as many sets as you can.", 20, 12*20);
				og.drawString("Click 'Start Game' above to play...", 20, 16*20);
			}	
        }        
        else
        {
            paintLayout(false);
	        og.setFont(new Font("Verdana",Font.PLAIN,18));
			hintButton.draw(og);
			sortNumberButton.draw(og);
			sortColorButton.draw(og);
			sortShapeButton.draw(og);
			sortShadingButton.draw(og);
        }
		controlButton.draw(og);
        g.drawImage(offscreenImg, 0, 0, this);
    }
	public void paintLayout(boolean gameover)
	{
        for (int i = 0; i < Card.getLayout().size(); i++)
        {
        	if (gameover)
        	{	
        		(Card.getCard(i)).setSelected(true);
        	}
			(Card.getCard(i)).draw(og);  
        }
        og.setColor(textColor);
		og.drawString("Time:", 20, 20);
		og.drawString("Points:", 140, 20);
		og.drawString("Sets:", 290, 20);
		og.drawString("Cards:", 380, 20);
        og.setColor(timeColor);
        og.setFont(new Font("Verdana",Font.PLAIN,24));
		og.drawString("" + timeLeft, 80, 20);
		og.setColor(dataColor);
		og.drawString("" + Card.getPoints(), 210, 20);
		og.drawString("" + Card.getSets(), 340, 20);
		og.drawString("" + (Card.TOTALCARDS - Card.getCardPointer()), 450, 20);
		og.setColor(textColor);
        og.drawString(message, 650, 485);
	}	
	
    public void update(Graphics g)
    {
        og.setColor(bgColor);
        og.fillRect(0, 1, Integer.parseInt(getParameter("WIDTH")),Integer.parseInt(getParameter("HEIGHT")));
        paint(g);
    }
	public void sortByNumber()
	{
		Card.sort("getNumber");
	}	
	public void sortByColor()
	{
		Card.sort("getColor");
	}	
	public void sortByShape()
	{
		Card.sort("getShape");
	}	
	public void sortByShading()
	{
		Card.sort("getShading");
	}	
	public void getHint()
	{
		Card.getHint();
	}	
    /*
     * Mouse methods
     */
    public void mousePressed(MouseEvent e) 
    {
        int x = e.getX();
        int y = e.getY();
        if (controlButton.clicked(x, y))
        {	
            reInit();
        }    
	    else
	    {
            if (hintButton.clicked(x, y))
            {
            	getHint();
            }
            else if (sortNumberButton.clicked(x, y))
			{
				sortByNumber();
			}
			else if (sortColorButton.clicked(x, y))
			{
				sortByColor();
			}
            else if (sortShapeButton.clicked(x, y))
			{
				sortByShape();
			}
			else if (sortShadingButton.clicked(x, y))
			{
				sortByShading();
			}
			else
			{
	            for (int i = 0; i < Card.getLayout().size(); i++)
	            {
					if (Card.getCard(i).checkClick(x, y) == true)
					{
						message = Card.checkSets();
						if (Card.GAMEOVER.equals(message))
						{
							this.endGame();
						}
						break;
					}		
	            }
			}
        }    
    }

    public void endGame()
    {
		try
		{
			Thread.sleep(5000);
			String encName = (this.getParameter("nm")).replaceAll(" ", "%20");
			String fwd = this.getParameter("site") + "SDF?score=" + Card.getPoints() + "&id=" + this.getParameter("id") + "&nm=" + encName + "&tsp=" + ToMarUtils.getDateTimeStamp();
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

}

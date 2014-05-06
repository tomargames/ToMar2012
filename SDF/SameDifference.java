import java.awt.*;
import java.awt.event.*;
import java.net.*;
import java.applet.*;

/**
 * This class reads PARAM tags from its HTML host page and sets
 * the color and label properties of the applet. Program execution
 * begins with the init() method. 
 */
public class SameDifference extends Applet
    implements MouseListener 
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -2609361118653956384L;
	private String version = "2.51";
	private static final int MAXTIME = 600;
    public static Color bgColor;
    private Color textColor;
    Image offscreenImg;
    Graphics og;
    boolean started = false;
	String display = "";
	LittleButton sortColorButton;
	LittleButton sortShadingButton;
	LittleButton hintButton;
	LittleButton sortNumberButton;
	LittleButton sortShapeButton;
	ToMarButton controlButton;
	int timeBonus = 0;
	String msg = "Welcome!";
	
    public void init()
    {
	    setSize(1200, 500);
	    String browser = getParameter("BROWSER");
	    bgColor = ToMarUtils.getColor(29);
		textColor = ToMarUtils.getColor(21);
		setBackground(bgColor);
	    addMouseListener(this);
        offscreenImg = createImage(getSize().width, getSize().height);
        og = offscreenImg.getGraphics();
        hintButton = new LittleButton(browser, 20, 475, 20, "Get Hint");
        sortNumberButton = new LittleButton(browser, 140, 475, 20, "Sort Number");
        sortColorButton = new LittleButton(browser, 260, 475, 20, "Sort Color");
        sortShapeButton = new LittleButton(browser, 380, 475, 20, "Sort Shape");
        sortShadingButton = new LittleButton(browser, 500, 475, 20, "Sort Shading");
        controlButton = new ToMarButton(browser, 650, 0, 100, "Start Game");
        controlButton.setHeight(30);
        controlButton.setColor(ToMarUtils.getColor(16));
        Card.createDeck();
	}
	
    public void reInit()
    {
		Card.createLayout();
		ToMarUtils.startTimer();
		controlButton.setLabel("Start Over");
		started = true;
        msg = "Find a set";
        showStats(msg);
    }    
    
    public void paint(Graphics g)
    {
        og.setFont(new Font("Verdana",Font.PLAIN,18));
        if  (!started)
        {
	        og.setColor(textColor);
            // print instructions if game hasn't started yet
			if (Card.getPoints() > 0)
			{
	            for (int i = 0; i < Card.getLayout().size(); i++)
	            {
					(Card.getCard(i)).draw(og);  
	            }
		        og.setColor(textColor);
				og.drawString(display, 20, 20);
			}	
			else
			{	
				og.drawString("Welcome to SameDifference!", 20, 1*20);
				og.drawString("Detailed instructions are below.", 20, 3*20);
				og.drawString("Each set is worth at least 30 points.", 20, 5*20);
				og.drawString("Sorts cost 5 points each.", 20, 7*20);
				og.drawString("Hints cost 10 points each.", 20, 9*20);
				og.drawString("1 point per second bonus if your time is under 10 minutes.", 20, 11*20);
				og.drawString("Click 'Start Game' above to play...", 20, 16*20);
			}	
        }        
        else
        {
            //paint all the tiles
            for (int i = 0; i < Card.getLayout().size(); i++)
            {
				(Card.getCard(i)).draw(og);  
            }
	        og.setColor(textColor);
			og.drawString(display, 20, 20);
			hintButton.draw(og);
			sortNumberButton.draw(og);
			sortColorButton.draw(og);
			sortShapeButton.draw(og);
			sortShadingButton.draw(og);
        }        
        og.setColor(textColor);
        og.drawString(msg, 650, 485);
		controlButton.draw(og);
        g.drawImage(offscreenImg, 0, 0, this);
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
		showStats("Sorted by number");
	}	
	public void sortByColor()
	{
		Card.sort("getColor");
		showStats("Sorted by color");
	}	
	public void sortByShape()
	{
		Card.sort("getShape");
		showStats("Sorted by shape");
	}	
	public void sortByShading()
	{
		Card.sort("getShading");
		showStats("Sorted by shading");
	}	
	public void getHint()
	{
		Card.getHint();
		msg = "Hint";
		showStats(msg);
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
						msg = Card.checkSets();
						showStats(msg);					
						if (Card.GAMEOVER.equals(msg))
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
        started = false;
        controlButton.setLabel("New Game");
        int points = (Card.getPoints() + timeBonus - Card.getPenalty()); 
        if (points > 0)
        {	
	        try
			{
	        	URL url = new URL(ToMarUtils.postHighScore(this, getParameter("PLAYER"), points, "SDF"));
	        	this.getAppletContext().showDocument(url, "jsp");
	        }
	        catch(Exception e)
			{
	        	System.out.println("Error 1: " + e);
	        }
        }    
    }

    public void showStats(String message)
    {
		if (started == true)
		{	
			String spacer = "   ";
			int secs = (int)ToMarUtils.getElapsedSeconds();
			timeBonus = secs < MAXTIME ? MAXTIME - secs : 0;
			display = "Earned: "  + ToMarUtils.formatNumber(Card.getPoints(),4) + spacer + 
						"Penalty: " + ToMarUtils.formatNumber(Card.getPenalty(),3) + spacer + 
						  "Bonus: " + timeBonus + spacer +
			          "Cards: "  +  ToMarUtils.formatNumber(Card.TOTALCARDS - Card.getCardPointer(), 2) + spacer +
			          "Points: " + (Card.getPoints() + timeBonus - Card.getPenalty()); 
			getAppletContext().showStatus("Version: " + version + spacer + display + spacer + message);
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

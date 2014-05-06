import java.awt.*;
import java.awt.event.*;
import java.applet.*;
import java.util.*;
import java.net.*;
import java.io.*;

/**
 * This class reads PARAM tags from its HTML host page and sets
 * the color and label properties of the applet. Program execution
 * begins with the init() method. 
 */
public class PrimalFactors extends Applet implements ActionListener
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 4563414722962131978L;
	private static final String VERSION = "ToMar Primal Factors Version 2.31";
	private static final int numberOfPuzzles = 10;
	private static final int maxTime = 480;
	public static final int POSSIBLES = 24;
	private static final int GUESSES = 15;
	private static final int SPACING = 1;
	private static final int NUMBERFONTSIZE = 18;
	private static final int ACTIONFONTSIZE = 16;
	private static final int MESSAGEFONTSIZE = 24;
	private static final int ROWS = 3;
	private int points = 0;
	private int guesses[];
	private int numberOfGuesses;
	private int numberSolved;
	int cheatUp;
	double cheatDown;
	private Puzzle[] puzzles;
	private int currentPuzzle = 0;
	private Panel mainPanel;
	private Panel topPanel;
	private Label cheatDownLabel;
	private Label cheatUpLabel;
	private Label puzzleLabel;
	private Panel guessesPanel;
	private Label[] guessLabels;
	private Panel possiblesPanel;
	private Button[] possibleButtons;
	private Panel optionsPanel;
	private Button giveUpButton;
	private Button submitButton;
//	private Button cheatsOnOffButton;
	private Button backUpButton;
	private Button clearButton;
//	private boolean cheats;
	private Label messageLabel;
	private boolean started;
	
	public void init()
	{
		this.setSize(770, 500);
		this.setBackground(ToMarUtils.getColor(29));
		Puzzle.loadPrimes();
		mainPanel = new Panel();
		mainPanel.setLayout(new GridLayout(5, 1 , SPACING, SPACING));
		this.add(mainPanel);
		topPanel = new Panel();
		topPanel.setLayout(new BorderLayout());
		cheatDownLabel = new Label("***********");
		cheatDownLabel.setAlignment(Label.CENTER);
		cheatDownLabel.setFont(new Font("Verdana",Font.PLAIN,24));
		topPanel.add(cheatDownLabel, BorderLayout.WEST);
		puzzleLabel = new Label("??????");
		puzzleLabel.setFont(new Font("Verdana",Font.PLAIN,48));
		puzzleLabel.setAlignment(Label.CENTER);
		topPanel.add(puzzleLabel, BorderLayout.CENTER);
		cheatUpLabel = new Label("*******");
		cheatUpLabel.setAlignment(Label.CENTER);
		cheatUpLabel.setFont(new Font("Verdana",Font.PLAIN,24));
		topPanel.add(cheatUpLabel, BorderLayout.EAST);
		mainPanel.add(topPanel);
		guessesPanel = new Panel();
		guessesPanel.setLayout(new GridLayout(1,GUESSES,SPACING,SPACING));
		guessLabels = new Label[GUESSES];
		for (int i = 0; i < GUESSES; i++)
		{
			guessLabels[i] = new Label("?");
			guessLabels[i].setFont(new Font("Verdana",Font.BOLD,NUMBERFONTSIZE));
			guessesPanel.add(guessLabels[i]);
		}	
		mainPanel.add(guessesPanel);
		possiblesPanel = new Panel();
		possiblesPanel.setLayout(new GridLayout(ROWS,(int)(POSSIBLES / ROWS),SPACING,SPACING));
		possibleButtons = new Button[POSSIBLES];
		for (int i = 0; i < POSSIBLES; i++)
		{
			possibleButtons[i] = new Button();
			possibleButtons[i].setBackground(ToMarUtils.getColor(13));
			possibleButtons[i].setFont(new Font("Verdana",Font.BOLD,NUMBERFONTSIZE));
			possibleButtons[i].setLabel("???");
			possibleButtons[i].addActionListener(this);
			possiblesPanel.add(possibleButtons[i]);
		}	
		mainPanel.add(possiblesPanel);
		optionsPanel = new Panel();
		optionsPanel.setLayout(new GridLayout(1, 5, SPACING, SPACING));
		giveUpButton = new Button();
		giveUpButton.setBackground(ToMarUtils.getColor(12));
		giveUpButton.setFont(new Font("Verdana",Font.BOLD,ACTIONFONTSIZE));
		giveUpButton.setLabel("Give Up");
		giveUpButton.addActionListener(this);
		optionsPanel.add(giveUpButton);
		backUpButton = new Button();
		backUpButton.setBackground(ToMarUtils.getColor(12));
		backUpButton.setFont(new Font("Verdana",Font.BOLD,ACTIONFONTSIZE));
		backUpButton.setLabel("Back Up");
		backUpButton.addActionListener(this);
		optionsPanel.add(backUpButton);
		submitButton = new Button("***     Start Game    ***");
		submitButton.setBackground(ToMarUtils.getColor(9));
		submitButton.setFont(new Font("Verdana",Font.BOLD,ACTIONFONTSIZE));
		submitButton.addActionListener(this);
		optionsPanel.add(submitButton, BorderLayout.CENTER);
//		cheatsOnOffButton = new Button();
//		cheatsOnOffButton.setBackground(new Color(102, 192, 192));
//		cheatsOnOffButton.setFont(new Font("Verdana",Font.PLAIN,FONTSIZE));
//		cheatsOnOffButton.setLabel("Cheating On");
//		cheatsOnOffButton.addActionListener(this);
//		optionsPanel.add(cheatsOnOffButton, BorderLayout.EAST);
		clearButton = new Button();
		clearButton.setBackground(ToMarUtils.getColor(12));
		clearButton.setFont(new Font("Verdana",Font.BOLD,ACTIONFONTSIZE));
		clearButton.setLabel("Clear");
		clearButton.addActionListener(this);
		optionsPanel.add(clearButton);
		mainPanel.add(optionsPanel);
		messageLabel = new Label();
		messageLabel.setFont(new Font("Verdana",Font.PLAIN,MESSAGEFONTSIZE));
		messageLabel.setAlignment(Label.CENTER);
		messageLabel.setText("Welcome to ToMar Primal Factors!");
		mainPanel.add(messageLabel);
		started = false;
	}
	public void reinit()
	{
		puzzles = new Puzzle[numberOfPuzzles];
		for (int i = 0; i < numberOfPuzzles; i++)
		{
			puzzles[i] = new Puzzle();
		}	
		currentPuzzle = points = numberSolved = 0;
		submitButton.setLabel("PASS");
		ToMarUtils.startTimer();
		setUpPuzzle();
	}	
	private void setUpPuzzle()
	{						 
		puzzleLabel.setText("" + puzzles[currentPuzzle].getTarget());
		for (int i = 0; i < POSSIBLES; i++)
		{
			possibleButtons[i].setLabel("" + puzzles[currentPuzzle].getPossibles()[i]);
		}
		guesses = new int[GUESSES];
		numberOfGuesses = 0;
		this.doCheats();
		this.doDisplay();
	}
	
	public void endChallenge()
	{	
		int secs = maxTime - (int) ToMarUtils.getElapsedSeconds() > 0 ? maxTime - (int) ToMarUtils.getElapsedSeconds() : 1;
		for (int i = 0; i < numberOfPuzzles; i++)
		{
			if (puzzles[i].isSolved())
			{
				points += puzzles[i].getPoints();
			}
		}
		points *= secs;
		try
		{
			URL url = new URL(ToMarUtils.postHighScore(this, getParameter("PLAYER"), points, "PRFC"));
			this.getAppletContext().showDocument(url, "jsp");
		}
		catch(Exception e)
		{
			ToMarUtils.log("Error 1: " + e);
		}
		this.messageLabel.setText("That's it! Your score was " + points + "!");
		submitButton.setLabel("***     Start Game    ***");
		started = false;
	}	
	
	private void getNextPuzzle()
	{
		do
		{
			currentPuzzle = (currentPuzzle == 9) ? 0 : currentPuzzle + 1;
		}	while (puzzles[currentPuzzle].isSolved());
		setUpPuzzle();
	}	
	public void actionPerformed(ActionEvent evt)
	{ 
		getAppletContext().showStatus(VERSION);
		if (evt.getSource() == giveUpButton)
		{
			if ("Give Up".equals(giveUpButton.getLabel()))
			{
				giveUpButton.setLabel("Confirm");
			}
			else
			{	
				giveUpButton.setEnabled(false);
				endChallenge();
			}	
		}
		else
		{
			giveUpButton.setLabel("Give Up");
			giveUpButton.setEnabled(true);
			if (started == false)
			{
				if (evt.getSource() == submitButton)
				{
					started = true;
					reinit();
				}
			}
			else if (evt.getSource() == clearButton)
			{
				numberOfGuesses = 0;
				doDisplay();
				doCheats();
			}
			else if (evt.getSource() == backUpButton)
			{
				numberOfGuesses -= 1;
				doDisplay();
				doCheats();
			}	
			else if (evt.getSource() == submitButton)
			{
				getNextPuzzle();
			}	
	//		else if (evt.getSource() == cheatsOnOffButton)
	//		{
	//			if (cheats == false)
	//			{
	//				cheats = true;
	//				doCheats();
	//			}
	//			else
	//			{
	//				cheats = false;
	//				cheatUpLabel.setText("");
	//				cheatDownLabel.setText("");
	//			}	
	//		}
			else
			{
				for (int i = 0; i < POSSIBLES; i++)
				{
					if (evt.getSource() == possibleButtons[i])
					{
						guesses[numberOfGuesses++] = puzzles[currentPuzzle].getPossibles()[i];
						doCheats();
						doDisplay();
						if (cheatUp == puzzles[currentPuzzle].getTarget() && cheatDown == 1.0)
						{
							puzzles[currentPuzzle].setSolved(true);
							numberSolved += 1;
							if (numberSolved == numberOfPuzzles)
							{
								endChallenge();
							}
							else
							{	
								getNextPuzzle();
							}	
						}
						break;
					}
				}	
			}
		}	
	}	
	private void doDisplay()
	{
		int i;
		for (i = 0; i < numberOfGuesses; i++)
		{	
			this.guessLabels[i].setForeground(Color.blue);
			this.guessLabels[i].setText("" + guesses[i]);
		}
		for (i = i; i < GUESSES; i++)
		{	
			this.guessLabels[i].setForeground(Color.black);
			this.guessLabels[i].setText("?");
		}
	}	
	private void doCheats()
	{
		cheatUp = 1;
		cheatDown = (double) puzzles[currentPuzzle].getTarget();
		cheatDownLabel.setForeground(Color.black);
		messageLabel.setText("Click on a factor to start...");
		for (int i = 0; i < numberOfGuesses; i++)
		{
			cheatUp *= guesses[i];
			cheatDown /= guesses[i];
			if (cheatDown - (int)cheatDown > 0)
			{
				cheatDownLabel.setForeground(Color.red);
				messageLabel.setText("You need to back up and try again!");
			}
			else
			{
				cheatDownLabel.setForeground(Color.black);
				messageLabel.setText("You're doing well!");
			}
		}	
		messageLabel.setText("Puzzle #" + (currentPuzzle + 1) +
							"   Difficulty: " + puzzles[currentPuzzle].getPoints() + 
							"   Solved: " + numberSolved + 
							"   Time: " + ToMarUtils.displayTime(ToMarUtils.getElapsedSeconds()));
		cheatUpLabel.setText("" + cheatUp);
		String cheatDownString = ("" + cheatDown + "          ").substring(0,8);
		cheatDownLabel.setText(cheatDownString);
	}	
	public static void main(String args[])
	{
	}
}

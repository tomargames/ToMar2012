import java.applet.Applet;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

public class Puzzle
{
	private static Vector[] wordTable = null;
	private static Vector pickWords = new Vector();
	private Vector answerWords = new Vector();
	private int points;
	private int timeAllowed = 300;
	private Vector selectedLetters = new Vector();
	private Letter[] puzzle;
	private static final int NUMBEROFLETTERS = 6;
	private String answer;
	
	public String toString()
	{
		return "Puzzle pickWords: " + pickWords.size() + ", answerWords: " + answerWords.size() + ", " + answer;
	}
	
	public Puzzle(Applet a)
	{
		if (wordTable == null)
		{
			String ServerPath = a.getDocumentBase().toString();
			ServerPath = ServerPath.substring(0,ServerPath.lastIndexOf("/"));
			ServerPath = ServerPath.substring(0,ServerPath.lastIndexOf("/") + 1) + "Words/";
			wordTable = new Vector[4];
			wordTable[0] = readFile(ServerPath + "words3.txt");
			wordTable[1] = readFile(ServerPath + "words4.txt");
			wordTable[2] = readFile(ServerPath + "words5.txt");
			wordTable[3] = readFile(ServerPath + "wordsuwm.txt");
			for (int i = 0; i < wordTable[3].size(); i++)
			{
				pickWords.addElement(((Word) wordTable[3].get(i)).getWord());
			}
		}
		puzzle = new Letter[NUMBEROFLETTERS];
		//pick a 6-letter word
		answer = (String) pickWords.elementAt(ToMarUtils.getRnd(pickWords.size()));
		for (int i = 0; i < NUMBEROFLETTERS; i++)
		{
			puzzle[i] = new Letter(i, answer.substring(i, i+ 1));
		}
		mix();
		answerWords = new Vector();
		timeAllowed = 60;
		// set up answer vectors
		for (int i = 0; i < 4; i++)
		{
			for (int j = 0; j < wordTable[i].size(); j++)
			{
				Word w = (Word) wordTable[i].get(j);
				if (goodWord(w.getWord()))
				{
					timeAllowed += w.getWord().length();
					answerWords.addElement(new AnswerWord(w, answerWords.size()));
				}
			}
		}
	}
	public int getTimeAllowed()
	{
//		return timeAllowed;
		return 180;
	}
	public boolean clicked(int x, int y)
	{
		for (int i = 0; i < NUMBEROFLETTERS; i++)
		{
			if (puzzle[i].clicked(x, y))
			{
				return selectLetter(puzzle[i]);
			}
		}
		return false;
	}
	public boolean keyTyped(String key)
	{
		for (int i = 0; i < NUMBEROFLETTERS; i++)
		{
			if (puzzle[i].isLetter(key))
			{
				if (selectLetter(puzzle[i]))
				{
					return true;
				}
			}
		}
		return false;
	}
	public void draw(Graphics og, int gameStage)
	{
		for (int i = 0; i < NUMBEROFLETTERS; i++)
		{
			puzzle[i].draw(og);
		}
		og.setFont(new Font("Verdana", Font.BOLD, 14));
		for (int i = 0; i < answerWords.size(); i++)
		{
			((AnswerWord) answerWords.get(i)).draw(og, gameStage);
		}
		og.setColor(new Color(80,0,125));
		og.setFont(new Font("Verdana", Font.PLAIN, 48));
		og.drawString(getSelectedWord(),Letter.RMARGIN + 50,Letter.TMARGIN - 20);
	}
	public void mix()
	{
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < NUMBEROFLETTERS; i++)
		{
			sb.append(puzzle[i].getLabel());
		}
		for (int i = 0; i < NUMBEROFLETTERS; i++)
		{
			int rnd = ToMarUtils.getRnd(sb.length());
			puzzle[i].setLabel("" + sb.charAt(rnd));
			sb.deleteCharAt(rnd);
		}
	}
	public Word evaluateWord()
	{
		// returns -2 for word already used
		// returns -1 for less than 3 letters
		// returns 0 for word not found
		String submittedWord = getSelectedWord();
		Word w = new Word(submittedWord, 0);
		if (submittedWord.length() < 3)
		{
			w.setPoints(-1);
		}	
		else
		{	
			// check to see if it's on the answer list
			for (int i = 0; i < answerWords.size(); i++)
			{
				AnswerWord aw = (AnswerWord) answerWords.elementAt(i);
				if (submittedWord.equalsIgnoreCase(aw.getWord()))
				{
					if (aw.isFound())
					{	
						w.setPoints(-2);
					}
					else
					{
						aw.setFound(true);
						w.setPoints(aw.getPoints());
						points += w.getPoints();
					}
					break;
				}
			}
		}	
		clearWord();
		return w;
	}
	private static Vector readFile(String filename)
	{	
		Vector v = new Vector();
        String s = new String();
        try
        {
            URL url = new URL(filename);
		    BufferedReader br =	new BufferedReader(new InputStreamReader(url.openStream()));
            while   (true)
            {
                s = br.readLine();
                if  (s == null)
                {
                    break;
                }
                else
                {
    				int pointCount = 0;
    				s = s.toUpperCase();
    				for (int j = 0; j < s.length(); j++)
    				{
    					String ch = s.substring(j, j+1);
    					if ("XQ".indexOf(ch) > -1)
    					{
    						pointCount += 3;
    					}	
    					else if ("JWVZ".indexOf(ch) > -1)
    					{
    						pointCount += 2;
    					}	
    					else
    					{
    						pointCount += 1;
    					}
    				}
    				pointCount *= s.length() * s.length(); 
    				v.addElement(new Word(s, pointCount));
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
            System.out.println("Exception = " + e);
		}
//		System.out.println("Read file: " + filename + ": " + h.size());
		return v;
	}
	public int getPoints()
	{
		return points;
	}
	public void setPoints(int points)
	{
		this.points = points;
	}
	public Vector getSelectedLetters()
	{
		return selectedLetters;
	}
	public String getSelectedWord()
	{
		StringBuffer wordOut = new StringBuffer(""); 
		for (int i = 0; i < selectedLetters.size(); i++)
		{
			wordOut.append(((Letter) selectedLetters.elementAt(i)).getLabel());
		}
		return wordOut.toString();
	}
	public void clearWord()
	{
		while (selectedLetters.size() > 0)
		{
			clearLetter();
		}
	}
	public void clearLetter()
	{
		// this will clear the most recently added letter
		if (selectedLetters.size() > 0)
		{
			((Letter) selectedLetters.elementAt(selectedLetters.size() - 1)).putBack();
		}
		selectedLetters.removeElementAt(selectedLetters.size() - 1);
	}
	public boolean selectLetter(Letter l)
	{
		if (l.selectLetter())
		{
			selectedLetters.addElement(l);
			return true;
		}
		return false;
	}
	private boolean goodWord(String guess)
	{
		// returns true if all the letters in guess are contained in target
		String tgt = answer;
		for (int i = 0; i < guess.length(); i++)
		{
			String g = guess.substring(i, i + 1);
			boolean found = false;
			for (int j = 0; j < tgt.length(); j++)
			{
				if (g.equals(tgt.substring(j, j + 1)))
				{
					tgt = tgt.substring(0,j) + tgt.substring(j+1, tgt.length());
					found = true;
					break;
				}
			}
			if (!found)
			{
				return false;
			}	
		}	
		return true;
	}
	public int getBonus()
	{
		return answerWords.size() * answerWords.size();
	}
	public boolean allWordsFound()
	{
		for (int i = 0; i < answerWords.size(); i++)
		{
			AnswerWord aw = (AnswerWord) answerWords.elementAt(i);
			if (!(aw.isFound()))
			{
				return false;
			}
		}
		return true;
	}
}

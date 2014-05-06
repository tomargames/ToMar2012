public class Word
{
	String word;
	int points;
	
	
	public Word(){}
	
	public Word(String word, int points)
	{
		this.word = word;
		this.points = points;
	}
	public int getPoints()
	{
		return points;
	}

	public void setPoints(int points)
	{
		this.points = points;
	}

	public String getWord()
	{
		return word;
	}

	public void setWord(String word)
	{
		this.word = word;
	}

}

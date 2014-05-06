
public class BColumn
{
	private boolean active;
	private Balloon[] balloons;
	private int[] positions = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
	private int lowest;
	private int column;
	
	public BColumn(int col)
	{
		column = col;
		active = true;
		balloons = new Balloon[BLN.NUMBEROFROWS];
		for (int r = 0; r < BLN.NUMBEROFROWS; r++)
		{
			balloons[r] = new Balloon(r, column);
			positions[r] = r;
		}
		lowest = BLN.NUMBEROFROWS-1;
	}
	
	public boolean isActive()
	{
		return active;
	}	
	
	public void reNumber()
	{
		int pos = 0;
		for (int r = 0; r < BLN.NUMBEROFROWS; r++)
		{	
			if (balloons[r].getState() != Balloon.POPPED)
			{
				positions[pos] = r;
				balloons[r].setRow(pos++);
			}
		}
		lowest = pos - 1;
		active = lowest < 0 ? false : true;
	}
	
	public void setColumn(int c)
	{
		column = c;
		for (int r = 0; r < BLN.NUMBEROFROWS; r++)
		{	
			balloons[r].setCol(c);
		}
	}
	
	public int getColumn()
	{
		return column;
	}
	
	public int getLowest()
	{
		return lowest;
	}
	
	public void setLowest(int l)
	{
		lowest = l;
	}
	
	public Balloon getBalloon(int p)
	{
		return balloons[positions[p]];
	}
	
	public Balloon[] getBalloons()
	{
		return balloons;
	}
}

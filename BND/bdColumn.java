import java.util.*;

public class bdColumn extends Vector
{
	private int column;
	
	public bdColumn(int column)
	{
		this.column = column;
	}
	
	public boolean isFull()
	{
		return (this.size() == BND.maxSize) ? true : false;
	}
	
	public boolean addLetter()
	{
		if (this.isFull())
		{	
			return false;
		}	
		this.addElement(new bdLetter(this.size(),this.column));
		return true;
	}	
	
	public boolean process(int r)
	{
		boolean bonus = ((bdLetter) this.elementAt(r)).getBonus();
//		System.out.println("Processing row " + r + " column " + column + " bonus is " + 
//						   bonus + " size of column is " + this.size());
		this.removeElementAt(r);
//		System.out.println("After removal, size is now " + this.size());
		for (int i = 0; i < this.size(); i++)
		{
			((bdLetter) this.elementAt(i)).setRow(i);
			((bdLetter) this.elementAt(i)).setSelected(false);
		}	
//		System.out.println("Renumbered rows");
		return bonus;
	}	
}


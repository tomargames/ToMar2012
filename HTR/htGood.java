import java.util.*;

public class htGood extends htTarget
{
	private long startTime;
	
	public htGood()
	{
		super();
		color = HTR.colorGood;
		Date date = new Date();
	    startTime = date.getTime();
	}	
	
	public long getStartTime()
	{
		return startTime;
	}	
}

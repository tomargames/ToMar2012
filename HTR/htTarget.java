

public class htTarget extends htThing
{
	
	public htTarget()
	{
		boolean goodLocation = false;
		while (goodLocation == false)
		{	
			row = ToMarUtils.getRnd(HTR.numRows);
			column = ToMarUtils.getRnd(HTR.numCols);
//			System.out.println("creating a new " + this.getClass().getName());
//			System.out.println("testing row = " + row + " column = " + column + " good = " + goodLocation);
			goodLocation = true;
//			System.out.println("    you row = " + HappyTrails.you.getRow() + " column = " + HappyTrails.you.getColumn() + " good = " + goodLocation);
//			if (HappyTrails.good != null)
//			{
//				System.out.println(" target row = " + HappyTrails.good.getRow() + " column = " + HappyTrails.good.getColumn() + " good = " + goodLocation);
//			}
			if (HTR.you.hit(row, column))
			{
				goodLocation = false;
				continue;
			}
			if (HTR.good != null && HTR.good.hit(row, column))
			{
				goodLocation = false;
				continue;
			}
			for (int i = 0; i < HTR.bombs.size(); i++)
			{
//				System.out.println("   bomb row = " + ((htBomb)HappyTrails.bombs.elementAt(i)).getRow() + " column = " + ((htBomb)HappyTrails.bombs.elementAt(i)).getColumn() + " good = " + goodLocation);
				if (((htBomb)HTR.bombs.elementAt(i)).hit(row, column))
				{
					goodLocation = false;
					break;
				}	
			}
			if (goodLocation == true)
			{	
				for (int i = 0; i < HTR.them.size(); i++)
				{
//					System.out.println("" + i + "   guy row = " + ((htThem) HappyTrails.them.elementAt(i)).getRow() + " column = " + ((htThem) HappyTrails.them.elementAt(i)).getColumn() + " good = " + goodLocation);
					if (((htThem) HTR.them.elementAt(i)).hit(row, column))
					{
						goodLocation = false;
						break;
					}
				}
				if (goodLocation == true)
				{
					for (int i = 0; i < HTR.badGuys.size(); i++)
					{
//						System.out.println("" + i + "redguy row = " + ((htBadGuy) HappyTrails.badGuys.elementAt(i)).getRow() + " column = " + ((htBadGuy) HappyTrails.badGuys.elementAt(i)).getColumn() + " good = " + goodLocation);
						if (((htBadGuy) HTR.badGuys.elementAt(i)).hit(row, column))
						{
							goodLocation = false;
							break;
						}	
					}
				}
			}
//			System.out.println("row = " + row + " column = " + column + " good = " + goodLocation);
		}
//		System.out.println("row = " + row + " column = " + column + " good = " + goodLocation);
	}	
}

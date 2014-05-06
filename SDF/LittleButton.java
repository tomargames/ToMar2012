import java.awt.Graphics;

public class LittleButton extends ToMarButton
{
	public LittleButton (int x, int y, int width, String label)
	{
		super(x, y, width, label);
		this.setHeight(width);
	}
	public void draw(Graphics og)
	{
		og.setColor(tmColors.PURPLE);
		og.fillRoundRect(x, y, width, height, 5, 5);
		og.setColor(tmColors.BLACK);
		og.drawRoundRect(x, y, width, height, 5, 5);
		og.setFont(font);
		og.drawString(label, x - width, y - width/2);
	}	


}

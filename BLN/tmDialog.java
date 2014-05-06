import java.awt.Color;
import java.awt.Graphics;

/*
 * Created on Mar 22, 2005
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
 
/**
 * @author marie
 *
 * To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
public class tmDialog extends tmAlert
{
	public static final int NONE = 0;
	public static final int ALPHA = 1;
	public static final int NUMERIC = 2;
	public static final int ALPHANUMERIC = 3;
	public static final int DECIMAL = 4;
	public static final int ANYTHING = 5;
	private String errorMessage;
	private String input;
	private int maxInput;
	private int inputType;
	tmButton cancelButton;
	
	public tmDialog(String message, int maxInput, int inputType)
	{
		super(message);
		this.maxInput = maxInput;
		this.inputType = inputType;
		this.width = 400;
		cancelButton = new tmButton(x + 10, y + 60, tmColors.LIGHTGRAY, 70, 35, "Cancel");
		cancelButton.setHeight(35);
		input = errorMessage = "";
	}
	public void processKeyInput(int keyCode, char keyChar)
	{
		setErrorMessage("");
		if (this.inputType == NONE)
		{
			setMessage("Click on Okay or Cancel...");
		}
		else if (keyCode == 8)	// backspace key
		{
			if (input.length() > 0)
			{
				input = input.substring(0, input.length() - 1);
			}
		}
		else if (input.length() < maxInput)
		{
			input += keyChar;
		}
		else
		{
			input = input.substring(0, maxInput - 1) + keyChar;
		}
	}
	public int processMouseInput(int x, int y)
	{
		setErrorMessage("");
		if (getCancelButton().clicked(x, y))
		{
			return CANCELLED;
		}
		if (getSubmitButton().clicked(x, y))
		{
			if (inputType == NONE || isValidInput())
			{
				return VALID;
			}
			return INVALID;
		}
		setErrorMessage("Click on Okay or Cancel...");
		return GARBAGE;
	}

	public void draw(Graphics og)
	{
		og.setColor(new Color(222,180,180));
		og.fillRoundRect(x, y, width, height, 5, 5);
		og.setColor(new Color(0,0,0));
		og.drawRoundRect(x, y, width, height, 5, 5);
		og.setFont(font);
		og.drawString(message, x + 5, y + 15);
		og.drawString(errorMessage, x + 5, y + 30);
		og.drawString(input, x + 5, y + 50);
		cancelButton.draw(og);
		submitButton.draw(og);
	}	
	private boolean goodNumber(String s)
	{
		try
		{
			(new Integer(s)).intValue();
		}
		catch (Exception e)
		{
			setErrorMessage("Input needs to be a number.");
			return false;
		}
		return true;
	}
	private boolean goodAlpha(String s)
	{
		String temp = s.toUpperCase();
		if ("A".compareTo(temp) < 1 &&
			"Z".compareTo(temp) > -1)
		{
			return true;
		}
		return false; 
	}
	public boolean isValidInput()
	{
		if (input.length() < 1)
		{
			setErrorMessage("At least one character must be entered...");
			return false;
		}
		if (inputType == NUMERIC)
		{
			return goodNumber(input);
		}
		else if (inputType == ALPHA)
		{
			for (int i = 0; i < input.length(); i++)
			{
				if (!goodAlpha(input.substring(i, i + 1)))
				{
					return false;
				}
			}
			return true;
		}
		else if (inputType == ALPHANUMERIC)
		{
			for (int i = 0; i < input.length(); i++)
			{
				if (!goodAlpha(input.substring(i, i + 1)) &&
					!goodNumber(input.substring(i, i + 1)))
				{
					return false;
				}
			}
			return true;
		}
		else if (inputType == DECIMAL)
		{
			try
			{
				new Double(input); 
			}
			catch (Exception e)
			{
				setErrorMessage("Input needs to be a number.");
				return false;
			}
			return true;
		}
		else
		{
			return true;
		}
	}
	/**
	 * @return Returns the maxInput.
	 */
	public int getMaxInput()
	{
		return maxInput;
	}

	/**
	 * @param maxInput The maxInput to set.
	 */
	public void setMaxInput(int maxInput)
	{
		this.maxInput = maxInput;
	}

	/**
	 * @return Returns the cancelButton.
	 */
	public tmButton getCancelButton()
	{
		return cancelButton;
	}

	/**
	 * @param cancelButton The cancelButton to set.
	 */
	public void setCancelButton(tmButton cancelButton)
	{
		this.cancelButton = cancelButton;
	}

	/**
	 * @return Returns the input.
	 */
	public String getInput()
	{
		return input;
	}

	/**
	 * @param input The input to set.
	 */
	public void setInput(String input)
	{
		this.input = input;
	}

	/**
	 * @return Returns the inputType.
	 */
	public int getInputType()
	{
		return inputType;
	}

	/**
	 * @param inputType The inputType to set.
	 */
	public void setInputType(int inputType)
	{
		this.inputType = inputType;
	}

	/**
	 * @return Returns the errorMessage.
	 */
	public String getErrorMessage()
	{
		return errorMessage;
	}

	/**
	 * @param errorMessage The errorMessage to set.
	 */
	public void setErrorMessage(String errorMessage)
	{
		this.errorMessage = errorMessage;
	}

}

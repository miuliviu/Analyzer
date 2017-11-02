package studentmarksanalyser.gui.components;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.Timer;

import studentmarksanalyser.StudentMarksAnalyser;

import javax.imageio.ImageIO;

// Can be clicked to perform some action / has a mouse listener
// Has a background colour and a mouseover background colour

public abstract class ImageButton extends JComponent {
	
	private static final int PADDING = 5;
	
	private BufferedImage image, imageDisabled;
	private Color backgroundColor, backgroundHoverColor, backgroundPressedColor;
	private String text = "";
	private Color textColor, textDisabledColor;
	
	private int textWidth = 0;
	private int imageWidth, imageHeight, buttonWidth, buttonHeight;
	
	private boolean hover = false;
	private boolean disabled = false;
	private boolean canDisable = false;
	private boolean pressed = false;
	
	private static final int TIMER_INTERVAL = 100;
	
	public ImageButton(String imageLocation, Color backgroundColor, Color backgroundHoverColor, Color backgroundPressedColor) throws IOException
	{
		this.backgroundColor = backgroundColor;
		this.backgroundHoverColor = backgroundHoverColor;
		this.backgroundPressedColor = backgroundPressedColor;
		image = ImageIO.read(StudentMarksAnalyser.class.getResourceAsStream((imageLocation)));
		imageWidth = image.getWidth();
		imageHeight = image.getHeight();
		buttonWidth = imageWidth + PADDING * 2;
		buttonHeight = imageHeight + PADDING * 2;
		repaint();
		
		this.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent arg0) {
				if (arg0.getButton() == MouseEvent.BUTTON1)
				{
					if (!disabled)
					{
						pressed = true;
						repaint();
					}
				}
			}
			@Override
			public void mouseReleased(MouseEvent arg0) {
				if (arg0.getButton() == MouseEvent.BUTTON1)
				{
					// Check if button still contains mouse
					if (!disabled && containsMouse())
					{
						pressed = false;
						hover = false;
						repaint();
						mousePress();
					}
				}
			}
			@Override
			public void mouseEntered(MouseEvent arg0) {
				if (!getMouseHover())
					mouseEnter();
			}
			@Override
			public void mouseExited(MouseEvent arg0) {
				if (getMouseHover())
					mouseLeave();
			}
		});
	}
	
	public ImageButton(
			String imageLocation, String imageDisabledLocation, 
			Color backgroundColor, Color backgroundHoverColor, Color backgroundPressedColor
		) throws IOException
	{
		this(imageLocation, backgroundColor, backgroundHoverColor, backgroundPressedColor);
		imageDisabled = ImageIO.read(StudentMarksAnalyser.class.getResourceAsStream((imageDisabledLocation)));
		canDisable = true;
	}
	
	public ImageButton(
			String text, Color textColor, Color textDisabledColor,
			String imageLocation, String imageDisabledLocation, 
			Color backgroundColor, Color backgroundHoverColor, Color backgroundPressedColor
		) throws IOException
	{
		this(imageLocation, imageDisabledLocation, backgroundColor, backgroundHoverColor, backgroundPressedColor);
		this.text = text;
		this.textColor = textColor;
		this.textDisabledColor = textDisabledColor;
		repaint();
	}
	
	public ImageButton(String imageLocation) throws IOException
	{
		this(
				imageLocation, Frame.getBackgroundColor(), Frame.getBackgroundColorLight(), Frame.getBackgroundColorDark()
			);
	}
	
	public ImageButton(String imageLocation, String imageDisabledLocation) throws IOException
	{
		this(
				imageLocation, imageDisabledLocation, 
				Frame.getBackgroundColor(), Frame.getBackgroundColorLight(), Frame.getBackgroundColorDark()
			);
	}
	
	public ImageButton(String text, String imageLocation, String imageDisabledLocation) throws IOException
	{
		this(
				text, Frame.getTextColor(), Frame.getTextColorDisabled(), 
				imageLocation, imageDisabledLocation, 
				Frame.getBackgroundColor(), Frame.getBackgroundColorLight(), Frame.getBackgroundColorDark()
			);
	}
	
	public boolean containsMouse()
	{
		// Get mouse position relative to button
		Point mouse = MouseInfo.getPointerInfo().getLocation();
		Point button = getLocationOnScreen();
		mouse = new Point(mouse.x - button.x, mouse.y - button.y);
		// Check if button does not contain mouse
		if (new Rectangle(0, 0, getWidth(), getHeight()).contains(mouse))
			return true;
		return false;
	}
	
	public Dimension getPreferredSize()
	{
		return new Dimension(buttonWidth, buttonHeight);
	}
	
	public void paintComponent(Graphics g)
	{
		if (pressed && !disabled)
			g.setColor(backgroundPressedColor);
		else if (hover && !disabled)
			g.setColor(backgroundHoverColor);
		else
			g.setColor(backgroundColor);
		g.fillRect(0, 0, buttonWidth, buttonHeight);
		
		if (!text.isEmpty())
		{
			g.setFont(Frame.getDefaultFont(14));
			
			// Calculate textWidth if not already done so
			if (textWidth == 0)
			{
				textWidth = g.getFontMetrics().stringWidth(text);
				if (textWidth > buttonWidth)
					buttonWidth = textWidth + PADDING * 2;
				buttonHeight = imageHeight + 18 + PADDING * 2;
				this.revalidate();
			}
		// Draw button
			Graphics2D g2d = (Graphics2D) g;
			g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB);
			
			if (disabled)
				g.setColor(textDisabledColor);
			else
				g.setColor(textColor);
			g.drawString(text, (buttonWidth - textWidth) / 2, imageHeight + 16 + PADDING);
		}
		if (disabled)
			g.drawImage(imageDisabled, (buttonWidth - image.getWidth()) / 2, PADDING, null);
		/*else if (hover)
			g.drawImage(imageHover, (buttonWidth - image.getWidth()) / 2, padding, null);*/
		else
			g.drawImage(image, (buttonWidth - image.getWidth()) / 2, PADDING, null);
	}
	
	public void mouseEnter()
	{
		hover = true;
		repaint();
	}
	
	public void mouseLeave()
	{
		hover = false;
		pressed = false;
		repaint();
	}
	
	public abstract void mousePress();
	
	public boolean getMouseHover()
	{
		return hover;
	}
	
	public void disable()
	{
		if (canDisable)
		{
			disabled = true;
			repaint();
		}
	}
	
	public void enable()
	{
		disabled = false;
		repaint();
	}
	
}

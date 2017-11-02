package studentmarksanalyser.gui.components;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.HashMap;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class Frame extends JFrame {
	
	public static final Dimension DEFAULT_WINDOW_SIZE = new Dimension(800, 640);
	public Dimension minimumWindowSize = DEFAULT_WINDOW_SIZE; //new Dimension(640, 400);
	
	public static final int BORDER_WIDTH = 2;
	
	/*public static final ColorTheme[] COLOR_THEMES = {
			new ColorTheme("Blue", new Color(41, 97, 157), new Color(50, 118, 190), new Color(67, 133, 205), Color.WHITE, new Color(110, 158, 209)),
			new ColorTheme("Orange", new Color(175, 84, 44), new Color(205, 101, 56), new Color(212, 123, 84), Color.WHITE, new Color(221, 149, 118)),
			new ColorTheme("Green", new Color(78, 122, 48), new Color(87, 137, 54), new Color(97, 152, 61), Color.WHITE, new Color(140, 174, 117)),
			new ColorTheme("Purple", new Color(107, 68, 140), new Color(120, 77, 158), new Color(135, 90, 175), Color.WHITE, new Color(162, 133, 188))
	};*/
	public static ColorTheme currentTheme = new ColorTheme("Blue", new Color(41, 97, 157), new Color(50, 118, 190), new Color(67, 133, 205), Color.WHITE, new Color(110, 158, 209));
	
	private static Map<Integer, Font> fonts = new HashMap<Integer, Font>();
	private static final String[] fontNames = {"Segoe UI", "Ubuntu", "Verdana"}; // Application will use the first font in this array which is on this machine
	private static String fontName = "";

	private ResizePanel[] resizePanels = new ResizePanel[8];
	private JPanel mainContainer;
	private JPanel content;
	
	protected TitleBar titleBar;
	
	/** 
	 * Constructor. Creates and displays the main application frame and adds a panel to it.
	 * @param content the panel to display in this frame.
	 * @param title the text to display on the title bar.
	 */
	public Frame(JPanel content, String title)
	{
		this(content, DEFAULT_WINDOW_SIZE, title);
	}
	
	/** 
	 * Constructor. Creates and displays the main application frame and adds a panel to it.
	 * @param content the panel to display in this frame. 
	 * @param minimumSize the minimum size of this frame.
	 * @param title the text to display on the title bar.
	 */
	public Frame(JPanel content, Dimension minimumSize, String title)
	{
		this(minimumSize, title);
		setContent(content);
	}
	
	/** 
	 * Constructor. Creates and displays the main application frame without any content.
	 * Use setContent() afterwards to add a panel.
	 * @param minimumSize the minimum size of this frame.
	 * @param title the text to display on the title bar.
	 */
	public Frame(Dimension minimumSize, String title)
	{
		this(minimumSize, title, true, true, true);
	}
	
	/** 
	 * Constructor. Creates and displays the main application frame and adds a panel to it.
	 * @param content the panel to display in this frame.
	 * @param title the text to display on the title bar.
	 */
	public Frame(JPanel content, String title, boolean minimise, boolean maximise, boolean close)
	{
		this(content, DEFAULT_WINDOW_SIZE, title, minimise, maximise, close);
	}
	
	/** 
	 * Constructor. Creates and displays the main application frame and adds a panel to it.
	 * @param content the panel to display in this frame. 
	 * @param minimumSize the minimum size of this frame.
	 * @param title the text to display on the title bar.
	 */
	public Frame(JPanel content, Dimension minimumSize, String title, boolean minimise, boolean maximise, boolean close)
	{
		this(minimumSize, title, minimise, maximise, close);
		setContent(content);
	}
	
	/** 
	 * Constructor. Creates and displays the main application frame without any content.
	 * Use setContent() afterwards to add a panel.
	 * @param minimumSize the minimum size of this frame.
	 * @param title the text to display on the title bar.
	 */
	public Frame(Dimension minimumSize, String title, boolean minimise, boolean maximise, boolean close)
	{
		this.setTitle(title);
		this.minimumWindowSize = minimumSize;
		this.titleBar = new TitleBar(this, title, minimise, maximise, close);
		setSize(minimumWindowSize);
		setLocationRelativeTo(null);
		setUndecorated(true);
		getContentPane().setLayout(new GridBagLayout());
		
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = 1;
		gbc.gridy = 1;
		gbc.weightx = 1.0f;
		gbc.weighty = 1.0f;
		gbc.fill = GridBagConstraints.BOTH;
		mainContainer = new JPanel(new GridBagLayout());
		getContentPane().add(mainContainer, gbc);
		resizePanels[0] = new ResizePanel(ResizePanel.NORTH, Frame.getBackgroundColor(), BORDER_WIDTH, this);
		resizePanels[1] = new ResizePanel(ResizePanel.NORTH_EAST, Frame.getBackgroundColor(), BORDER_WIDTH, this);
		resizePanels[2] = new ResizePanel(ResizePanel.EAST, Frame.getBackgroundColor(), BORDER_WIDTH, this);
		resizePanels[3] = new ResizePanel(ResizePanel.SOUTH_EAST, Frame.getBackgroundColor(), BORDER_WIDTH, this);
		resizePanels[4] = new ResizePanel(ResizePanel.SOUTH, Frame.getBackgroundColor(), BORDER_WIDTH, this);
		resizePanels[5] = new ResizePanel(ResizePanel.SOUTH_WEST, Frame.getBackgroundColor(), BORDER_WIDTH, this);
		resizePanels[6] = new ResizePanel(ResizePanel.WEST, Frame.getBackgroundColor(), BORDER_WIDTH, this);
		resizePanels[7] = new ResizePanel(ResizePanel.NORTH_WEST, Frame.getBackgroundColor(), BORDER_WIDTH, this);
		if (!getMaximised())
			enableResizing();
		
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.weighty = 0.0f;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		mainContainer.add(titleBar, gbc);
		titleBar.reloadComponents();
		
		setVisible(true);
	}
	
	public void setContent(JPanel content)
	{
		this.content = content;
		
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridy = 1;
		gbc.weighty = 1.0f;
		gbc.fill = GridBagConstraints.BOTH;
		mainContainer.add(content, gbc);
	}
	
	public TitleBar getTitleBar(TitleBar titleBar)
	{
		return titleBar;
	}
	
	private void enableResizing()
	{
		for (ResizePanel rp : resizePanels)
		{
			if (!rp.isShowing())
			{
				this.add(rp, rp.getGridBagConstraints());
			}
		}
	}
	
	private void disableResizing()
	{
		for (ResizePanel rp : resizePanels)
		{
			this.remove(rp);
		}
	}
	
	void minimise()
	{
		setExtendedState(JFrame.ICONIFIED);
		enableResizing();
	}
	
	void maximise()
	{
		super.setMaximizedBounds(GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds());
		setExtendedState(JFrame.MAXIMIZED_BOTH);
		disableResizing();
	}
	
	void restore()
	{
		setExtendedState(JFrame.NORMAL);
		enableResizing();
	}
	
	boolean getMaximised()
	{
		return this.getExtendedState() == JFrame.MAXIMIZED_BOTH;
	}
	
	void toggleMaximise()
	{
		if (!getMaximised())
			maximise();
		else
			restore();
	}
	
	public TitleBar getTitleBar()
	{
		return titleBar;
	}
	
	/**
	 * Gets a font object with the default style and the specified size 
	 * @param size the size of the font
	 * @return <b>Font</b> the font object
	 */
	public static Font getDefaultFont(int size)
	{
		// If this font has been used before, get it from the map
		if (fonts.containsKey(size))
			return fonts.get(size);
		
		// If not already done so, find the first font in fontNames installed on this machine
		if (fontName.isEmpty())
		{
			int i = 0;
			String[] installedFonts = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();
			while (fontName.isEmpty() && i < fontNames.length)
			{
				int j = -1;
				do {
					j++;
				} while (!fontNames[i].equals(installedFonts[j]) && j < installedFonts.length - 1);
				if (fontNames[i].equals(installedFonts[j]))
					fontName = fontNames[i];
				i++;
			}
			if (fontName.isEmpty())
				fontName = "SansSerif"; // If none of the fonts are installed, use the standard SansSerif font
		}
		// Add the new font to the map and return it
		Font f = new Font(fontName, Font.PLAIN, size);
		fonts.put(size, f);
		return f;
	}
	
	public static class ColorTheme {
		String name;
		Color colorDark;
		Color color;
		Color colorLight;
		Color textColor;
		Color textColorDisabled;
		ColorTheme(String name, Color colorDark, Color color, Color colorLight, Color textColor, Color textColorDisabled)
		{
			this.name = name; this.colorDark = colorDark; this.color = color; this.colorLight = colorLight; this.textColor = textColor; this.textColorDisabled = textColorDisabled;
		}
	}
	
	/*public static void setColorTheme(String name)
	{
		for (ColorTheme ct : COLOR_THEMES)
			if (ct.name == name)
				currentTheme = ct;
	}*/
	
	public static Color getBackgroundColor() { return currentTheme.color; }
	public static Color getBackgroundColorDark() { return currentTheme.colorDark; }
	public static Color getBackgroundColorLight() { return currentTheme.colorLight; }
	public static Color getTextColor() { return currentTheme.textColor; }
	public static Color getTextColorDisabled() { return currentTheme.textColorDisabled; }
}

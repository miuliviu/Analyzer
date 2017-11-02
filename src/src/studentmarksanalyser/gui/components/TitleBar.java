package studentmarksanalyser.gui.components;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.filechooser.FileNameExtensionFilter;

import studentmarksanalyser.StudentMarksAnalyser;

public class TitleBar extends JPanel {
	
	public static final Color CLOSE_BUTTON_COLOR_PRESSED = new Color(180, 52, 52);
	public static final Color CLOSE_BUTTON_COLOR = new Color(203, 66, 66);
	public static final Color CLOSE_BUTTON_COLOR_HOVER = new Color(215, 84, 84);
	
	public static final String MINIMISE_ICON = "/Minimise.png";
	public static final String MAXIMISE_ICON = "/Maximise.png";
	public static final String CLOSE_ICON = "/Close.png";
	
	public static final String IMPORT_DATA_ICON = "/Import.png";
	public static final String CREATE_REPORT_ICON = "/Create.png";
	public static final String CREATE_REPORT_DISABLED_ICON = "/CreateDisabled.png";
	public static final String EDIT_DATA_ICON = "/Edit.png";
	
	protected Frame frame;
	
	private String titleText;
	
	private List<ImageButton> buttons = new ArrayList<ImageButton>();
	
	private ImageButton buttonMinimise;
	private ImageButton buttonMaximise;
	private ImageButton buttonClose;
	private boolean minimiseEnabled;
	private boolean maximiseEnabled;
	private boolean closeEnabled;
	
	private boolean dragging = false;
	private int offsetX, offsetY;
	
	private JPopupMenu rightClickMenu = new JPopupMenu();
	
	public TitleBar(Frame frame, String title)
	{
		this.frame = frame;
		this.titleText = title;
		this.setBackground(Frame.getBackgroundColor());
		this.setLayout(new GridBagLayout());
		
		// Load buttons
		try {
			buttonMinimise = new ImageButton(MINIMISE_ICON)
			{
				@Override
				public void mousePress() {
					frame.minimise();
				}
			};
			buttonMaximise = new ImageButton(MAXIMISE_ICON)
			{
				@Override
				public void mousePress() {
					frame.toggleMaximise();
				}
			};
			buttonClose = new ImageButton(CLOSE_ICON, Frame.getBackgroundColor(), CLOSE_BUTTON_COLOR_HOVER, CLOSE_BUTTON_COLOR_PRESSED)
			{
				@Override
				public void mousePress() {
					if (frame.getDefaultCloseOperation() == JFrame.EXIT_ON_CLOSE)
						System.exit(0);
					else
						frame.setVisible(false);
				}
			};
		} catch (IOException e) { e.printStackTrace(); System.exit(ERROR); }
		
		// Add menu items to right click menu
		/*for (int i = 0; i < Frame.COLOR_THEMES.length; i++)
		{
			JMenuItem item = new JMenuItem();
			item.setText(Frame.COLOR_THEMES[i].name);
			item.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent arg0) {
					Frame.setColorTheme(arg0.getActionCommand());
					frame.reload();
				}
			});
			rightClickMenu.add(item);
		}
		this.setComponentPopupMenu(rightClickMenu);*/
		
		this.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent arg0) {
				if (arg0.getButton() == 1)
				{
					dragging = true;
					offsetX = arg0.getX();
					offsetY = arg0.getY();
				}
			}
			@Override
			public void mouseReleased(MouseEvent arg0) {
				if (arg0.getButton() == 1)
				{
					if (dragging)
					{
						dragging = false;
						Point mouse = MouseInfo.getPointerInfo().getLocation();
						Rectangle screenBounds = MouseInfo.getPointerInfo().getDevice().getDefaultConfiguration().getBounds();
						if (mouse.getY() - screenBounds.getY() == 0)
						{
							frame.maximise();
						}
					}
				}
			}
		});
		
		this.addMouseMotionListener(new MouseAdapter() {
			@Override
			public void mouseDragged(MouseEvent arg0) {
				if (dragging)
				{
					if (frame.getMaximised())
					{
						int previousFrameWidth = frame.getWidth();
						frame.restore();
						offsetX *= (float)frame.getWidth() / (float)previousFrameWidth;
					}
					Point mousePos = MouseInfo.getPointerInfo().getLocation();
					frame.setBounds(mousePos.x - offsetX, mousePos.y - offsetY, frame.getWidth(), frame.getHeight());
				}
			}
		});
	}
	
	public TitleBar(Frame frame, String title, boolean minimise, boolean maximise, boolean close)
	{
		this(frame, title);
		minimiseEnabled = minimise;
		maximiseEnabled = maximise;
		closeEnabled = close;
	}
	
	public void loadComponents()
	{
		int gridX = 0;
		GridBagConstraints gbc = new GridBagConstraints();
		
		// Add buttons
		gbc.gridheight = 2;
		gbc.insets = new Insets(0, 0, 2, 6);
		for (ImageButton button : buttons)
		{
			gbc.gridx = gridX;
			add(button, gbc);
			gridX++;
		}
		gbc.gridheight = 1;
		gbc.insets = new Insets(0, 0, 0, 0);
		
		// Add title
		JLabel title = new JLabel(titleText);
		title.setForeground(Color.WHITE);
		title.setFont(Frame.getDefaultFont(18));
		gbc.gridx = gridX + 1;
		this.add(title, gbc);
		
		// Add spacers on either side of the title which expand to fill all available space
		JLabel spacer1 = new JLabel();
		JLabel spacer2 = new JLabel();
		gbc.gridx = gridX;
		gbc.gridy = 1;
		gbc.weightx = 1.0f;
		gbc.weighty = 1.0f;
		gbc.fill = GridBagConstraints.BOTH;
		this.add(spacer1, gbc);
		gbc.gridx = gridX + 2;
		this.add(spacer2, gbc);
		
		// Add minimise, maximise and close buttons
		gbc.insets = new Insets(0, 0, 2, 0);
		gbc.weightx = 0.0f;
		gbc.weighty = 0.0f;
		gbc.gridy = 0;
		gbc.fill = GridBagConstraints.NONE;
		gridX += 3;
		gbc.gridx = gridX;
		if (minimiseEnabled)
			this.add(buttonMinimise, gbc);
		gbc.gridx = gridX + 1;
		if (maximiseEnabled)
			this.add(buttonMaximise, gbc);
		gbc.insets = new Insets(0, 0, 2, -1);
		gbc.gridx = gridX + 2;
		if (closeEnabled)
			this.add(buttonClose, gbc);
		
		repaint();
	}
	
	public void reloadComponents()
	{
		this.removeAll();
		this.revalidate();
		loadComponents();
	}

	/**
	 * Add a button to the title bar. Must call reloadComponents() afterwards to redraw the component with the button. 
	 * @param button the ImageButton object to add.
	 */
	public void addButton(ImageButton button)
	{
		buttons.add(button);
	}
	
	/**
	 * Removes a button from the title bar. Must call reloadComponents() afterwards to redraw the component without the button. 
	 * @param button the ImageButton object to remove.
	 * @return true if the title bar contained the specified button.
	 */
	public boolean removeButton(ImageButton button)
	{
		return buttons.remove(button);
	}
	
	/**
	 * Remove all buttons from the title bar. Must call reloadComponents() afterwards to redraw the component without the buttons.
	 */
	public void removeAllButtons()
	{
		buttons = new ArrayList<ImageButton>();
	}
}

package studentmarksanalyser.gui.components;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

import javax.swing.JPanel;

public class ResizePanel extends JPanel {
	
	public static final int NORTH = 0;
	public static final int NORTH_EAST = 1;
	public static final int EAST = 2;
	public static final int SOUTH_EAST = 3;
	public static final int SOUTH = 4;
	public static final int SOUTH_WEST = 5;
	public static final int WEST = 6;
	public static final int NORTH_WEST = 7;
	
	private int resizeDirection;
	
	private Frame frame;
	
	private int dragStartX, dragStartY;
	private boolean dragging = false;
	
	public ResizePanel(int resizeDirection, Color background, int thickness, Frame frame)
	{
		this.resizeDirection = resizeDirection;
		this.setBackground(background);
		setThickness(thickness);
		this.frame = frame;
		this.setCursor(cursor());
		
		this.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				if (e.getButton() == MouseEvent.BUTTON1)
				{
					dragStartX = e.getX();
					dragStartY = e.getY();
					dragging = true;
				}
			}
			@Override
			public void mouseReleased(MouseEvent e) {
				if (e.getButton() == MouseEvent.BUTTON1)
				{
					dragging = false;
				}
			}
		});

		this.addMouseMotionListener(new MouseMotionListener() {
			@Override
			public void mouseDragged(MouseEvent e) {
				if (dragging)
				{
					int frameX = frame.getX();
					int frameY = frame.getY();
					int frameWidth = frame.getWidth();
					int frameHeight = frame.getHeight();
					int mouseDiffX = e.getX() - dragStartX;
					int mouseDiffY = e.getY() - dragStartY;
					if (resizeDirection == NORTH || resizeDirection == NORTH_EAST || resizeDirection == NORTH_WEST) // Drag up
					{
						// Prevent shrinking below minimum size
						if (frameHeight - mouseDiffY < frame.minimumWindowSize.height)
							mouseDiffY = frameHeight - frame.minimumWindowSize.height;
						// Calculate new position and size
						frameY += mouseDiffY;
						frameHeight -= mouseDiffY;
					}
					else if (resizeDirection == SOUTH || resizeDirection == SOUTH_EAST || resizeDirection == SOUTH_WEST) // Drag down
					{
						// Prevent shrinking below minimum size
						if (frameHeight + mouseDiffY < frame.minimumWindowSize.height)
							mouseDiffY = frame.minimumWindowSize.height - frameHeight;
						// Calculate new size
						frameHeight += mouseDiffY;
					}
					if (resizeDirection == WEST || resizeDirection == NORTH_WEST || resizeDirection == SOUTH_WEST) // Drag left
					{
						// Prevent shrinking below minimum size
						if (frameWidth - mouseDiffX < frame.minimumWindowSize.width)
							mouseDiffX = frameWidth - frame.minimumWindowSize.width;
						// Calculate new position and size
						frameX += mouseDiffX;
						frameWidth -= mouseDiffX;
					}
					else if (resizeDirection == EAST || resizeDirection == SOUTH_EAST || resizeDirection == NORTH_EAST) // Drag right
					{
						// Prevent shrinking below minimum size
						if (frameWidth + mouseDiffX < frame.minimumWindowSize.width)
							mouseDiffX = frame.minimumWindowSize.width - frameWidth;
						// Calculate new size
						frameWidth += mouseDiffX;
					}
					frame.setBounds(new Rectangle(frameX, frameY, frameWidth, frameHeight));
					frame.revalidate();
				}
			}
			@Override
			public void mouseMoved(MouseEvent e) { }
		});
	}
	
	private Cursor cursor()
	{
		switch (resizeDirection)
		{
		case NORTH:
			return new Cursor(Cursor.N_RESIZE_CURSOR);
		case NORTH_EAST:
			return new Cursor(Cursor.NE_RESIZE_CURSOR);
		case EAST:
			return new Cursor(Cursor.E_RESIZE_CURSOR);
		case SOUTH_EAST:
			return new Cursor(Cursor.SE_RESIZE_CURSOR);
		case SOUTH:
			return new Cursor(Cursor.S_RESIZE_CURSOR);
		case SOUTH_WEST:
			return new Cursor(Cursor.SW_RESIZE_CURSOR);
		case WEST:
			return new Cursor(Cursor.W_RESIZE_CURSOR);
		case NORTH_WEST:
			return new Cursor(Cursor.NW_RESIZE_CURSOR);
		}
		return new Cursor(Cursor.DEFAULT_CURSOR);
	}
	
	private void setThickness(int thickness)
	{
		switch (resizeDirection)
		{
		case NORTH:
		case SOUTH:
			setPreferredSize(new Dimension(0, thickness));
			break;
		case EAST:
		case WEST:
			setPreferredSize(new Dimension(thickness, 0));
			break;
		case NORTH_EAST:
		case SOUTH_EAST:
		case SOUTH_WEST:
		case NORTH_WEST:
			setPreferredSize(new Dimension(thickness, thickness));
		}
	}
	
	public GridBagConstraints getGridBagConstraints()
	{
		GridBagConstraints gbc = new GridBagConstraints();
		
		// Set grid x
		if (resizeDirection == SOUTH_WEST || resizeDirection == WEST || resizeDirection == NORTH_WEST) // West
		{
			gbc.gridx = 0;
		}
		else if (resizeDirection == NORTH || resizeDirection == SOUTH) // Centre
		{
			gbc.gridx = 1;
		}
		else // East
		{
			gbc.gridx = 2;
		}
		
		// Set grid y
		if (resizeDirection == NORTH || resizeDirection == NORTH_EAST || resizeDirection == NORTH_WEST) // North
		{
			gbc.gridy = 0;
		}
		else if (resizeDirection == EAST || resizeDirection == WEST) // Centre
		{
			gbc.gridy = 1;
		}
		else // South
		{
			gbc.gridy = 2;
		}
		
		// Set grid fill
		if (resizeDirection == NORTH || resizeDirection == SOUTH) // Horizontal fill
		{
			gbc.weightx = 1.0f;
			gbc.fill = GridBagConstraints.HORIZONTAL;
		}
		else if (resizeDirection == EAST || resizeDirection == WEST) // Vertical fill
		{
			gbc.weighty = 1.0f;
			gbc.fill = GridBagConstraints.VERTICAL;
		}
		
		return gbc;
	}
}

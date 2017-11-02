package studentmarksanalyser;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.GeneralPath;
import java.awt.image.BufferedImage;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import studentmarksanalyser.gui.components.Frame;

public class Charts {
	
	public static final int IMAGE_WIDTH = 2000;
	public static final int IMAGE_HEIGHT = 1200;
	
	public static final double INSET_TOP = 64;
	public static final double INSET_RIGHT = 64;
	public static final double INSET_BOTTOM = 128;
	public static final double INSET_LEFT = 128;
	
	public static final double GRAPH_WIDTH = IMAGE_WIDTH - INSET_LEFT - INSET_RIGHT;
	public static final double GRAPH_HEIGHT = IMAGE_HEIGHT - INSET_TOP - INSET_BOTTOM;
	
	public static final Stroke AXIS_STROKE = new BasicStroke(2);
	
	private static final int FREQUENCY_HISTOGRAM_WIDTH = 10;
	
	private static final int FREQUENCY_TICKS = 10; // How many horizontal lines to show on the chart
	private static final int MARK_TICKS = 10; // How many vertical lines to show on the chart
	
	public static final int AVERAGE_GRAPH_TICKS_X = 10;
	public static final int AVERAGE_GRAPH_TICKS_Y = 10;
	
	public static final Shape AVERAGE_GRAPH_POINT = new Ellipse2D.Double(-6, -6, 12, 12);
	
	public static final int PLOT_COLOR_R = 255;
	public static final int PLOT_COLOR_G = 31;
	public static final int PLOT_COLOR_B = 31;
	
	public static final int PLOT2_COLOR_R = 31;
	public static final int PLOT2_COLOR_G = 31;
	public static final int PLOT2_COLOR_B = 255;
	
	private static final int TITLE_FONT_SIZE = 48;
	private static final int AXIS_FONT_SIZE = 32;
	private static final int TICK_FONT_SIZE = 24;
	private static final int LEGEND_FONT_SIZE = 24;

	public static BufferedImage createFrequencyCurve(StudentList studentsList, String module)
	{
		// Create image
		BufferedImage chart = new BufferedImage(IMAGE_WIDTH, IMAGE_HEIGHT, BufferedImage.TYPE_BYTE_INDEXED);
		Graphics2D g = chart.createGraphics();
		
		// Create map of marks with frequencies
		Map<Integer, Integer> marks = new TreeMap<Integer, Integer>();
		for(Student s:studentsList)
		{
			if( s.getMark(module)==null)
				continue;
			int m = (int)(s.getMark(module) / FREQUENCY_HISTOGRAM_WIDTH) * FREQUENCY_HISTOGRAM_WIDTH + FREQUENCY_HISTOGRAM_WIDTH / 2;
			if(marks.containsKey(m))
				marks.put(m, (int) marks.get(m)+1);
			if(!marks.containsKey(m))
				marks.put(m, 1);
		}
		marks.put(0, 0);
		marks.put(100, 0);

		// Fill white
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, IMAGE_WIDTH, IMAGE_HEIGHT);
		
		// Draw axis
		g.setColor(Color.BLACK);
		g.setStroke(AXIS_STROKE);
		g.drawLine((int)INSET_LEFT, (int)INSET_TOP, (int)INSET_LEFT, (int)(IMAGE_HEIGHT - INSET_BOTTOM));
		g.drawLine((int)INSET_LEFT, (int)(IMAGE_HEIGHT - INSET_BOTTOM), (int)(IMAGE_WIDTH - INSET_RIGHT), (int)(IMAGE_HEIGHT - INSET_BOTTOM));

		// Draw x-axis labels
		int xRangeMax = 100;
		double tickUnitX = 100 / MARK_TICKS;
		g.setFont(Frame.getDefaultFont(TICK_FONT_SIZE));
		for (int i = 0; i <= MARK_TICKS; i++)
		{
			int posX = (int)(INSET_LEFT + (i * (GRAPH_WIDTH / MARK_TICKS)));
			String s = String.format("%.2f", tickUnitX * i);
			if (s.charAt(s.length() - 2) == '0' && s.charAt(s.length() - 1) == '0')
				s = s.substring(0, s.length() - 3);
			g.drawString(
					s, 
					posX - g.getFontMetrics().stringWidth(s) / 2,
					(int)(IMAGE_HEIGHT - INSET_BOTTOM + 48)
				);
			g.drawLine(posX, (int)(IMAGE_HEIGHT - INSET_BOTTOM), posX, (int)(IMAGE_HEIGHT - INSET_BOTTOM + 8));
		}
		g.setFont(Frame.getDefaultFont(AXIS_FONT_SIZE));
		g.drawString("Mark", (int)(INSET_LEFT + GRAPH_WIDTH / 2 - g.getFontMetrics().stringWidth("Mark")), IMAGE_HEIGHT - AXIS_FONT_SIZE - 8);
		
		// Draw y-axis labels
		int yRangeMax = 0;
		for (int freq : marks.values())
			if (freq > yRangeMax)
				yRangeMax = freq;
		double tickUnitY = (double)yRangeMax / FREQUENCY_TICKS;
		if (tickUnitY > 2)
			tickUnitY = (int)tickUnitY;
		g.setFont(Frame.getDefaultFont(TICK_FONT_SIZE));
		for (int i = 0; i <= FREQUENCY_TICKS; i++)
		{
			int posY = (int)(IMAGE_HEIGHT - INSET_BOTTOM - (i * (GRAPH_HEIGHT / FREQUENCY_TICKS)));
			String s = String.format("%.2f", tickUnitY * i);
			if (s.charAt(s.length() - 2) == '0' && s.charAt(s.length() - 1) == '0')
				s = s.substring(0, s.length() - 3);
			g.drawString(
					s, 
					(int)(INSET_LEFT - 16 - g.getFontMetrics().stringWidth(s)), 
					posY + 8
				);
			g.drawLine((int)INSET_LEFT - 8, posY, (int)INSET_LEFT, posY);
		}
		g.setFont(Frame.getDefaultFont(AXIS_FONT_SIZE));
		AffineTransform af = g.getTransform();
		g.rotate(-Math.PI / 2);
		g.drawString("Frequency", -(int)(IMAGE_HEIGHT - INSET_BOTTOM - GRAPH_HEIGHT / 2 + g.getFontMetrics().stringWidth("Frequency") / 2), AXIS_FONT_SIZE + 8);
		g.setTransform(af);
		
		// Plot graph
		GeneralPath plot = new GeneralPath();
		double xScale = (double)GRAPH_WIDTH / xRangeMax;
		double yScale = (double)GRAPH_HEIGHT / yRangeMax;
		plot.moveTo(INSET_LEFT, IMAGE_HEIGHT - INSET_BOTTOM);
		for (Entry<Integer, Integer> mark : marks.entrySet())
		{
			plot.lineTo((int)(INSET_LEFT + mark.getKey() * xScale), (int)(IMAGE_HEIGHT - INSET_BOTTOM - mark.getValue() * yScale));
		}
		g.setColor(new Color(PLOT_COLOR_R, PLOT_COLOR_G, PLOT_COLOR_B));
		g.draw(plot);
		
		// Return image
		return chart;
	};
	
	public static BufferedImage createAveragesChart(StudentList studentsList, String module)
	{
		// Create image
		BufferedImage chart = new BufferedImage(IMAGE_WIDTH, IMAGE_HEIGHT, BufferedImage.TYPE_BYTE_INDEXED);
		Graphics2D g = chart.createGraphics();
		
		// Create a map of all marks with the average mark of the student
		Map<Integer, Float> marks = new TreeMap<Integer, Float>();
		for(Student s:studentsList)
		{
			if( s.getMark(module)==null)
				continue;
			marks.put((int)(s.getMark(module)), s.getAverageMark());
		}

		// Fill white
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, IMAGE_WIDTH, IMAGE_HEIGHT);
		
		// Draw axis
		g.setColor(Color.BLACK);
		g.setStroke(AXIS_STROKE);
		g.drawLine((int)INSET_LEFT, (int)INSET_TOP, (int)INSET_LEFT, (int)(IMAGE_HEIGHT - INSET_BOTTOM));
		g.drawLine((int)INSET_LEFT, (int)(IMAGE_HEIGHT - INSET_BOTTOM), (int)(IMAGE_WIDTH - INSET_RIGHT), (int)(IMAGE_HEIGHT - INSET_BOTTOM));

		// Draw x-axis labels
		int xRangeMax = 100;
		double tickUnitX = 100 / MARK_TICKS;
		g.setFont(Frame.getDefaultFont(TICK_FONT_SIZE));
		for (int i = 0; i <= MARK_TICKS; i++)
		{
			int posX = (int)(INSET_LEFT + (i * (GRAPH_WIDTH / MARK_TICKS)));
			String s = String.format("%.2f", tickUnitX * i);
			if (s.charAt(s.length() - 2) == '0' && s.charAt(s.length() - 1) == '0')
				s = s.substring(0, s.length() - 3);
			g.drawString(
					s, 
					posX - g.getFontMetrics().stringWidth(s) / 2,
					(int)(IMAGE_HEIGHT - INSET_BOTTOM + 48)
				);
			g.drawLine(posX, (int)(IMAGE_HEIGHT - INSET_BOTTOM), posX, (int)(IMAGE_HEIGHT - INSET_BOTTOM + 8));
		}
		g.setFont(Frame.getDefaultFont(AXIS_FONT_SIZE));
		g.drawString("Average Mark for Each Student", (int)(INSET_LEFT + GRAPH_WIDTH / 2 - g.getFontMetrics().stringWidth("Average Mark for Each Student") / 2), IMAGE_HEIGHT - AXIS_FONT_SIZE - 8);
		
		// Draw y-axis labels
		int yRangeMax = 100;
		double tickUnitY = (double)yRangeMax / FREQUENCY_TICKS;
		g.setFont(Frame.getDefaultFont(TICK_FONT_SIZE));
		for (int i = 0; i <= FREQUENCY_TICKS; i++)
		{
			int posY = (int)(IMAGE_HEIGHT - INSET_BOTTOM - (i * (GRAPH_HEIGHT / FREQUENCY_TICKS)));
			String s = String.format("%.2f", tickUnitY * i);
			if (s.charAt(s.length() - 2) == '0' && s.charAt(s.length() - 1) == '0')
				s = s.substring(0, s.length() - 3);
			g.drawString(
					s, 
					(int)(INSET_LEFT - 16 - g.getFontMetrics().stringWidth(s)), 
					posY + 8
				);
			g.drawLine((int)INSET_LEFT - 8, posY, (int)INSET_LEFT, posY);
		}
		g.setFont(Frame.getDefaultFont(AXIS_FONT_SIZE));
		AffineTransform af = g.getTransform();
		g.rotate(-Math.PI / 2);
		g.drawString(module + " Mark for Each Student", -(int)(IMAGE_HEIGHT - INSET_BOTTOM - GRAPH_HEIGHT / 2 + g.getFontMetrics().stringWidth(module + " Mark for Each Student") / 2), AXIS_FONT_SIZE + 8);
		g.setTransform(af);
		
		// Plot graph
		double xScale = (double)GRAPH_WIDTH / xRangeMax;
		double yScale = (double)GRAPH_HEIGHT / yRangeMax;
		g.setColor(new Color(PLOT2_COLOR_R, PLOT2_COLOR_G, PLOT2_COLOR_B));
		g.drawLine((int)(INSET_LEFT), (int)(IMAGE_HEIGHT - INSET_BOTTOM), (int)(INSET_LEFT + GRAPH_WIDTH), (int)(INSET_TOP));
		g.setColor(new Color(PLOT_COLOR_R, PLOT_COLOR_G, PLOT_COLOR_B));
		for (Entry<Integer, Float> mark : marks.entrySet())
		{
			g.translate(INSET_LEFT + mark.getValue() * xScale, IMAGE_HEIGHT - INSET_BOTTOM - mark.getKey() * yScale);
			g.fill(AVERAGE_GRAPH_POINT);
			g.setTransform(af);
		}
		
		// Return image
		return chart;
	};
	
	
}

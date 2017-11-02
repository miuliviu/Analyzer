package studentmarksanalyser.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;

import studentmarksanalyser.Student;
import studentmarksanalyser.gui.components.Frame;
import studentmarksanalyser.gui.components.TitleBar;

public class StudentInfoPanel extends JPanel
{
	JLabel title = new JLabel("Select a Student");
	
	MarksTable marksTable;
	InfoTable infoTable;
	
	StudentInfoPanel()
	{
		setLayout(new GridBagLayout());
		
		title.setFont(Frame.getDefaultFont(20));
		title.setForeground(StudentPanel.TEXT_COLOR);
		title.setBorder(BorderFactory.createEmptyBorder(24, 0, 24, 0));
		
		marksTable = new MarksTable();
		infoTable = new InfoTable();
		
		updateStudentInfo(null);
	}
	
	public void updateStudentInfo(Student s)
	{
		removeAll();
		revalidate();
		GridBagConstraints gbc = new GridBagConstraints();
		if (s == null)
		{
			title.setText("Select a Student");
			gbc.gridwidth = 4;
			add(title, gbc);
		}
		else
		{
			title.setText("Student " + s.getRegNo());
			gbc.gridwidth = 4;
			add(title, gbc);

			marksTable.updateMarks(s.getMarks());
			gbc.gridx = 1;
			gbc.gridwidth = 1;
			gbc.gridheight = 2;
			add(marksTable, gbc);
			
			infoTable.updateInfo(s);
			gbc.gridx = 2;
			gbc.gridheight = 1;
			add(infoTable, gbc);

			gbc.weightx = 1.0;
			gbc.fill = GridBagConstraints.HORIZONTAL;
			
			JPanel spacer1 = new JPanel();
			gbc.gridx = 0;
			gbc.gridy = 1;
			gbc.gridheight = 1;
			add(spacer1, gbc);
			
			JPanel spacer2 = new JPanel();
			gbc.gridx = 3;
			add(spacer2, gbc);
		}
		repaint();
	}

	class MarksTable extends JPanel
	{
		private static final int PREFERRED_WIDTH = 200;
		
		JLabel module = new JLabel("   Module   ");
		JLabel mark = new JLabel("   Mark   ");
		JLabel noMarksMessage = new JLabel("Student has no marks");
		
		MarksTable()
		{
			setLayout(new GridLayout(0, 2));
			module.setFont(Frame.getDefaultFont(18));
			module.setForeground(StudentPanel.TEXT_COLOR);
			module.setHorizontalAlignment(JLabel.CENTER);
			mark.setFont(Frame.getDefaultFont(18));
			mark.setForeground(StudentPanel.TEXT_COLOR);
			mark.setHorizontalAlignment(JLabel.CENTER);
			noMarksMessage.setFont(Frame.getDefaultFont(20));
			noMarksMessage.setForeground(TitleBar.CLOSE_BUTTON_COLOR);
			noMarksMessage.setHorizontalAlignment(JLabel.CENTER);
		}
		
		@Override
		public Dimension getPreferredSize()
		{
			Dimension prefSize = super.getPreferredSize();
			prefSize.width = PREFERRED_WIDTH;
			return prefSize;
		}
		
		public void updateMarks(Map<String, Integer> mapMarks)
		{
			removeAll();
			revalidate();
			if (mapMarks.size() > 0)
			{
				setLayout(new GridLayout(0, 2));
				add(module);
				add(mark);
				for (String s : mapMarks.keySet())
				{
					JLabel moduleCode = new JLabel(s);
					moduleCode.setFont(Frame.getDefaultFont(14));
					moduleCode.setForeground(StudentPanel.TEXT_COLOR);
					moduleCode.setHorizontalAlignment(JLabel.CENTER);
					add(moduleCode);
					JLabel moduleMark = new JLabel(mapMarks.get(s).toString());
					moduleMark.setFont(Frame.getDefaultFont(14));
					moduleMark.setForeground(StudentPanel.TEXT_COLOR);
					moduleMark.setHorizontalAlignment(JLabel.CENTER);
					add(moduleMark);
				}
			}
			else
			{
				setLayout(new GridLayout(0, 1));
				add(noMarksMessage);
			}
			repaint();
		}
	}
	
	class InfoTable extends JPanel
	{
		private static final int PREFERRED_WIDTH = 350;
		
		JLabel[] headingLabels = new JLabel[3];
		JLabel[] valueLabels = new JLabel[3];
		
		JPanel headings = new JPanel();
		JPanel values = new JPanel();
		
		InfoTable()
		{
			setLayout(new GridBagLayout());
			headings.setLayout(new GridLayout(0, 1));
			values.setLayout(new GridLayout(0, 1));
			GridBagConstraints gbc = new GridBagConstraints();
			add(headings, gbc);
			gbc.gridx = 1;
			gbc.weightx = 1.0;
			gbc.fill = GridBagConstraints.HORIZONTAL;
			add(values, gbc);
			
			headingLabels[0] = new JLabel("Exam Number   ");
			headingLabels[1] = new JLabel("Stage   ");
			headingLabels[2] = new JLabel("Average Mark   ");
			for (JLabel headingLabel : headingLabels)
			{
				headingLabel.setFont(Frame.getDefaultFont(18));
				headingLabel.setForeground(StudentPanel.TEXT_COLOR);
				headingLabel.setHorizontalAlignment(JLabel.RIGHT);
				headingLabel.setVerticalAlignment(JLabel.BOTTOM);
				headingLabel.setBorder(BorderFactory.createEmptyBorder(0, 64, 8, 0));
			}
			for (int i = 0; i < valueLabels.length; i++)
			{
				valueLabels[i] = new JLabel();
				valueLabels[i].setFont(Frame.getDefaultFont(16));
				valueLabels[i].setForeground(StudentPanel.TEXT_COLOR);
				valueLabels[i].setVerticalAlignment(JLabel.BOTTOM);
				valueLabels[i].setBorder(BorderFactory.createEmptyBorder(2, 0, 9, 0));
			}
		}
		
		@Override
		public Dimension getPreferredSize()
		{
			Dimension prefSize = super.getPreferredSize();
			prefSize.width = PREFERRED_WIDTH;
			return prefSize;
		}
		
		public void updateInfo(Student s)
		{
			for (int i = 0; i < headingLabels.length; i++)
			{
				headings.add(headingLabels[i]);
				switch (i)
				{
				case 0:
					valueLabels[i].setText(s.getExNo());
					break;
				case 1:
					valueLabels[i].setText(s.getStage());
					break;
				case 2:
					float averageMark = s.getAverageMark();
					if (Float.isNaN(averageMark))
					{
						valueLabels[i].setText("No marks");
						valueLabels[i].setForeground(TitleBar.CLOSE_BUTTON_COLOR);
					}
					else
					{
						valueLabels[i].setText(String.format("%.1f", averageMark));
						valueLabels[i].setForeground(StudentPanel.TEXT_COLOR);
					}
				}
				values.add(valueLabels[i]);
			}
			repaint();
		}
	}
}
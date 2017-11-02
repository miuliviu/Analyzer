package studentmarksanalyser.gui;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JPanel;

import studentmarksanalyser.StudentList;
import studentmarksanalyser.StudentMarksAnalyser;

public class MainPanel extends JPanel {
	
	private StudentPanel studentPanel = new StudentPanel();
	private ReportPanel reportPanel = new ReportPanel();
	
	public MainPanel()
	{
		setLayout(new GridBagLayout());
		addComponents();
		setAppState(StudentMarksAnalyser.STATE_IMPORT_DATA);
	}
	
	public void addComponents()
	{
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.weightx = 1.0f;
		gbc.weighty = 1.0f;
		gbc.fill = GridBagConstraints.BOTH;
		add(studentPanel, gbc);
		add(reportPanel, gbc);
	}
	
	public void removeComponents()
	{
		remove(studentPanel);
		remove(reportPanel);
	}
	
	public void setAppState(int state)
	{
		studentPanel.setVisible(false);
		reportPanel.setVisible(false);
		if (state == StudentMarksAnalyser.STATE_IMPORT_DATA)
		{
			studentPanel.setVisible(true);
		}
		else if (state == StudentMarksAnalyser.STATE_CREATE_REPORT)
		{
			reportPanel.setVisible(true);
		}
		this.repaint();
	}
	
	public void updateStudents(StudentList students)
	{
		studentPanel.updateStudents(students);
		reportPanel.updateStudents(students);
	}
}

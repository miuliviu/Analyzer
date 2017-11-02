package studentmarksanalyser.gui;

import java.awt.Color;
import java.awt.Container;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.BorderLayout;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import studentmarksanalyser.Student;
import studentmarksanalyser.StudentList;

// Has a list of students, updated by StudentMarksAnalyser/Frame
// Has a panel displaying details of currently selected student, also updated by StudentMarksAnalyser/Frame

public class StudentPanel extends JPanel {

	public static final Color BACKGROUND_COLOR_DARKER = new Color(170, 170, 170);
	public static final Color BACKGROUND_COLOR_DARK = new Color(190, 190, 190);
	public static final Color BACKGROUND_COLOR = new Color(230, 230, 230);
	
	public static final Color TEXT_COLOR = new Color(70, 70, 70);

	private StudentInfoPanel infoPanel = new StudentInfoPanel();
	private StudentListPanel listPanel = new StudentListPanel(infoPanel);
	
	public StudentPanel()
	{
		//this.setBorder(BorderFactory.createMatteBorder(0, 1, 1, 1, BACKGROUND_COLOR_DARK));
		this.setLayout(new GridBagLayout());

		GridBagConstraints gbc = new GridBagConstraints();
		
		gbc.weightx = 0.0;
		gbc.weighty = 1.0;
		gbc.gridheight = 2;
		gbc.fill = GridBagConstraints.VERTICAL;
		add(listPanel, gbc);
		
		gbc.gridx = 1;
		gbc.weightx = 1.0;
		gbc.weighty = 0.0;
		gbc.gridheight = 1;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		add(infoPanel, gbc);
	}
	
	public void updateStudents(StudentList students)
	{
		listPanel.updateStudents(students);
	}
	
	public void addStudent(Student s)
	{
		listPanel.addStudent(s);
	}
	
	public boolean removeStudent(Student s)
	{
		return listPanel.removeStudent(s);
	}
}

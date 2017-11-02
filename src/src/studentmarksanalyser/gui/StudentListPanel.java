package studentmarksanalyser.gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListCellRenderer;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import studentmarksanalyser.Student;
import studentmarksanalyser.StudentList;
import studentmarksanalyser.gui.components.Frame;
import studentmarksanalyser.gui.components.TitleBar;

public class StudentListPanel extends JPanel
{
	private JLabel listTitle = new JLabel("Students");
	private JList<Student> listStudents = new JList<Student>();
	private JScrollPane listScrollPane;
	public DefaultListModel<Student> listModelStudents = new DefaultListModel<Student>();;
	
	private StudentInfoPanel studentInfo;
	
	StudentListPanel(StudentInfoPanel studentInfo)
	{
		this.studentInfo = studentInfo;
		GridBagConstraints gbc = new GridBagConstraints();
		listStudents.setModel(listModelStudents);
		listStudents.setCellRenderer(new StudentListCellRenderer());
		listStudents.addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent arg0)
			{
				studentInfo.updateStudentInfo(listStudents.getSelectedValue());
			}
		});
		
		listScrollPane = new JScrollPane(listStudents);
		listScrollPane.setPreferredSize(new Dimension(140, 0));
		listScrollPane.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 1, StudentPanel.BACKGROUND_COLOR_DARK));
		setLayout(new GridBagLayout());
		
		listTitle.setFont(Frame.getDefaultFont(16));
		listTitle.setForeground(StudentPanel.TEXT_COLOR);
		listTitle.setBorder(BorderFactory.createEmptyBorder(8, 0, 4, 0));
		gbc.gridx = 0;
		gbc.gridy = 0;
		add(listTitle);
		
		gbc.gridy = 1;
		gbc.weightx = 0.0;
		gbc.weighty = 1.0;
		gbc.fill = GridBagConstraints.VERTICAL;
		add(listScrollPane, gbc);
		gbc.gridy = 0;
	}
	
	public void updateStudents(StudentList students)
	{
		listModelStudents.clear();
		for (Student s : students)
		{
			listModelStudents.addElement(s);
		}
	}
	
	public void addStudent(Student s)
	{
		listModelStudents.addElement(s);
	}
	
	public boolean removeStudent(Student s)
	{
		return listModelStudents.removeElement(s);
	}
	
	class StudentListCellRenderer extends JLabel implements ListCellRenderer<Student>
	{
		StudentListCellRenderer()
		{
			setOpaque(true);
			setFont(Frame.getDefaultFont(14));
			setBorder(BorderFactory.createEmptyBorder(6, 10, 6, 0));
		}
		
		@Override
		public Component getListCellRendererComponent(JList<? extends Student> list, Student value, int index,
				boolean isSelected, boolean cellHasFocus)
		{
			setText(value.getRegNo());
			if (isSelected)
			{
				setForeground(Color.WHITE);
				setBackground(Frame.getBackgroundColor());
			}
			else
			{
				setForeground(StudentPanel.TEXT_COLOR);
				setBackground(Color.WHITE);
			}
			return this;
		}
	}
}
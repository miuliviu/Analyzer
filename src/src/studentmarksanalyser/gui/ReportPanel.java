package studentmarksanalyser.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

import studentmarksanalyser.PDFExport;
import studentmarksanalyser.Student;
import studentmarksanalyser.StudentList;
import studentmarksanalyser.gui.components.Frame;

public class ReportPanel extends JPanel {

	//JFreeChart distributive = new ChartFactory.createLineChart


    static final Color BACKGROUND_COLOR_DARKER = new Color(170, 170, 170);
    static final Color BACKGROUND_COLOR_DARK = new Color(190, 190, 190);
    static final Color BACKGROUND_COLOR = new Color(230, 230, 230);
	
    static final Color TEXT_COLOR = new Color(70, 70, 70);

    StudentList studentsList = new StudentList();

	private JPanel titlePanel = new JPanel();
	private JPanel createReportPanel = new JPanel();
	private JPanel modulesPanel = new JPanel(new GridBagLayout());
	private JPanel pagesPanel = new JPanel(new GridBagLayout());


	private JButton createReport = new JButton("Create Report");

	private JLabel options = new JLabel("Options");

	private JCheckBox overviewPage = new JCheckBox("Overview Page");
	private JCheckBox detailedModulePage = new JCheckBox("Individual Module Pages");
	private JCheckBox helpPage = new JCheckBox("Help Page");

	private JCheckBox ce1EE = new JCheckBox("CE1 EE");
	private JCheckBox ce1cs = new JCheckBox("CE1 CS");

	public ReportPanel()
	{
		this.setBorder(BorderFactory.createMatteBorder(0, 1, 1, 1, BACKGROUND_COLOR_DARK));
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = 0; gbc.gridy = 0;

		this.setLayout(new GridBagLayout());


		options.setFont(Frame.getDefaultFont(24));
		options.setForeground(TEXT_COLOR);

		/**
		 * Created the button createReport, so the user will have to press it to save the graphs to pdf
		 * Added ActionListener, which creates a student list with all their marks using the random student creator
		 * since the parser haven't been finished.
		 * Used the JFreeChart lib to create a Progressive Line Chart for one Module and saved it in a PDF using PDFBox
		 * TODO: Jordan - Change the actionPerformed below to create and save charts for each modules which exists in the CSV file
		 * TODO: Save As menu for the createReport Button
		 * TODO: Style for the Report sub-menu content
		 * */
		createReport.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				
				JFrame frame = (JFrame)SwingUtilities.getWindowAncestor(ReportPanel.this);
				PDFExport.exportPDF(frame, studentsList);
			}
		});

		gbc.insets = new Insets(0, 0, 0, 0);
		
		// Add panels
		gbc.gridx = 0; 
		gbc.gridy = 0;
		gbc.gridwidth = 3;
		add(titlePanel, gbc);
		gbc.gridx = 0; 
		gbc.gridy = 1;
		gbc.gridwidth = 1;
		gbc.gridheight = 2;
		add(modulesPanel, gbc);
		gbc.gridx = 1; 
		gbc.gridy = 1;
		gbc.gridheight = 1;
		add(pagesPanel, gbc);
		gbc.gridx = 2; 
		gbc.gridy = 2;
		gbc.anchor = GridBagConstraints.PAGE_END;
		add(createReportPanel, gbc);
		
		
		// Add content to panels
		gbc.anchor = GridBagConstraints.LINE_START;
		titlePanel.add(options);

		TitledBorder leftPanelBorder = new TitledBorder("Modules");
		leftPanelBorder.setTitleFont(Frame.getDefaultFont(16));
		modulesPanel.setBorder(leftPanelBorder);

		overviewPage.setFont(Frame.getDefaultFont(12));
		overviewPage.setSelected(true);
		detailedModulePage.setFont(Frame.getDefaultFont(12));
		detailedModulePage.setSelected(true);
		helpPage.setFont(Frame.getDefaultFont(12));
		helpPage.setSelected(true);
		TitledBorder middlePanelBorder = new TitledBorder("Pages");
		middlePanelBorder.setTitleFont(Frame.getDefaultFont(16));
		pagesPanel.setBorder(middlePanelBorder);
		gbc.gridy = 0;
		pagesPanel.add(overviewPage, gbc);
		gbc.gridy = 1;
		pagesPanel.add(detailedModulePage, gbc);
		gbc.gridy = 2;
		pagesPanel.add(helpPage, gbc);

		gbc.gridx = 2; 
		gbc.gridy = 0;
		gbc.anchor = GridBagConstraints.LINE_END;
		createReport.setFont(Frame.getDefaultFont(22));
		createReport.setBackground(Color.WHITE);
		createReport.setBorder(new LineBorder(new Color(121, 176, 47), 2));
		createReport.setForeground(new Color(121, 176, 47));
		createReport.setPreferredSize(new Dimension(192, 80));
		createReport.setContentAreaFilled(false);
		createReportPanel.add(createReport);


//		add(topPanel, BorderLayout.PAGE_START);
//		add(bottomPanel, BorderLayout.PAGE_END);
	}
	public void updateStudents (StudentList s)
	{
		studentsList.clear();
		for(Student stud : s){
			studentsList.add(stud);
		}
		
		java.util.List<String> modules = studentsList.getModules();
		GridBagConstraints gbc = new GridBagConstraints();
		modulesPanel.removeAll();
		gbc.gridx = 0; 
		for (int i = 0; i < modules.size(); i++)
		{
			gbc.gridy = i;
			JCheckBox checkBox = new JCheckBox(modules.get(i));
			checkBox.setFont(Frame.getDefaultFont(12));
			checkBox.setSelected(true);
			modulesPanel.add(checkBox, gbc);
		}
		revalidate();
	}

}

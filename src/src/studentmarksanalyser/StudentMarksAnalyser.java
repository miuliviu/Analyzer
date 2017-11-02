package studentmarksanalyser;

import java.io.IOException;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.UIManager;
import javax.swing.filechooser.FileNameExtensionFilter;

import studentmarksanalyser.gui.MainPanel;
import studentmarksanalyser.gui.components.Frame;
import studentmarksanalyser.gui.components.ImageButton;
import studentmarksanalyser.gui.components.TitleBar;

public class StudentMarksAnalyser {

	private ImageButton buttonImportData;
	private ImageButton buttonCreateReport;
	private ImageButton buttonEditData;
	
	public static final int STATE_IMPORT_DATA = 0;
	public static final int STATE_CREATE_REPORT = 1;
	private int state = -1;
	

	public StudentList students = new StudentList(); // Holds the data of all of the students

	private Frame mainFrame;
	private MainPanel mainPanel;

	/** Application entry point. Creates an instance of this class */
	public static void main(String[] args) {
    	StudentMarksAnalyser app = new StudentMarksAnalyser();
	}
	
	/** Creates an instance of the application and displays a frame */
	public StudentMarksAnalyser()
	{
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {}
		
		// Create main frame
		mainPanel = new MainPanel();
		mainFrame = new Frame(mainPanel, "Student Marks Analyser");
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		// Load buttons
		try {
			buttonImportData = new ImageButton("Import Data", TitleBar.IMPORT_DATA_ICON, TitleBar.IMPORT_DATA_ICON)
			{
				@Override
				public void mousePress() {
					importStudents();
				}
			};
			buttonCreateReport = new ImageButton("Create Report", TitleBar.CREATE_REPORT_ICON, TitleBar.CREATE_REPORT_DISABLED_ICON)
			{
				@Override
				public void mousePress() {
					setAppState(StudentMarksAnalyser.STATE_CREATE_REPORT);
				}
			};
			buttonCreateReport.disable();
			buttonEditData = new ImageButton("Edit Data", TitleBar.EDIT_DATA_ICON, TitleBar.EDIT_DATA_ICON)
			{
				@Override
				public void mousePress() {
					setAppState(StudentMarksAnalyser.STATE_IMPORT_DATA);
				}
			};
		} catch (IOException e) { e.printStackTrace(); System.exit(1); }
		mainFrame.getTitleBar().addButton(buttonImportData);
		mainFrame.getTitleBar().addButton(buttonCreateReport);
		mainFrame.getTitleBar().reloadComponents();
	}
	
	/** Imports students from the specified .csv file
	 *  @param studentDataFile the file to import the data from */
	public void importStudents()
	{
		// Open file chooser and pass the file name to StudentMarksAnalyser
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setFileFilter(new FileNameExtensionFilter(".CSV spreadsheets", "csv"));
		int returnState = fileChooser.showOpenDialog(mainFrame);
		
		if (returnState == JFileChooser.APPROVE_OPTION)
		{
			// Add students from CSV parser to the student list
			students = CSVParser.parse(fileChooser.getSelectedFile());
			/*StudentList students = new StudentList();
			for (int i = 0; i <= 9; i++)
			{
				Student s = students.addStudent("25900" + i, Integer.toString((int)(Math.random() * 9000) + 1000), "CE1 EE");
				int studentAverageGrade = (int)(Math.random() * 90);
				if ((int)(Math.random() * 10) != 0)
				{
					s.addMark("CE10" + (int)(Math.random() * 10), (int)(Math.random() * 100));
				}
			}*/
			
			// Update GUI
			mainPanel.updateStudents(students);
			
			if (students.size() > 0)
				buttonCreateReport.enable();
			else
				buttonCreateReport.disable();
		}
	}
	
	/**
	 * Gets the data for all of the students as a StudentList object.
	 * @return StudentList containing all imported students.
	 */
	public StudentList getStudents()
	{
		return students;
	}
	
	/**
	 * Sets the state of the application.
	 * @param state the new state of the program, either STATE_IMPORT_DATA or STATE_CREATE_REPORT.
	 */
	public void setAppState(int state)
	{
		this.state = state;
		mainPanel.setAppState(state);
		if (state == StudentMarksAnalyser.STATE_IMPORT_DATA)
		{
			mainFrame.getTitleBar().removeAllButtons();
			mainFrame.getTitleBar().addButton(buttonImportData);
			mainFrame.getTitleBar().addButton(buttonCreateReport);
			mainFrame.getTitleBar().reloadComponents();
		}
		else if (state == StudentMarksAnalyser.STATE_CREATE_REPORT)
		{
			mainFrame.getTitleBar().removeAllButtons();
			mainFrame.getTitleBar().addButton(buttonEditData);
			mainFrame.getTitleBar().reloadComponents();
		}
	}
	
	/**
	 * @return the state of the program, either STATE_IMPORT_DATA or STATE_CREATE_REPORT.
	 */
	public int getAppState()
	{
		return state;
	}
}

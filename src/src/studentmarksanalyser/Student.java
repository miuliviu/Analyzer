package studentmarksanalyser;

import java.util.Map;
import java.util.TreeMap;

public class Student {
	
	private String regNo;
	private String exNo;
	private String stage;
	private Map<String, Integer> marks; // Maps module codes to marks

	/**
	 * Constructor. Creates a student object with ID numbers and stage
	 * @param regNo the student's regno value
	 * @param exNo the student's exno value
	 * @param stage the student's stage
	 */
	public Student(String regNo, String exNo, String stage)
	{
		this.regNo = regNo;
		this.exNo = exNo;
		this.stage = stage;
		this.marks = new TreeMap<String, Integer>();
	}
	
	/**
	 * Constructor. Creates a student object with ID numbers, stage and marks
	 * @param regNo the student's regno value
	 * @param exNo the student's exno value
	 * @param stage the student's stage
	 * @param marks a map of module codes and the marks achieved in the modules
	 */
	public Student(String regNo, String exNo, String stage, Map<String, Integer> marks)
	{
		this(regNo, exNo, stage);
		this.marks = marks;
	}
	
	/** Returns the student's registration number */
	public String getRegNo()
	{
		return regNo;
	}

	/** Returns the student's exam number */
	public String getExNo()
	{
		return exNo;
	}
	
	/** Returns the student's stage */
	public String getStage() {
		return stage;
	}
	
	/** Returns the student's average mark */
	public float getAverageMark() {
		float total = 0;
		for (int mark : marks.values())
			total += mark;
		return total / marks.size();
	}

	/** Returns a map of the student's marks in each module */
	public Map<String, Integer> getMarks()
	{
		return marks;
	}
	
	/**
	 * Adds a mark for a single module to the Student object.
	 * @param module code of the module (e.g. CE201)
	 * @param mark the mark achieved by the student in the module
	 */
	public Integer addMark(String module, int mark)
	{
		return marks.put(module, mark);
	}

	/**
	 * Gets a mark for a single module from the Student object.
	 * @param module code of the module (e.g. CE201)
	 * @returns <b>Integer</b> the student's previous mark for the module, could be null
	 */
	public Integer getMark(String module)
	{
		return marks.get(module);
	}

	/**
	 * Removes a mark for a single module from the Student object.
	 * @param module code of the module (e.g. CE201)
	 * @returns <b>Integer</b> the student's mark in the removed module
	 */
	public Integer removeMark(String module)
	{
		return marks.remove(module);
	}
	
}

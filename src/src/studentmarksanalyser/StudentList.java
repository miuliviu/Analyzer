package studentmarksanalyser;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

public class StudentList extends ArrayList<Student> {
	
	/**
	 * Adds a new student object to the list.
	 * @param regNo student's registration number
	 * @param exNo student's exam number
	 * @param stage student's stage
	 * @return <b>Student</b> the new student object
	 */
	public Student addStudent(String regNo, String exNo, String stage)
	{
		Student s = new Student(regNo, exNo, stage);
		add(s);
		return s;
	}
	
	/**
	 * Gets the student object with the specified registration number
	 * @param regNo the registration number to search for
	 * @return <b>Student</b> the student object, or null if student was not found
	 */
	public Student getStudent(String regNo)
	{
		for (Student s : this)
		{
			if (s.getRegNo().equals(regNo))
			{
				return s;
			}
		}
		return null;
	}

	/**
	 * Adds a mark for a single module to a Student object.
	 * @param regNo registration number of the student
	 * @param module code of the module (e.g. CE201)
	 * @param mark the mark achieved by the student in the module
	 */
	public Integer addMark(String regNo, String module, int mark)
	{
		Student s = getStudent(regNo);
		if (s != null)
			return s.addMark(module, mark);
		return null;
	}

	/**
	 * Gets a mark for a single module from a Student object.
	 * @param regNo registration number of the student
	 * @param module code of the module (e.g. CE201)
	 * @returns <b>Integer</b> the student's previous mark for the module, could be null
	 */
	public Integer getMark(String regNo, String module)
	{
		Student s = getStudent(regNo);
		if (s != null)
			return s.getMark(module);
		return null;
	}

	/**
	 * Removes a mark for a single module from a Student object.
	 * @param regNo registration number of the student
	 * @param module code of the module (e.g. CE201)
	 * @returns <b>Integer</b> the student's mark in the removed module
	 */
	public Integer removeMark(String regNo, String module)
	{
		Student s = getStudent(regNo);
		if (s != null)
			return s.removeMark(module);
		return null;
	}
	
	/**
	 * Gets a list of every module taken by a student.
	 * @return <b>List<String></b> list of module codes
	 */
	public List<String> getModules()
	{
		List<String> modules = new LinkedList<String>();
		for (Student s : this)
		{
			for (String module : s.getMarks().keySet())
			{
				if (!modules.contains(module))
				{
					modules.add(module);
				}
			}
		}
		modules.sort(new Comparator<String>() {
			@Override
			public int compare(String arg0, String arg1) {
				return arg0.compareTo(arg1);
			}
		});
		return modules;
	}
	
}

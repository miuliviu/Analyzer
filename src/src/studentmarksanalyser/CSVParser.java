package studentmarksanalyser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

public class CSVParser
{
	/**
	 * Reads student data from the specified file and stores it in a StudentList.
	 * @param file the .csv file containing the student data.
	 * @return <b>StudentList</b> a list of students obtained from the file.
	 */
	public static StudentList parse(File file)
	{
		StudentList students = new StudentList() ;
		try
		{
			BufferedReader br = new BufferedReader(new FileReader(file));
			String[] headings = br.readLine().split(",");
			String line;
			while ((line = br.readLine()) != null)
			{
				String[] seperated = line.split(",");
				try
				{
					String regNo = seperated[0];
					String exNo = seperated[1];
					String stage = seperated[2];
					Student student = new Student(regNo, exNo, stage);
					for (int i = 3; !headings[i].isEmpty() && i < seperated.length; i++)
					{
						try
						{
							student.addMark(headings[i], Integer.parseInt(seperated[i]));
						}
						catch (NumberFormatException e) { /* Student does not have a mark in this module */ }
					}
					students.add(student);
				}
				catch (ArrayIndexOutOfBoundsException e) { /* Line did not contain the required data, go to the next */ }
			}
			br.close();
			return students;
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return new StudentList();
		}
	}
}


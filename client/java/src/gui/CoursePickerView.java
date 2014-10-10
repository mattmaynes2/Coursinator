package gui;

import javax.swing.*;
import java.awt.*;
import javax.swing.UIManager;
import cr.*;

public class CoursePickerView extends JFrame
{
	private StudentInfoPanel studentInfo;
	private CourseGrid courses;
	
	public CoursePickerView()
	{
		//Initialize components
		studentInfo = new StudentInfoPanel();
		courses = new CourseGrid();
		setLayout(new BorderLayout());
		setSize(1000, 500);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		setDefaultLookAndFeelDecorated(false);
		
		add(studentInfo, BorderLayout.WEST);
	
		add(courses,BorderLayout.EAST);
		
		setVisible(true);
		
		Program testProgram = new Program("../../../xml/software_eng.xml");
		courses.loadCourses(testProgram.getElements());
		pack();
	}

}


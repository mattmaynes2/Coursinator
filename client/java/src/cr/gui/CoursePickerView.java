package cr.gui;

import javax.swing.JFrame;

import java.awt.BorderLayout;

import cr.Program;


public class CoursePickerView extends JFrame
{

	private static final long serialVersionUID = 1L;
	private StudentInfoPanel studentInfo;
	private CourseGrid courses;
	
	public CoursePickerView(){
		//Initialize components
		studentInfo = new StudentInfoPanel();
		courses = new CourseGrid();
		setLayout(new BorderLayout());
		setSize(1000, 500);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		setDefaultLookAndFeelDecorated(false);
		studentInfo.addSubmitRequestListener(courses);
		
		add(studentInfo, BorderLayout.WEST);
	
		add(courses,BorderLayout.EAST);
		
		Program testProgram = new Program("../../../xml/software_eng.xml");
		courses.loadCourses(testProgram.getElements());
		pack();
	}

}


package cr.gui;

import javax.swing.JFrame;

import java.awt.BorderLayout;


public class Main extends JFrame
{

	private static final long serialVersionUID = 1L;
	private StudentInfoPanel studentInfo;
	private CourseGrid courses;
	
	public Main(){
		//Initialize components
		studentInfo = new StudentInfoPanel();
		courses = new CourseGrid();
		setLayout(new BorderLayout());
		setSize(1000, 650);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		setDefaultLookAndFeelDecorated(false);
		studentInfo.addSubmitRequestListener(courses);
		
		add(studentInfo, BorderLayout.WEST);
	
		add(courses,BorderLayout.EAST);
		
		pack();
	}

}


package cr.gui;

import javax.swing.JFrame;

import cr.CourseOffering;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class Main extends JFrame implements ScheduleSelectedListener
{

	private static final long serialVersionUID = 1L;
	private StudentInfoPanel studentInfo;
	private CourseGrid courses;
	private RegisterPanel registerPanel;
	
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
		courses.addScheduleSelectedListener(this);
		
		
		pack();
	}
	
	public ActionListener backListener(){
		return new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				remove(registerPanel);
				add(studentInfo, BorderLayout.WEST);
				repaint();
			}
		};
	}

	public void scheduleSelected(CourseOffering[] offerings) {
		this.registerPanel = new RegisterPanel(offerings);
		this.registerPanel.addBackListener(backListener());
		remove(studentInfo);
		add(registerPanel, BorderLayout.WEST);
		repaint();
		
	}


	

}


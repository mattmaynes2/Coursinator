package cr.gui;

import javax.swing.JFrame;
import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import cr.CRRequest;
import cr.CourseOffering;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;


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
	
	public ActionListener registerListener(){
		return new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				CRRequest request = new CRRequest();
				try {
					registerPanel.setResponse(request.register(registerPanel.getSelectedCourses()));
				} catch (ParserConfigurationException | SAXException | IOException e1) {
					e1.printStackTrace();
				}

				
			}
		};
	}

	public void scheduleSelected(CourseOffering[] offerings) {
		this.registerPanel = new RegisterPanel(offerings);
		this.registerPanel.addBackListener(backListener());
		this.registerPanel.addRegisterListener(registerListener());
		remove(studentInfo);
		add(registerPanel, BorderLayout.WEST);
		repaint();
		
	}


	

}


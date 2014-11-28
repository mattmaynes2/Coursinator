package cr.gui;

import javax.swing.JPanel;
import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import java.awt.GridLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;

import cr.CRRequest;
import cr.Course;
import cr.CourseOffering;
import cr.ProgramElement;
import cr.Schedule;

public class CourseGrid extends JPanel implements SubmitRequestListener
{
		
	/**
	 * 
	 */
	private static final long serialVersionUID = 1809847793779415302L;
	
	private ArrayList<Schedule> schedules;

	private int currentSchedule;
	
	public CourseGrid()
	{
		schedules = new ArrayList<Schedule>();
		currentSchedule = 0;
		
		setLayout(new GridLayout(8,0));
		setPreferredSize(new Dimension(500,500));
		
	}
	
	public void update(){
		displaySchedule(this.schedules.get(currentSchedule));
	}
	
	public void displaySchedule(Schedule s){
		for(CourseOffering c : s.getCourseOfferings()){
			System.out.println(c.toString());
		}
	}
	
	public void reset(){
		this.schedules.clear();
	}

	@Override
	public void requestSubmitted(SubmitRequest request) {
		CRRequest req = new CRRequest();
		Schedule[] schedules;
		this.reset();
		try {
			if(request.isOnPattern()){
				schedules = req.getSchedule("" + request.getYear(), "" + request.getProgram().getId());
			}
			else{
				schedules = req.getSchedule(request.getCompletedCourses().toArray(new Course[0]), "" + request.getProgram().getId());
			}
			for(Schedule s : schedules){
				this.schedules.add(s);
			}
			update();
		} catch (ParserConfigurationException | SAXException | IOException e) {
			e.printStackTrace();
		}		
	}
}


package cr.gui;

import javax.swing.JPanel;
import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.Dimension;
import java.io.IOException;
import java.util.ArrayList;

import cr.CRRequest;
import cr.Course;
import cr.CourseOffering;
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
		
		setLayout(new GridLayout(Schedule.TIME_SLOTS,5));
		setPreferredSize(new Dimension(800,600));
		setBackground(Color.WHITE);
	}
	
	public void update(){
		displaySchedule(this.schedules.get(currentSchedule));
	}
	
	private SectionBlock[] organizeSchedule(Schedule s){
		ArrayList<SectionBlock> blocks = new ArrayList<SectionBlock>();
		CourseOffering[] offers = s.getCourseOfferingByBlock();
		String days = "MTWRF";
		boolean added;
		
		for(int i = 0; i < Schedule.TIME_SLOTS; i++){
			for(int j = 0; j < days.length(); j++){
				added = false;
				for(int k = 0; k < offers.length; k++){
					CourseOffering c = offers[k];
					if(c.getDays().contains(days.subSequence(j, j + 1))){
						if(!added && c.getStartTime() == s.trueTime(Schedule.START_TIME, i)){
							added = true;
							blocks.add(new SectionBlock(c));
						}
						
					}
				}
				if(!added){
					blocks.add(new SectionBlock());
				}
			}
		}
		
		return blocks.toArray(new SectionBlock[0]);
	}
	
	public void displaySchedule(Schedule s){
		this.removeAll();
		for(SectionBlock b : this.organizeSchedule(s)){
			this.add(b);
			b.display();
		}
		this.repaint();
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


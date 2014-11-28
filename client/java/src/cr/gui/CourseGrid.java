package cr.gui;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.BevelBorder;
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
	
	private ArrayList<ScheduleSelectedListener> scheduleSelectedListeners;

	private int currentSchedule;
	
	private static final String[] COLUMNS = {
		"Time",
		"Monday",
		"Tuesday",
		"Wednesday",
		"Thursday",
		"Friday"
	};
	
	public CourseGrid()
	{
		schedules = new ArrayList<Schedule>();
		scheduleSelectedListeners = new ArrayList<ScheduleSelectedListener>();
		currentSchedule = 0;
		schedules.add(new Schedule());
		setLayout(new GridLayout(Schedule.TIME_SLOTS + 1, COLUMNS.length));
		setPreferredSize(new Dimension(800,600));
		setBackground(Color.WHITE);
		update();
	}
	
	public void update(){
		displaySchedule(this.schedules.get(currentSchedule));
	}
	
	private JComponent[] organizeSchedule(Schedule s){
		ArrayList<JComponent> blocks = new ArrayList<JComponent>();
		JLabel label;
		CourseOffering[] offers = s.getCourseOfferingByBlock();
		String days = "MTWRF";
		boolean added;
		
		
		// Rows
		for(int i = 0; i < Schedule.TIME_SLOTS + 1; i++){
			// Columns
			for(int j = 0; j < COLUMNS.length; j++){
				// Column headers
				if(i == 0){
					label = new JLabel(COLUMNS[j]);
					label.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
					blocks.add(label);
					continue;
				}
				// Row times
				if(j == 0){
					label = new JLabel("" + s.trueTime(Schedule.START_TIME, i - 1));
					label.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
					blocks.add(label);
					continue;
				}
				
				
				
				added = false;
				for(int k = 0; k < offers.length; k++){
					CourseOffering c = offers[k];
					if(c.getDays().contains(days.subSequence(j - 1, j))){
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
		
		return blocks.toArray(new JComponent[0]);
	}
	
	public void displaySchedule(Schedule s){
		this.removeAll();
		for(JComponent b : this.organizeSchedule(s)){
			this.add(b);
			if(b instanceof SectionBlock) ((SectionBlock)b).display();
		}
		this.repaint();
		for(ScheduleSelectedListener listener : scheduleSelectedListeners){
			listener.scheduleSelected(s.getCourseOfferings());
		}
	}
	
	public void addScheduleSelectedListener(ScheduleSelectedListener listen){
		this.scheduleSelectedListeners.add(listen);
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


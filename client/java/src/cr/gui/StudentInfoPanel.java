package cr.gui;

import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.ButtonGroup;
import javax.swing.JLabel;
import javax.swing.BoxLayout;
import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import cr.CRRequest;
import cr.Program;

import java.awt.Dimension;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.IOException;
import java.util.ArrayList;

public class StudentInfoPanel extends JPanel
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -2620222548256086599L;
	private JRadioButton onPattern;
	private JRadioButton offPattern;
	private JButton submit;
	private JComboBox<Integer> yearStatus;
	private JComboBox<Program> program;
	private ButtonGroup bg;
	private JLabel instructions;
	private ArrayList<SubmitRequestListener> requestListeners;
	private CoursePicker coursePicker;
	
	public StudentInfoPanel()
	{
		
		Integer comboBoxOptions[] = {1, 2, 3, 4};
		Program programOptions[] = this.getProgramsFromServer();
		
		//Initialize components
		onPattern = new JRadioButton("I am on pattern");
		offPattern = new JRadioButton("I am off pattern");
		submit = new JButton("Get Course Suggestions");
		yearStatus = new JComboBox<Integer>(comboBoxOptions);
		program = new JComboBox<Program>(programOptions);
		bg = new ButtonGroup();
		instructions = new JLabel("<html>Please select your program, <br/> year status, and the courses<br/> that you have completed</html>");
		requestListeners = new ArrayList<SubmitRequestListener>();
		coursePicker = new CoursePicker();
		coursePicker.setVisible(false);
		
		submit.addActionListener(submitListener());
		//Set up panel
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		
		program.setMaximumSize(new Dimension(200,30));
		yearStatus.setMaximumSize(new Dimension(200,30));
		offPattern.setAlignmentX(Component.LEFT_ALIGNMENT);
		onPattern.setAlignmentX(Component.LEFT_ALIGNMENT);
		yearStatus.setAlignmentX(Component.LEFT_ALIGNMENT);
		coursePicker.setAlignmentX(Component.LEFT_ALIGNMENT);
		
		program.addItemListener(programListener());
		onPattern.addActionListener(this.hideCoursePickerListener());
		offPattern.addActionListener(this.showCoursePickerListener());
		
		
		//Add components
		add(instructions);
		add(program);
		program.setAlignmentX(Component.LEFT_ALIGNMENT);
		add(onPattern);
		bg.add(onPattern);
		onPattern.setAlignmentX(Component.LEFT_ALIGNMENT);
		
		add(offPattern);
		bg.add(offPattern);
	
		add(coursePicker);
		add(yearStatus);
		add(submit);
	

		setPreferredSize(new Dimension(250,120));
		setMaximumSize(new Dimension(250,120));
	}
	
	public ActionListener showCoursePickerListener(){
		return new ActionListener(){
			public void actionPerformed(ActionEvent e){
				coursePicker.setVisible(true);
				coursePicker.setSelectedProgramId(((Program)program.getSelectedItem()).getId());
				coursePicker.refeshList();
				repaint();
			}
		};
	}
	
	public ActionListener hideCoursePickerListener(){
		return new ActionListener(){
			public void actionPerformed(ActionEvent e){
				coursePicker.setVisible(false);
				repaint();
			}
		};
	}
	
	public ItemListener programListener(){
		return new ItemListener(){

			@Override
			public void itemStateChanged(ItemEvent e) {
				coursePicker.setSelectedProgramId(((Program)program.getSelectedItem()).getId());
				coursePicker.refeshList();
			}
			
		};
	}
	
	public Program[] getProgramsFromServer(){
		try {
			CRRequest request = new CRRequest();
			Program[] programs = request.getPrograms();

			return programs;
		} catch (ParserConfigurationException | SAXException | IOException e) {
			e.printStackTrace();
		}
		return new Program[0];
	}
	
	public void addSubmitRequestListener(SubmitRequestListener listener){
		this.requestListeners.add(listener);
	}
	
	public ActionListener submitListener(){
		return new ActionListener(){
			public void actionPerformed(ActionEvent e){
				SubmitRequest request = new SubmitRequest();
				Program prog = (Program)program.getSelectedItem();
				request.setOnPattern(onPattern.isSelected());
				request.setProgram(prog);
				request.setYear(Integer.parseInt(yearStatus.getSelectedItem().toString()));
				for(SubmitRequestListener listener: requestListeners){
					listener.requestSubmitted(request);
				}
			}
		};		
	}
}

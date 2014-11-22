package cr.gui;

import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.ButtonGroup;
import javax.swing.JLabel;
import javax.swing.BoxLayout;

import cr.Program;

import java.awt.Dimension;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
	private JComboBox<String> program;
	private ButtonGroup bg;
	private JLabel instructions;
	private ArrayList<SubmitRequestListener> requestListeners;
	
	public StudentInfoPanel()
	{
		Integer comboBoxOptions[] = {1, 2, 3, 4};
		String programOptions[] = {"Software Engineering", "Computer Systems Engineering", "Communications Engineering", "Electrical Engineering"};
		
		//Initialize components
		onPattern = new JRadioButton("I am on pattern");
		offPattern = new JRadioButton("I am off pattern");
		submit = new JButton("Get Course Suggestions");
		yearStatus = new JComboBox<Integer>(comboBoxOptions);
		program = new JComboBox<String>(programOptions);
		bg = new ButtonGroup();
		instructions = new JLabel("<html>Please select your program, <br/> year status, and the courses<br/> that you have completed</html>");
		requestListeners = new ArrayList<SubmitRequestListener>();
		
		
		submit.addActionListener(submitListener());
		//Set up panel
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		
		program.setMaximumSize(new Dimension(200,30));
		yearStatus.setMaximumSize(new Dimension(200,30));
		offPattern.setAlignmentX(Component.LEFT_ALIGNMENT);
		onPattern.setAlignmentX(Component.LEFT_ALIGNMENT);
		yearStatus.setAlignmentX(Component.LEFT_ALIGNMENT);
		
		//Add components
		add(instructions);
		add(program);
		program.setAlignmentX(Component.LEFT_ALIGNMENT);
		add(onPattern);
		bg.add(onPattern);
		onPattern.setAlignmentX(Component.LEFT_ALIGNMENT);
		
		add(offPattern);
		bg.add(offPattern);
	
		add(yearStatus);
		add(submit);
	

		setPreferredSize(new Dimension(200,120));
		setMaximumSize(new Dimension(200,120));
	}
	
	public void addSubmitRequestListener(SubmitRequestListener listener){
		this.requestListeners.add(listener);
	}
	
	public ActionListener submitListener(){
		return new ActionListener(){
			public void actionPerformed(ActionEvent e){
				SubmitRequest request = new SubmitRequest();
				Program prog = new Program(program.getSelectedItem().toString());
				request.setOnPattern(onPattern.isSelected());
				request.setProgram(prog);
				request.setYear(Integer.parseInt(yearStatus.getSelectedItem().toString()));
				for(SubmitRequestListener listener: requestListeners){
					listener.requestSubmitted(request);
				}
				System.out.println("[DEBUG] Requesting: " + request);
			}
		};		
	}
}

package gui;

import javax.swing.*;
import java.awt.*;
import gui.*;

public class StudentInfoPanel extends JPanel
{
	private JRadioButton onPattern;
	private JRadioButton offPattern;
	private JButton submit;
	private JComboBox<Integer> yearStatus;
	private ButtonGroup bg;
	
	public StudentInfoPanel()
	{
		Integer comboBoxOptions[] = {1, 2, 3, 4};
		
		//Initialize components
		onPattern = new JRadioButton("I am on pattern");
		offPattern = new JRadioButton("I am off pattern");
		submit = new JButton("Get Course Suggestions");
		yearStatus = new JComboBox<Integer>(comboBoxOptions);
		bg = new ButtonGroup();
		
		//Set up panel
		setLayout(new GridLayout(0,1));
		
		//Add components
		add(onPattern);
		bg.add(onPattern);
		
		add(offPattern);
		bg.add(offPattern);
	
		add(yearStatus);
		add(submit);
	
		setPreferredSize(new Dimension(200,100));
	}
}

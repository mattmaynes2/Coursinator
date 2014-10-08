package gui;

import javax.swing.*;
import java.awt.*;
import javax.swing.UIManager;
import cr.*;

public class CoursePickerView extends JFrame
{
	private StudentInfoPanel studentInfo;
	private CourseGrid courses;
	
	public CoursePickerView()
	{
		//Initialize components
		studentInfo = new StudentInfoPanel();
		courses = new CourseGrid();
		setLayout(new GridBagLayout());
		setSize(1000, 500);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		setDefaultLookAndFeelDecorated(false);
		
		GridBagConstraints gbc = new GridBagConstraints();
		
		gbc.anchor = GridBagConstraints.NORTHWEST;
		gbc.insets = new Insets(10,10,10,10);
		
		gbc.gridx = 0;
		gbc.gridy = 0;
		
		add(studentInfo, gbc);
		
		gbc.gridx = 1;
		gbc.gridy = 0;
		add(courses,gbc);
		setVisible(true);
		pack();
	}

}


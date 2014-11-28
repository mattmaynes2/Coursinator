package cr.gui;

import java.awt.Color;

import javax.swing.JLabel;
import javax.swing.JPanel;

import cr.CourseOffering;

public class SectionBlock extends JPanel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private CourseOffering offering;
	
	private JLabel courseCodeLabel;
	
	public SectionBlock(){
		this(null);
	}
	
	public SectionBlock(CourseOffering offering){
		this.offering = offering;
		courseCodeLabel = new JLabel();
		

		setBackground(Color.WHITE);
		setSize(100, 100);
		this.add(courseCodeLabel);
	}

	public void display(){
		if(offering != null){
			courseCodeLabel.setText(offering.getCode() + " " + offering.getSection());
		}
	}	
	
}

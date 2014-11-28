package cr.gui;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JPanel;

import cr.CourseOffering;

public class RegisterPanel extends JPanel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private JButton backButton;
	private JPanel checkBoxPanel;
	private HashMap<CourseOffering, JCheckBox> selections;
	
	public RegisterPanel(CourseOffering[] offerings){
		super();
		JCheckBox box;
		backButton = new JButton("Back");
		checkBoxPanel = new JPanel();
		checkBoxPanel.setLayout(new GridLayout(offerings.length, 1));
		this.selections = new HashMap<CourseOffering, JCheckBox>();
		setLayout(new BorderLayout());
		for(CourseOffering c : offerings){
			box = new JCheckBox(c.getCode());
			box.setSelected(true);
			this.selections.put(c, box);
			checkBoxPanel.add(box);
		}
		this.add(checkBoxPanel, BorderLayout.NORTH);
		this.add(backButton, BorderLayout.SOUTH);
	
	}
	
	public void addBackListener(ActionListener listen){
		backButton.addActionListener(listen);
	}
	
	public CourseOffering[] getSelectedCourses(){
		ArrayList<CourseOffering> selected = new ArrayList<CourseOffering>();
		for(Entry<CourseOffering, JCheckBox> entry : selections.entrySet()){
			if(entry.getValue().isSelected()){
				selected.add(entry.getKey());
			}
		}
		return selected.toArray(new CourseOffering[0]);
	}
	
	
	
}

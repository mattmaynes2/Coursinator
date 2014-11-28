package cr.gui;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import cr.CourseOffering;

public class RegisterPanel extends JPanel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private JButton backButton;
	private JButton registerButton;
	private JPanel buttonPanel;
	private JPanel checkBoxPanel;
	private JLabel responseLabel;
	
	private HashMap<CourseOffering, JCheckBox> selections;
	
	public RegisterPanel(CourseOffering[] offerings){
		super();
		JCheckBox box;
		backButton = new JButton("Back");
		registerButton = new JButton("Register");
		buttonPanel = new JPanel();
		checkBoxPanel = new JPanel();
		responseLabel = new JLabel();
		checkBoxPanel.setLayout(new GridLayout(offerings.length, 1));
		this.selections = new HashMap<CourseOffering, JCheckBox>();
		setLayout(new BorderLayout());
		for(CourseOffering c : offerings){
			box = new JCheckBox(c.getCode() + " " + c.getSection());
			box.setSelected(true);
			this.selections.put(c, box);
			checkBoxPanel.add(box);
		}
		buttonPanel.add(backButton);
		buttonPanel.add(registerButton);
		
		this.add(checkBoxPanel, BorderLayout.NORTH);
		this.add(responseLabel, BorderLayout.CENTER);
		this.add(buttonPanel, BorderLayout.SOUTH);
	}
	
	public void addBackListener(ActionListener listen){
		backButton.addActionListener(listen);
	}
	
	public void addRegisterListener(ActionListener listen){
		registerButton.addActionListener(listen);
	}
	
	public void setResponse(String response){
		this.responseLabel.setText("<html>" + response + "</html>");
		this.registerButton.setVisible(false);
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

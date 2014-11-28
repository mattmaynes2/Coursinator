package cr.gui;

import javax.swing.DefaultListModel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JList;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import cr.CRRequest;
import cr.Course;

public class CoursePicker extends JPanel{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private DefaultListModel<Course> optionsModel;
	private DefaultListModel<Course> takenModel;
	private JList<Course> options;
	private JList<Course> taken;
	private JPanel coursePanel;
	private JPanel buttonPanel;
	private JPanel insertPanel;
	private JButton addButton;
	private JButton removeButton;
	private JTextField courseField;
	private JButton insertButton;
	
	private int selectedProgramId;

	public CoursePicker(){
		super();
		
		setLayout(new BorderLayout());
		coursePanel = new JPanel();
		buttonPanel = new JPanel();
		insertPanel = new JPanel();
		coursePanel.setLayout(new BorderLayout());
		buttonPanel.setLayout(new BorderLayout());
		options = new JList<Course>();
		taken = new JList<Course>();
		addButton = new JButton(">");
		removeButton = new JButton("<");
		insertButton = new JButton("Insert");
		courseField = new JTextField();
		
		optionsModel = new DefaultListModel<Course>();
		takenModel = new DefaultListModel<Course>();
		
		for(Course c : this.getCoursesFromServer()){
			optionsModel.addElement(c);
		}
		
		options.setModel(optionsModel);
		taken.setModel(takenModel);
		
		addButton.setMaximumSize(new Dimension(30, 20));
		removeButton.setMaximumSize(new Dimension(30, 20));
		
		JScrollPane optionsScrollPane = new JScrollPane(options);
		JScrollPane takenScrollPane = new JScrollPane(taken);
		
		optionsScrollPane.setPreferredSize(new Dimension(100, 350));
		takenScrollPane.setPreferredSize(new Dimension(100, 350));
		
		
		addButton.addActionListener(addListener());
		removeButton.addActionListener(removeListener());
		
		buttonPanel.add(addButton, BorderLayout.NORTH);
		buttonPanel.add(removeButton, BorderLayout.SOUTH);
		
		courseField.setPreferredSize(new Dimension(100, 30));
		insertButton.addActionListener(insertListener());
		insertPanel.add(courseField);
		insertPanel.add(insertButton);
		
		coursePanel.add(optionsScrollPane, BorderLayout.WEST);
		coursePanel.add(takenScrollPane, BorderLayout.EAST);
		coursePanel.add(buttonPanel, BorderLayout.CENTER);
		coursePanel.add(insertPanel, BorderLayout.SOUTH);
		
		this.add(new JLabel("Options"), BorderLayout.WEST);
		this.add(new JLabel("Selected"), BorderLayout.EAST);
		this.add(coursePanel, BorderLayout.SOUTH);
		
	}
	
	public ActionListener addListener(){
		return new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){
				int index = options.getSelectedIndex();
				if(index >= 0){
					Course p = options.getSelectedValue();
					optionsModel.removeElement(p);
					takenModel.addElement(p);
				}
			}
		};
	}
	
	
	public ActionListener removeListener(){
		return new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){
				int index = taken.getSelectedIndex();
				if(index >= 0){
					Course p = taken.getSelectedValue();
					takenModel.removeElement(p);
					optionsModel.addElement(p);
				}
			}
		};
	}
	

	public ActionListener insertListener(){
		return new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){
				String text = courseField.getText();
				if(text.length() == 8 && text.substring(0, 4).toUpperCase().equals(text.substring(0, 4))){
					Course p = new Course();
					p.setCode(text);
					takenModel.addElement(p);
				}
				else{
					JOptionPane.showMessageDialog(null, "Invalid course title " + text, "Invalid Input", JOptionPane.INFORMATION_MESSAGE);
				}
				courseField.setText("");
			}
		};
	}
	/**
	 * @return the selectedProgramId
	 */
	public int getSelectedProgramId() {
		return selectedProgramId;
	}

	/**
	 * @param selectedProgramId the selectedProgramId to set
	 */
	public void setSelectedProgramId(int selectedProgramId) {
		this.selectedProgramId = selectedProgramId;
	}

	public void refeshList(){
		this.optionsModel.clear();
		for(Course e : this.getCoursesFromServer()){
			this.optionsModel.addElement(e);
		}
		this.options.setModel(this.optionsModel);
	}
	
	public Course[] getCoursesFromServer(){		
		try {
			CRRequest request = new CRRequest();
			return request.getProgramElements("" + selectedProgramId);
		} catch (IOException | ParserConfigurationException | SAXException e) {
			e.printStackTrace();
		}
		return new Course[0];
	}
	
	public Course[] getSelectedCourses(){
		Course[] courses = new Course[takenModel.getSize()];
		for(int i = 0; i < takenModel.size(); i++){
			courses[i] = takenModel.get(i);
		}
		return courses;
	}
	
	
	public void setVisible(boolean visible){
		super.setVisible(visible);
		options.updateUI();
		taken.updateUI();
	}
}

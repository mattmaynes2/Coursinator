import javax.swing.*;
import java.awt.*;

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
		setLayout(new GridBagLayout());
		
		GridBagConstraints gbc = new GridBagConstraints();
		
		//Add components
		
		gbc.anchor = GridBagConstraints.NORTHWEST;
		
		gbc.gridx=0;
		gbc.gridy=0;

		
		gbc.gridy = 1;
		add(onPattern,gbc);
		bg.add(onPattern);
		
		gbc.gridy = 2;
		add(offPattern, gbc);
		bg.add(offPattern);
		
		gbc.gridy = 3;
		add(submit, gbc);
		
		gbc.gridy = 4;
		add(yearStatus, gbc);
		
	}
}

package gui;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import cr.*;

public class CourseGrid extends JPanel
{
		
	private ArrayList<CourseToggle> toggles;
	
	public CourseGrid()
	{
		setLayout(new GridLayout(8,0));
		setPreferredSize(new Dimension(500,500));

		toggles = new ArrayList<CourseToggle>();
	}
	
	public void loadCourses(java.util.List<ProgramElement> elements)
	{
		GridBagConstraints gbc = new GridBagConstraints();
		
		for (ProgramElement e : elements)
		{
			CourseToggle newToggle = new CourseToggle(e);
			toggles.add(newToggle);
			gbc.gridx = e.getTerm();
			this.add(newToggle, gbc);
		}
	}
}


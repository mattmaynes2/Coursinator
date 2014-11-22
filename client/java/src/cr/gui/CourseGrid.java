package cr.gui;

import javax.swing.JPanel;

import java.awt.GridLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;

import java.util.List;
import java.util.ArrayList;

import cr.ProgramElement;

public class CourseGrid extends JPanel implements SubmitRequestListener
{
		
	/**
	 * 
	 */
	private static final long serialVersionUID = 1809847793779415302L;
	
	private ArrayList<CourseToggle> toggles;
	
	public CourseGrid()
	{
		setLayout(new GridLayout(8,0));
		setPreferredSize(new Dimension(500,500));

		toggles = new ArrayList<CourseToggle>();
	}
	
	public void loadCourses(List<ProgramElement> elements)
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

	@Override
	public void requestSubmitted(SubmitRequest request) {
		//TODO send request to server
		
	}
}


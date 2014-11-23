package cr.gui;

import javax.swing.JToggleButton;

import cr.ProgramElement;

public class CourseToggle extends JToggleButton
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 4581053307962208137L;
	
	
	public CourseToggle(ProgramElement e)
	{
		super(e.getCourse().getCode());
	}

}

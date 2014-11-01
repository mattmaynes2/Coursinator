package gui;

import javax.swing.*;
import cr.*;

public class CourseToggle extends JToggleButton
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 4581053307962208137L;
	private ProgramElement element;
	
	public CourseToggle(ProgramElement e)
	{
		super(e.getCourse().getCode());
		this.element = e;
	}

}

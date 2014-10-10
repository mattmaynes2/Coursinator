package gui;

import javax.swing.*;
import java.awt.*;
import cr.*;

public class CourseToggle extends JToggleButton
{
	private ProgramElement element;
	
	public CourseToggle(ProgramElement e)
	{
		super(e.getCourse().getCode());
		this.element = e;
	}

}

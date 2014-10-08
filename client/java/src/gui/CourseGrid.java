package gui;

import javax.swing.*;
import java.awt.*;

public class CourseGrid extends JPanel
{
	public CourseGrid()
	{
		setLayout(new GridBagLayout());
		setPreferredSize(new Dimension(500,500));
		setBorder(BorderFactory.createLineBorder(Color.BLACK));
	}
}

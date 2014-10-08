import javax.swing.*;
import java.awt.*;

public class CoursePickerView extends JFrame
{
	private StudentInfoPanel studentInfo;

	public CoursePickerView()
	{
		//Initialize components
		studentInfo = new StudentInfoPanel();
		
		setLayout(new BorderLayout());
		setSize(1000, 700);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		
		add(studentInfo, BorderLayout.WEST);
		setVisible(true);
	}

	public static void main(String args[])
	{
		CoursePickerView view = new CoursePickerView();
	}
}


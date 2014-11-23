package cr.gui;

import javax.swing.UIManager;

/**
 * Coursinator
 *
 * Coursinator allows users to input their completed time schedule and get suggestions
 * for their future courses.
 *
 * @version 0.0.0
 * @since October 6, 2014
 * @author Matthew Maynes, Cameron Blanchard and Kevin Cox
 */
public class Coursinator{

	public static void main(String[] args){
		try{
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		}catch (Exception ex){
			ex.printStackTrace();
		}
		Main view = new Main();
		view.setVisible(true);
	}
	
}

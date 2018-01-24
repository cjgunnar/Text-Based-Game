package gamelogic;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class LevelSelectionWindow extends JFrame
{
	private JPanel mainPanel;
	private JLabel instructions;
	
	final static int MAX_HEIGHT = 400;
	final static int MAX_WIDTH = 400;
	
	public LevelSelectionWindow()
	{
		setSize(MAX_HEIGHT, MAX_WIDTH);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setTitle("Level Selection Panel");
		
		CreateComponents();
	}
	
	private void CreateComponents()
	{
		mainPanel = new JPanel();
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
		
		instructions = new JLabel("Select which level to play");
		mainPanel.add(instructions);
		
		String[] fileNames = FindLevels();
		
		for(int i = 0; i < fileNames.length; i++)
		{
			JButton button = new JButton(fileNames[i]);
			
			button.setAlignmentX(Component.CENTER_ALIGNMENT);
			
			ClickListener listener = new ClickListener();
			
			button.addActionListener(listener);
			
			mainPanel.add(button);
		}
		
		add(mainPanel);
	}
	
	class ClickListener implements ActionListener
	{

		@Override
		public void actionPerformed(ActionEvent x)
		{
			//action to performed when clicked
			JButton button = (JButton) x.getSource();
			String levelSelected = button.getText();
			Runner.PlayLevel(levelSelected);
			closeWindow();
		}

	}
	
	private void closeWindow()
	{
		dispose();
	}
	
	private String[] FindLevels()
	{
		File dir = new File("./src/Levels");
		
		String[] fileNames = dir.list();
		
		String[] output = new String[fileNames.length];
		
		for(int i = 0; i < fileNames.length; i++)
		{
			output[i] = fileNames[i];
		}
		
		return output;
	}
	
}

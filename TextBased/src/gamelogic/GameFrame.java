package gamelogic;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
//import java.awt.event.ActionListener;
//import java.awt.event.KeyEvent;
//import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
//import javax.swing.JButton;
import javax.swing.JFrame;
//import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
//import javax.swing.KeyStroke;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.KeyStroke;

@SuppressWarnings("serial") //note: learn what that does
public class GameFrame extends JFrame 
{
	private List<String> entries = new ArrayList<String>();
	
	int MAX_ENTRIES = 0;
	
	private JTextField inputText;
	private JTextArea logText;
	private JScrollPane logScroll;
	private JTextArea errText;
	
	private Game game;
	
	final boolean fullScreen = true;
	
	final int FRAME_WIDTH = 1000;
	final int FRAME_HEIGHT = 700;
	
	public GameFrame(Game game)
	{
		this.game = game;
		
		CreateComponents();
		
		if(fullScreen)
		{
			setExtendedState(JFrame.MAXIMIZED_BOTH);
		}
		else
		{
			setSize(FRAME_WIDTH, FRAME_HEIGHT);
		}
		
		setMinimumSize(new Dimension(900, 500));
		setTitle("Text Based Game by Caden Gunnarson");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	private void TextEntered ()
	{
		if (inputText.getText().length() > 0)
		{
			game.Input(inputText.getText());
			
			//AddEntry(inputText.getText());

			//UpdateDisplay();
			
			inputText.setText("");
		}
	}
	
	public void AddEntry (String input)
	{
		entries.add(input);
		UpdateDisplay();
	}
	
	public void UpdateDisplay()
	{
		String display = "";
		
		if(MAX_ENTRIES == 0) //infinite setting
		{
			for (String entry : entries)
			{
				display += entry + "\n";
			}
		}
		else if(entries.size() > MAX_ENTRIES) //display last elements of list
		{
			for (int i = entries.size() - MAX_ENTRIES; i < entries.size(); i++) //display last entries according to MAX_ENTRIES
			{
				display += entries.get(i) + "\n";
			}
		}
		else //display entire list
		{
			for (String entry : entries)
			{
				display += entry + "\n";
			}
		}
		
		logText.setText(display);
		ClearErrorMessage();
		
		//update the scrollbars
		logScroll.revalidate();
	}
	
	public void setErrorMessage(String msg)
	{
		errText.setText(msg);
	}
	
	public void ClearErrorMessage()
	{
		errText.setText("");
	}
	
	private void CreateComponents ()
	{
		//The UI goes like this:
		//At the top/center, there is the logText, which is basically the output and it has a scrollbar
		//Then there is the input area
		//The input area has a button, a textbox, and a camoflauged error message
		//If bad input is entered, the error message space can be used
		
		//The nested panels go like this:
		//panel is the main panel
		//input panel contains the textbox and error message
		//outerInputPanel is used so inputPanel's components don't stretch with the window

		//error text
		errText = new JTextArea(1, 10);
		errText.setBackground(Color.black);
		errText.setForeground(Color.red);
		errText.setEditable(false);
		
		Font errTextFont = new Font("Arial", Font.BOLD, 24);
		errText.setFont(errTextFont);
		
		//Input text
		Font inputTextFont = new Font("Arial", Font.PLAIN, 24);
		inputText = new JTextField(20);
		inputText.setFont(inputTextFont);
		
		AbstractAction pressEnter = new AbstractAction() {
		    public void actionPerformed(ActionEvent e) {
		        TextEntered();
		    }
		};
		
		inputText.getInputMap().put(KeyStroke.getKeyStroke("ENTER"),
				"pressEnter");
		inputText.getActionMap().put("pressEnter",
				pressEnter);
		
		//log text
		Font logTextFont = new Font("Arial", Font.PLAIN, 24);

		logText = new JTextArea(20, 20);
		logText.setEditable(false);
		logText.setFont(logTextFont);
		logText.setBackground(Color.black);
		logText.setForeground(Color.white);
		logText.setLineWrap(true);
		
		JPanel logPanel = new JPanel(new BorderLayout());
		logPanel.add(logText);
		
		//add scrollbar
		logScroll = new JScrollPane(logPanel, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		logScroll.setForeground(Color.black);
		logScroll.setBorder(BorderFactory.createEmptyBorder());
		
		//panels and nested panels (see above)
		JPanel panel = new JPanel();
		JPanel inputPanel = new JPanel();
		JPanel outerInputPanel = new JPanel(new BorderLayout());
		
		panel.setLayout(new BorderLayout());
		inputPanel.setLayout(new BorderLayout());
		
		inputPanel.add(inputText, BorderLayout.CENTER);
		inputPanel.add(errText, BorderLayout.NORTH);
		
		outerInputPanel.add(inputPanel, BorderLayout.CENTER);
		
		panel.add(logScroll, BorderLayout.CENTER);
		panel.setBackground(Color.black);
		
		panel.add(outerInputPanel, BorderLayout.SOUTH);
		
		//finally add the main panel
		add(panel);
		setBackground(Color.black);
	}
}

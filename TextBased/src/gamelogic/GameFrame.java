package gamelogic;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
//import java.awt.event.KeyEvent;
//import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JButton;
//import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
//import javax.swing.KeyStroke;
import javax.swing.JTextArea;

@SuppressWarnings("serial") //note: learn what that does
public class GameFrame extends JFrame 
{
	private List<String> entries = new ArrayList<String>();
	
	final int MAX_ENTRIES = 10;
	
	private JTextField inputText;
	private JButton button;
	private JTextArea logText;
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
		
		setTitle("Text Based Game by Caden Gunnarson");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	class ClickListener implements ActionListener
	{

		@Override
		public void actionPerformed(ActionEvent arg0)
		{
			TextEntered();
			
		}

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
		
		if(entries.size() > MAX_ENTRIES)
		{
			for (int i = entries.size() - MAX_ENTRIES; i < entries.size(); i++) //display last entries according to MAX_ENTRIES
			{
				display += entries.get(i) + "\n";
			}
		}
		else
		{
			for (String entry : entries)
			{
				display += entry + "\n";
			}
		}
		
		logText.setText(display);
		ClearErrorMessage();
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
		button = new JButton("Go");

		errText = new JTextArea(1, 10);
		errText.setBackground(Color.black);
		errText.setForeground(Color.red);
		errText.setEditable(false);
		
		Font errTextFont = new Font("Arial", Font.BOLD, 24);
		errText.setFont(errTextFont);
		
		Font inputTextFont = new Font("Arial", Font.PLAIN, 24);
		inputText = new JTextField(20);
		inputText.setFont(inputTextFont);
		
		Font logTextFont = new Font("Arial", Font.PLAIN, 24);

		logText = new JTextArea(20, 20);
		logText.setEditable(false);
		//logText.setText("Log:");
		logText.setFont(logTextFont);
		logText.setBackground(Color.black);
		logText.setForeground(Color.white);
		logText.setLineWrap(true);
		
		JPanel panel = new JPanel();
		JPanel inputPanel = new JPanel();
		
		panel.setLayout(new BorderLayout());
		inputPanel.setLayout(new BorderLayout());
		
		inputPanel.add(button, BorderLayout.WEST);
		inputPanel.add(inputText, BorderLayout.CENTER);
		inputPanel.add(errText, BorderLayout.NORTH);
		
		panel.add(logText, BorderLayout.NORTH);
		panel.setBackground(Color.black);
		
		panel.add(inputPanel);
		
		add(panel);
		//add(inputPanel);
		
		ClickListener listener = new ClickListener();
		//KeyListener listener = new EnterListener();
		//this.addKeyListener(listener);
		//setFocusable(true);
		button.addActionListener(listener);
	}
}

package levelCreator;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.ListSelectionModel;

import gamelogic.Request;

@SuppressWarnings("serial")
public class RequestPanel extends JPanel
{
	Request request;
	
	DefaultListModel<String> verbList;
	JList<String> verbsUI;
	
	DefaultListModel<String> exactList;
	JList<String> exactsUI;
	
	JPanel subPanel;
	
	public RequestPanel(Request request)
	{
		this.request = request;
		CreateComponents();
	}
	
	public RequestPanel()
	{
		request = new Request();
		CreateComponents();
	}
	
	private void CreateComponents()
	{
		//used to show/hide the panel
		JButton showButton = new JButton("+");
		showButton.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent arg0)
			{
				//toggle visibility of subPanel
				subPanel.setVisible(!subPanel.isVisible());
			}
		});
		
		JLabel instructions = new JLabel("Add exact phrases, verbs to trigger request");
		
		verbList = new DefaultListModel<String>();
		
		verbsUI = new JList<String>(verbList);
		verbsUI.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		
		exactList = new DefaultListModel<String>();
		
		exactsUI = new JList<String>(exactList);
		exactsUI.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		
		JButton addVerb = new JButton("Add verb");
		addVerb.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e)
			{
				String verb = JOptionPane.showInputDialog("Verb to recognize");
				if(!(verb == null || verb.trim().equals("")))
				{
					verbList.addElement(verb);
					request.addVerb(verb);
				}
			}
		});
		
		JButton addExact = new JButton("Add Exact");
		addExact.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e)
			{
				String exact = JOptionPane.showInputDialog("Exact phrase to recognize");
				if(!(exact == null || exact.trim().equals("")))
				{
					exactList.addElement(exact);
					request.addExact(exact);
				}
			}
		});
		
		JButton delVerb = new JButton("Delete Verb");
		JButton delExact = new JButton("Delete Exact");
		
		JDialog editor = new ExecutableEditor();

		JButton exeEditorBtn = new JButton("Open Executable Editor");
		exeEditorBtn.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent arg0)
			{
				editor.setVisible(true);
			}
		});
		
		//add components to hideable subpanel, showButton will be used to expand it
		subPanel = new JPanel();
		
		subPanel.add(instructions);
		subPanel.add(addVerb);
		subPanel.add(addExact);
		subPanel.add(verbsUI);
		subPanel.add(exactsUI);
		subPanel.add(delVerb);
		subPanel.add(delExact);
		
		subPanel.add(exeEditorBtn);
		
		//hide by default
		subPanel.setVisible(false);
		
		add(showButton);
		add(subPanel);
	}
	
	public void setRequest(Request request)
	{
		this.request = request;
	}
	
}

package levelCreator;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import sceneObjects.SceneObject;

@SuppressWarnings("serial")
public class RequestSelectorPanel extends JPanel
{
	SceneObject owner;
	
	ArrayList<RequestPanel> reqPanels;
	
	JPanel reqPanelHolder;
	JScrollPane reqScroll;
	
	public RequestSelectorPanel()
	{
		CreateComponents();
	}
	
	private void CreateComponents()
	{
		JLabel instructions = new JLabel("Instructions");
		add(instructions);
		
		reqPanels = new ArrayList<RequestPanel>();
		
		JButton newReq = new JButton("Create Request");
		newReq.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e)
			{
				reqPanels.add(new RequestPanel());
				refresh();
			}
			
		});
		
		//create reqPanelHodler
		reqPanelHolder = new JPanel();
		reqPanelHolder.setLayout(new BoxLayout(reqPanelHolder, BoxLayout.Y_AXIS));
		
		reqScroll = new JScrollPane(reqPanelHolder);
		
		add(newReq);
		add(reqScroll);
		
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
	}
	
	public void refresh()
	{		
		//add everything to request panel holder
		for(RequestPanel reqPanel: reqPanels)
		{
			if(reqPanel == null)
				System.out.println("Error: null request panel");
			reqPanelHolder.add(reqPanel);
		}
		
		reqScroll.revalidate();
	}
}

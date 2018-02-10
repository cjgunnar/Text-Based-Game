package levelCreator;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import sceneObjects.SceneObject;

/**
 * Allows creation/deletion of aliases
 * @author Caden
 *
 */
@SuppressWarnings("serial")
class AliasPanel extends JPanel
{
	SceneObject owner;
	
	DefaultListModel<String> aliasesList;
	
	public AliasPanel()
	{
		CreateComponents();
	}
	
	private void CreateComponents()
	{
		aliasesList = new DefaultListModel<String>();
		JList<String> aliasesListUI = new JList<String>(aliasesList);
		
		JTextField newAlias = new JTextField(20);
		JButton addButton = new JButton("Add");
		addButton.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent arg0)
			{
				if(!newAlias.getText().trim().equals(""))
				{
					if(owner == null)
						System.out.println("LEVEL CREATOR: ALIASPANEL: ERROR: SceneObject owner is null");
					else
					{
						owner.addAlias(newAlias.getText());
						aliasesList.addElement(newAlias.getText());
						newAlias.setText("");
					}
				}
			}
		});
		
		JButton delButton = new JButton("Delete");
		delButton.setEnabled(false);
		delButton.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				int selected = aliasesListUI.getSelectedIndex();
				if(selected != -1)
				{
					if(owner == null)
					{
						System.out.println("LEVEL CREATOR: ALIASPANEL: ERROR: SceneObject owner is null");
						return;
					}
					String aliasRemove = aliasesList.get(selected);
					owner.removeAlias(aliasRemove);
					aliasesList.remove(selected);
				}
			}
		});
		
		//disable/enable delte button depending on if something is selected
		aliasesListUI.addListSelectionListener(new ListSelectionListener()
		{
			@Override
			public void valueChanged(ListSelectionEvent event)
			{
				if(aliasesListUI.getSelectedIndex() != -1) //nothing selected
					delButton.setEnabled(true); //disable button
				else
					delButton.setEnabled(false); //enable button
			}	
		});
		
		this.add(aliasesListUI);
		this.add(newAlias);
		this.add(addButton);
		this.add(delButton);
	}
	
	public void setOwner(SceneObject owner)
	{
		clear();
		
		this.owner = owner;
		
		for(String alias : owner.getAliases())
		{
			aliasesList.addElement(alias);
		}
	}
	
	public void clear()
	{
		if(aliasesList == null)
			System.out.println("error");
		aliasesList.clear();
	}
}

package levelCreator;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTextField;

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
	
	public AliasPanel(SceneObject owner)
	{
		this.owner = owner;
		CreateComponents();
	}
	
	private void CreateComponents()
	{
		DefaultListModel<String> aliasesList = new DefaultListModel<String>();
		JList<String> aliasesListUI = new JList<String>(aliasesList);
		
		/*
		for(int i = 0; i < owner.getAliases().length; i++)
		{
			aliasesList.addElement(owner.getAliases()[i]);
		}
		*/
		
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
		
		this.add(aliasesListUI);
		this.add(newAlias);
		this.add(addButton);
		this.add(delButton);
	}
}

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * This is for manipulating the changes made from the skill combo boxes
 * @author Tihhoo
 */
public class ItemChangeListener implements ItemListener 
{
	private JPanel rightPanel;
	private JPanel selectedPanel;
	
	public ItemChangeListener(JPanel rightPanel, JPanel selectedPanel)
	{
		this.rightPanel = rightPanel;
		this.selectedPanel = selectedPanel;
	}
	
	@Override
	public void itemStateChanged(ItemEvent e) 
	{
		if (e.getStateChange() == ItemEvent.DESELECTED) 
		{
			String prev = ((String) e.getItem());
			remove(prev);
		}
			
		else if (e.getStateChange() == ItemEvent.SELECTED)
		{
			String current = ((String) e.getItem());
			add(current);
		}
		rightPanel.validate();
		rightPanel.repaint();
	}
	
	private void remove(String item)
	{
		for(int i = 0; i < selectedPanel.getComponentCount(); i++)
		{
			JLabel label = (JLabel) selectedPanel.getComponent(i);
			if(label.getText().equals(item))
			{
				selectedPanel.remove(label);
				break;
			}
		}
	}
	
	private void add(String item)
	{
		String[] splitted = item.split("\\+");
		
		//nothing is added
		if(splitted[0].length() == item.length())
			return;
		
		JLabel label = new JLabel(item);
		selectedPanel.add(label);
	}
}

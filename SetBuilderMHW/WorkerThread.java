import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

public class WorkerThread implements Runnable 
{
	private Processor processor;
	private boolean english;
	private Component[] selected;
	private Weapon template;
	
	private JPanel rightProgressPanel;
	private JPanel resultPanel;
	private JScrollPane bottomSP;
	
	public WorkerThread(Processor processor, boolean english, Component[] selected, Weapon template, JPanel rightProgressPanel, JPanel resultPanel, JScrollPane bottomSP)
	{
		this.processor = processor;
		this.english = english;
		this.selected = selected;
		this.template = template;
		
		this.rightProgressPanel = rightProgressPanel;
		this.resultPanel = resultPanel;
		this.bottomSP = bottomSP;
	}
	
	@Override
	public void run() 
	{
		rightProgressPanel.removeAll();
		rightProgressPanel.validate();
		rightProgressPanel.repaint();
		
		processor.generateArmorSet(english, selected, template, rightProgressPanel);
		
		if(processor.getResultSize() == 0)
		{
			JLabel resultText = new JLabel();
			if(english)
				resultText.setText("No Matching ArmorSets Found!");
			else
				resultText.setText("这是目前不可能的配装！");
			resultPanel.add(resultText);
		}
		else
			processor.displayArmorSet(resultPanel, english);
		
		bottomSP.validate();
		bottomSP.repaint();
	}
}

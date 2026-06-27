import java.awt.BorderLayout;
import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;
import javax.swing.border.LineBorder;
import java.awt.Color;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class SetGeneratorMHW extends JFrame 
{
	/**
	 * Made by Ken(Tihhoo) Chen
	 */
	private static final long serialVersionUID = 4697571424435347832L;
	
	private JPanel contentPane;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) 
	{
		EventQueue.invokeLater(new Runnable() 
		{
			public void run() 
			{
				try 
				{					
					SetGeneratorMHW frame = new SetGeneratorMHW();
					frame.setVisible(true);
				} catch (Exception e) {e.printStackTrace();}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public SetGeneratorMHW() 
	{
		DataCollector dataCollector = new DataCollector("config.txt");
		dataCollector.loadData();
		Processor processor = dataCollector.buildProcessor();
		
		setResizable(false);
		setTitle("MHW配装器SetGenerator");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new TitledBorder(new LineBorder(new Color(0, 0, 0)), "\u8BED\u8A00/Language", TitledBorder.CENTER, TitledBorder.BOTTOM, null, new Color(0, 0, 0)));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		JPanel centrePanel = new JPanel();
		contentPane.add(centrePanel, BorderLayout.CENTER);
		centrePanel.setLayout(null);
		
		JButton enbtn = new JButton("English");
		enbtn.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent e) 
			{
				MainWindow main = new MainWindow('e',processor);
				main.setVisible(true);
				close();
			}
		});
		
		enbtn.setBounds(156, 119, 129, 43);
		centrePanel.add(enbtn);
		
		JButton cnbtn = new JButton("中文");
		cnbtn.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent e) 
			{
				MainWindow main = new MainWindow('c',processor);
				main.setVisible(true);
				close();
			}
		});
		
		cnbtn.setBounds(156, 64, 129, 43);
		centrePanel.add(cnbtn);
	}

	public void close()
	{
		this.setVisible(false);
		this.setEnabled(false);
	}
}

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.SpinnerNumberModel;
import javax.swing.JSpinner;
import javax.swing.JComboBox;
import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import java.awt.Color;

import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import java.awt.SystemColor;
import java.util.ArrayList;
import javax.swing.JTextField;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.JButton;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.FlowLayout;

public class MainWindow extends JFrame 
{
	/**
	 * Made by Ken(Tihhoo) Chen
	 */
	private static final long serialVersionUID = -6172792314391695505L;
	private JPanel contentPane;
	private JTextField txtSearchbar;
	ItemChangeListener itemChangeListener;
	
	/**
	 * Create the frame.
	 */
	public MainWindow(char language, Processor processor) 
	{
		setResizable(false);
		boolean english = language == 'e'? true:false;
		setTitle("MHW配装器SetGenerator");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 550, 650);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JPanel topPanel = new JPanel();
		topPanel.setBorder(new TitledBorder(new LineBorder(new Color(0, 0, 0)), "Presets", TitledBorder.LEADING, TitledBorder.BOTTOM, null, new Color(0, 0, 0)));
		topPanel.setBackground(SystemColor.window);
		topPanel.setBounds(0, 0, 550, 69);
		contentPane.add(topPanel);
		topPanel.setLayout(null);
		
		JLabel lblWeaponDecoSlot = new JLabel("Weapon Deco Slots");
		if(!english)
			lblWeaponDecoSlot.setText("武器镶嵌槽");
		
		lblWeaponDecoSlot.setBounds(16, 4, 120, 16);
		topPanel.add(lblWeaponDecoSlot);
		
		SpinnerNumberModel slots1 = new SpinnerNumberModel(0,0,3,1);
		SpinnerNumberModel slots2 = new SpinnerNumberModel(0,0,3,1);
		SpinnerNumberModel slots3 = new SpinnerNumberModel(0,0,3,1);
		
		JSpinner weaponSlot1 = new JSpinner(slots1);
		weaponSlot1.setBounds(16, 19, 33, 25);
		topPanel.add(weaponSlot1);
		
		JSpinner weaponSlot2 = new JSpinner(slots2);
		weaponSlot2.setBounds(53, 19, 33, 25);
		topPanel.add(weaponSlot2);
		
		JSpinner weaponSlot3 = new JSpinner(slots3);
		weaponSlot3.setBounds(93, 19, 33, 25);
		topPanel.add(weaponSlot3);
		
		JLabel lblWeaponSkill = new JLabel("Weapon Skill");
		if(!english)
			lblWeaponSkill.setText("武器技能设置");
		
		lblWeaponSkill.setBounds(228, 4, 80, 16);
		topPanel.add(lblWeaponSkill);
		
		JComboBox<String> comboboxWeaponSkill = new JComboBox<String>();
		if(english)
			comboboxWeaponSkill.setModel(new DefaultComboBoxModel<String>(processor.getWeaponSkills()));
		else
			comboboxWeaponSkill.setModel(new DefaultComboBoxModel<String>(translateWS(processor)));
		
		comboboxWeaponSkill.setBounds(179, 22, 180, 23);
		topPanel.add(comboboxWeaponSkill);
		
		JButton btnGenerate = new JButton("Generate");
		btnGenerate.setBounds(424, 19, 93, 29);
		topPanel.add(btnGenerate);
		
		JButton btnStop = new JButton("停");
		btnStop.setForeground(Color.RED);
		btnStop.setBounds(504, 19, 40, 29);
		topPanel.add(btnStop);
		
		if(!english)
			btnGenerate.setText("生成配装");
		
		JPanel bottomPanel = new JPanel();
		bottomPanel.setBorder(new TitledBorder(new LineBorder(new Color(0, 0, 0)), "Results", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		bottomPanel.setBounds(0, 465, 550, 163);
		contentPane.add(bottomPanel);
		bottomPanel.setLayout(null);
		
		JScrollPane bottomSP = new JScrollPane();
		bottomSP.setBounds(6, 16, 538, 141);
		bottomPanel.add(bottomSP);
		
		JPanel resultPanel = new JPanel();
		bottomSP.setViewportView(resultPanel);
		resultPanel.setLayout(new BoxLayout(resultPanel, BoxLayout.Y_AXIS));
		
		JPanel rightPanel = new JPanel();
		rightPanel.setBorder(new TitledBorder(new LineBorder(new Color(0, 0, 0)), "Selected", TitledBorder.LEFT, TitledBorder.TOP, null, new Color(0, 0, 0)));
		rightPanel.setBounds(333, 68, 211, 398);
		contentPane.add(rightPanel);
		rightPanel.setLayout(null);
		
		JScrollPane rightSP = new JScrollPane();
		rightSP.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		rightSP.setBounds(6, 17, 199, 341);
		rightPanel.add(rightSP);
		
		JPanel selectedPanel = new JPanel();
		BoxLayout bxl = new BoxLayout(selectedPanel, BoxLayout.Y_AXIS);
		selectedPanel.setLayout(bxl);
		rightSP.setViewportView(selectedPanel);
		
		JPanel leftPanel = new JPanel();
		leftPanel.setBorder(new TitledBorder(new LineBorder(new Color(0, 0, 0)), "Skills", TitledBorder.TRAILING, TitledBorder.TOP, null, null));
		leftPanel.setBounds(0, 68, 332, 398);
		contentPane.add(leftPanel);
		leftPanel.setLayout(null);
		
		JPanel searchPanel = new JPanel();
		searchPanel.setBounds(6, 16, 320, 27);
		leftPanel.add(searchPanel);
		searchPanel.setLayout(null);
		
		JLabel lblSearch = new JLabel("Search:");
		if(!english)
			lblSearch.setText("搜索：");
		lblSearch.setBounds(6, 5, 45, 16);
		searchPanel.add(lblSearch);
		
		txtSearchbar = new JTextField();
		txtSearchbar.setBounds(50, 0, 214, 26);
		searchPanel.add(txtSearchbar);
		txtSearchbar.setColumns(10);
		
		JButton btnClear = new JButton("Reset");
		if(!english)
			btnClear.setText("重置");
		btnClear.setBounds(260, 0, 60, 29);
		searchPanel.add(btnClear);
		
		JScrollPane leftSP = new JScrollPane();
		leftSP.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		leftSP.setBounds(6, 46, 320, 346);
		leftPanel.add(leftSP);
		
		JPanel skillListPanel = new JPanel();
		WrapLayout wrapLayout = new WrapLayout();
		wrapLayout.setHgap(3);
		wrapLayout.setAlignment(WrapLayout.LEFT);
		skillListPanel.setLayout(wrapLayout);
		leftSP.setViewportView(skillListPanel);
		
		itemChangeListener = new ItemChangeListener(rightPanel, selectedPanel);
		
		JPanel rightProgressPanel = new JPanel();
		rightProgressPanel.setBounds(6, 359, 199, 33);
		rightPanel.add(rightProgressPanel);
		rightProgressPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));
		comboboxWeaponSkill.addItemListener(itemChangeListener);
		
		btnClear.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) 
			{
				comboboxWeaponSkill.setSelectedIndex(0);
				selectedPanel.removeAll();
				for(int i = 0; i < skillListPanel.getComponentCount(); i++)
				{
					@SuppressWarnings("unchecked")
					JComboBox<String> skillBox = (JComboBox<String>)skillListPanel.getComponent(i);
					skillBox.setSelectedIndex(0);
					skillBox.setForeground(Color.BLACK);
				}
				txtSearchbar.setText("");
			}
		});
		
		btnGenerate.addMouseListener(new MouseAdapter() 
		{
			@Override
			public void mouseClicked(MouseEvent e) 
			{
				if(resultPanel.getComponentCount() > 0)
					resultPanel.removeAll();
				
				int[] weaponSlots = new int[3];
				weaponSlots[0] = (int) weaponSlot1.getValue();
				weaponSlots[1] = (int) weaponSlot2.getValue();
				weaponSlots[2] = (int) weaponSlot3.getValue();
				String wps = (String)comboboxWeaponSkill.getSelectedItem();
				
				Weapon template = new Weapon(wps, weaponSlots);
				if(selectedPanel.getComponentCount() < 2)
				{
					JLabel resultText = new JLabel();
					if(english)
						resultText.setText("No Enough Skills are selected!");
					else
						resultText.setText("技能选的太少了懒得配！");
					resultPanel.add(resultText);
					bottomSP.validate();
					bottomSP.repaint();
					return;
				}
				Thread t = new Thread(new WorkerThread(processor, english, selectedPanel.getComponents(), template, rightProgressPanel,resultPanel, bottomSP));
				t.setDaemon(true);
				t.start();
			}
		});
		
		btnStop.addMouseListener(new MouseAdapter() 
		{
			@Override
			public void mouseClicked(MouseEvent e) {processor.stopGeneration();}
		});
		
		processor.sortSkillsByLanguage(english);
		displaySkillList(skillListPanel, processor.getSkills(), english);
		txtSearchbar.addKeyListener(new KeyAdapter() 
		{
			@Override
			public void keyTyped(KeyEvent e) 
			{
				searchHandler(e.getExtendedKeyCode() > 10?txtSearchbar.getText()+e.getKeyChar():txtSearchbar.getText(), skillListPanel);
			}
		});
	}
	
	public void displaySkillList(JPanel panel, ArrayList<Skill> skills, boolean english)
	{
		for(Skill s: skills)
		{
			JComboBox<String> skillComboBox = new JComboBox<String>();
			
			String skill = english?s.getNameE():s.getNameC();
			int maxlv = s.getMaxLevel()+1;
			for(int i = 0; i < maxlv; i++)
			{
				if(i == 0)
					skillComboBox.addItem(skill);
				else
					skillComboBox.addItem(skill+"+"+i);
			}
			skillComboBox.addItemListener(itemChangeListener);
			skillComboBox.setPrototypeDisplayValue("xxxxxxxxxxx");
			panel.add(skillComboBox);
		}
	}
	
	/**
	 * translate the english weapon skills to chinese and return it
	 * @param processor stores all the data we need
	 * @return return String[] to be easily add to combobox
	 */
	public String[] translateWS(Processor processor)
	{
		String[] ws = processor.getWeaponSkills();
		ArrayList<Skill> skills = processor.getSkills();
		
		//i=0 is "none" so start from i = 1
		for(int i = 1; i < ws.length; i++)
		{
			String[] enSkill = ws[i].split("\\+");
			for(Skill s: skills)
			{
				if(enSkill[0].equals(s.getNameE()))
				{
					ws[i] = s.getNameC()+"+"+enSkill[1];
					break;
				}
			}
		}
		return ws;
	}
	
	@SuppressWarnings("unchecked")
	public void searchHandler(String keyword, JPanel skillLists)
	{	
		for(int i = 0; i < skillLists.getComponentCount(); i++)
		{
			JComboBox<String> skillBox = (JComboBox<String>)skillLists.getComponent(i);
			String skill = (String)skillBox.getSelectedItem();
			if(!(keyword.length() < 1) && (skill.startsWith(keyword)))
				skillBox.setForeground(Color.RED);
			
			else if(!skillBox.getForeground().equals(Color.BLACK))
				skillBox.setForeground(Color.BLACK);
		}
		skillLists.validate();
		skillLists.repaint();
	}
}
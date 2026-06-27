import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class DataCollector 
{
	File config;
	Scanner reader;
	
	HashMap<String,Skill> skills;
	ArrayList<Decoration> decos;
	ArrayList<Charm> charms;
	
	ArrayList<Armor> helms;
	ArrayList<Armor> chests;
	ArrayList<Armor> arms;
	ArrayList<Armor> waists;
	ArrayList<Armor> legs;
	
	ArrayList<String> weaponSkills;
	
	public DataCollector(String configName)
	{
		config = new File(configName);
		try
		{
			reader = new Scanner(config);
		}catch(FileNotFoundException e) {System.out.println("Cannot find the file son!");}
		
		skills = new HashMap<String,Skill>();
		decos = new ArrayList<Decoration>();
		charms = new ArrayList<Charm>();
		
		helms = new ArrayList<Armor>();
		chests = new ArrayList<Armor>();
		arms = new ArrayList<Armor>();
		waists = new ArrayList<Armor>();
		legs = new ArrayList<Armor>();
		
		weaponSkills = new ArrayList<String>();
	}
	
	public void loadData()
	{
		boolean collectSkills = false;
		boolean collectDeco = false;
		boolean collectCharm = false;
		boolean collectHelm = false;
		boolean collectChest = false;
		boolean collectArm = false;
		boolean collectWaist = false;
		boolean collectLeg = false;
		
		while(reader.hasNextLine())
		{
			String line = reader.nextLine();
			if(line.length() < 1)
				continue;
			String[] separate = line.split("  ");
			
//------------------------------collect legs-------------------------------------
			if(collectLeg)
			{
				legs.add(armorGenerator(separate,'l'));
			}
			else if(line.equals("[腿:Legs]"))
			{
				collectWaist = false;
				collectLeg = true;
			}
			
//------------------------------collect waists-------------------------------------
			if(collectWaist)
			{
				waists.add(armorGenerator(separate,'w'));
			}
			else if(line.equals("[腰:Waist]"))
			{
				collectArm = false;
				collectWaist = true;
			}
				
//------------------------------collect arms-------------------------------------
			if(collectArm)
			{
				arms.add(armorGenerator(separate,'a'));
			}
			else if(line.equals("[手:Arms]"))
			{
				collectChest = false;
				collectArm = true;
			}
			
//------------------------------collect chests-------------------------------------
			if(collectChest)
			{
				chests.add(armorGenerator(separate,'c'));
			}
			else if(line.equals("[胸:Chests]"))
			{
				collectHelm = false;
				collectChest = true;
			}
			
//------------------------------collect helms-------------------------------------
			if(collectHelm)
			{
				helms.add(armorGenerator(separate,'h'));
			}
			else if(line.equals("[头:Helms]"))
			{
				collectCharm = false;
				collectHelm = true;
			}
			
//------------------------------collect charms-------------------------------------
			if(collectCharm)
			{
				String skill1,skill2;
				int s1lv,s2lv;
				
				Charm charm = new Charm(separate[0]);
				if(separate.length > 1)
				{
					String[] firstSkill = separate[1].split("\\+");
					skill1 = firstSkill[0];
					s1lv = Integer.parseInt(firstSkill[1]);
					charm.addSkill(skill1, s1lv);
					charm.addActualSkill(skill1, skills.get(skill1));
				}
				
				if(separate.length > 2)
				{
					String[] secondSkill = separate[2].split("\\+");
					skill2 = secondSkill[0];
					s2lv = Integer.parseInt(secondSkill[1]);
					charm.addSkill(skill2, s2lv);
					charm.addActualSkill(skill2, skills.get(skill2));
				}
				
				charms.add(charm);
			}
			else if(line.equals("[护石:Charms]"))
			{
				collectDeco = false;
				collectCharm = true;
			}
			
//------------------------------collect decorations--------------------------------			
			if(collectDeco)
			{
				String decoName = separate[0];
				int socket = Integer.parseInt(separate[1]);
				String skillName = separate[2];
				
				Decoration d = new Decoration(decoName,socket,skillName);
				Skill s = skills.get(skillName);
				if(s != null)
				{
					d.setSkill(s);
					s.setSocket(socket);	
				}
				decos.add(d);
			}
			else if(line.equals("[装饰:Decorations]"))
			{
				collectSkills = false;
				collectDeco = true;
			}
			
//------------------------------collect skills---------------------------------
			if(collectSkills)
			{
				String[] skillName = separate[0].split(":");
				String nameC = skillName[0];
				String nameE = skillName[1];
				int maxLevel = Integer.parseInt(separate[1]);
				
				if(separate.length == 4 && separate[2].equals("Weapon"))
				{
					String wps = nameE + separate[3];
					weaponSkills.add(wps);
				}
				
				skills.put(nameE, new Skill(nameE, nameC, maxLevel));		
			}
			else if(line.equals("[技能:Skills]"))
				collectSkills = true;
		}
	}
	
	public Processor buildProcessor()
	{
		return new Processor(new ArrayList<Skill>(skills.values()),decos,charms,helms,chests,arms,waists,legs,weaponSkills);
	}
	
	public Armor armorGenerator(String[] separate, char type)
	{
		String[] armorName = separate[0].split(":");
		String nameC = armorName[0];
		String nameE = armorName[1];
		String version = armorName[2];
		
		Armor prototype = null;
		if(type == 'h')
			prototype = new Helm(nameC+"头", nameE, version);
		else if(type == 'c')
			prototype = new Chest(nameC+"胸", nameE, version);
		else if(type == 'a')
			prototype = new Arm(nameC+"手", nameE, version);
		else if(type == 'w')
			prototype = new Waist(nameC+"腰", nameE, version);
		else if(type == 'l')
			prototype = new Leg(nameC+"腿", nameE, version);
		
		for(String ss: separate)
		{
			//setbouns skills
			if(ss.startsWith("S:"))
			{
				String[] setBouns = ss.split(":");
				String setBounsName = setBouns[1];
				int setBounsRequirement = Integer.parseInt(setBouns[2]);
				String setBounsSkill = setBouns[3];
				
				prototype.addSetBounsName(setBounsName);
				prototype.addSetBounsSkill(setBounsRequirement, setBounsSkill);
			}
			
			//set slots
			else if(ss.startsWith("-"))
			{
				String[] slots = ss.split("-");
				
				//slot[0] is empty,so we start with slot 1
				for(int i = 1; i < slots.length; i++)
				{
					prototype.setSocket(Integer.parseInt(slots[i]));
				}
			}
			
			//set normal skills
			else if(!ss.equals(separate[0]))
			{
				String[] skillInfo = ss.split("\\+");
				String skillName = skillInfo[0];
				int skillLevel = Integer.parseInt(skillInfo[1]);
				
				prototype.addSkill(skillName, skillLevel);
				prototype.addActualSkill(skillName, skills.get(skillName));
			}
		}
		
		return prototype;
	}
}

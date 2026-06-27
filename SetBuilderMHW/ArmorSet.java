import java.util.ArrayList;
import java.util.HashMap;

public class ArmorSet 
{
	private Charm charm;
	private Armor helm;
	private Armor chest;
	private Armor arms;
	private Armor waist;
	private Armor legs;
	private ArrayList<String> decos;
	
	//setBounsName, requirement, currentlevel
	private HashMap<String, HashMap<Integer,Integer>> setBounsTracker;
	
	//HashMap<String,Integer> skillsTracker;
	private HashMap<String, Integer> skillsTracker;
	
	private HashMap<Skill, Integer> extra;
	
	private int numOf1slot = 0;
	private int numOf2slot = 0;
	private int numOf3slot = 0;
	
	private int charmCursor = 0;
	private int helmCursor = 0;
	private int chestCursor = 0;
	private int armsCursor = 0;
	private int waistCursor = 0;
	private int legsCursor = 0;
	
	private int[] setScore;
	
	public ArmorSet(int[] weaponSlots, Charm charm, Armor helm, Armor chest, Armor arms, Armor waist, Armor legs)
	{
		this.charm = charm;
		this.helm = helm;
		this.chest = chest;
		this.arms = arms;
		this.waist = waist;
		this.legs = legs;
		this.decos = null;
		
		this.setScore = new int[3];
		this.setBounsTracker = null;
		this.skillsTracker = null;
		this.extra = new HashMap<Skill, Integer>();
		
		for(int i = 0; i < 3; i++)
		{
			int wpnSK = weaponSlots[i];
			int helmSK = helm.getSocketByIndex(i);
			int chestSK = chest.getSocketByIndex(i);
			int armsSK = arms.getSocketByIndex(i);
			int waistSK = waist.getSocketByIndex(i);
			int legsSK = legs.getSocketByIndex(i);
			
			if(wpnSK == 0 && helmSK == 0 && chestSK == 0 && armsSK == 0 && waistSK == 0 && legsSK == 0)
				continue;
		
			if(wpnSK == 1)numOf1slot++;
			else if(wpnSK == 2)numOf2slot++;
			else if(wpnSK == 3)numOf3slot++;
			
			if(helmSK == 1)numOf1slot++;
			else if(helmSK == 2)numOf2slot++;
			else if(helmSK == 3)numOf3slot++;
			
			if(chestSK == 1)numOf1slot++;
			else if(chestSK == 2)numOf2slot++;
			else if(chestSK == 3)numOf3slot++;
			
			if(armsSK == 1)numOf1slot++;
			else if(armsSK == 2)numOf2slot++;
			else if(armsSK == 3)numOf3slot++;
			
			if(waistSK == 1)numOf1slot++;
			else if(waistSK == 2)numOf2slot++;
			else if(waistSK == 3)numOf3slot++;
			
			if(legsSK == 1)numOf1slot++;
			else if(legsSK == 2)numOf2slot++;
			else if(legsSK == 3)numOf3slot++;
		}
	}

	private void initSetBounsSkills()
	{
		setBounsTracker = new HashMap<String, HashMap<Integer, Integer>>();
		setBounsInitalisation(helm);
		setBounsInitalisation(chest);
		setBounsInitalisation(arms);
		setBounsInitalisation(waist);
		setBounsInitalisation(legs);
	}
	
	private void setBounsInitalisation(Armor armor) 
	{
		if(armor.emptySetBounsSkills())
			return;
		for(int requirement: armor.getSetBounsRequirements())
		{
			String setBounsName = armor.getSetBounsName();
			if(setBounsTracker.containsKey(setBounsName))
			{
				if(setBounsTracker.get(setBounsName).containsKey(requirement))
				{
					int currentLv = setBounsTracker.get(setBounsName).get(requirement)+1;
					if(currentLv == requirement && skillsTracker.containsKey(armor.getSetBounsSkill(requirement)))
						skillsTracker.remove(armor.getSetBounsSkill(requirement));
					setBounsTracker.get(setBounsName).put(requirement,currentLv);
				}
				else
					setBounsTracker.get(setBounsName).put(requirement,1);
			}
			else
			{
				HashMap<Integer, Integer> requirePieces = new HashMap<Integer,Integer>();
				requirePieces.put(requirement, 1);
				setBounsTracker.put(setBounsName, requirePieces);
			}
		}
	}

	public Charm getCharm() {return this.charm;}
	public Armor getHelm() {return this.helm;}
	public Armor getChest() {return this.chest;}
	public Armor getArms() {return this.arms;}
	public Armor getWaist() {return this.waist;}
	public Armor getLegs() {return this.legs;}
	
	public void setCursors(int chaC, int helC, int cheC, int armC, int waiC, int legC)
	{
		charmCursor = chaC;
		helmCursor = helC;
		chestCursor = cheC;
		armsCursor = armC;
		waistCursor = waiC;
		legsCursor = legC;
	}
	
	public int getCharmCursor() {return charmCursor;}
	public int getHelmCursor() {return helmCursor;}
	public int getChestCursor() {return chestCursor;}
	public int getArmsCursor() {return armsCursor;}
	public int getWaistCursor() {return waistCursor;}
	public int getLegsCursor() {return legsCursor;}
	
	public void setDecorations(ArrayList<String> decorations) {this.decos = decorations;}
	
	public boolean insertDeco(int slotLevel)
	{
		if(slotLevel == 1)
		{
			if(numOf1slot  > 0)
				numOf1slot -=1;
			else if(numOf2slot > 0)
				numOf2slot -=1;
			else if(numOf3slot > 0)
				numOf3slot -=1;
			else
				return false;
		}
		
		else if(slotLevel == 2)
		{
			if(numOf2slot > 0)
				numOf2slot -=1;
			else if(numOf3slot > 0)
				numOf3slot -=1;
			else
				return false;
		}
		
		else if(slotLevel == 3)
		{
			if(numOf3slot > 0)
				numOf3slot -=1;
			else
				return false;
		}
		return true;
	}
	
	public HashMap<String, Integer> suitsSelectedSkills(HashMap<String,Integer> selected, int[]selectedScore)
	{	
		skillsTracker = new HashMap<String, Integer>(selected);
		initSetBounsSkills();
		
		if(charm!= null)
		{
			for(String skill: charm.getAllSkills())
			{
				int skillLv = charm.getSkillLevel(skill);
				if(skillsTracker.containsKey(skill))
				{
					int requireLv = skillsTracker.get(skill);
					if(requireLv > skillLv)
						skillsTracker.put(skill, requireLv-skillLv);
					else
						skillsTracker.remove(skill);
				}
			}	
		}
		suitsSelectedSkillsAssistant(helm);
		suitsSelectedSkillsAssistant(chest);
		suitsSelectedSkillsAssistant(arms);
		suitsSelectedSkillsAssistant(waist);
		suitsSelectedSkillsAssistant(legs);
		
		return skillsTracker;
	}

	public void suitsSelectedSkillsAssistant(Armor armor)
	{		
		for(String skill: armor.getContainedSkills())
		{
			int skillLv = armor.getSkillLevel(skill);
			if(skillsTracker.containsKey(skill))
			{
				int requireLv = skillsTracker.get(skill);
				if(requireLv > skillLv)
					skillsTracker.put(skill, requireLv-skillLv);
				else
					skillsTracker.remove(skill);
			}
			else
			{
				Skill keySkill = armor.getActualSkillByName(skill);
				if(extra.containsKey(keySkill))
					extra.put(keySkill, extra.get(keySkill)+skillLv);
				else
					extra.put(keySkill, skillLv);
			}
		}
	}
	
	public String armorToString(boolean english)
	{
		if(english)
			return(helm.getNameE()+helm.getVersion()+" /"+chest.getNameE()+chest.getVersion()+" /"+arms.getNameE()+arms.getVersion()
			+" /"+waist.getNameE()+waist.getVersion()+" /"+legs.getNameE()+legs.getVersion());
		else
			return(helm.getNameC()+helm.getVersion()+" /"+chest.getNameC()+chest.getVersion()+" /"+arms.getNameC()+arms.getVersion()
			+" /"+waist.getNameC()+waist.getVersion()+" /"+legs.getNameC()+legs.getVersion());
	}
	
	public String extraToString(boolean english)
	{
		String sentence = english?"Extra:":"额外:";
		if(numOf1slot > 0)
			sentence+= english?"LV1Socket*":"一级孔*"+numOf1slot+" ";
		if(numOf2slot > 0)
			sentence+= english?"LV2Socket*":"二级孔*"+numOf2slot+" ";
		if(numOf3slot > 0)
			sentence+= english?"LV3Socket*":"三级孔*"+numOf3slot+" ";
		
		sentence += "/";
		for(Skill sk: extra.keySet())
			sentence+= english?sk.getNameE():sk.getNameC()+"+"+extra.get(sk)+" ";

		return sentence;
	}
	
	public String charmDecoToString(boolean english, ArrayList<Skill> skillList)
	{
		String sentence = "";
		if(english)
		{
			if(charm != null)
				sentence+= charm.getName()+" Charm /Jewels:";
			else
				sentence+="Charm: No Compulsory Charm needed /Jewels:";
		}
		else
		{
			sentence+= "护石:";
			if(charm != null)
			{
				for(String c: charm.getAllSkills())
				{
					for(Skill s: skillList)
					{
						if(s.getNameE().equals(c))
						{
							sentence+= s.getNameC()+"+"+charm.getSkillLevel(c)+" ";
						}
					}
				}	
			}
			else
			{
				sentence+="没有必须的护石";
			}
			sentence+=" /珠子:";
		}
		
		if(decos == null || decos.size()==0)
		{
			if(english)
				sentence+= "No Specific Jewels Needed";
			else
				sentence+= "不需要任何珠子";
			return sentence;
		}
		
		for(String d: decos)
		{
			sentence+= d +" ";
		}
		return sentence;
	}
	
	public boolean sameArmorSet(int charmT, int helmT, int chestT, int armsT, int waistT, int legsT)
	{	
		if(helmCursor != helmT)
			return false;
		if(chestCursor != chestT)
			return false;
		if(armsCursor != armsT)
			return false;
		if(waistCursor != waistT)
			return false;
		if(legsCursor != legsT)
			return false;
		if(charmCursor != charmT)
			return false;
		return true;
	}
	
	public String getCompareCode()
	{
		return charmCursor+""+helmCursor+""+chestCursor+""+armsCursor+""+waistCursor+""+legsCursor;
	}

	public String getCharmString() 
	{
		if(charm!=null)
			return charm.getName();
		else
			return null;
	}
	
	private void calculateSetScore() 
	{
		setScore[0] += numOf1slot + helm.getArmorScoreByLevel(1)+chest.getArmorScoreByLevel(1)+arms.getArmorScoreByLevel(1)+waist.getArmorScoreByLevel(1)+legs.getArmorScoreByLevel(1);
		setScore[1] += numOf2slot + helm.getArmorScoreByLevel(2)+chest.getArmorScoreByLevel(2)+arms.getArmorScoreByLevel(2)+waist.getArmorScoreByLevel(2)+legs.getArmorScoreByLevel(2);
		setScore[2] += numOf3slot + helm.getArmorScoreByLevel(3)+chest.getArmorScoreByLevel(3)+arms.getArmorScoreByLevel(3)+waist.getArmorScoreByLevel(3)+legs.getArmorScoreByLevel(3);
		
	}
	
	private boolean scoreComparePass(int[] selectedScore) 
	{
		if(setScore[2] < selectedScore[2])
			return false;
		if((setScore[1]+setScore[2])<(selectedScore[1]+selectedScore[2]))
			return false;
		if((setScore[0]+setScore[1]+setScore[2])<(selectedScore[0]+selectedScore[1]+selectedScore[2]))
			return false;
		return true;
	}
}
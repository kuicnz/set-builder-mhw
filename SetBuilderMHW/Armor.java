import java.util.HashMap;
import java.util.Set;

public abstract class Armor 
{
	String nameC;
	String nameE;
	String version;
	
	HashMap<String,Integer> containedSkills;
	HashMap<String,Skill> actualSkills;
	HashMap<Integer,String> setBounsSkills;
	
	String setBounsName;
	
	int[] sockets;
	
	//how many 1slot skills+socket, 2..3.. does this armor piece have.
	int[] armorScore;
	
	public Armor(String nameC, String nameE, String version)
	{
		this.nameC = nameC;
		this.nameE = nameE;
		this.version = version;
		
		this.actualSkills = null;
		this.containedSkills = null;
		this.setBounsSkills = null;
		this.setBounsName = null;
		
		this.sockets = new int[3];
		this.armorScore = new int[3];
	}
	
	public String getNameC() {return this.nameC;}
	public String getNameE() {return this.nameE;}
	public String getVersion() {return this.version;}
	
	public boolean emptyContainedSkills() {return containedSkills==null;}
	public boolean emptySetBounsSkills() {return setBounsSkills== null;}
	
	public int[] getSockets() {return sockets;}
	public int getSocketByIndex(int index) {return sockets[index];}
	public void setSocket(int slot)
	{
		if(sockets[0] == 0)
			sockets[0] = slot;
		else if(sockets[1] == 0)
			sockets[1] = slot;
		else
			sockets[2] = slot;
		
		if(slot > 0)
			armorScore[slot-1] += 1;
	}
	
	public boolean hasSetBouns() {return setBounsName!=null;}
	public String getSetBounsName() {return setBounsName;}
	public void addSetBounsName(String setBounsName)
	{
		this.setBounsName = setBounsName;
	}
	
	public Set<Integer> getSetBounsRequirements(){return setBounsSkills.keySet();}
	public String getSetBounsSkill(int requirement) {return setBounsSkills.get(requirement);}
	public void addSetBounsSkill(int setNumber, String skillName)
	{
		if(setBounsSkills == null)
			setBounsSkills = new HashMap<Integer,String>();
		setBounsSkills.put(setNumber, skillName);
	}
	
	public Skill getActualSkillByName(String name) {return actualSkills.get(name);}
	public Set<String> getActualSkills(){return actualSkills.keySet();}
	public void addActualSkill(String skillName, Skill actual)
	{
		if(actualSkills == null)
			actualSkills = new HashMap<String, Skill>();
		actualSkills.put(skillName, actual);
		
		int socketlv = actual.getSocket();
		int skilllv = containedSkills.get(skillName);
		if(socketlv > 0)
			armorScore[socketlv-1] +=skilllv;
	}
	
	public Set<String> getContainedSkills(){return containedSkills.keySet();}
	public int getSkillLevel(String skillName){return containedSkills.get(skillName);}
	public void addSkill(String skillName, int skillLevel)
	{
		if(containedSkills == null)
			containedSkills = new HashMap<String,Integer>();
		containedSkills.put(skillName,skillLevel);
	}
	
	public boolean contains(String skill)
	{
		if(containedSkills != null && containedSkills.containsKey(skill))
			return true;
		else
		{
			if(setBounsSkills != null && setBounsSkills.containsValue(skill))
				return true;
			return false;
		}
	}
	
	public boolean containsNormalSkill(String skill)
	{
		return containedSkills.containsKey(skill);
	}
	
	public int getArmorScoreByLevel(int level)
	{
		if(level > 0)
			return armorScore[level-1];
		else
			return 0;
	}
	
	public int getSocketScore(int slot)
	{
		int n1 = 0;
		int n2 = 0;
		int n3 = 0;
		
		for(int s: sockets)
		{
			if(s == 1)
				n1++;
			if(s == 2)
				n2++;
			if(s == 3)
				n3++;
		}
		
		if(slot == 1)
			return n1*10+n2+n3;
		if(slot == 2)
			return(n2+n3)*10 + n1;
		if(slot == 3)
			return n3*10 + n2 + n1;
		
		return 0;
	}
}
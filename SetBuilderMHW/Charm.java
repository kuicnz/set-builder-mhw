import java.util.HashMap;
import java.util.Set;

public class Charm 
{
	String name;
	HashMap<String,Integer> skills;
	HashMap<String,Skill> actualSkills;
	
	//what is the slot value for this charm, array length 3 for slot lv1,2,3
	int[] charmScore;
	
	public Charm(String name)
	{
		this.name = name;
		this.skills = new HashMap<String,Integer>();
		this.actualSkills = new HashMap<String, Skill>();
		this.charmScore = new int[3];
	}
	
	public String getName() {return name;}
	
	public Set<String> getAllSkills(){return skills.keySet();}
	public Skill getActualSkill(String skillName)
	{
		return actualSkills.get(skillName);
	}
	public int getSkillLevel(String skillName)
	{
		return skills.get(skillName);
	}
	
	public void addActualSkill(String skillName, Skill actual)
	{
		actualSkills.put(skillName, actual);
		int socketlv = actual.getSocket();
		int skilllv = skills.get(skillName);
		if(socketlv > 0)
			charmScore[socketlv-1] +=skilllv;
	}
	public void addSkill(String skillName, int skillLevel)
	{
		skills.put(skillName, skillLevel);
	}
	
	public boolean contains(String skill)
	{
		return skills.containsKey(skill);
	}
}
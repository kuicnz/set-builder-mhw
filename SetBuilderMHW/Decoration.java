
public class Decoration 
{
	String name;
	int socket;
	String skillName;
	Skill actualSkill;
	
	public Decoration(String name, int socket, String skillName)
	{
		this.name = name;
		this.socket = socket;
		this.skillName = skillName;
		this.actualSkill = null;
	}
	
	public void setSkill(Skill skill) {actualSkill = skill;}
	
	public String getName() {return name;}
	public int getSocket() {return socket;}
	public String getSkillName() {return skillName;}
	public Skill getActualSkill() {return actualSkill;}
	
	public boolean contains(String skill)
	{
		return skillName.equals(skill);
	}
}

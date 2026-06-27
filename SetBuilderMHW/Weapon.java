//this is just a prototype of weapon
public class Weapon 
{
	private String skill;
	private int[] slots;
	private Skill actualSkill;
	
	public Weapon(String skill, int[] slots)
	{
		this.skill = skill;
		this.slots = slots;
		this.actualSkill = null;
	}
	
	public String getSkill() {return skill;}
	public int[] getSlots() {return slots;}
	
	public void setActualSkill(Skill actualone) {actualSkill = actualone;}
	public Skill getActualSkill() {return this.actualSkill;}
}

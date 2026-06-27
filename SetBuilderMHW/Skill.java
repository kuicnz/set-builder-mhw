
public class Skill 
{
	String nameE;
	String nameC;
	int maxlevel;
	
	int socket;
	
	public Skill(String nameE, String nameC, int maxlevel)
	{
		this.nameE = nameE;
		this.nameC = nameC;
		this.maxlevel = maxlevel;
		this.socket = 0;
	}
	
	public String getNameC() {return nameC;}
	public String getNameE() {return nameE;}
	public int getMaxLevel() {return maxlevel;}
	
	public void setSocket(int socket) {this.socket = socket;}
	public int getSocket() {return this.socket;}
}

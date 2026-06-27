import java.util.Comparator;
import java.util.Locale;
import java.text.Collator;

public class SkillComparator implements Comparator<Skill> 
{
	private boolean english;
	
	public SkillComparator(boolean english)
	{
		this.english = english;
	}
	
	@Override
	public int compare(Skill o1, Skill o2) 
	{
		if(english)
			return o1.getNameE().compareTo(o2.getNameE());
		else
			return Collator.getInstance(Locale.CHINESE).compare(o1.getNameC(), o2.getNameC());
	}
}

import java.awt.Color;
import java.awt.Component;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class Processor
{
	private ArrayList<Skill> skills;
	private ArrayList<Decoration> decos;
	private ArrayList<Charm> charms;
	private ArrayList<Armor> helms;
	private ArrayList<Armor> chests;
	private ArrayList<Armor> arms;
	private ArrayList<Armor> waists;
	private ArrayList<Armor> legs;
	private ArrayList<ArmorSet> results;
	private ArrayList<String> weaponSkills;
	private boolean stop;
	
	public Processor(ArrayList<Skill> skills, ArrayList<Decoration> decos, ArrayList<Charm> charms, 
			ArrayList<Armor> helms, ArrayList<Armor> chests, ArrayList<Armor> arms, ArrayList<Armor> waists, ArrayList<Armor> legs,
			ArrayList<String> weaponSkills)
	{
		this.skills = skills;
		this.decos = decos;
		this.charms = charms;
		this.helms = helms;
		this.chests = chests;
		this.arms = arms;
		this.waists = waists;
		this.legs = legs;
		this.results = new ArrayList<ArmorSet>();
		this.weaponSkills = weaponSkills;
		this.weaponSkills.add(0, "None");
		this.stop = false;
	}
	
	public ArrayList<Skill> getSkills(){return skills;}
	public ArrayList<Decoration> getDecorations(){return decos;}
	public ArrayList<Charm> getCharms(){return charms;}
	public ArrayList<Armor> getHelms(){return helms;}
	public ArrayList<Armor> getChest(){return chests;}
	public ArrayList<Armor> getArms(){return arms;}
	public ArrayList<Armor> getWaists(){return waists;}
	public ArrayList<Armor> getLegs(){return legs;}
	
	public String[] getWeaponSkills()
	{
		String[] formattedWS = weaponSkills.toArray(new String[weaponSkills.size()]);
		return formattedWS;
	}
	
	public ArrayList<ArmorSet> getResult(){return results;}
	public int getResultSize() {return results.size();}

	public void generateArmorSet(boolean english, Component[] components, Weapon templateWeapon, JPanel rightProgressPanel) 
	{
		stop = false;
		if(!results.isEmpty())
			results.clear();
		
		HashMap<String,Integer> selected = new HashMap<String, Integer>();
		if(!templateWeapon.getSkill().equals("None"))
			templateWeapon.setActualSkill(findSkill(templateWeapon.getSkill().split("\\+")[0]));
		
		int[] selectedScore = new int[3];
		for(int i = 0; i < components.length; i++)
		{
			String[] sltSkill = ((JLabel)components[i]).getText().split("\\+");
			Skill found = findSkill(sltSkill[0]);
			int foundLevel = Integer.parseInt(sltSkill[1]);
			if(found.getSocket() > 0)
				selectedScore[found.getSocket()-1]+= foundLevel;
			
			if(templateWeapon.getActualSkill() == null || !found.nameE.equals(templateWeapon.getActualSkill().getNameE()))
				selected.put(found.nameE, foundLevel);
		}
		
		//using a filter to reduce the number of items we need to look through
		ArrayList<Charm> filteredCharms = new ArrayList<Charm>();
		HashMap<String,Decoration> filteredDecos = new HashMap<String,Decoration>();
		ArrayList<Armor> filteredHelms = new ArrayList<Armor>();
		ArrayList<Armor> filteredChests = new ArrayList<Armor>();
		ArrayList<Armor> filteredArms = new ArrayList<Armor>();
		ArrayList<Armor> filteredWaists = new ArrayList<Armor>();
		ArrayList<Armor> filteredLegs = new ArrayList<Armor>();
		
		if(selected.isEmpty())
			return;
		
		selectedSkillFilter(selected, filteredCharms, filteredDecos, filteredHelms, filteredChests, filteredArms, filteredWaists, filteredLegs);
		bfsTraverse(rightProgressPanel,english,selected,selectedScore,templateWeapon.getSlots(),filteredCharms,filteredDecos,filteredHelms,filteredChests,filteredArms,filteredWaists,filteredLegs);
	}
	
	public void bfsTraverse(JPanel rightProgressPanel, boolean english, HashMap<String, Integer> selected, int[] selectedScore, int[] weaponSlots, ArrayList<Charm> filteredCharms, HashMap<String, Decoration> filteredDecos, ArrayList<Armor> filteredHelms, ArrayList<Armor> filteredChests, ArrayList<Armor> filteredArms, ArrayList<Armor> filteredWaists, ArrayList<Armor> filteredLegs)
	{
		JLabel progress = new JLabel("检索了:");
		if(english)
			progress.setText("Checked:");
		
		JLabel found = new JLabel("匹配:");
		if(english)
			found.setText("Found:");
		found.setForeground(Color.RED);
		
		rightProgressPanel.add(progress);
		rightProgressPanel.add(found);
		String pstxt = progress.getText();
		String fdtxt = found.getText();

		int charmCursor = filteredCharms.size()-1;
		int helmCursor = filteredHelms.size()-1;
		int chestCursor = filteredChests.size()-1;
		int armsCursor = filteredArms.size()-1;
		int waistCursor = filteredWaists.size()-1;
		int legsCursor = filteredLegs.size()-1;
		
		LinkedList<ArmorSet> fringe = new LinkedList<ArmorSet>();
		
		Charm tempCharm = charmCursor < 0? null:filteredCharms.get(charmCursor);
		Armor tempHelm = helmCursor < 0? null:filteredHelms.get(helmCursor);
		Armor tempChest = chestCursor < 0? null:filteredChests.get(chestCursor);
		Armor tempArms = armsCursor < 0? null:filteredArms.get(armsCursor);
		Armor tempWaist = waistCursor < 0? null:filteredWaists.get(waistCursor);
		Armor tempLegs = legsCursor < 0? null:filteredLegs.get(legsCursor);
		
		ArmorSet tempSet = new ArmorSet(weaponSlots,tempCharm, tempHelm, tempChest, tempArms, tempWaist, tempLegs);
		tempSet.setCursors(charmCursor, helmCursor, chestCursor, armsCursor, waistCursor, legsCursor);
		fringe.push(tempSet);
		
		int sleepCounter = 0;
		HashMap<String,Character> expanded = new HashMap<String,Character>();
		while(!fringe.isEmpty())
		{
			if(stop || results.size() > 100)
			{
				progress.setText(pstxt+expanded.size());
				found.setText(fdtxt+results.size());
				break;
			}
			
			ArmorSet examinee = fringe.poll();
			expanded.put(examinee.getCompareCode(),'f');			
			
			HashMap<String,Integer> unsatisfied = examinee.suitsSelectedSkills(selected,selectedScore);
			if(decorationFillUpFinalCheck(examinee, unsatisfied, filteredDecos, english))
				results.add(examinee);
			
			sleepCounter++;
			
			tempCharm = examinee.getCharm();
			tempHelm = examinee.getHelm();
			tempChest = examinee.getChest();
			tempArms = examinee.getArms();
			tempWaist = examinee.getWaist();
			tempLegs = examinee.getLegs();
			
			charmCursor = examinee.getCharmCursor();
			helmCursor = examinee.getHelmCursor();
			chestCursor = examinee.getChestCursor();
			armsCursor = examinee.getArmsCursor();
			waistCursor = examinee.getWaistCursor();
			legsCursor = examinee.getLegsCursor();
			
			Charm nextCharm = examinee.getCharmCursor() < 1? null: filteredCharms.get(charmCursor-1);
			tempSet = new ArmorSet(weaponSlots, nextCharm,tempHelm,tempChest,tempArms,tempWaist,tempLegs);
			tempSet.setCursors(nextCharm==null?charmCursor:charmCursor-1,helmCursor,chestCursor,armsCursor,waistCursor,legsCursor);
			if(!expanded.containsKey(tempSet.getCompareCode()))
				fringe.push(tempSet);
			
			if(helmCursor > 0)
			{
				tempSet = new ArmorSet(weaponSlots, tempCharm, filteredHelms.get(helmCursor-1),tempChest,tempArms,tempWaist,tempLegs);
				tempSet.setCursors(charmCursor,helmCursor-1,chestCursor,armsCursor,waistCursor,legsCursor);
				if(!expanded.containsKey(tempSet.getCompareCode()))
					fringe.push(tempSet);
			}
			
			if(chestCursor > 0)
			{
				tempSet = new ArmorSet(weaponSlots, tempCharm, tempHelm,filteredChests.get(chestCursor-1),tempArms,tempWaist,tempLegs);
				tempSet.setCursors(charmCursor,helmCursor,chestCursor-1,armsCursor,waistCursor,legsCursor);
				if(!expanded.containsKey(tempSet.getCompareCode()))
					fringe.push(tempSet);
			}
			
			if(armsCursor > 0)
			{
				tempSet = new ArmorSet(weaponSlots, tempCharm, tempHelm,tempChest,filteredArms.get(armsCursor-1),tempWaist,tempLegs);
				tempSet.setCursors(charmCursor,helmCursor,chestCursor,armsCursor-1,waistCursor,legsCursor);
				if(!expanded.containsKey(tempSet.getCompareCode()))
					fringe.push(tempSet);
			}
			
			if(waistCursor > 0)
			{
				tempSet = new ArmorSet(weaponSlots, tempCharm, tempHelm,tempChest,tempArms,filteredWaists.get(waistCursor-1),tempLegs);
				tempSet.setCursors(charmCursor,helmCursor,chestCursor,armsCursor,waistCursor-1,legsCursor);
				if(!expanded.containsKey(tempSet.getCompareCode()))
					fringe.push(tempSet);
			}
			
			if(legsCursor > 0)
			{
				tempSet = new ArmorSet(weaponSlots, tempCharm, tempHelm,tempChest,tempArms,tempWaist,filteredLegs.get(legsCursor-1));
				tempSet.setCursors(charmCursor,helmCursor,chestCursor,armsCursor,waistCursor,legsCursor-1);
				if(!expanded.containsKey(tempSet.getCompareCode()))
					fringe.push(tempSet);
			}
			
			if(sleepCounter%10000 == 0)
			{
				progress.setText(pstxt+expanded.size());
				found.setText(fdtxt+results.size());
				try {Thread.sleep(150);} 
				catch (InterruptedException e) {break;}	
			}
		}
	}

	private boolean decorationFillUpFinalCheck(ArmorSet examinee, HashMap<String, Integer> unsatisfied, HashMap<String, Decoration> filteredDecos, boolean english) 
	{
		if(unsatisfied == null)
			return false;
		if(unsatisfied.isEmpty())
			return true;
		
		ArrayList<String> insertedDecos = new ArrayList<String>();
		
		for(String skill: unsatisfied.keySet())
		{
			if(!filteredDecos.containsKey(skill))
				return false;
			
			Decoration d = filteredDecos.get(skill);
			int socketLevel = d.getSocket();
			int numOfJewel = 0;
			
			int remainedlv = unsatisfied.get(skill);
			while(remainedlv > 0)
			{
				if(!examinee.insertDeco(socketLevel))
					return false;	
				numOfJewel++;
				remainedlv--;
			}
			
			if(english)
				insertedDecos.add(d.getName()+"*"+numOfJewel);
			else
				insertedDecos.add(d.getActualSkill().nameC+"珠子*"+numOfJewel);
		}
		
		examinee.setDecorations(insertedDecos);
		return true;
	}

	private void selectedSkillFilter(HashMap<String, Integer> selected, ArrayList<Charm> filteredCharms,
			HashMap<String, Decoration> filteredDecos, ArrayList<Armor> filteredHelms, ArrayList<Armor> filteredChests,
			ArrayList<Armor> filteredArms, ArrayList<Armor> filteredWaists, ArrayList<Armor> filteredLegs) 
	{
		int charmSize = charms.size();
		int decoSize = decos.size();
		int helmSize = helms.size();
		int chestSize = chests.size();
		int armsSize = arms.size();
		int waistSize = waists.size();
		int legsSize = legs.size();
		int maxSize = 0;
		
		maxSize = maxSize > charmSize? maxSize: charmSize;
		maxSize = maxSize > decoSize? maxSize: decoSize;
		maxSize = maxSize > helmSize? maxSize: helmSize;
		maxSize = maxSize > chestSize? maxSize: chestSize;
		maxSize = maxSize > armsSize? maxSize: armsSize;
		maxSize = maxSize > waistSize? maxSize: waistSize;
		maxSize = maxSize > legsSize? maxSize: legsSize;
		
		Armor[] bestSlotHelm = new Armor[3];
		Armor[] bestSlotChest = new Armor[3];
		Armor[] bestSlotArms = new Armor[3];
		Armor[] bestSlotWaist = new Armor[3];
		Armor[] bestSlotLegs = new Armor[3];
		
		for(int i = 0; i < maxSize; i++)
		{
			for(String key: selected.keySet())
			{
				int value = selected.get(key);
				if(i < charmSize)
				{
					Charm tempCharm = charms.get(i);
					if(tempCharm.contains(key))
						filteredCharms.add(tempCharm);
				}
				
				if(i < decoSize)
				{
					Decoration tempDeco = decos.get(i);
					if(tempDeco.contains(key))
						filteredDecos.put(key,tempDeco);
				}
				
				if(i < helmSize)
					armorFilterAssistant(helms,filteredHelms,bestSlotHelm,key,value,i);
				if(i < chestSize)
					armorFilterAssistant(chests,filteredChests,bestSlotChest,key,value,i);
				if(i < armsSize)
					armorFilterAssistant(arms,filteredArms,bestSlotArms,key,value,i);
				if(i < waistSize)
					armorFilterAssistant(waists,filteredWaists,bestSlotWaist,key,value,i);
				if(i < legsSize)
					armorFilterAssistant(legs,filteredLegs,bestSlotLegs,key,value,i);
			}
		}
		
		if(!filteredHelms.contains(bestSlotHelm[0]))filteredHelms.add(bestSlotHelm[0]);
		if(!filteredHelms.contains(bestSlotHelm[1]))filteredHelms.add(bestSlotHelm[1]);
		if(!filteredHelms.contains(bestSlotHelm[2]))filteredHelms.add(bestSlotHelm[2]);
		
		if(!filteredChests.contains(bestSlotChest[0]))filteredChests.add(bestSlotChest[0]);
		if(!filteredChests.contains(bestSlotChest[1]))filteredChests.add(bestSlotChest[1]);
		if(!filteredChests.contains(bestSlotChest[2]))filteredChests.add(bestSlotChest[2]);
		
		if(!filteredArms.contains(bestSlotArms[0]))filteredArms.add(bestSlotArms[0]);
		if(!filteredArms.contains(bestSlotArms[1]))filteredArms.add(bestSlotArms[1]);
		if(!filteredArms.contains(bestSlotArms[2]))filteredArms.add(bestSlotArms[2]);
		
		if(!filteredWaists.contains(bestSlotWaist[0]))filteredWaists.add(bestSlotWaist[0]);
		if(!filteredWaists.contains(bestSlotWaist[1]))filteredWaists.add(bestSlotWaist[1]);
		if(!filteredWaists.contains(bestSlotWaist[2]))filteredWaists.add(bestSlotWaist[2]);
		
		if(!filteredLegs.contains(bestSlotLegs[0]))filteredLegs.add(bestSlotLegs[0]);
		if(!filteredLegs.contains(bestSlotLegs[1]))filteredLegs.add(bestSlotLegs[1]);
		if(!filteredLegs.contains(bestSlotLegs[2]))filteredLegs.add(bestSlotLegs[2]);
	}

	public Skill findSkill(String skillname)
	{
		for(Skill s: skills)
		{
			if(s.getNameC().equals(skillname) || s.getNameE().equals(skillname))
				return s;
		}
		return null;
	}
	
	public void armorFilterAssistant(ArrayList<Armor> armors, ArrayList<Armor> filteredArmor, Armor[] bestSlotArmor, String skill, Integer skillLevel,int index)
	{
		Armor tempArmor = armors.get(index);
		if(tempArmor.contains(skill))
		{
			if(tempArmor.containsNormalSkill(skill) && tempArmor.getSkillLevel(skill) > skillLevel)
				return;
			filteredArmor.add(tempArmor);
		}
		
		else
		{
			if(bestSlotArmor[0]== null || tempArmor.getSocketScore(1) > bestSlotArmor[0].getSocketScore(1))
				bestSlotArmor[0] = tempArmor;
			else if(bestSlotArmor[1]== null || tempArmor.getSocketScore(2) > bestSlotArmor[1].getSocketScore(2))
				bestSlotArmor[1] = tempArmor;
			else if(bestSlotArmor[2]== null || tempArmor.getSocketScore(3) > bestSlotArmor[2].getSocketScore(3))
				bestSlotArmor[2] = tempArmor;
		}
	}
	
	public void displayArmorSet(JPanel resultPanel, boolean english) 
	{
		int counter = 1;
		for(ArmorSet as: results)
		{
			JLabel setNumber = new JLabel("Set "+counter+":");
			setNumber.setForeground(Color.RED);
			JLabel armorString = new JLabel(as.armorToString(english));
			JLabel charmDecoString = new JLabel(as.charmDecoToString(english, skills));
			JLabel extraString = new JLabel(as.extraToString(english));
			extraString.setForeground(Color.BLUE);
			resultPanel.add(setNumber);
			resultPanel.add(armorString);
			resultPanel.add(charmDecoString);
			resultPanel.add(extraString);
			counter++;
		}
	}
	
	public void stopGeneration() {stop = true;}

	public void sortSkillsByLanguage(boolean english) 
	{
		SkillComparator sc = new SkillComparator(english);
		skills.sort(sc);
	}
}
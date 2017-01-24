package se.wowsim.classes;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import se.wowsim.spells.*;

public class ClassBuilder {
	
	private Classes myClass;
	private int level;
	private int intellect;
	private Set<Spell> allSpells;
	
	public ClassBuilder(Classes myClass, int level, int intellect) {
		this.myClass = myClass;
		this.level = level;
        this.intellect = intellect;
		allSpells = new HashSet<>();
		importSpells();
	}
	
	public ClassTemplate getClassInstance() {
		switch (myClass) {
		case WARLOCK:
			Warlock warlock = new Warlock(intellect);
			for(Spell s : allSpells) {
				if(s.getSpellClass().equals(Classes.WARLOCK)) {
					warlock.addSpell(s.getName(), s);
                    if (s instanceof DirectDamage){
                        ((DirectDamage) s).setCritChance(myClass.calculateCritChance(intellect, level));
                    }
                    s.init();
				}
			}
			return warlock;
		default:
			return null;
		}
	}
	
	private void importSpells() {
		allSpells.add(new Corruption(determineSpellLevel(Corruption.levelUps)));
		allSpells.add(new CurseOfAgony(determineSpellLevel(CurseOfAgony.levelUps)));
		allSpells.add(new Shadowbolt(determineSpellLevel(Shadowbolt.levelUps)));
	}
	
	private int determineSpellLevel(List<Integer> levelups) {
		int levelOfSpell = 0;
		for(int i : levelups) {
			if (this.level >= i) { levelOfSpell++; }
		}
		return levelOfSpell;
	}
}

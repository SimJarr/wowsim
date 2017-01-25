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
            case PRIEST:
                Priest priest = new Priest(intellect);
                addSpellsToMyClass(priest);
                return priest;
            case WARLOCK:
                Warlock warlock = new Warlock(intellect);
                addSpellsToMyClass(warlock);
                return warlock;
            default:
                return null;
        }
    }

    private void addSpellsToMyClass(ClassTemplate classTemplate) {
        for (Spell s : allSpells) {
            if (s.getSpellClass().equals(myClass)) {
                classTemplate.addSpell(s.getName(), s);
                if (s instanceof DirectDamage) {
                    ((DirectDamage) s).setCritChance(myClass.calculateCritChance(intellect, level));
                }
                s.init();
            }
        }
    }

    private void importSpells() {
        allSpells.add(new Corruption(determineSpellLevel(Corruption.levelUps)));
        allSpells.add(new CurseOfAgony(determineSpellLevel(CurseOfAgony.levelUps)));
        allSpells.add(new Shadowbolt(determineSpellLevel(Shadowbolt.levelUps)));
        allSpells.add(new Immolation(determineSpellLevel(Immolation.levelUps)));
    }

    private int determineSpellLevel(List<Integer> levelups) {
        int levelOfSpell = 0;
        for (int i : levelups) {
            if (this.level >= i) {
                levelOfSpell++;
            }
        }
        return levelOfSpell;
    }
}

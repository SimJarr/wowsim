package se.wowsim.classes;

import java.lang.reflect.Constructor;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import se.wowsim.spells.*;
import se.wowsim.spells.types.DirectDamage;
import se.wowsim.spells.types.Spell;

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
                Priest priest = new Priest(level, intellect);
                addSpellsToMyClass(priest);
                return priest;
            case WARLOCK:
                Warlock warlock = new Warlock(level, intellect);
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
                //TODO have critChance be set on a class instead of each spell
                if (s instanceof DirectDamage) {
                    ((DirectDamage) s).setCritChance(myClass.calculateCritChance(level, intellect));
                }
                //TODO end
                s.init();
            }
        }
    }

    private void importSpells() {
        importSpellsHelper("Corruption", Corruption.levelUps);
        importSpellsHelper("CurseOfAgony", CurseOfAgony.levelUps);
        importSpellsHelper("Shadowbolt", Shadowbolt.levelUps);
        importSpellsHelper("Immolate", Immolate.levelUps);
        importSpellsHelper("SearingPain", SearingPain.levelUps);
    }

    private void importSpellsHelper(String spell, List<Integer> integerList) {

        try {
            int rank = determineSpellLevel(integerList);
            if (rank > 0) {
                Constructor mySpell = Class.forName("se.wowsim.spells." + spell).getConstructor(int.class);
                allSpells.add((Spell) mySpell.newInstance(rank));
            }
        } catch (Exception e) {
            throw new RuntimeException(e);        }

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

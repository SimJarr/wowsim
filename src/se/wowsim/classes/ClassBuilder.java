package se.wowsim.classes;

import se.wowsim.spells.*;
import se.wowsim.spells.types.Channeling;
import se.wowsim.spells.types.Spell;
import se.wowsim.talents.Shadow;

import java.lang.reflect.Constructor;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Class used to create a class instance.
 * Example usage: Warlock warlock = (Warlock) new ClassBuilder(Classes.WARLOCK, 36, 127).getClassInstance();
 */
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

    /**
     * Will create an instance of the chosen Class enum, add correct spells and apply talents then return it.
     * @return returns an instance of the chosen Class enum
     */
    public ClassTemplate getClassInstance() {
        switch (myClass) {
            case PRIEST:
                Priest priest = new Priest(level, intellect);
                addSpellsToMyClass(priest);
                priest = (Priest) applyTalentsToPriest(priest);
                return priest;
            case WARLOCK:
                Warlock warlock = new Warlock(level, intellect);
                addSpellsToMyClass(warlock);
                warlock = (Warlock) applyTalentsToWarlock(warlock);
                return warlock;
            default:
                return null;
        }
    }

    /**
     * getClassInstance will use this to add the correct Spells to the classTemplate's list of spells,
     * will also create clones of Spells of the typ Channeling
     * @param classTemplate the current Class
     */
    private void addSpellsToMyClass(ClassTemplate classTemplate) {
        for (Spell spell : allSpells) {
            if (spell.getSpellClass() == myClass) {
                if (spell instanceof Channeling) {
                    cloneChannelingSpell(classTemplate, spell);
                } else {
                    classTemplate.addSpell(spell.getName(), spell);
                    spell.init();
                }
            }
        }
    }

    /**
     * addSpellsToMyClass will use this to create clones of a Channeling spell with different Channeling times for later comparisons
     * @param classTemplate the current class
     * @param spell the Channeling Spell
     */
    private void cloneChannelingSpell(ClassTemplate classTemplate, Spell spell) {
        int tickInterval = ((Channeling) spell).getTickInterval();
        int totalTickNumber = ((Channeling) spell).getChannelTime() / tickInterval;
        Integer timeChanneled;

        for (int i = totalTickNumber; i >= 1; i--) {

            timeChanneled = i * tickInterval;

            try {
                Channeling spellNewInstance = (Channeling) spell.clone();
                spellNewInstance.setChannelTime(timeChanneled);
                classTemplate.addSpell(spell.getName() + " " + timeChanneled, spellNewInstance);
                spellNewInstance.init();
                //System.out.println(currentSpellNewInstance.getName() + " value: " + (currentSpellNewInstance.calculateDamageDealt(target, timeLeft, i * tickInterval)) / timeChanneled + " channelDuration: " + currentSpellNewInstance.getTimeTakenFromCaster());

            } catch (Exception e) {
                throw new RuntimeException(e);
            }

        }
    }

    /**
     * applies talents to the Priest class (only Shadow talents exist)
     * @param classTemplate the current class
     * @return the same classTemplate but now with talents applied
     */
    private ClassTemplate applyTalentsToPriest(ClassTemplate classTemplate) {
        //TODO more talent trees
        return new Shadow(classTemplate, "shadow").getMyClass();
    }

    /**
     * not yet implemented, because there is no talent tree created for Warlock
     * @param classTemplate the current class
     * @return the same classTemplate
     */
    private ClassTemplate applyTalentsToWarlock(ClassTemplate classTemplate) {
        //TODO talent trees
        return classTemplate;
    }

    /**
     * Will add all existing spells to the allSpells List
     */
    private void importSpells() {
        // WARLOCK
        importSpellsHelper("Corruption", Corruption.levelUps);
        importSpellsHelper("CurseOfAgony", CurseOfAgony.levelUps);
        importSpellsHelper("Shadowbolt", Shadowbolt.levelUps);
        importSpellsHelper("Immolate", Immolate.levelUps);
        importSpellsHelper("SearingPain", SearingPain.levelUps);
        importSpellsHelper("SiphonLife", SiphonLife.levelUps);
        importSpellsHelper("DrainLife", DrainLife.levelUps);
        importSpellsHelper("DrainSoul", DrainSoul.levelUps);

        // PRIEST
        importSpellsHelper("MindBlast", MindBlast.levelUps);
        importSpellsHelper("ShadowWordPain", ShadowWordPain.levelUps);
        // PRIEST TALENTS
        importSpellsHelper("MindFlay", MindFlay.levelUps);

    }

    /**
     * Will add the given Spells name to the allSpells List
     * @param spell name of the Spell
     * @param integerList the Spell's list of what levels it will get another rank
     */
    private void importSpellsHelper(String spell, List<Integer> integerList) {

        try {
            int rank = determineSpellRank(integerList);
            if (rank > 0) {
                Constructor mySpell = Class.forName("se.wowsim.spells." + spell).getConstructor(int.class);
                allSpells.add((Spell) mySpell.newInstance(rank));
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    /**
     * Will set the correct rank of a Spell
     * @param levelups the list of when the Spell
     * @return an int which symbolizes the rank of a Spell
     */
    private int determineSpellRank(List<Integer> levelups) {
        int levelOfSpell = 0;
        for (int i : levelups) {
            if (this.level >= i) {
                levelOfSpell++;
            }
        }
        return levelOfSpell;
    }
}

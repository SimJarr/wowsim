package se.wowsim.spells;


import se.wowsim.classes.Classes;
import se.wowsim.spells.types.DirectDamage;

import java.util.Arrays;
import java.util.List;

public final class SearingPain extends DirectDamage {

    public static List<Integer> levelUps = Arrays.asList(18, 26, 34, 42, 50, 58);

    public SearingPain(int rank) {
        super(rank);
        this.spellClass = Classes.WARLOCK;
        this.school = School.FIRE;
    }

    @Override
    public String getName() {
        return "Searing Pain";
    }

    /**
     * initializes the Spell by giving it the the correct baseDamage according to rank/level
     * also sets the other necessary variables
     */
    @Override
    public void init() {
        this.castTime = 15;
        switch (rank) {
            case 1:
                this.baseDamage = calculateDamage(34, 43);
                break;
            case 2:
                this.baseDamage = calculateDamage(59, 72);
                break;
            case 3:
                this.baseDamage = calculateDamage(86, 105);
                break;
            case 4:
                this.baseDamage = calculateDamage(122, 147);
                break;
            case 5:
                this.baseDamage = calculateDamage(158, 189);
                break;
            case 6:
                this.baseDamage = calculateDamage(204, 241);
                break;
            default:
                throw new IllegalArgumentException("Given rank does not exist");
        }
        this.totalDamage = baseDamage;
    }

}

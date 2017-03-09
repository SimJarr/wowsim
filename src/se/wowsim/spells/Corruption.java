package se.wowsim.spells;

import se.wowsim.classes.Classes;
import se.wowsim.spells.types.DamageOverTime;

import java.util.Arrays;
import java.util.List;

public final class Corruption extends DamageOverTime {

    public static List<Integer> levelUps = Arrays.asList(4, 14, 24, 34, 44, 54);

    public Corruption(int rank) {
        super(rank);
        this.spellClass = Classes.WARLOCK;
        this.school = School.SHADOW;
    }

    @Override
    public String getName() {
        return "Corruption";
    }

    /**
     * initializes the Spell by giving it the the correct maxDuration and baseDamage according to rank/level
     * also sets the other necessary variables
     */
    @Override
    public void init() {
        this.maxDuration = 180;
        switch (rank) {
            case 1:
                this.maxDuration = 120;
                this.baseDamage = 40;
                break;
            case 2:
                this.maxDuration = 150;
                this.baseDamage = 90;
                break;
            case 3:
                this.baseDamage = 222;
                break;
            case 4:
                this.baseDamage = 324;
                break;
            case 5:
                this.baseDamage = 486;
                break;
            case 6:
                this.baseDamage = 666;
                break;
            default:
                throw new IllegalArgumentException("Given rank does not exist");
        }
        this.totalDamage = baseDamage;
        this.duration = this.maxDuration;
        this.tickInterval = 30;
        this.tickNumber = 1;
        this.totalTickNumber = duration / tickInterval;
        this.castTime = 20;
    }
}

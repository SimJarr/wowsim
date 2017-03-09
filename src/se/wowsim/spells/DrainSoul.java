package se.wowsim.spells;

import se.wowsim.classes.Classes;
import se.wowsim.spells.types.Channeling;

import java.util.Arrays;
import java.util.List;

public final class DrainSoul extends Channeling {

    public static List<Integer> levelUps = Arrays.asList(10, 24, 38, 52);

    public DrainSoul(int rank) {
        super(rank);
        this.spellClass = Classes.WARLOCK;
        this.school = School.SHADOW;
        this.maxDuration = 150;
        init();
    }

    @Override
    public String getName() {
        return "Drain Soul";
    }

    /**
     * initializes the Spell by giving it the the correct baseDamage according to rank/level
     * also sets the other necessary variables
     */
    @Override
    public void init() {
        switch (rank) {
            case 1:
                this.baseDamage = 55;
                break;
            case 2:
                this.baseDamage = 155;
                break;
            case 3:
                this.baseDamage = 295;
                break;
            case 4:
                this.baseDamage = 455;
                break;
            default:
                throw new IllegalArgumentException("Given rank does not exist");
        }
        this.totalDamage = baseDamage;
        this.duration = this.maxDuration;
        this.tickInterval = 30;
        this.tickNumber = 1;
        this.totalTickNumber = duration / tickInterval;
    }

}

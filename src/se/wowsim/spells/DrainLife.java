package se.wowsim.spells;

import se.wowsim.classes.Classes;
import se.wowsim.spells.types.Channeling;

import java.util.Arrays;
import java.util.List;

public final class DrainLife extends Channeling {

    public static List<Integer> levelUps = Arrays.asList(14, 22, 30, 38, 46, 54);

    public DrainLife(int rank) {
        super(rank);
        this.spellClass = Classes.WARLOCK;
        this.school = School.SHADOW;
        this.maxDuration = 50;
        init();
    }

    @Override
    public String getName() {
        return "Drain Life";
    }

    /**
     * initializes the Spell by giving it the the correct baseDamage according to rank/level
     * also sets the other necessary variables
     */
    @Override
    public void init() {
        switch (rank) {
            case 1:
                this.baseDamage = 50;
                break;
            case 2:
                this.baseDamage = 85;
                break;
            case 3:
                this.baseDamage = 145;
                break;
            case 4:
                this.baseDamage = 205;
                break;
            case 5:
                this.baseDamage = 275;
                break;
            case 6:
                this.baseDamage = 355;
                break;
            default:
                throw new IllegalArgumentException("Given rank does not exist");
        }
        this.totalDamage = baseDamage;
        this.duration = this.maxDuration;
        this.tickInterval = 10;
        this.tickNumber = 1;
        this.totalTickNumber = duration / tickInterval;
    }

}

package se.wowsim.spells;

import java.util.Arrays;
import java.util.List;

import se.wowsim.classes.Classes;
import se.wowsim.spells.types.Channeling;

public final class DrainLife extends Channeling {

    public static List<Integer> levelUps = Arrays.asList(14, 22, 30, 38, 46, 54);

    public DrainLife(int rank) {
        super(rank);
        this.spellClass = Classes.WARLOCK;
    }

    @Override
    public String getName() {
        return "Drain Life";
    }

    @Override
    public void init() {
        switch (rank) {
            case 1:
                this.totalDamage = 50;
                break;
            case 2:
                this.totalDamage = 85;
                break;
            case 3:
                this.totalDamage = 145;
                break;
            case 4:
                this.totalDamage = 205;
                break;
            case 5:
                this.totalDamage = 275;
                break;
            case 6:
                this.totalDamage = 355;
                break;
            default:
                throw new IllegalArgumentException("Given rank does not exist");
        }
        this.maxDuration = 50;
        this.duration = this.maxDuration;
        this.tickInterval = 10;
        this.tickNumber = 1;
        this.totalTickNumber = duration / tickInterval;
        this.castTime = 0;
    }

}

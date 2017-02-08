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
    }

    @Override
    public String getName() {
        return "Drain Soul";
    }

    @Override
    public void init() {
        switch (rank) {
            case 1:
                this.totalDamage = 55;
                break;
            case 2:
                this.totalDamage = 155;
                break;
            case 3:
                this.totalDamage = 295;
                break;
            case 4:
                this.totalDamage = 455;
                break;
            default:
                throw new IllegalArgumentException("Given rank does not exist");
        }
        this.maxDuration = 150;
        this.duration = this.maxDuration;
        this.tickInterval = 30;
        this.tickNumber = 1;
        this.totalTickNumber = duration / tickInterval;
    }

}

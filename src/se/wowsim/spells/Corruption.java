package se.wowsim.spells;

import java.util.Arrays;
import java.util.List;

import se.wowsim.Observer;
import se.wowsim.classes.Classes;
import se.wowsim.spells.types.DamageOverTime;

public final class Corruption extends DamageOverTime implements Observer {

    public static List<Integer> levelUps = Arrays.asList(4, 14, 24, 34, 44, 54);

    public Corruption(int rank) {
        super(rank);
        this.spellClass = Classes.WARLOCK;
    }

    @Override
    public String getName() {
        return "Corruption";
    }

    @Override
    public void init() {
        this.maxDuration = 180;
        switch (rank) {
            case 1:
                this.maxDuration = 120;
                this.totalDamage = 40;
                break;
            case 2:
                this.maxDuration = 150;
                this.totalDamage = 90;
                break;
            case 3:
                this.totalDamage = 222;
                break;
            case 4:
                this.totalDamage = 324;
                break;
            case 5:
                this.totalDamage = 486;
                break;
            case 6:
                this.totalDamage = 666;
                break;
            default:
                throw new IllegalArgumentException("Given rank does not exist");
        }
        this.duration = this.maxDuration;
        this.tickInterval = 30;
        this.tickNumber = 1;
        this.totalTickNumber = duration / tickInterval;
        this.castTime = 20;
    }
}

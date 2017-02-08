package se.wowsim.spells;

import se.wowsim.classes.Classes;
import se.wowsim.spells.types.DamageOverTime;

import java.util.Arrays;
import java.util.List;

public final class SiphonLife extends DamageOverTime {

    public static List<Integer> levelUps = Arrays.asList(30, 38, 48, 58);

    public SiphonLife(int rank) {
        super(rank);
        this.spellClass = Classes.WARLOCK;
    }

    @Override
    public String getName() {
        return "Siphon Life";
    }

    @Override
    public void init() {
        switch (rank) {
            case 1:
                this.totalDamage = 150;
                break;
            case 2:
                this.totalDamage = 220;
                break;
            case 3:
                this.totalDamage = 330;
                break;
            case 4:
                this.totalDamage = 450;
                break;
            default:
                throw new IllegalArgumentException("Given rank does not exist");
        }
        this.maxDuration = 300;
        this.duration = this.maxDuration;
        this.tickInterval = 30;
        this.tickNumber = 1;
        this.totalTickNumber = duration / tickInterval;
        this.castTime = 0;
    }
}

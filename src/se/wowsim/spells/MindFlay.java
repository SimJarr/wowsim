package se.wowsim.spells;

import se.wowsim.classes.Classes;
import se.wowsim.spells.types.Channeling;

import java.util.Arrays;
import java.util.List;

public final class MindFlay extends Channeling {

    public static List<Integer> levelUps = Arrays.asList(20, 28, 36, 44, 52, 60);

    public MindFlay(int rank) {
        super(rank);
        this.spellClass = Classes.PRIEST;
        this.school = School.SHADOW;
        this.channelTime = 30;
        this.tickInterval = 10;
    }

    @Override
    public String getName() {
        return "Mind Flay";
    }

    @Override
    public void init() {
        switch (rank) {
            case 1:
                this.baseDamage = 75;
                break;
            case 2:
                this.baseDamage = 126;
                break;
            case 3:
                this.baseDamage = 186;
                break;
            case 4:
                this.baseDamage = 261;
                break;
            case 5:
                this.baseDamage = 330;
                break;
            case 6:
                this.baseDamage = 426;
                break;
            default:
                throw new IllegalArgumentException("Given rank does not exist");
        }
        this.maxDuration = 30;
        this.duration = this.channelTime;
        this.totalDamage = ((baseDamage / (maxDuration / tickInterval)) * (channelTime / 10));
        this.tickNumber = 1;
        this.totalTickNumber = duration / tickInterval;
    }

}

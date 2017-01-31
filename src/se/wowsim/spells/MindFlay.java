package se.wowsim.spells;

import java.util.Arrays;
import java.util.List;

import se.wowsim.Observer;
import se.wowsim.classes.Classes;
import se.wowsim.spells.types.Channeling;

public final class MindFlay extends Channeling implements Observer {

    public static List<Integer> levelUps = Arrays.asList(20, 28, 36, 44, 52, 60);

    public MindFlay(int rank) {
        super(rank);
        this.spellClass = Classes.PRIEST;
    }

    @Override
    public void applySpell() {
        this.duration = this.maxDuration;
        this.tickNumber = 1;
        target.register(this);
    }

    @Override
    public void update() {
        this.duration--;
        if ((duration % tickInterval == 0) && duration != maxDuration) {
            System.out.println("MindFlay tick(" + tickNumber + "/" + totalTickNumber + "): "
                    + totalDamage / totalTickNumber + " damage");
            tickNumber++;
        }
        if (duration == 0) {
            target.unregister(this);
        }
    }

    @Override
    public void init() {
        this.maxDuration = 30;
        switch (rank) {
            case 1:
                this.totalDamage = 75;
                break;
            case 2:
                this.totalDamage = 126;
                break;
            case 3:
                this.totalDamage = 186;
                break;
            case 4:
                this.totalDamage = 330;
                break;
            case 5:
                this.totalDamage = 426;
                break;
            default:
                throw new IllegalArgumentException("Given rank does not exist");
        }
        this.duration = this.maxDuration;
        this.tickInterval = 10;
        this.tickNumber = 1;
        this.totalTickNumber = duration / tickInterval;
        this.castTime = 0;
    }

    @Override
    public String getName() {
        return "Mind Flay";
    }
}

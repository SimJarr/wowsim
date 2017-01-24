package se.wowsim.spells;

import java.util.Arrays;
import java.util.List;

import se.wowsim.Observer;
import se.wowsim.Target;
import se.wowsim.classes.Classes;

public final class CurseOfAgony extends DamageOverTime implements Observer {

    private int tickNumber;
    private int totalTickNumber;
    private Target target;
    public static List<Integer> levelUps = Arrays.asList(8, 18, 28, 38, 48, 58);

    public CurseOfAgony(int rank) {
        super(rank);
        init(rank);
        this.spellClass = Classes.WARLOCK;
    }

    @Override
    public void applySpell() {
    	init(rank);
        target.register(this);
    }

    @Override
    public void setTarget(Target target) {
        this.target = target;
    }

    @Override
    public double getDotDamage(int timeLeft) {

        int timeAfterCast = (castTime <= 15) ? timeLeft - 15 : timeLeft - castTime;
        double damage = 0;
        int tickNumber = 1;
        for (int i = 1; i < timeAfterCast; i++) {
            if (i % tickInterval == 0) {
                if (tickNumber <= 4) { damage += (totalDamage / totalTickNumber) * 0.5; }
                else if (tickNumber > 4 && tickNumber <= 8) { damage += (totalDamage / totalTickNumber); }
                else if (tickNumber > 8 && tickNumber <= 12)  { damage += (totalDamage / totalTickNumber) * 1.5; }
                tickNumber++;
            }
        }

        return damage;

    }

    @Override
    public void update() {
        this.duration--;

        if ((duration % tickInterval == 0) && duration != maxDuration) {

            double tickDamage = 0.0;

            if (tickNumber <= 4) { tickDamage = (totalDamage / totalTickNumber) * 0.5; }
            else if (tickNumber > 4 && tickNumber <= 8) { tickDamage = (totalDamage / totalTickNumber); }
            else if (tickNumber > 8 && tickNumber <= 12)  { tickDamage = (totalDamage / totalTickNumber) * 1.5; }

            System.out.println("CurseOfAgony tick(" + tickNumber + "/" + totalTickNumber + "): " + tickDamage + " damage");
            tickNumber++;
        }
        if (duration == 0) {
            target.unregister(this);
        }
    }

    @Override
    public String getName() {
        return "CurseOfAgony";
    }

    private void init(int rank) {
        switch (rank) {
            case 3:
                this.duration = 240;
                this.maxDuration = 240;
                this.totalDamage = 324;
                break;
            default:
                throw new IllegalArgumentException("Given rank does not exist");
        }
        this.tickInterval = 20;
        this.tickNumber = 1;
        this.totalTickNumber = duration / tickInterval;
        this.castTime = 0;
    }
}
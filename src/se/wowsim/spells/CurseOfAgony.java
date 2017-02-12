package se.wowsim.spells;

import se.wowsim.classes.Classes;
import se.wowsim.spells.types.DamageOverTime;

import java.util.Arrays;
import java.util.List;

public final class CurseOfAgony extends DamageOverTime {

    public static List<Integer> levelUps = Arrays.asList(8, 18, 28, 38, 48, 58);

    public CurseOfAgony(int rank) {
        super(rank);
        this.spellClass = Classes.WARLOCK;
        this.school = School.SHADOW;
    }

    @Override
    public double calculateDotDamage(int timeLeft) {

        double damage = 0;
        int tickNumber = 1;
        for (int i = 1; i <= timeLeft; i++) {
            if (i % tickInterval == 0) {
                if (tickNumber <= 4) {
                    damage += (totalDamage / totalTickNumber) * 0.5;
                } else if (tickNumber > 4 && tickNumber <= 8) {
                    damage += (totalDamage / totalTickNumber);
                } else if (tickNumber > 8 && tickNumber <= 12) {
                    damage += (totalDamage / totalTickNumber) * 1.5;
                }
                tickNumber++;
            }
        }

        return damage;

    }

    @Override
    public void update() {

        if ((duration % tickInterval == 0) && duration != maxDuration) {

            double tickDamage = 0.0;

            if (tickNumber <= 4) {
                tickDamage = (totalDamage / totalTickNumber) * 0.5;
            } else if (tickNumber > 4 && tickNumber <= 8) {
                tickDamage = (totalDamage / totalTickNumber);
            } else if (tickNumber > 8 && tickNumber <= 12) {
                tickDamage = (totalDamage / totalTickNumber) * 1.5;
            }

            System.out.println(getName() + " tick(" + tickNumber + "/" + totalTickNumber + "): " + tickDamage + " damage");
            tickNumber++;
        }

        if (oneMoreTick) {
            oneMoreTick = false;
            System.out.println(getName() + " tick(" + totalTickNumber + "/" + totalTickNumber + "): " + (totalDamage / totalTickNumber) * 1.5 + " damage");
        }

        if (duration == 0) {
            target.unregister(this);
        }

        this.duration--;
    }

    @Override
    public String getName() {
        return "CurseOfAgony";
    }

    @Override
    public void init() {
        switch (rank) {
            case 1:
                this.baseDamage = 84;
                break;
            case 2:
                this.baseDamage = 180;
                break;
            case 3:
                this.baseDamage = 324;
                break;
            case 4:
                this.baseDamage = 504;
                break;
            case 5:
                this.baseDamage = 780;
                break;
            case 6:
                this.baseDamage = 1044;
                break;
            default:
                throw new IllegalArgumentException("Given rank does not exist");
        }
        this.totalDamage = baseDamage;
        this.maxDuration = 240;
        this.duration = this.maxDuration;
        this.tickInterval = 20;
        this.tickNumber = 1;
        this.totalTickNumber = duration / tickInterval;
        this.castTime = 0;
    }
}

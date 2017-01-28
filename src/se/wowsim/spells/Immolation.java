package se.wowsim.spells;

import java.util.Arrays;
import java.util.List;

import se.wowsim.Observer;
import se.wowsim.Target;
import se.wowsim.classes.Classes;

public final class Immolation extends DirectDamage implements Observer {

    private ImmolationDot immolationDot;
    private Target target;
    public static List<Integer> levelUps = Arrays.asList(1, 10, 20, 30, 40, 50, 60);

    public Immolation(int rank) {
        super(rank);
        this.immolationDot = new ImmolationDot(rank);
        this.spellClass = Classes.WARLOCK;
    }

    public ImmolationDot getImmolationDot() {
        return immolationDot;
    }

    @Override
    public void applySpell() {
        target.register(this);
        this.immolationDot.applySpell();
    }

    @Override
    public void setTarget(Target target) {
        this.target = target;
        this.immolationDot.setTarget(target);
    }

    @Override
    public String getName() {
        return "Immolation";
    }

    @Override
    public void update() {
        target.unregister(this);
        System.out.println(getName() + " dealt " + (int) totalDamage + " damage");
    }

    @Override
    public void init() {
        this.castTime = 20;
        switch (rank) {
            case 1:
                this.totalDamage = calculateDamage(8);
                break;
            case 2:
                this.totalDamage = calculateDamage(19);
                break;
            case 3:
                this.totalDamage = calculateDamage(45);
                break;
            case 4:
                this.totalDamage = calculateDamage(90);
                break;
            case 5:
                this.totalDamage = calculateDamage(134);
                break;
            case 6:
                this.totalDamage = calculateDamage(192);
                break;
            case 7:
                this.totalDamage = calculateDamage(258);
                break;
            default:
                throw new IllegalArgumentException("Given rank does not exist");
        }
    }

    private double calculateDamage(double damage) {
        return damage * (1 - critChance) + (damage * critMulti) * critChance;
    }

    @Override
    public double calculateDamageDealt(Target target, int timeleft) {
        double damageDealt = 0;
        if (target.notAffected(this.getImmolationDot())) {
            damageDealt += this.getTotalDamage();
            damageDealt += this.getImmolationDot().calculateDotDamage(timeleft);
        }
        return damageDealt / this.getCastTime();
    }

    private final class ImmolationDot extends DamageOverTime implements Observer {

        private int tickNumber;
        private int totalTickNumber;
        private Target target;

        public ImmolationDot(int rank) {
            super(rank);
            init();
        }

        @Override
        public void applySpell() {
            this.duration = this.maxDuration;
            this.tickNumber = 1;
            init();
            target.register(this);
        }

        @Override
        public void setTarget(Target target) {
            this.target = target;
        }

        @Override
        public String getName() {
            return "ImmolationDot";
        }

        @Override
        public void update() {
            this.duration--;
            if ((duration % tickInterval == 0) && duration != maxDuration) {
                System.out.println("Immolation tick(" + tickNumber + "/" + totalTickNumber + "): " + totalDamage / totalTickNumber + " damage");
                tickNumber++;
            }
            if (duration == 0) {
                target.unregister(this);
            }
        }

        @Override
        public void init() {
            switch (rank) {
                case 1:
                    this.totalDamage = 20;
                    break;
                case 2:
                    this.totalDamage = 40;
                    break;
                case 3:
                    this.totalDamage = 90;
                    break;
                case 4:
                    this.totalDamage = 165;
                    break;
                case 5:
                    this.totalDamage = 255;
                    break;
                case 6:
                    this.totalDamage = 365;
                    break;
                case 7:
                    this.totalDamage = 485;
                default:
                    throw new IllegalArgumentException("Given rank does not exist");
            }
            this.maxDuration = 150;
            this.duration = this.maxDuration;
            this.tickInterval = 30;
            this.tickNumber = 1;
            this.totalTickNumber = duration / tickInterval;
            this.castTime = 0;
        }
    }
}

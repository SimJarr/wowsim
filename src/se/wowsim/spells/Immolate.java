package se.wowsim.spells;

import se.wowsim.Target;
import se.wowsim.classes.Classes;
import se.wowsim.spells.types.DamageOverTime;
import se.wowsim.spells.types.DirectDamage;

import java.util.Arrays;
import java.util.List;

public final class Immolate extends DirectDamage {

    private ImmolateDot immolateDot;
    public static List<Integer> levelUps = Arrays.asList(1, 10, 20, 30, 40, 50, 60);

    public Immolate(int rank) {
        super(rank);
        this.immolateDot = new ImmolateDot(rank);
        this.spellClass = Classes.WARLOCK;
    }

    public ImmolateDot getImmolateDot() {
        return immolateDot;
    }

    @Override
    public void applySpell() {
        target.register(this);
        this.immolateDot.applySpell();
    }

    @Override
    public void setTarget(Target target) {
        this.target = target;
        this.immolateDot.setTarget(target);
    }

    @Override
    public String getName() {
        return "Immolate";
    }

    @Override
    public void init() {
        this.castTime = 20;
        immolateDot.setCastTime(this.castTime);
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
        if (this.getCastTime() > timeleft) {
            return 0.0;
        }
        if (target.notAffected(this.getImmolateDot())) {
            damageDealt += this.getTotalDamage();
            damageDealt += this.getImmolateDot().calculateDotDamage(timeleft);
        }

        // Here we tried to implement being able to overwrite immolate.
        // Conclusion: it is hard to compare this value with direct damage and/or anything else really.
        /*else {

            int dotUptime = (this.getImmolateDot().getMaxDuration() - this.getImmolateDot().getDuration());
            int maxDuration = (this.getImmolateDot().getMaxDuration() < timeleft) ? this.getImmolateDot().getMaxDuration() : timeleft;
            int tickInterval = this.getImmolateDot().getTickInterval();
            int ticksLeft = 0;
            for (int i = dotUptime + 1; i <= maxDuration; i++){
                if (i % tickInterval == 0){
                    ticksLeft++;
                }
            }
            double tickDamage = (this.getImmolateDot().getTotalDamage() / this.getImmolateDot().getTotalTickNumber());
            double totalDamageLeft = tickDamage * ticksLeft;

            damageDealt += this.getTotalDamage();
            damageDealt += this.getImmolateDot().calculateDotDamage(timeleft);
            damageDealt -= totalDamageLeft;

        }*/
        return damageDealt;
    }

    private final class ImmolateDot extends DamageOverTime {

        public ImmolateDot(int rank) {
            super(rank);
            init();
        }

        @Override
        public String getName() {
            return "ImmolateDot";
        }

        private void setCastTime(int castTime) {
            this.castTime = castTime;
        }

        public int getTotalTickNumber() {
            return this.totalTickNumber;
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
}

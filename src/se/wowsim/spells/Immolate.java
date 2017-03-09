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
        this.school = School.FIRE;
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

    /**
     * initializes the Spell by giving it the the correct baseDamage according to rank/level
     * also sets the other necessary variables and the DamageOverTime part of Immolate
     */
    @Override
    public void init() {
        this.castTime = 20;
        immolateDot.setCastTime(this.castTime);
        switch (rank) {
            case 1:
                this.baseDamage = calculateDamage(8, 8);
                break;
            case 2:
                this.baseDamage = calculateDamage(19, 19);
                break;
            case 3:
                this.baseDamage = calculateDamage(45, 45);
                break;
            case 4:
                this.baseDamage = calculateDamage(90, 90);
                break;
            case 5:
                this.baseDamage = calculateDamage(134, 134);
                break;
            case 6:
                this.baseDamage = calculateDamage(192, 192);
                break;
            case 7:
                this.baseDamage = calculateDamage(258, 258);
                break;
            default:
                throw new IllegalArgumentException("Given rank does not exist");
        }
        this.totalDamage = baseDamage;
    }

    /**
     * Immolate have a DamageOverTime component that applies at the same time
     * @param target the target
     * @param timeLeft the time we have to work with
     * @return damage the Spell will do
     */
    @Override
    public double calculateDamageDealt(Target target, int timeLeft) {
        double damageDealt = 0;
        if (this.getCastTime() > timeLeft) {
            return 0.0;
        }
        if (target.notAffected(this.getImmolateDot())) {
            damageDealt += this.getTotalDamage();
            damageDealt += this.getImmolateDot().calculateDotDamage(timeLeft);
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

    /**
     * a class inside the Immolate class that explains the ImmolateDamageOverTime
     */
    private final class ImmolateDot extends DamageOverTime {

        public ImmolateDot(int rank) {
            super(rank);
            this.school = School.FIRE;
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

        /**
         * initializes the Spell by giving it the the correct baseDamage according to rank/level
         * also sets the other necessary variables
         */
        @Override
        public void init() {
            switch (rank) {
                case 1:
                    this.baseDamage = 20;
                    break;
                case 2:
                    this.baseDamage = 40;
                    break;
                case 3:
                    this.baseDamage = 90;
                    break;
                case 4:
                    this.baseDamage = 165;
                    break;
                case 5:
                    this.baseDamage = 255;
                    break;
                case 6:
                    this.baseDamage = 365;
                    break;
                case 7:
                    this.baseDamage = 485;
                    break;
                default:
                    throw new IllegalArgumentException("Given rank does not exist");
            }
            this.totalDamage = baseDamage;
            this.maxDuration = 150;
            this.duration = this.maxDuration;
            this.tickInterval = 30;
            this.tickNumber = 1;
            this.totalTickNumber = duration / tickInterval;
        }
    }
}

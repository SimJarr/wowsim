package se.wowsim.spells;

import java.util.Arrays;
import java.util.List;

import se.wowsim.Observer;
import se.wowsim.Target;
import se.wowsim.classes.Classes;

public final class Shadowbolt extends DirectDamage implements Observer {

    private Target target;
    public static List<Integer> levelUps = Arrays.asList(1, 6, 12, 20, 28, 36, 44, 52, 60);

    public Shadowbolt(int rank) {
        super(rank);
        this.spellClass = Classes.WARLOCK;
    }

    @Override
    public void applySpell() {
        target.register(this);
    }

    @Override
    public void setTarget(Target target) {
        this.target = target;
    }

    @Override
    public void update() {
        target.unregister(this);
        System.out.println(getName() + " dealt " + (int) totalDamage + " damage");
    }

    @Override
    public String getName() {
        return "Shadowbolt";
    }

    @Override
    public void init() {
        this.castTime = 30;
        switch (rank) {
            case 1:
                this.totalDamage = calculateDamage(12, 17);
                this.castTime = 17;
                break;
            case 2:
                this.totalDamage = calculateDamage(23, 30);
                this.castTime = 22;
                break;
            case 3:
                this.totalDamage = calculateDamage(48, 57);
                this.castTime = 28;
                break;
            case 4:
                this.totalDamage = calculateDamage(86, 99);
                break;
            case 5:
                this.totalDamage = calculateDamage(142, 163);
                break;
            case 6:
                this.totalDamage = calculateDamage(204, 231);
                break;
            case 7:
                this.totalDamage = calculateDamage(281, 316);
                break;
            case 8:
                this.totalDamage = calculateDamage(360, 403);
                break;
            case 9:
                this.totalDamage = calculateDamage(455, 508);
                break;
            default:
                throw new IllegalArgumentException("Given rank does not exist");
        }

    }

    private double calculateDamage(double minDamage, double maxDamage) {
        return ((((minDamage + maxDamage) / 2) * (1 - critChance)) + ((((minDamage + maxDamage) / 2) * critMulti) * critChance));
    }
}

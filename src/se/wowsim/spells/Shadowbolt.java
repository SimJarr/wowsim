package se.wowsim.spells;

import java.util.Arrays;
import java.util.List;

import se.wowsim.Observer;
import se.wowsim.Target;
import se.wowsim.classes.Classes;

public final class Shadowbolt extends DirectDamage implements Observer {

    private double critChance;
    private double critMulti;
    private Target target;
    public static List<Integer> levelUps = Arrays.asList(1, 6, 12, 20, 28, 36, 44, 52, 60);

    public Shadowbolt(int rank) {
    	super(rank);
    	init(rank);
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
        System.out.println(getName() + " dealt " + (int)totalDamage + " damage");
    }

    @Override
    public String getName() {
        return "Shadowbolt";
    }

    private void init(int rank) {
        switch(rank) {
		case 5:
			this.totalDamage = calculateDamage(152.5);
			this.castTime = 30;
			break;
		default:
			break;
		}
        this.critChance = 0.05;
        this.critMulti = 1.5;
    }

    private double calculateDamage(double avgDamage) {
        return ((avgDamage * (1 - critChance)) + ((avgDamage * critMulti) * critChance));
    }
}

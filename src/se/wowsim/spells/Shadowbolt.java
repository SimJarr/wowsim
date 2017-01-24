package se.wowsim.spells;

import se.wowsim.Observer;
import se.wowsim.Target;

public final class Shadowbolt extends DirectDamage implements Observer {

    private double critChance;
    private double critMulti;
    private Target target;

    public Shadowbolt(int rank) {
    	init(rank);
    }

    public Shadowbolt(Target t, int rank) {
    	this.target = t;
        init(rank);
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

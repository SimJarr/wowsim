package se.wowsim.spells;

import se.wowsim.Observer;
import se.wowsim.Subject;

public class Shadowbolt extends DirectDamage implements Observer {

    private double totalDamage;
    private double critChance;
    private double critMulti;
    private Subject subject;

    public Shadowbolt(int rank) {
    	init(rank);
    }
    
    public Shadowbolt(Subject s, int rank) {
    	this.subject = s;
        s.register(this);
        init(rank);
    }

    @Override
    public void update() {
        subject.unregister(this);
        System.out.println(getName() + " dealt " + (int)totalDamage + " damage");
    }

    @Override
    public String getName() {
        return "Shadowbolt";
    }
    
    private void init(int rank) {
        this.critChance = 0.05;
        this.critMulti = 1.5; 
        switch(rank) {
		case 5:
			this.totalDamage = calculateDamage(152.5);
			break;
		default:
			break;
		}
    }

    private double calculateDamage(double avgDamage) {
        return ((avgDamage * (1 - critChance)) + ((avgDamage * critMulti) * critChance));
    }
}

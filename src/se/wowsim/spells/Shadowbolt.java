package se.wowsim.spells;

import se.wowsim.Observer;
import se.wowsim.Subject;

public class Shadowbolt extends DirectDamage implements Observer {

    private double totalDamage;
    private double critChance;
    private double critMulti;
<<<<<<< HEAD
    private Subject subject;
=======
    private Subject s;
>>>>>>> c634c9c12735a51038ea7f26ae206960d82469e5

    public Shadowbolt(int rank) {
    	init(rank);
    }
    
    public Shadowbolt(Subject s, int rank) {
    	this.subject = s;
        s.register(this);
<<<<<<< HEAD
        init(rank);
=======
        this.s = s;
        this.critChance = 0.05;
        this.critMulti = 1.5;
        this.totalDamage = calculateDamage(150);
>>>>>>> c634c9c12735a51038ea7f26ae206960d82469e5
    }

    @Override
    public void update() {
<<<<<<< HEAD
        System.out.println(getName() + " dealt " + totalDamage + " damage");
        subject.unregister(this);
=======
        s.unregister(this);
        System.out.println(getName() + " dealt " + (int)totalDamage + " damage");
>>>>>>> c634c9c12735a51038ea7f26ae206960d82469e5
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

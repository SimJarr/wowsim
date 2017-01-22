package se.wowsim.spells;

import se.wowsim.Observer;
import se.wowsim.Subject;

public class Shadowbolt extends DirectDamage implements Observer{

    private double totalDamage;
    private double critChance;
    private double critMulti;

    public Shadowbolt(Subject s){
        s.register(this);
        this.critChance = 0.05;
        this.critMulti = 1.5;
        this.totalDamage = calculateDamage(150);
    }

    @Override
    public void update() {

    }

    @Override
    public String getName() {
        return null;
    }

    private double calculateDamage(int avgDamage){
        return ((avgDamage * (1 - critChance)) + ((avgDamage * critMulti) * critChance));
    }
}

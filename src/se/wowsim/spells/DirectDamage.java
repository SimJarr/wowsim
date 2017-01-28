package se.wowsim.spells;


import java.text.DecimalFormat;
import java.text.NumberFormat;

public abstract class DirectDamage extends Spell {

    protected double totalDamage;
    protected double critChance;
    protected double critMulti = 1.5;

    public DirectDamage(int rank) {
        super(rank);
    }

    public double getTotalDamage() {
        return totalDamage;
    }

    public void setCritChance(double critChance) {
        NumberFormat nf = new DecimalFormat("#0.000");
        this.critChance = Double.parseDouble(nf.format(critChance).replaceAll(",", "."));
    }

    public abstract void init();
}

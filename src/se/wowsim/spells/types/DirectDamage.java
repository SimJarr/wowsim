package se.wowsim.spells.types;


import java.text.DecimalFormat;
import java.text.NumberFormat;

public abstract class DirectDamage extends Spell {

    protected double critChance;
    protected double critMulti = 1.5;

    public DirectDamage(int rank) {
        super(rank);
    }

    public void setCritChance(double critChance) {
        NumberFormat nf = new DecimalFormat("#0.000");
        this.critChance = Double.parseDouble(nf.format(critChance).replaceAll(",", "."));
    }

    /**
     * calculates how much damage this Spell would do, using the critChance and critMultiplier
     * @param minDamage lowest damage
     * @param maxDamage highest damage
     * @return the average damage of the Spell
     */
    protected double calculateDamage(double minDamage, double maxDamage) {
        return ((((minDamage + maxDamage) / 2) * (1 - critChance)) + ((((minDamage + maxDamage) / 2) * critMulti) * critChance));
    }

    /**
     * applies the Spell to the target.
     * since it is a DirectDamage it deals it's full damage directly
     */
    public void applySpell() {
        this.cooldown = this.maxCooldown;
        System.out.println(getName() + " dealt " + (int) totalDamage + " damage");
        damageDoneDuringSim += totalDamage;
        target.register(this);
    }

    public void update() {
        target.unregister(this);
    }
}

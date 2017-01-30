package se.wowsim.classes;

public final class Warlock extends ClassTemplate {

    public Warlock(int level, int intellect) {
        super(level, intellect);
        this.myClass = Classes.WARLOCK;
        this.critChance = myClass.calculateCritChance(level, intellect);
        System.out.println("mr wherlocks crit: " + this.critChance);
    }

}
package se.wowsim.classes;

public final class Priest extends ClassTemplate {

    public Priest(int intellect, int level) {
        super(intellect, level);
        this.myClass = Classes.PRIEST;
        this.critChance = myClass.calculateCritChance(level, intellect);
    }

}
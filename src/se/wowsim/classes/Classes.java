package se.wowsim.classes;

public enum Classes {

    /**
     * Some constants for each Class
     * Used to calculate base critChance and how much intellect you need for 1% critChance
     */
    DRUID(1.85, 60.0),
    MAGE(0.91, 59.5),
    PALADIN(3.336, 54),
    PRIEST(1.24, 59.2),
    SHAMAN(2.2, 59.2),
    WARLOCK(1.701, 60.6);

    private final double critConst;
    private final double intPerOneCrit;

    /**
     * @param critConst the base critChance
     * @param intPerOneCrit how much intellect you need for 1% critChance
     */
    Classes(double critConst, double intPerOneCrit) {
        this.critConst = critConst;
        this.intPerOneCrit = intPerOneCrit;
    }

    /**
     * Calculates the critChance using the constants, level and current intellect
     * @param level the level of the Class
     * @param intellect current intellect of the Class
     * @return in decimal the chance to crit
     */
    public double calculateCritChance(int level, int intellect) {
        return (intellect / (((this.intPerOneCrit) / 60) * level) + this.critConst) / 100;
    }
}
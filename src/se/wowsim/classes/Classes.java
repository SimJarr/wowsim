package se.wowsim.classes;

public enum Classes {
    DRUID(1.85, 60.0),
    MAGE(0.91, 59.5),
    PALADIN(3.336, 54),
    PRIEST(1.24, 59.2),
    SHAMAN(2.2, 59.2),
    WARLOCK(1.701, 60.6);

    private final double critConst;
    private final double intPerOneCrit;

    Classes(double critConst, double intPerOneCrit) {
        this.critConst = critConst;
        this.intPerOneCrit = intPerOneCrit;
    }

    public double calculateCritChance(int level, int intellect) {
        return (intellect / (((this.intPerOneCrit) / 60) * level) + this.critConst) / 100;
    }
}
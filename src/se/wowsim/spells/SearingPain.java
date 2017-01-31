package se.wowsim.spells;


import se.wowsim.Observer;
import se.wowsim.classes.Classes;
import se.wowsim.spells.types.DirectDamage;

import java.util.Arrays;
import java.util.List;

public final class SearingPain extends DirectDamage implements Observer {

    public static List<Integer> levelUps = Arrays.asList(18, 26, 34, 42, 50, 58);

    public SearingPain(int rank) {
        super(rank);
        this.spellClass = Classes.WARLOCK;
    }

    @Override
    public void applySpell() {
        target.register(this);
    }
    
    @Override
    public void update() {
        target.unregister(this);
        System.out.println(getName() + " dealt " + (int) totalDamage + " damage");
    }

    @Override
    public String getName() {
        return "Searing Pain";
    }

    @Override
    public void init() {
        this.castTime = 15;
        switch (rank) {
            case 1:
                this.totalDamage = calculateDamage(34, 43);
                break;
            case 2:
                this.totalDamage = calculateDamage(59, 72);
                break;
            case 3:
                this.totalDamage = calculateDamage(86, 105);
                break;
            case 4:
                this.totalDamage = calculateDamage(122, 147);
                break;
            case 5:
                this.totalDamage = calculateDamage(158, 189);
                break;
            case 6:
                this.totalDamage = calculateDamage(204, 241);
                break;
            default:
                throw new IllegalArgumentException("Given rank does not exist");
        }

    }

}

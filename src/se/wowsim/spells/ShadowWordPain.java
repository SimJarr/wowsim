package se.wowsim.spells;

import se.wowsim.classes.Classes;
import se.wowsim.spells.types.DamageOverTime;

import java.util.Arrays;
import java.util.List;

public final class ShadowWordPain extends DamageOverTime {

    public static List<Integer> levelUps = Arrays.asList(4, 10, 18, 26, 34, 42, 50, 58);

    public ShadowWordPain(int rank) {
        super(rank);
        this.spellClass = Classes.PRIEST;
        this.school = School.SHADOW;
    }

    @Override
    public String getName() {
        return "Shadow Word: Pain";
    }

    /**
     * initializes the Spell by giving it the the correct baseDamage according to rank/level
     * also sets the other necessary variables
     */
    @Override
    public void init() {
        switch (rank) {
            case 1:
                this.baseDamage = 30;
                break;
            case 2:
                this.baseDamage = 66;
                break;
            case 3:
                this.baseDamage = 132;
                break;
            case 4:
                this.baseDamage = 234;
                break;
            case 5:
                this.baseDamage = 366;
                break;
            case 6:
                this.baseDamage = 510;
                break;
            case 7:
                this.baseDamage = 672;
                break;
            case 8:
                this.baseDamage = 852;
                break;
            default:
                throw new IllegalArgumentException("Given rank does not exist");
        }
        this.totalDamage = baseDamage;
        this.maxDuration = 180;
        this.duration = this.maxDuration;
        this.tickInterval = 30;
        this.tickNumber = 1;
        this.totalTickNumber = duration / tickInterval;
        this.castTime = 0;
    }

}

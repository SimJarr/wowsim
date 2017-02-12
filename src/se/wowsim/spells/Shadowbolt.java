package se.wowsim.spells;

import se.wowsim.classes.Classes;
import se.wowsim.spells.types.DirectDamage;

import java.util.Arrays;
import java.util.List;

public final class Shadowbolt extends DirectDamage {

    public static List<Integer> levelUps = Arrays.asList(1, 6, 12, 20, 28, 36, 44, 52, 60);

    public Shadowbolt(int rank) {
        super(rank);
        this.spellClass = Classes.WARLOCK;
        this.school = School.SHADOW;
    }

    @Override
    public String getName() {
        return "Shadowbolt";
    }

    @Override
    public void init() {
        this.castTime = 30;
        switch (rank) {
            case 1:
                this.baseDamage = calculateDamage(12, 17);
                this.castTime = 17;
                break;
            case 2:
                this.baseDamage = calculateDamage(23, 30);
                this.castTime = 22;
                break;
            case 3:
                this.baseDamage = calculateDamage(48, 57);
                this.castTime = 28;
                break;
            case 4:
                this.baseDamage = calculateDamage(86, 99);
                break;
            case 5:
                this.baseDamage = calculateDamage(142, 163);
                break;
            case 6:
                this.baseDamage = calculateDamage(204, 231);
                break;
            case 7:
                this.baseDamage = calculateDamage(281, 316);
                break;
            case 8:
                this.baseDamage = calculateDamage(360, 403);
                break;
            case 9:
                this.baseDamage = calculateDamage(455, 508);
                break;
            default:
                throw new IllegalArgumentException("Given rank does not exist");
        }
        this.totalDamage = baseDamage;
    }

}

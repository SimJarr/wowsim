package se.wowsim.spells;

import java.util.Arrays;
import java.util.List;

import se.wowsim.Observer;
import se.wowsim.classes.Classes;
import se.wowsim.spells.types.DirectDamage;

public final class Shadowbolt extends DirectDamage {

    public static List<Integer> levelUps = Arrays.asList(1, 6, 12, 20, 28, 36, 44, 52, 60);

    public Shadowbolt(int rank) {
        super(rank);
        this.spellClass = Classes.WARLOCK;
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
                this.totalDamage = calculateDamage(12, 17);
                this.castTime = 17;
                break;
            case 2:
                this.totalDamage = calculateDamage(23, 30);
                this.castTime = 22;
                break;
            case 3:
                this.totalDamage = calculateDamage(48, 57);
                this.castTime = 28;
                break;
            case 4:
                this.totalDamage = calculateDamage(86, 99);
                break;
            case 5:
                this.totalDamage = calculateDamage(142, 163);
                //TODO this is for testing
                this.castTime = 22;
                break;
            case 6:
                this.totalDamage = calculateDamage(204, 231);
                break;
            case 7:
                this.totalDamage = calculateDamage(281, 316);
                break;
            case 8:
                this.totalDamage = calculateDamage(360, 403);
                break;
            case 9:
                this.totalDamage = calculateDamage(455, 508);
                break;
            default:
                throw new IllegalArgumentException("Given rank does not exist");
        }

    }

}

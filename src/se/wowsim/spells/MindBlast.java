package se.wowsim.spells;

import java.util.Arrays;
import java.util.List;

import se.wowsim.Observer;
import se.wowsim.classes.Classes;
import se.wowsim.spells.types.DirectDamage;

public final class MindBlast extends DirectDamage {

    public static List<Integer> levelUps = Arrays.asList(10, 16, 22, 28, 34, 40, 46, 52, 58);

    public MindBlast(int rank) {
        super(rank);
        this.spellClass = Classes.PRIEST;
    }

    @Override
    public String getName() {
        return "Mind Blast";
    }

    @Override
    public void init() {
        switch (rank) {
            case 1:
                this.totalDamage = calculateDamage(39, 44);
                break;
            case 2:
                this.totalDamage = calculateDamage(72, 79);
                break;
            case 3:
                this.totalDamage = calculateDamage(112, 121);
                break;
            case 4:
                this.totalDamage = calculateDamage(167, 178);
                break;
            case 5:
                this.totalDamage = calculateDamage(217, 232);
                break;
            case 6:
                this.totalDamage = calculateDamage(279, 298);
                break;
            case 7:
                this.totalDamage = calculateDamage(346, 367);
                break;
            case 8:
                this.totalDamage = calculateDamage(425, 450);
                break;
            case 9:
                this.totalDamage = calculateDamage(503, 532);
                break;
            default:
                throw new IllegalArgumentException("Given rank does not exist");
        }
        this.castTime = 15;
        this.maxCooldown = 80;
        this.cooldown = 0;

    }

}

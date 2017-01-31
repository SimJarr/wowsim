package se.wowsim.spells;

import java.util.Arrays;
import java.util.List;

import se.wowsim.classes.Classes;
import se.wowsim.spells.types.DamageOverTime;

public final class ShadowWordPain extends DamageOverTime {

	public static List<Integer> levelUps = Arrays.asList(4, 10, 18, 26, 34, 42, 50, 58);
	
	public ShadowWordPain(int rank) {
		super(rank);
		this.spellClass = Classes.PRIEST;
	}

	@Override
	public String getName() {
		return "Shadow Word: Pain";
	}
	
	@Override
    public void init() {
        this.maxDuration = 180;
        switch (rank) {
            case 1:
                this.totalDamage = 30;
                break;
            case 2:
                this.totalDamage = 66;
                break;
            case 3:
                this.totalDamage = 132;
                break;
            case 4:
                this.totalDamage = 234;
                break;
            case 5:
                this.totalDamage = 366;
                break;
            case 6:
                this.totalDamage = 510;
                break;
            case 7:
                this.totalDamage = 672;
                break;
            case 8:
                this.totalDamage = 852;
                break;
            default:
                throw new IllegalArgumentException("Given rank does not exist");
        }
        this.duration = this.maxDuration;
        this.tickInterval = 30;
        this.tickNumber = 1;
        this.totalTickNumber = duration / tickInterval;
        this.castTime = 0;
    }

}

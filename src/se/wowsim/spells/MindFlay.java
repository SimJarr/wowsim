package se.wowsim.spells;

import java.util.Arrays;
import java.util.List;

import se.wowsim.Observer;
import se.wowsim.spells.types.Channeling;

public final class MindFlay extends Channeling implements Observer {

	public static List<Integer> levelUps = Arrays.asList(4, 14, 24, 34, 44, 54);
	
	public MindFlay(int rank) {
		super(rank);
	}

	@Override
	public void applySpell() {
        this.duration = this.maxDuration;
        this.tickNumber = 1;
        target.register(this);
	}
	
	@Override
	public void update() {
		// TODO Auto-generated method stub
	}

	@Override
	public void init() {
		  this.maxDuration = 30;
	        switch (rank) {
	            case 1:
	                this.totalDamage = 40;
	                break;
	            case 2:
	                this.totalDamage = 90;
	                break;
	            case 3:
	                this.totalDamage = 222;
	                break;
	            case 4:
	                this.totalDamage = 324;
	                break;
	            case 5:
	                this.totalDamage = 486;
	                break;
	            case 6:
	                this.totalDamage = 666;
	                break;
	            default:
	                throw new IllegalArgumentException("Given rank does not exist");
	        }
	        this.duration = this.maxDuration;
	        this.tickInterval = 10;
	        this.tickNumber = 1;
	        this.totalTickNumber = duration / tickInterval;
	        this.castTime = 0;
	}

	@Override
	public String getName() {
		return "Mind Flay";
	}
}

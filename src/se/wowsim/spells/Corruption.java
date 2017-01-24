package se.wowsim.spells;

import java.util.Arrays;
import java.util.List;

import se.wowsim.Observer;
import se.wowsim.Target;
import se.wowsim.classes.Classes;

public final class Corruption extends DamageOverTime implements Observer {

	private int tickNumber;
	private int totalTickNumber;
	private Target target;
	public static List<Integer> levelUps = Arrays.asList(4, 14, 24, 34, 44, 54);
	
	public Corruption(int rank) {
		super(rank);
		init(rank);
		this.spellClass = Classes.WARLOCK;
	}
	
	@Override
	public void applySpell() {
		init(rank);
		target.register(this);
	}

	@Override
	public void setTarget(Target target) {
		this.target = target;
	}

	@Override
	public void update() {
		this.duration --;
		if((duration % tickInterval == 0) && duration != maxDuration) {
			System.out.println("Corruption tick(" + tickNumber + "/" + totalTickNumber + "): " + totalDamage / totalTickNumber + " damage");
			tickNumber++;
		}
		if(duration == 0) { target.unregister(this); }
	}
	
	@Override
	public String getName() {
		return "Corruption";
	}
	
	private void init(int rank) {
		switch(rank) {
		case 3:
			this.duration = 180;
			this.maxDuration = 180;
			this.totalDamage = 222;
			break;
		default:
			throw new IllegalArgumentException("Given rank does not exist");
		}
		this.tickInterval = 30;
		this.tickNumber = 1;
		this.totalTickNumber = duration/tickInterval;
		this.castTime = 20;
	}
}

package se.wowsim;

import se.wowsim.spells.types.Channeling;
import se.wowsim.spells.types.DamageIncrease;
import se.wowsim.spells.types.DamageOverTime;
import se.wowsim.spells.types.Spell;

import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

public class Target implements Subject {

    private Set<Observer> observers;
    private Set<DamageIncrease> damageIncreases;

    public Target() {
        this.observers = new CopyOnWriteArraySet<>();
        this.damageIncreases = new CopyOnWriteArraySet<>();
    }

    public void register(DamageIncrease n) {
        if(damageIncreases.contains(n)) {
            n.incrementStacks();
            System.out.println(n.getName() + " reapplied to target, current stacks: " + n.getCurrentStacks());
        } else {
            damageIncreases.add(n);
            n.incrementStacks();
            System.out.println(n.getName() + " applied to target");
        }
    }

    public void unregister(DamageIncrease n) {
        damageIncreases.remove(n);
        System.out.println(n.getName() + " faded");
    }

    public double getSchoolAmp(Spell.School school) {
        double amp = 1.0;
        for(DamageIncrease d : damageIncreases) {
            if(d.school == school)
                amp += d.getCurrentAmp();
        }
        return amp;
    }

    @Override
    public void register(Observer o) {
        observers.add(o);
        if (o instanceof DamageOverTime)
            System.out.println(o.getName() + " applied to target");
    }

    @Override
    public void unregister(Observer o) {
        observers.remove(o);
        if (o instanceof DamageOverTime)
            System.out.println(o.getName() + " faded");
    }

    @Override
    public void notifyObservers() {
        for (Observer o : observers) {
            o.update();
        }
        for(DamageIncrease d : damageIncreases) {
            d.update(this);
        }
    }

    public DamageOverTime getNextDotTimeOut() {
        DamageOverTime nextDot = null;
        for (Object dot : this.getObservers()) {
            if (dot instanceof DamageOverTime && !(dot instanceof Channeling)) {
                if (nextDot == null) {
                    nextDot = (DamageOverTime) dot;
                } else {
                    if (nextDot.getDuration() > ((DamageOverTime) dot).getDuration()) {
                        nextDot = (DamageOverTime) dot;
                    }
                }
            }
        }
        return nextDot;
    }

    public boolean notAffected(DamageOverTime dot) {
        for (Observer o : getObservers()) {
            if (o.getClass().equals(dot.getClass())) {
                return (((DamageOverTime) o).getDuration() <= dot.getCastTime());
            }
        }
        return true;
    }

    public Set<Observer> getObservers() {
        return observers;
    }
}

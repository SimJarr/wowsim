package se.wowsim.spells;

import se.wowsim.Observer;
import se.wowsim.Subject;

public final class CurseOfAgony extends DamageOverTime implements Observer {

    private int tickNumber;
    private int totalTickNumber;
    private Subject subject;

    public CurseOfAgony(int rank) {
        init(rank);
    }

    public CurseOfAgony(Subject s, int rank) {
        this.subject = s;
        init(rank);
    }

    @Override
    public void applySpell() {
        subject.register(this);
    }

    @Override
    public void update() {
        this.duration--;

        if ((duration % tickInterval == 0) && duration != maxDuration) {

            double tickDamage = 0.0;

            if (tickNumber <= 4) { tickDamage = (totalDamage / totalTickNumber) * 0.5; }
            else if (tickNumber > 4 && tickNumber <= 8) { tickDamage = (totalDamage / totalTickNumber); }
            else if (tickNumber > 8 && tickNumber <= 12)  { tickDamage = (totalDamage / totalTickNumber) * 1.5; }

            System.out.println("CurseOfAgony tick(" + tickNumber + "/" + totalTickNumber + "): " + tickDamage + " damage");
            tickNumber++;
        }
        if (duration == 0) {
            subject.unregister(this);
        }
    }

    @Override
    public String getName() {
        return "CurseOfAgony";
    }

    private void init(int rank) {
        switch (rank) {
            case 3:
                this.duration = 240;
                this.maxDuration = 240;
                this.totalDamage = 324;
                break;
            default:
                break;
        }
        this.tickInterval = 20;
        this.tickNumber = 1;
        this.totalTickNumber = duration / tickInterval;
        this.castTime = 0;
    }
}

package se.wowsim.graphics;

import se.wowsim.spells.types.Spell;

import java.util.Map;
import java.util.TimerTask;

public class MyTimerTask extends TimerTask {

    private Subject subject;
    private Animation animation;

    public MyTimerTask(Subject subject, Animation animation) {
        this.subject = subject;
        this.animation = animation;
    }

    public void run() {
        System.out.println("decisecond: " + animation.getDecisecond());

        Map<Integer, Spell> usedSpellsWithTime = animation.getUsedSpellsWithTime();

        for (Map.Entry entry : usedSpellsWithTime.entrySet()) {
            if (entry.getKey().equals(animation.getDecisecond())) {
                animation.addAnimation(new Shadowbolt(subject, 0, 50));
            }
        }

        subject.notifyObservers();
        animation.setDecisecond(animation.getDecisecond() + 1);
        animation.rePaint();
    }
}
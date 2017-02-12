package se.wowsim.animation;

import java.util.Map;
import java.util.TimerTask;

public class MyTimerTask extends TimerTask {

    private Subject subject;
    private Animation animation;
    private Map<Integer, AnimatedEntity> usedSpellsWithTime;

    public MyTimerTask(Subject subject, Animation animation, Map<Integer, AnimatedEntity> map) {
        this.subject = subject;
        this.animation = animation;
        this.usedSpellsWithTime = map;
    }

    public void run() {
        System.out.println("decisecond: " + animation.getDecisecond());

        for (Map.Entry entry : usedSpellsWithTime.entrySet()) {
            if (entry.getKey().equals(animation.getDecisecond())) {
                animation.addAnimation((AnimatedEntity) entry.getValue());
            }
        }

        subject.notifyObservers();
        animation.setDecisecond(animation.getDecisecond() + 1);
        animation.rePaint();
    }
}
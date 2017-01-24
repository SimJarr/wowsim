package se.wowsim;

import se.wowsim.classes.ClassTemplate;

public abstract class Simulator {

    public static double simulate(ClassTemplate classTemplate, Target target, int simDuration){

        double damage = 0.0;

        for(int i = 0; i <= simDuration; i ++) {
            System.out.println("decisecond: " + i);
            classTemplate.currentActivity(target, simDuration-i);
            target.notifyObservers();

        }

        return damage / simDuration;
    }

}

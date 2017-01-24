package se.wowsim;

import se.wowsim.classes.ClassTemplate;

public abstract class Simulator {

    public static double totalDamageDone = 0.0;
    public static double dps = 0.0;

    public static void simulate(ClassTemplate classTemplate, Target target, int simDuration){

        totalDamageDone = classTemplate.resetTotalDamageDone();
        dps = 0.0;

        for(int i = 0; i <= simDuration; i ++) {
            System.out.println("decisecond: " + i);
            classTemplate.currentActivity(target, simDuration-i);
            target.notifyObservers();

        }

        totalDamageDone = classTemplate.getTotalDamageDone();

        dps = (totalDamageDone / simDuration) * 10;
    }

    public static double getTotalDamageDone() {
        return totalDamageDone;
    }

    public static double getDps() {
        return dps;
    }
}

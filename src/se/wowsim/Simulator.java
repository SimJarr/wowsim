package se.wowsim;

import se.wowsim.classes.ClassTemplate;

import java.util.ArrayList;
import java.util.List;

public abstract class Simulator {

    public static double totalDamageDone = 0.0;
    public static double dps = 0.0;

    private Simulator() {
    }

    public static void simulate(ClassTemplate classTemplate, Target target, int simDuration) {

        totalDamageDone = classTemplate.resetTotalDamageDone();
        dps = 0.0;

        for (int i = 0; i <= simDuration; i++) {
            System.out.println("decisecond: " + i);
            classTemplate.currentActivity(target, simDuration - i);
            target.notifyObservers();

        }

        totalDamageDone = classTemplate.getTotalDamageDone();

        dps = (totalDamageDone / simDuration) * 10;

        System.out.println("Total Damage Done: " + getTotalDamageDone());
        System.out.println("Dps: " + getDps());

        System.out.println(formatSpellList(classTemplate.getUsedSpells()));

    }

    public static double getTotalDamageDone() {
        return totalDamageDone;
    }

    public static double getDps() {
        return dps;
    }

    private static List<String> formatSpellList(List<String> spellList) {

        List<String> resultList = new ArrayList<>();

        for (int i = 0; i < spellList.size(); i++) {

            int howMany = howManyNextSame(spellList, i);

            if (howMany == 1) {
                resultList.add(spellList.get(i));
            } else {
                resultList.add(spellList.get(i) + "*" + howMany);
                i += (howMany - 1);
            }

        }

        return resultList;
    }

    private static int howManyNextSame(List<String> list, int index) {
        return howManyNextSame(list, list.get(index), index, 0);
    }

    private static int howManyNextSame(List<String> list, String currentChecking, int index, int result) {

        if (list.size() > index + 1) {
            if (currentChecking.equals(list.get(index + 1))) {
                result += howManyNextSame(list, currentChecking, index + 1, result);
            }
        }

        return result + 1;
    }
}

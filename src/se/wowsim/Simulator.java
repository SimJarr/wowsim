package se.wowsim;

import se.wowsim.animation.Animation;
import se.wowsim.classes.ClassTemplate;
import se.wowsim.spells.types.Spell;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class Simulator {

    private static Map<Integer, Spell> usedSpellsWithTime = new HashMap<>();
    private static int totalTime;

    private Simulator() {
    }

    public static void simulate(ClassTemplate classTemplate, Target target, int simDuration) {

        double totalDamageDone;
        double dps;
        totalTime = simDuration;

        for (int i = 0; i <= simDuration; i++) {
            System.out.println("decisecond: " + i);
            classTemplate.currentActivity(target, simDuration - i);
        }

        totalDamageDone = gatherEverySpellsDamageDone(classTemplate);

        dps = (totalDamageDone / simDuration) * 10;

        System.out.println("Total Damage Done: " + totalDamageDone);
        System.out.println("Dps: " + dps);

        System.out.println(formatSpellList(classTemplate.getUsedSpells()));

        usedSpellsWithTime = classTemplate.getUsedSpellsWithTime();

    }

    public static void animate() {

        EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Animation(usedSpellsWithTime, totalTime);
            }
        });

    }

    private static double gatherEverySpellsDamageDone(ClassTemplate classTemplate) {
        Map<String, Spell> spell = classTemplate.getSpells();
        double result = 0.0;

        for (Map.Entry<String, Spell> entry : spell.entrySet()) {
            Spell currentSpell = entry.getValue();
            result += currentSpell.getDamageDoneDuringSim();
        }

        return result;
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

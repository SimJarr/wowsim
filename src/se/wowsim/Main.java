package se.wowsim;

import se.wowsim.spells.Corruption;
import se.wowsim.spells.Shadowbolt;

public class Main {
    public static void main(String[] args) {

        Target target = new Target();
        new Corruption(target);
        new Shadowbolt(target);

        for (int i = 1; i <= 180; i++) {
            if (i % 10 == 0)
                System.out.println("second: " + i / 10);
            if (i == 60 || i == 150)
                new Shadowbolt(target);

            target.notifyObservers();
        }
    }
}

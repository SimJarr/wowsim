package se.wowsim.animation;

import se.wowsim.spells.types.DamageOverTime;
import se.wowsim.spells.types.Spell;

import javax.swing.*;
import java.awt.*;
import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.Timer;
import java.util.concurrent.CopyOnWriteArraySet;

public class Animation extends JFrame {
    private int width = 400;
    private int height = 200;
    private int decisecond = 0;
    private int completedAnimations = 0;
    private int totalTime = 0;
    private Subject subject = new Subject();
    private DrawPanel drawPanel = new DrawPanel();
    private Timer timer = new Timer();
    private Map<Integer, AnimatedEntity> usedSpellsWithTime = new HashMap<>();

    private Set<AnimatedEntity> animatedEntities = new CopyOnWriteArraySet<>();

    public Animation(Map<Integer, Spell> map, int totalTime) {

        this.usedSpellsWithTime = formatMapWithSpells(map);
        this.totalTime = totalTime + 20;

        addAnimation(new WarlockAnimation(subject, 5, 0, this.totalTime));

        add(drawPanel);

        pack();
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);

        startTimer();
    }


    public int getDecisecond() {
        return decisecond;
    }

    public void setDecisecond(int decisecond) {
        this.decisecond = decisecond;
    }

    public void addAnimation(AnimatedEntity entity) {
        subject.register(entity);
        animatedEntities.add(entity);
    }

    private void startTimer() {
        if (usedSpellsWithTime.size() > 0) {
            MyTimerTask myTimerTask = new MyTimerTask(subject, this, usedSpellsWithTime);
            timer.schedule(myTimerTask, 0, 100);
        }
    }

    public void rePaint() {
        drawPanel.repaint();
    }

    private Map<Integer, AnimatedEntity> formatMapWithSpells(Map<Integer, Spell> map) {

        Map<Integer, AnimatedEntity> result = new HashMap<>();

        for (Map.Entry entry : map.entrySet()) {

            Spell currentSpell = (Spell) entry.getValue();

            try {
                Constructor mySpell = Class.forName("se.wowsim.animation." + currentSpell.getClass().getSimpleName() + "Animation").getConstructor(Subject.class, int.class, int.class, int.class);
                int animationDuration = 15;
                if (currentSpell instanceof DamageOverTime) {
                    animationDuration = ((DamageOverTime) currentSpell).getMaxDuration();
                }
                result.put((int) entry.getKey(), (AnimatedEntity) mySpell.newInstance(subject, 0, 50, animationDuration));
            } catch (Exception e) {
                throw new RuntimeException("Animation for: " + entry.getValue().getClass().getSimpleName() + " doesn't exist");
            }
        }
        return result;
    }

    private class DrawPanel extends JPanel {


        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            //BACKGROUND
            g.setColor(Color.GRAY);
            g.fillRect(0, 0, getWidth(), getHeight());

            for (AnimatedEntity ae : animatedEntities) {
                if (ae.isAnimationDone()) {
                    animatedEntities.remove(ae);
                    completedAnimations++;
                }
                ae.draw(g);
            }
            if ((completedAnimations == usedSpellsWithTime.size() + 1) || decisecond == totalTime + 1) {
                timer.cancel();
            }
        }

        public Dimension getPreferredSize() {
            return new Dimension(width, height);
        }
    }
}
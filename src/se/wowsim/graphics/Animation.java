package se.wowsim.graphics;

import se.wowsim.spells.types.Spell;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.Timer;
import java.util.concurrent.CopyOnWriteArraySet;

public class Animation extends JFrame {
    private int width = 400;
    private int height = 200;
    private int decisecond = 0;
    private Subject subject = new Subject();
    private DrawPanel drawPanel = new DrawPanel();
    private Timer timer = new Timer();
    private Map<Integer, Spell> usedSpellsWithTime = new HashMap<>();

    private Set<AnimatedEntity> animatedEntities = new CopyOnWriteArraySet<>();

    public Animation(Map<Integer, Spell> map) {

        this.usedSpellsWithTime = map;

        for (Map.Entry entry : map.entrySet()) {
            //addAnimation(new Shadowbolt(subject, 0, 50), entry.getKey());
        }

        //TODO test
        //addAnimation(new Shadowbolt(subject, 0, 50));
        //addAnimation(new Shadowbolt(subject, 20, 150));


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

    public Map<Integer, Spell> getUsedSpellsWithTime() {
        return usedSpellsWithTime;
    }

    public void addAnimation(AnimatedEntity entity) {
        subject.register(entity);
        animatedEntities.add(entity);
    }

    private void startTimer() {
        MyTimerTask myTimerTask = new MyTimerTask(subject, this);
        timer.schedule(myTimerTask, 0, 100);
    }

    public void rePaint() {
        drawPanel.repaint();
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
                }
                ae.draw(g);
                if (animatedEntities.size() == 0) {
                    timer.cancel();
                    drawPanel.repaint();
                }
            }
        }

        public Dimension getPreferredSize() {
            return new Dimension(width, height);
        }
    }
}
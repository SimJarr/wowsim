package se.wowsim.animation;


import java.awt.*;

public class HumanAnimation extends AnimatedEntity {

    private int x;
    private int y;
    private int duration;

    public HumanAnimation(Subject subject, int x, int y, int duration) {
        super(subject);
        this.x = x;
        this.y = y;
        this.duration = duration;
        this.animationDone = false;
    }

    @Override
    public void draw(Graphics g) {
        g.setColor(Color.BLACK);
        g.fillRoundRect(x, y, 10, 10, 10, 10);
        g.fillRect(x + 3, y + 5, 5, 25);
        int xPoints[] = new int[4];
        xPoints[0] = x + 3;
        xPoints[1] = x - 5;
        xPoints[2] = x;
        xPoints[3] = x + 8;
        int yPoints[] = new int[4];
        yPoints[0] = y + 30;
        yPoints[1] = y + 35;
        yPoints[2] = y + 35;
        yPoints[3] = y + 30;
        g.fillPolygon(xPoints, yPoints, 4);
        xPoints[0] = x + 8;
        xPoints[1] = x + 16;
        xPoints[2] = x + 11;
        xPoints[3] = x + 3;
        g.fillPolygon(xPoints, yPoints, 4);
        g.fillRect(x - 5, y + 15, 21, 3);

    }

    @Override
    public void update() {
        if (duration == 0) {
            subject.unregister(this);
            this.animationDone = true;
            return;
        }
        this.duration--;
    }

    @Override
    public String getName() {
        return "";
    }
}

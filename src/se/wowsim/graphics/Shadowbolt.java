package se.wowsim.graphics;


import java.awt.*;

public class Shadowbolt extends AnimatedEntity{

    private int x;
    private int y;
    private int speed;

    public Shadowbolt(Subject subject, int x, int y) {
        super(subject);
        this.x = x;
        this.y = y;
        this.speed = 5;
        this.duration = 60;
        this.animationDone = false;
    }

    @Override
    public void draw(Graphics g){
            g.setColor(Color.MAGENTA);
            g.fillRect(x, y, 40, 10);
            g.setColor(Color.BLACK);
            g.fillRoundRect(x + 35, y, 10, 10, 10, 10);
            g.setColor(new Color(0, 77, 0));
            g.fillRoundRect(x + 35, y - 10, 7, 7, 10, 10);
            g.setColor(new Color(0, 77, 0));
            g.fillRoundRect(x + 35, y + 12, 7, 7, 10, 10);
    }

    @Override
    public void update() {
        this.x += speed;
        if (duration == 0){
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

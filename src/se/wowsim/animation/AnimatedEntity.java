package se.wowsim.animation;

import se.wowsim.Observer;

import java.awt.*;

public abstract class AnimatedEntity implements Observer {

    protected int duration;
    protected Subject subject;
    protected boolean animationDone;

    public AnimatedEntity(Subject subject) {
        this.subject = subject;
    }

    public boolean isAnimationDone() {
        return animationDone;
    }

    public abstract void update();

    public abstract String getName();

    public abstract void draw(Graphics g);
}

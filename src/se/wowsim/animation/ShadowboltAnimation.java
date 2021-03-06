package se.wowsim.animation;


import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ShadowboltAnimation extends AnimatedEntity {

    private int x;
    private int y;
    private int speed;
    private int duration;
    private BufferedImage image;

    //ignore unusedConstructor
    public ShadowboltAnimation(Subject subject, int x, int y, int duration) {
        super(subject);
        this.x = x;
        this.y = y + 120;
        this.speed = 20;
        this.duration = duration;
        this.animationDone = false;
        try {
            this.image = ImageIO.read(new File("src/images/shadowbolt.png"));
        } catch (IOException e) {
            throw new RuntimeException("Cannot find file");
        }
    }

    @Override
    public void draw(Graphics g) {
        g.drawImage(image, x, y, image.getWidth(), image.getHeight(), null);
    }

    @Override
    public void update() {
        this.x += speed;
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

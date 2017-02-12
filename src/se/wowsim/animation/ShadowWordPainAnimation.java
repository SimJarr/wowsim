package se.wowsim.animation;


import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ShadowWordPainAnimation extends AnimatedEntity {

    private int x;
    private int y;
    private int duration;
    private BufferedImage image;

    //ignore unusedConstructor
    public ShadowWordPainAnimation(Subject subject, int x, int y, int duration) {
        super(subject);
        this.x = x + 320;
        this.y = y + 20;
        this.duration = duration;
        this.animationDone = false;
        try {
            this.image = ImageIO.read(new File("src/images/shadowWordPain.png"));
        } catch (IOException e) {
            throw new RuntimeException("Cannot find file");
        }
    }

    @Override
    public void draw(Graphics g) {
        g.drawImage(image, x, y, image.getWidth() / 7, image.getHeight() / 7, null);
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

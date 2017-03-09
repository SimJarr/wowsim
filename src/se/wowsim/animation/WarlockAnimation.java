package se.wowsim.animation;


import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class WarlockAnimation extends AnimatedEntity {

    private int x;
    private int y;
    private int duration;
    private BufferedImage image;
    private BufferedImage currentImage;

    public WarlockAnimation(Subject subject, int x, int y, int duration) {
        super(subject);
        this.x = x;
        this.y = y + 100;
        this.duration = duration;
        this.animationDone = false;
        try {
            this.image = ImageIO.read(new File("src/images/warlock.png"));
            this.currentImage = image;
        } catch (IOException e) {
            throw new RuntimeException("Cannot find file");
        }
    }

    @Override
    public void draw(Graphics g) {
        g.drawImage(currentImage, x, y, image.getWidth(), image.getHeight(), null);
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

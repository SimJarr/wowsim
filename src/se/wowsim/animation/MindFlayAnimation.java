package se.wowsim.animation;


import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class MindFlayAnimation extends AnimatedEntity {

    private int x;
    private int y;
    private int duration;
    private BufferedImage image;
    private BufferedImage image2;
    private boolean flipImage;

    //ignore unusedConstructor
    public MindFlayAnimation(Subject subject, int x, int y, int duration) {
        super(subject);
        this.x = x + 90;
        this.y = y + 120;
        this.duration = duration;
        this.animationDone = false;
        this.flipImage = false;
        try {
            this.image = ImageIO.read(new File("src/images/mindFlay.png"));
            this.image2 = ImageIO.read(new File("src/images/mindFlay2.png"));
        } catch (IOException e) {
            throw new RuntimeException("Cannot find file");
        }
    }

    @Override
    public void draw(Graphics g) {
        if(flipImage){
            g.drawImage(image, x, y, image.getWidth() / 3, image.getHeight() / 6, null);
        } else {
            g.drawImage(image2, x, y, image.getWidth() / 3, image.getHeight() / 6, null);
        }
        flipImage = !flipImage;
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

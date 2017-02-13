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
    private static final Font FONT = new Font(Font.MONOSPACED, Font.BOLD, 32);

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
        g.drawImage(image, x, y, image.getWidth() / 10, image.getHeight() / 10, null);
        g.setFont(FONT);
        FontMetrics fontMetrics = g.getFontMetrics();
        String text = String.valueOf(duration / 10);
        int textX = x + 35 - fontMetrics.stringWidth(text) / 2;
        int textY = y + 35 + fontMetrics.getHeight();
        g.setColor(Color.BLACK);
        g.drawString(text, textX, textY);
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

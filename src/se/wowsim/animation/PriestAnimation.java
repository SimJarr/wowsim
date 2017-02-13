package se.wowsim.animation;


import se.wowsim.spells.types.Channeling;
import se.wowsim.spells.types.Spell;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Map;

public class PriestAnimation extends AnimatedEntity {

    private int x;
    private int y;
    private int duration;
    private int currentDecisec;
    private int waitTime;
    private int previousTimeOfAnimation;
    private BufferedImage image;
    private BufferedImage image2;
    private BufferedImage image3;
    private BufferedImage currentImage;
    private boolean busyCasting;
    private boolean offsetByOne;
    private States state;
    private Map<Integer, Spell> actualSpellsUsed;

    //TODO create parent class, eg. CasterEntity?? Move alot of stuff from here and many other animation classes.

    public PriestAnimation(Subject subject, int x, int y, int duration, Map<Integer, Spell> map) {
        super(subject);
        this.x = x;
        this.y = y + 85;
        this.duration = duration;
        this.actualSpellsUsed = map;
        this.animationDone = false;
        this.busyCasting = false;
        this.offsetByOne = false;
        this.state = States.ENDCAST;
        try {
            this.image = ImageIO.read(new File("src/images/priest.png"));
            this.image2 = ImageIO.read(new File("src/images/priest2.png"));
            this.image3 = ImageIO.read(new File("src/images/priest3.png"));
            this.currentImage = this.image;
        } catch (IOException e) {
            throw new RuntimeException("Cannot find file");
        }
    }

    @Override
    public void draw(Graphics g) {
        g.drawImage(currentImage, x, y, currentImage.getWidth(), currentImage.getHeight(), null);
    }

    @Override
    public void update() {
        if (duration == 0) {
            subject.unregister(this);
            this.animationDone = true;
            return;
        }
        this.duration--;
        this.currentDecisec++;

        waitTime--;
        if (waitTime == 0) {
            busyCasting = false;
        }
        if (busyCasting) {
            return;
        }


        int timeOfNextAnimation;

        if (actualSpellsUsed.get(currentDecisec) != null) {
            timeOfNextAnimation = currentDecisec;
        } else {
            timeOfNextAnimation = calcTimeOfNextAnimation(currentDecisec);
        }

        if (previousTimeOfAnimation == timeOfNextAnimation - 1){
            offsetByOne = true;
        }
        previousTimeOfAnimation = timeOfNextAnimation;

        if (currentDecisec == timeOfNextAnimation && timeOfNextAnimation != -1 && actualSpellsUsed.get(timeOfNextAnimation) instanceof Channeling) {
            state = States.CHANNELING;
            busyCasting = true;
            if (offsetByOne) {
                waitTime = actualSpellsUsed.get(timeOfNextAnimation).getTimeTakenFromCaster()-1;
                offsetByOne = false;
            } else {
                waitTime = actualSpellsUsed.get(timeOfNextAnimation).getTimeTakenFromCaster();
            }
        } else if (timeOfNextAnimation != -1 && currentDecisec == (timeOfNextAnimation - actualSpellsUsed.get(timeOfNextAnimation).getCastTime())) {
            state = States.CASTING;
            busyCasting = true;
            waitTime = actualSpellsUsed.get(timeOfNextAnimation).getCastTime();
        } else if (currentDecisec == timeOfNextAnimation) {
            state = States.ENDCAST;
        } else {
            state = States.IDLE;
        }


        switch (state) {
            case IDLE:
                currentImage = image;
                break;
            case ENDCAST:
            case CHANNELING:
                currentImage = image2;
                break;
            case CASTING:
                currentImage = image3;
                break;
        }
    }

    private int calcTimeOfNextAnimation(int currentDecisec) {
        int result = -1;
        for (Integer integer : actualSpellsUsed.keySet()) {
            if (integer > currentDecisec && result == -1) {
                result = integer;
            } else if (integer > currentDecisec && integer < result) {
                result = integer;
            }
        }

        if (result == currentDecisec + 1) {
            offsetByOne = true;
        }

        return result;
    }

    @Override
    public String getName() {
        return "";
    }

    private enum States{
        IDLE,
        ENDCAST,
        CHANNELING,
        CASTING
    }
}

package se.wowsim.classes;

import se.wowsim.SpellAndValue;
import se.wowsim.Target;
import se.wowsim.spells.types.Channeling;
import se.wowsim.spells.types.DamageOverTime;
import se.wowsim.spells.types.DirectDamage;
import se.wowsim.spells.types.Spell;

import java.lang.reflect.Constructor;
import java.util.*;

import static se.wowsim.classes.GeneralRules.GLOBAL_COOLDOWN;

public abstract class ClassTemplate {

    private double totalDamageDone;
    private int level;
    private int intellect;
    private int stamina;
    private int spirit;
    private List<String> usedSpells = new ArrayList<>();
    protected Map<String, Spell> spells;
    protected boolean busyCasting;
    protected double critChance;
    protected int castProgress;
    protected int globalCooldown;
    protected int downTime;
    protected Spell nextSpell;
    protected Classes myClass;

    protected ClassTemplate(int level, int intellect) {
        this.busyCasting = false;
        this.globalCooldown = GLOBAL_COOLDOWN;
        this.totalDamageDone = 0.0;
        this.nextSpell = null;
        this.spells = new HashMap<>();
        this.intellect = intellect;
        this.level = level;
    }

    public double getTotalDamageDone() {
        return totalDamageDone;
    }

    public List<String> getUsedSpells() {
        return usedSpells;
    }

    public double resetTotalDamageDone() {
        this.totalDamageDone = 0.0;
        return 0.0;
    }

    public Map<String, Spell> getSpells() {
        return spells;
    }

    protected void addSpell(String name, Spell spell) {
        spells.put(name, spell);
    }

    public void currentActivity(Target target, int timeLeft) {

        //TODO pre-casta en spell innan decisekund 0

        castProgress--;
        downTime--;
        decrementEverySpellsCooldown();

        if (nextSpell != null && castProgress <= 0) {
            nextSpell.applySpell();
            nextSpell = null;
        }
        if (downTime == 0) {
            busyCasting = false;
        }
        if (busyCasting) {
            return;
        }

        nextSpell = determineSpell(target, timeLeft);

        if (nextSpell != null) {
            nextSpell.setTarget(target);

            castProgress = nextSpell.getCastTime();
            if (nextSpell instanceof Channeling) {
                downTime = ((Channeling) nextSpell).getTimeTakenFromCaster();
            } else {
                downTime = (nextSpell.getCastTime() > globalCooldown) ? nextSpell.getCastTime() : globalCooldown;
            }

            busyCasting = true;
            System.out.println("Casting " + nextSpell.getName());
            if (castProgress == 0) {
                nextSpell.applySpell();
                nextSpell = null;
            }
        }
    }

    private void decrementEverySpellsCooldown() {
        for (Map.Entry<String, Spell> entry : spells.entrySet()) {
            Spell currentSpell = entry.getValue();
            currentSpell.decrementCooldown();
        }
    }

    private Spell determineSpell(Target target, int timeLeft) {

        //TODO future-sight, understand what order of spells yields the greatest ePeen

        DamageOverTime highestDamageDot = calculateHighestDamageDot(target, timeLeft);

        List<SpellAndValue> spellCandidates = insertSpellsWithValues(target, timeLeft, highestDamageDot);

        return selectSpell(target, timeLeft, spellCandidates);
    }

    private DamageOverTime calculateHighestDamageDot(Target target, int timeLeft) {

        DamageOverTime highestDamageDot = null;
        double highestDamageDotDps = 0.0;
        DamageOverTime secondHighestDamageDot = null;

        for (Map.Entry<String, Spell> entry : spells.entrySet()) {

            Spell currentSpell = entry.getValue();

            if (currentSpell instanceof DamageOverTime && !(currentSpell instanceof Channeling)) {
                if (highestDamageDot == null && (currentSpell.calculateDamageDealt(target, timeLeft) != 0.0)) {
                    highestDamageDot = (DamageOverTime) currentSpell;
                    highestDamageDotDps = ((currentSpell.calculateDamageDealt(target, timeLeft)) / (((DamageOverTime) currentSpell).getDuration() + currentSpell.getCastTime())) * 10;
                } else {
                    double currentSpellDps = ((currentSpell.calculateDamageDealt(target, timeLeft)) / (((DamageOverTime) currentSpell).getDuration() + currentSpell.getCastTime())) * 10;
                    if (currentSpellDps > highestDamageDotDps) {
                        secondHighestDamageDot = highestDamageDot;
                        highestDamageDot = (DamageOverTime) currentSpell;
                        highestDamageDotDps = currentSpellDps;
                    }
                }
            }
        }

        if (highestDamageDot != null && secondHighestDamageDot != null) {
            if (highestDamageDot.getTimeTakenFromCaster() + secondHighestDamageDot.getCastTime() + secondHighestDamageDot.getMaxDuration() > timeLeft) {
                if (secondHighestDamageDot.getTimeTakenFromCaster() + highestDamageDot.getCastTime() + highestDamageDot.getMaxDuration() <= timeLeft) {
                    return secondHighestDamageDot;
                }
            }
        }

        return highestDamageDot;
    }

    private List<SpellAndValue> insertSpellsWithValues(Target target, int timeLeft, DamageOverTime highestDamageDot) {

        List<SpellAndValue> result = new ArrayList<>();

        for (Map.Entry<String, Spell> entry : spells.entrySet()) {

            Spell currentSpell = entry.getValue();

            if (currentSpell instanceof DirectDamage) {
                ((DirectDamage) currentSpell).setCritChance(this.myClass.calculateCritChance(level, intellect));
            }

            if (currentSpell instanceof Channeling) {

                int tickInterval = ((Channeling) currentSpell).getTickInterval();
                int rank = currentSpell.getRank();
                int loopCounter = 0;
                Integer timeChanneled;
                List<Channeling> temporaryList = new ArrayList<>();

                for (int i = ((Channeling) currentSpell).getTotalTickNumber(); i >= 1; i--) {


                    timeChanneled = (i * tickInterval < GLOBAL_COOLDOWN) ? GLOBAL_COOLDOWN : i * tickInterval;


                    try {
                        Constructor constructor = Class.forName(currentSpell.getClass().getName()).getConstructor(int.class);
                        temporaryList.add((Channeling) constructor.newInstance(rank));
                        Channeling currentSpellNewInstance = temporaryList.get(loopCounter);
                        currentSpellNewInstance.init();
                        currentSpellNewInstance.setTemporaryChannelTime(timeChanneled);

                        SpellAndValue spellAndValue = new SpellAndValue(currentSpellNewInstance, (currentSpellNewInstance.calculateDamageDealt(target, timeLeft, i * tickInterval)) / timeChanneled);
                        result.add(spellAndValue);
                        //System.out.println(currentSpellNewInstance.getName() + " value: " + (currentSpellNewInstance.calculateDamageDealt(target, timeLeft, i * tickInterval)) / timeChanneled + " channelDuration: " + currentSpellNewInstance.getTimeTakenFromCaster());


                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }

                    loopCounter++;

                }


            }

            if (currentSpell instanceof DirectDamage || currentSpell == highestDamageDot) {

                SpellAndValue spellAndValue = new SpellAndValue(currentSpell, (currentSpell.calculateDamageDealt(target, timeLeft)) / currentSpell.getTimeTakenFromCaster());
                result.add(spellAndValue);
                //System.out.println(currentSpell.getName() + " value: " + (currentSpell.calculateDamageDealt(target, timeLeft)) / currentSpell.getTimeTakenFromCaster());
            }

        }

        return result;
    }

    private Spell selectSpell(Target target, int timeLeft, List<SpellAndValue> candidates) {

        SpellAndValue selectedSpellWithValue = pickHighestValueSpell(candidates);

        if (selectedSpellWithValue != null) {
            int waitTime = (worthDoingNothing(target, selectedSpellWithValue.getSpell(), timeLeft));
            if (waitTime > 0) {
                System.out.println("Finding another spell with at most: " + waitTime + " castTime");
                selectedSpellWithValue = pickHighestValueSpell(candidates, waitTime);
            }
        }

        if (selectedSpellWithValue != null) {
            totalDamageDone += selectedSpellWithValue.getValue() * selectedSpellWithValue.getSpell().getTimeTakenFromCaster();
            usedSpells.add(selectedSpellWithValue.getSpell().getName() + " " + (int) (selectedSpellWithValue.getValue() * selectedSpellWithValue.getSpell().getTimeTakenFromCaster()));
        }

        if (selectedSpellWithValue == null) {
            return null;
        }

        return selectedSpellWithValue.getSpell();
    }

    private SpellAndValue pickHighestValueSpell(List<SpellAndValue> candidates) {
        return pickHighestValueSpell(candidates, 0);
    }

    private SpellAndValue pickHighestValueSpell(List<SpellAndValue> candidates, int maximumTimeAllowed) {

        SpellAndValue selectedSpellWithValue = null;

        if (maximumTimeAllowed == 0) {

            for (SpellAndValue spellAndValue : candidates) {

                if (selectedSpellWithValue == null) {
                    selectedSpellWithValue = new SpellAndValue(spellAndValue.getSpell(), spellAndValue.getValue());

                } else {
                    if (spellAndValue.getValue() > selectedSpellWithValue.getValue()) {
                        selectedSpellWithValue.setSpell(spellAndValue.getSpell());
                        selectedSpellWithValue.setValue(spellAndValue.getValue());
                    }
                }

            }
        } else {
            for (SpellAndValue spellAndValue : candidates) {

                if (selectedSpellWithValue == null && spellAndValue.getSpell().getTimeTakenFromCaster() <= maximumTimeAllowed) {
                    selectedSpellWithValue = new SpellAndValue(spellAndValue.getSpell(), spellAndValue.getValue());

                } else {
                    if (selectedSpellWithValue != null && spellAndValue.getValue() > selectedSpellWithValue.getValue() && spellAndValue.getSpell().getTimeTakenFromCaster() <= maximumTimeAllowed) {
                        selectedSpellWithValue.setSpell(spellAndValue.getSpell());
                        selectedSpellWithValue.setValue(spellAndValue.getValue());
                    }
                }

            }
        }

        if (selectedSpellWithValue != null && selectedSpellWithValue.getValue() == 0.0) {
            selectedSpellWithValue = null;
        }

        return selectedSpellWithValue;
    }

    private int worthDoingNothing(Target target, Spell nextCalculatedSpell, int timeLeft) {

        DamageOverTime dot = target.getNextDotTimeOut();

        if (dot != null && nextCalculatedSpell != null && dot != nextCalculatedSpell) {


            int timeSpentOnNextSpell = nextCalculatedSpell.getTimeTakenFromCaster();
            int dotDurationIfWeCastNext = dot.getDuration() - timeSpentOnNextSpell;

            if (dotDurationIfWeCastNext < dot.getCastTime()) {
                int timeTheDotWouldBeGone = dot.getCastTime() - dotDurationIfWeCastNext;

                int timeTheDotCouldBeUp = (dot.getMaxDuration() < timeLeft) ? dot.getMaxDuration() : timeLeft;
                double damageDotWouldHaveDone = (dot.calculateDotDamage(timeLeft) / timeTheDotCouldBeUp) * timeTheDotWouldBeGone;
                damageDotWouldHaveDone = (damageDotWouldHaveDone < 0) ? 0 : damageDotWouldHaveDone;

                int timeTheNextSpellWouldMiss = (dot.getDuration() - dot.getCastTime());
                double damageNextSpellWouldHaveDone = ((nextCalculatedSpell.calculateDamageDealt(target, timeLeft)) / timeSpentOnNextSpell) * timeTheNextSpellWouldMiss;
                damageNextSpellWouldHaveDone = (damageNextSpellWouldHaveDone < 0) ? 0 : damageNextSpellWouldHaveDone;

                if (false) {
                    System.out.println(dot.getName() + " har lägst tid: " + dot.getDuration() + " decisec");
                    System.out.println("om vi castar: " + nextCalculatedSpell.getName() + " så har vi bara: " + dotDurationIfWeCastNext + " decisec");
                    System.out.println("då skulle " + dot.getName() + " vara nere i: " + timeTheDotWouldBeGone + " decisec");
                    System.out.println("på " + timeTheDotWouldBeGone + " decisec skulle " + dot.getName() + " hinna göra: " + damageDotWouldHaveDone + " damage");
                    System.out.println("på " + timeTheNextSpellWouldMiss + " decisec skulle " + nextCalculatedSpell.getName() + " hinna göra: " + damageNextSpellWouldHaveDone + " damage");
                }

                if ((damageDotWouldHaveDone > damageNextSpellWouldHaveDone) && timeTheNextSpellWouldMiss > 0) {
                    System.out.println("HAAAAAAAAAAAAAAALTAR for: " + (dot.getDuration() - dot.getCastTime()) + " decisec, so we can put up: " + dot.getName());
                    return (dot.getDuration() - dot.getCastTime());
                }

            }

        }
        return 0;
    }
}
























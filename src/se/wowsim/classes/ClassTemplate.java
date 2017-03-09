package se.wowsim.classes;

import se.wowsim.SpellAndValue;
import se.wowsim.Target;
import se.wowsim.spells.types.Channeling;
import se.wowsim.spells.types.DamageOverTime;
import se.wowsim.spells.types.DirectDamage;
import se.wowsim.spells.types.Spell;

import java.util.*;

import static se.wowsim.classes.GeneralRules.GLOBAL_COOLDOWN;

public abstract class ClassTemplate {

    private int level;
    private int intellect;
    private int stamina;
    private int spirit;
    private int currentDecisecond;
    private boolean recentlyHalted;
    private List<String> usedSpells;
    private Map<Integer, Spell> usedSpellsWithTime;
    protected Map<String, Spell> spells;
    protected boolean busyCasting;
    protected double critChance;
    protected int castProgress;
    protected int globalCooldown;
    protected int downTime;
    protected Spell nextSpell;
    protected Classes myClass;
    protected Map<Spell.School, Double> schoolAmp;

    protected ClassTemplate(int level, int intellect) {
        schoolAmp = new HashMap<>();
        for (Spell.School school : Spell.School.values()) {
            schoolAmp.put(school, 1.0);
        }

        this.busyCasting = false;
        this.globalCooldown = GLOBAL_COOLDOWN;
        this.nextSpell = null;
        this.intellect = intellect;
        this.level = level;
        this.recentlyHalted = false;
        this.usedSpells = new ArrayList<>();
        this.usedSpellsWithTime = new HashMap<>();
        this.spells = new TreeMap<>(Comparator.reverseOrder());
    }

    public abstract void applyDamageIncrease(Spell spell, Target target);

    public Map<Spell.School, Double> getSchoolAmp() {
        return schoolAmp;
    }

    public List<String> getUsedSpells() {
        return usedSpells;
    }

    public Map<Integer, Spell> getUsedSpellsWithTime() {
        return usedSpellsWithTime;
    }

    public Map<String, Spell> getSpells() {
        return spells;
    }

    protected void addSpell(String name, Spell spell) {
        spells.put(name, spell);
    }

    /**
     * find out what we should do this decisecond
     * @param target the target
     * @param timeLeft the time we have to work with
     */
    public void currentActivity(Target target, int timeLeft) {

        //TODO pre-casta en spell innan decisekund 0

        currentDecisecond++;
        castProgress--;
        downTime--;
        decrementEverySpellsCooldown();

        if (nextSpell != null && castProgress <= 0) {
            nextSpell.applySpell();
            applyDamageIncrease(nextSpell, target);
            updateDamageValues(target);
            nextSpell = null;
        }
        if (downTime == 0) {
            busyCasting = false;
        }
        if (busyCasting) {
            target.notifyObservers();
            return;
        }

        nextSpell = determineSpell(target, timeLeft);

        if (nextSpell != null) {
            nextSpell.setTarget(target);

            castProgress = nextSpell.getCastTime();
            if (nextSpell instanceof Channeling) {
                downTime = nextSpell.getTimeTakenFromCaster();
            } else {
                downTime = (nextSpell.getCastTime() > globalCooldown) ? nextSpell.getCastTime() : globalCooldown;
            }

            busyCasting = true;

            target.notifyObservers();
            System.out.println("Casting " + nextSpell.getName());
            usedSpells.add(nextSpell.getName());
            if (nextSpell instanceof Channeling) {
                String lastEntry = usedSpells.get(usedSpells.size() - 1);
                lastEntry += " " + ((Channeling) nextSpell).getChannelTime() / 10;
                usedSpells.remove(usedSpells.size() - 1);
                usedSpells.add(lastEntry);
            }
            recentlyHalted = false;
            if (castProgress == 0) {
                nextSpell.applySpell();
                nextSpell.update();
                applyDamageIncrease(nextSpell, target);
                updateDamageValues(target);
                nextSpell = null;
            }
            return;
        }
        target.notifyObservers();
    }

    /**
     * reduce every Spells cooldown by one decisecond
     */
    private void decrementEverySpellsCooldown() {
        for (Map.Entry<String, Spell> entry : spells.entrySet()) {
            Spell currentSpell = entry.getValue();
            currentSpell.decrementCooldown();
        }
    }

    /**
     * figure out what spell to cast
     * @param target the target
     * @param timeLeft the time we have to work with
     * @return the Spell to cast
     */
    private Spell determineSpell(Target target, int timeLeft) {

        //TODO future-sight, understand what order of spells yields the greatest ePeen

        updateDamageValues(target);

        DamageOverTime highestDamageDot = calculateHighestDamageDot(target, timeLeft);

        List<SpellAndValue> spellCandidates = insertSpellsWithValues(target, timeLeft, highestDamageDot);

        return selectSpell(target, timeLeft, spellCandidates);
    }

    /**
     * updates the damage of every Spell according to increases from various places
     * @param target the target, needed to see if our target takes extra damage from some Spell School
     */
    private void updateDamageValues(Target target) {
        for (Map.Entry<String, Spell> entry : spells.entrySet()) {
            Spell currentSpell = entry.getValue();
            if (currentSpell instanceof DirectDamage) {
                ((DirectDamage) currentSpell).setCritChance(this.myClass.calculateCritChance(level, intellect));
            }
//            System.out.println(currentSpell.getName());
//            System.out.println(currentSpell.getBaseDamage() + " : damage before amp");
            double baseDamage = currentSpell.getBaseDamage();
            double currentAmp = schoolAmp.get(currentSpell.getSchool());
            currentAmp *= target.getSchoolAmp(currentSpell.getSchool());
            currentSpell.setTotalDamage(baseDamage * currentAmp);
//            System.out.println(currentSpell.getTotalDamage() + " : damage after amp");
//            System.out.println(currentAmp + " : amp");
        }
    }

    /**
     * find out which dot will do the most damage per spent time if we start casting it now
     * @param target the target
     * @param timeLeft the time we have to work with
     * @return a DamageOverTime that does the most damage
     */
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

    /**
     * creates a list of every spell with a calculated value
     * @param target the target
     * @param timeLeft the time we have to work with
     * @param highestDamageDot calculated highest damage dealing DamageOverTime
     * @return an ArrayList containing SpellAndValue object
     */
    private List<SpellAndValue> insertSpellsWithValues(Target target, int timeLeft, DamageOverTime highestDamageDot) {

        List<SpellAndValue> result = new ArrayList<>();

        for (Map.Entry<String, Spell> entry : spells.entrySet()) {

            Spell currentSpell = entry.getValue();

            if (!(currentSpell instanceof DamageOverTime) || (currentSpell instanceof Channeling) || currentSpell == highestDamageDot) {

                SpellAndValue spellAndValue = new SpellAndValue(currentSpell, (currentSpell.calculateDamageDealt(target, timeLeft)) / currentSpell.getTimeTakenFromCaster());
                result.add(spellAndValue);
                //System.out.println(currentSpell.getName() + " value: " + (currentSpell.calculateDamageDealt(target, timeLeft)) / currentSpell.getTimeTakenFromCaster());

            }

        }

        return result;
    }

    /**
     * selects which spell to cast given the SpellAndValue ArrayList
     * @param target the target
     * @param timeLeft the time we have to work with
     * @param candidates SpellAndValue ArrayList
     * @return the Spell we should cast
     */
    private Spell selectSpell(Target target, int timeLeft, List<SpellAndValue> candidates) {

        //TODO chose MB over Pain in case MB gets to do more casts
        SpellAndValue selectedSpellWithValue = pickHighestValueSpell(candidates, timeLeft);

        if (selectedSpellWithValue != null) {
            Spell nextSpell = selectedSpellWithValue.getSpell();
            WaitTimeAndDamageDiff waitTimeAndDamageDiff = worthDoingNothing(target, selectedSpellWithValue.getSpell(), candidates, timeLeft);
            int waitTime = waitTimeAndDamageDiff.getWaitTime();
            double damageDifference = waitTimeAndDamageDiff.getDamageDiff();
            String nameWaitSpell = waitTimeAndDamageDiff.getSpellName();
            if (nextSpell instanceof Channeling) {
                int maxTime = (((Channeling) nextSpell).getChannelTime() - 1 < waitTime) ? ((Channeling) nextSpell).getChannelTime() - 1 : waitTime;
                SpellAndValue secondSelectedSpellWithValue = pickHighestValueSpell(candidates, maxTime);
                if (secondSelectedSpellWithValue != null && secondSelectedSpellWithValue.getSpell() != selectedSpellWithValue.getSpell()) {
                    WaitTimeAndDamageDiff secondWaitTimeAndDamageDiff = worthDoingNothing(target, secondSelectedSpellWithValue.getSpell(), candidates, timeLeft);
                    int secondWaitTime = secondWaitTimeAndDamageDiff.getWaitTime();
                    double secondDamageDifference = secondWaitTimeAndDamageDiff.getDamageDiff();
                    String secondNameWaitSpell = secondWaitTimeAndDamageDiff.getSpellName();

                    if (secondDamageDifference < damageDifference) {
                        waitTime = secondWaitTime;
                        nameWaitSpell = secondNameWaitSpell;
                        selectedSpellWithValue = secondSelectedSpellWithValue;
                    }
                }
            }
            if (waitTime > 0 && damageDifference > 0) {
                if (!recentlyHalted) {
                    System.out.println("Finding another spell with at most: " + waitTime + " castTime");
                }
                selectedSpellWithValue = pickHighestValueSpell(candidates, waitTime);
                if (selectedSpellWithValue == null) {
                    if (!recentlyHalted) {
                        recentlyHalted = true;
                        System.out.println("HAAAAAAAAAAAAAAALTING for: " + waitTime + " decisec, so we can put up: " + nameWaitSpell);
                        usedSpells.add("Halting " + waitTime + " decisec");
                    }
                    return null;
                }
            }

            int animationStart = selectedSpellWithValue.getSpell().getCastTime();
            if (usedSpellsWithTime.get(currentDecisecond - 1 + animationStart) != null) {
                animationStart++;
            }
            usedSpellsWithTime.put(currentDecisecond - 1 + animationStart, selectedSpellWithValue.getSpell());
        }

        if (selectedSpellWithValue == null) {
            return null;
        }

        return selectedSpellWithValue.getSpell();
    }

    /**
     * picks the Spell with the highest value from the ArrayList containing SpellAndValue objects
     * @param candidates SpellAndValue ArrayList
     * @param timeLeft the time we have to work with
     * @return the SpellAndValue with the highest Value from the candidates list
     */
    private SpellAndValue pickHighestValueSpell(List<SpellAndValue> candidates, int timeLeft) {

        //TODO picks a dot when it shouldn't (try simulate with only corruption & shadowbolt for 100) (should only spam SBs)

        SpellAndValue selectedSpellWithValue = null;
        List<SpellAndValue> cooldownSpells = new ArrayList<>();
        List<SpellAndValue> dotSpells = new ArrayList<>();

        for (SpellAndValue spellAndValue : candidates) {

            if (selectedSpellWithValue == null && spellAndValue.getSpell().getTimeTakenFromCaster() <= timeLeft) {
                selectedSpellWithValue = new SpellAndValue(spellAndValue.getSpell(), spellAndValue.getValue());

            } else {
                if (selectedSpellWithValue != null && spellAndValue.getValue() > selectedSpellWithValue.getValue() && spellAndValue.getSpell().getTimeTakenFromCaster() <= timeLeft) {
                    selectedSpellWithValue.setSpell(spellAndValue.getSpell());
                    selectedSpellWithValue.setValue(spellAndValue.getValue());
                }
            }

            if (spellAndValue.getSpell().getMaxCooldown() > 0) {
                cooldownSpells.add(spellAndValue);
            }

            if (spellAndValue.getSpell() instanceof DamageOverTime && !(spellAndValue.getSpell() instanceof Channeling)) {
                dotSpells.add(spellAndValue);
            }

        }

        if (selectedSpellWithValue != null) {
            for (SpellAndValue spellAndValue : cooldownSpells) {
                Spell currentSpell = spellAndValue.getSpell();
                int currentSpellTimeTaken = currentSpell.getTimeTakenFromCaster();
                int possibleUsesBefore = (timeLeft - currentSpellTimeTaken) / (currentSpellTimeTaken + currentSpell.getMaxCooldown());
                int possibleUsesAfter = (timeLeft - currentSpellTimeTaken - selectedSpellWithValue.getSpell().getTimeTakenFromCaster()) / (currentSpellTimeTaken + currentSpell.getMaxCooldown());
                if (possibleUsesAfter < possibleUsesBefore) {
                    SpellAndValue affectedSpellAndValue = candidates.get(candidates.indexOf(spellAndValue));
                    affectedSpellAndValue.setValue(affectedSpellAndValue.getValue() * 2);
                    if (affectedSpellAndValue.getValue() > selectedSpellWithValue.getValue()) {
                        selectedSpellWithValue = affectedSpellAndValue;
                        //System.out.println(affectedSpellAndValue.getSpell().getName() + " NEW value: " + affectedSpellAndValue.getValue());
                    }
                }
            }
        }

        if (selectedSpellWithValue != null) {
            for (SpellAndValue spellAndValue : dotSpells) {
                Spell currentSpell = spellAndValue.getSpell();
                int currentSpellTimeTaken = currentSpell.getTimeTakenFromCaster();
                int possibleTicksBefore = (timeLeft - currentSpellTimeTaken) / ((DamageOverTime) currentSpell).getTickInterval();
                int possibleTicksAfter = (timeLeft - currentSpellTimeTaken - selectedSpellWithValue.getSpell().getTimeTakenFromCaster()) / ((DamageOverTime) currentSpell).getTickInterval();
                if (possibleTicksAfter < possibleTicksBefore) {
                    int lessNumberOfTicks = possibleTicksBefore - possibleTicksAfter;
                    SpellAndValue affectedSpellAndValue = candidates.get(candidates.indexOf(spellAndValue));
                    double damagePerTick = affectedSpellAndValue.getSpell().getTotalDamage() / ((DamageOverTime) affectedSpellAndValue.getSpell()).getTotalTickNumber();
                    double openedValue = affectedSpellAndValue.getValue() * affectedSpellAndValue.getSpell().getTimeTakenFromCaster();
                    double newValue = (openedValue + (damagePerTick * lessNumberOfTicks)) / affectedSpellAndValue.getSpell().getTimeTakenFromCaster();
                    affectedSpellAndValue.setValue(newValue);
                    if (affectedSpellAndValue.getValue() > selectedSpellWithValue.getValue()) {
                        selectedSpellWithValue = affectedSpellAndValue;
                        //System.out.println(affectedSpellAndValue.getSpell().getName() + " NEW value: " + affectedSpellAndValue.getValue());
                    }
                }
            }
        }

        if (selectedSpellWithValue != null && selectedSpellWithValue.getValue() == 0.0) {
            selectedSpellWithValue = null;
        }

        return selectedSpellWithValue;
    }

    /**
     * selects the channel duration of a Channeling Spell which is closest to suggestedMaxTime but still smaller than the suggestedMaxTime
     * @param spell the Channeling Spell
     * @param candidates SpellAndValue ArrayList, needed to find the other Spells
     * @param suggestedMaxTime the maximum time the result's channel duration can be
     * @return highest value SpellAndValue but shorter than suggestedMaxTime
     */
    private SpellAndValue pickShortestDurationChannel(Channeling spell, List<SpellAndValue> candidates, int suggestedMaxTime) {

        SpellAndValue selectedSpellWithValue = null;

        for (SpellAndValue spellAndValue : candidates) {

            if (selectedSpellWithValue == null && spellAndValue.getSpell().getName().equals(spell.getName())) {
                selectedSpellWithValue = new SpellAndValue(spellAndValue.getSpell(), spellAndValue.getValue());

            } else {
                if (selectedSpellWithValue != null && spellAndValue.getSpell().getName().equals(spell.getName())) {
                    if (spellAndValue.getSpell().getTimeTakenFromCaster() < selectedSpellWithValue.getSpell().getTimeTakenFromCaster() && spellAndValue.getSpell().getTimeTakenFromCaster() <= suggestedMaxTime) {
                        selectedSpellWithValue.setSpell(spellAndValue.getSpell());
                        selectedSpellWithValue.setValue(spellAndValue.getValue());
                        return selectedSpellWithValue;
                    } else if (spellAndValue.getSpell().getTimeTakenFromCaster() < selectedSpellWithValue.getSpell().getTimeTakenFromCaster() && spellAndValue.getSpell().getTimeTakenFromCaster() > suggestedMaxTime) {
                        selectedSpellWithValue.setSpell(spellAndValue.getSpell());
                        selectedSpellWithValue.setValue(spellAndValue.getValue());
                    }
                }
            }

        }

        return selectedSpellWithValue;
    }

    /**
     * calculates if it is worth to wait for another Spell to either come off cooldown or a DamageOverTime to run out of duration
     * @param target the target
     * @param nextCalculatedSpell current calculated next Spell to use
     * @param candidates SpellAndValue ArrayList
     * @param timeLeft the time we have to work with
     * @return a WaitTimeAndDamageDiff object that will tell how much damage would be lost and how much time to wait
     */
    private WaitTimeAndDamageDiff worthDoingNothing(Target target, Spell nextCalculatedSpell, List<SpellAndValue> candidates, int timeLeft) {
        return worthDoingNothing(target, nextCalculatedSpell, candidates, timeLeft, 0);
    }

    /**
     * calculates if it is worth to wait for another Spell to either come off cooldown or a DamageOverTime to run out of duration
     * @param target the target
     * @param nextCalculatedSpell current calculated next Spell to use
     * @param candidates SpellAndValue ArrayList
     * @param timeLeft the time we have to work with
     * @param timeIntoFuture time we try to look into the future to see what will happen if we do something first
     * @return a WaitTimeAndDamageDiff object that will tell how much damage would be lost and how much time to wait
     */
    private WaitTimeAndDamageDiff worthDoingNothing(Target target, Spell nextCalculatedSpell, List<SpellAndValue> candidates, int timeLeft, int timeIntoFuture) {

        DamageOverTime dot = target.getNextDotTimeOut();
        Spell shortCooldownSpell = getShortestCooldownSpell();
        boolean haveDot = false;
        boolean haveCooldownSpell = false;
        if (dot != null && shortCooldownSpell != null) {
            if (dot.getDuration() < shortCooldownSpell.getCooldown()) {
                haveDot = true;
            } else {
                haveCooldownSpell = true;
            }
        } else if (dot != null) {
            haveDot = true;
        } else if (shortCooldownSpell != null) {
            haveCooldownSpell = true;
        }

        double damageDotWouldHaveDone;
        double damageShortCooldownWouldHaveDone;
        double damageNextSpellWouldHaveDone;
        int downTimeNextSpell;
        double lossFromSkippingDot = 0;
        double lossFromSkippingShortCooldownSpell = 0;

        if (haveDot) {

            if (timeIntoFuture != 0 && nextCalculatedSpell instanceof Channeling) {
                SpellAndValue spellAndValue = pickShortestDurationChannel((Channeling) nextCalculatedSpell, candidates, (dot.getDuration() - timeIntoFuture));
                nextCalculatedSpell = spellAndValue.getSpell();
            }

            int downTimeDoT = downTimeIfCastNext(dot, nextCalculatedSpell, timeIntoFuture);
            downTimeNextSpell = downTimeIfCastNext(nextCalculatedSpell, dot, timeIntoFuture);

            if (downTimeDoT > 0 || (downTimeNextSpell + downTimeDoT) == nextCalculatedSpell.getTimeTakenFromCaster()) {

                damageDotWouldHaveDone = damageSpellWouldHaveDone(dot, downTimeDoT, timeLeft - timeIntoFuture);
                damageNextSpellWouldHaveDone = damageSpellWouldHaveDone(nextCalculatedSpell, downTimeNextSpell, timeLeft - timeIntoFuture);

                lossFromSkippingDot = damageDotWouldHaveDone - damageNextSpellWouldHaveDone;

                if (false) {
                    System.out.println(dot.getName() + " har lägst tid: " + (dot.getDuration() - timeIntoFuture) + " decisec");
                    System.out.println("om vi castar: " + nextCalculatedSpell.getName());
                    System.out.println("då skulle " + dot.getName() + " vara nere i: " + downTimeDoT + " decisec");
                    System.out.println("på " + downTimeDoT + " decisec skulle " + dot.getName() + " hinna göra: " + damageDotWouldHaveDone + " damage");
                    System.out.println("på " + downTimeNextSpell + " decisec skulle " + nextCalculatedSpell.getName() + " hinna göra: " + damageNextSpellWouldHaveDone + " damage");
                }

                int halfTickInterval = 0;
                if (nextCalculatedSpell instanceof Channeling) {
                    halfTickInterval = ((Channeling) nextCalculatedSpell).getTickInterval() / 2;
                }
                if (downTimeDoT < 0 && ((downTimeDoT >= (nextCalculatedSpell.getTimeTakenFromCaster() - halfTickInterval) * -1) || (downTimeDoT == nextCalculatedSpell.getTimeTakenFromCaster() * -1))) {
                    System.out.println("looking into the future for: " + (nextCalculatedSpell.getTimeTakenFromCaster() + timeIntoFuture) + " decisec");
                    WaitTimeAndDamageDiff waitTimeAndDamageDiff = worthDoingNothing(target, nextCalculatedSpell, candidates, timeLeft, (nextCalculatedSpell.getTimeTakenFromCaster() + timeIntoFuture));
                    if (waitTimeAndDamageDiff.getDamageDiff() > 0) {
                        return new WaitTimeAndDamageDiff(dot.getDuration() - dot.getCastTime(), lossFromSkippingDot, dot.getName());
                    }
                    return new WaitTimeAndDamageDiff(dot.getDuration() - dot.getCastTime(), lossFromSkippingDot + waitTimeAndDamageDiff.getDamageDiff(), dot.getName());
                }
            }

            return new WaitTimeAndDamageDiff(dot.getDuration() - dot.getCastTime(), lossFromSkippingDot, dot.getName());

        }

        if (haveCooldownSpell)

        {

            if (timeIntoFuture != 0 && nextCalculatedSpell instanceof Channeling) {
                SpellAndValue spellAndValue = pickShortestDurationChannel((Channeling) nextCalculatedSpell, candidates, (shortCooldownSpell.getCooldown() - timeIntoFuture));
                nextCalculatedSpell = spellAndValue.getSpell();
            }

            int downTimeCooldownSpell = downTimeIfCastNext(shortCooldownSpell, nextCalculatedSpell, timeIntoFuture);
            downTimeNextSpell = downTimeIfCastNext(nextCalculatedSpell, shortCooldownSpell, timeIntoFuture);

            if (downTimeCooldownSpell > 0 || (downTimeNextSpell + downTimeCooldownSpell) == nextCalculatedSpell.getTimeTakenFromCaster()) {

                damageShortCooldownWouldHaveDone = damageSpellWouldHaveDone(shortCooldownSpell, downTimeCooldownSpell, timeLeft - timeIntoFuture);
                damageNextSpellWouldHaveDone = damageSpellWouldHaveDone(nextCalculatedSpell, downTimeNextSpell, timeLeft - timeIntoFuture);

                lossFromSkippingShortCooldownSpell = damageShortCooldownWouldHaveDone - damageNextSpellWouldHaveDone;

                if (false) {
                    System.out.println(shortCooldownSpell.getName() + " har lägst tid: " + (shortCooldownSpell.getCooldown() - timeIntoFuture) + " decisec");
                    System.out.println("om vi castar: " + nextCalculatedSpell.getName());
                    System.out.println("då skulle " + shortCooldownSpell.getName() + " vara nere i: " + downTimeCooldownSpell + " decisec");
                    System.out.println("på " + downTimeCooldownSpell + " decisec skulle " + shortCooldownSpell.getName() + " hinna göra: " + damageShortCooldownWouldHaveDone + " damage");
                    System.out.println("på " + downTimeNextSpell + " decisec skulle " + nextCalculatedSpell.getName() + " hinna göra: " + damageNextSpellWouldHaveDone + " damage");
                }

                int halfTickInterval = 0;
                if (nextCalculatedSpell instanceof Channeling) {
                    halfTickInterval = ((Channeling) nextCalculatedSpell).getTickInterval() / 2;
                }

                if (downTimeCooldownSpell < 0 && ((downTimeCooldownSpell >= (nextCalculatedSpell.getTimeTakenFromCaster() - halfTickInterval) * -1) || (downTimeCooldownSpell == nextCalculatedSpell.getTimeTakenFromCaster() * -1))) {
                    System.out.println("looking into the future for: " + (nextCalculatedSpell.getTimeTakenFromCaster() + timeIntoFuture) + " decisec");
                    WaitTimeAndDamageDiff waitTimeAndDamageDiff = worthDoingNothing(target, nextCalculatedSpell, candidates, timeLeft, (nextCalculatedSpell.getTimeTakenFromCaster() + timeIntoFuture));
                    if (waitTimeAndDamageDiff.getDamageDiff() > 0) {
                        return new WaitTimeAndDamageDiff(shortCooldownSpell.getCooldown(), lossFromSkippingShortCooldownSpell, shortCooldownSpell.getName());
                    }
                    return new WaitTimeAndDamageDiff(shortCooldownSpell.getCooldown(), lossFromSkippingShortCooldownSpell + waitTimeAndDamageDiff.getDamageDiff(), shortCooldownSpell.getName());
                }
            }

            return new WaitTimeAndDamageDiff(shortCooldownSpell.getCooldown(), lossFromSkippingShortCooldownSpell, shortCooldownSpell.getName());
        }

        return new WaitTimeAndDamageDiff(0, 0, "NO SPELL");
    }

    /**
     * finds the Spell in our Spells list that will be the next to come off cooldown
     * @return a Spell with a the shortest cooldown
     */
    private Spell getShortestCooldownSpell() {
        Spell spell = null;
        int lowestCooldown = Integer.MAX_VALUE;

        for (Map.Entry<String, Spell> entry : spells.entrySet()) {
            Spell currentSpell = entry.getValue();
            if (spell == null && currentSpell.getCooldown() > 0) {
                spell = currentSpell;
                lowestCooldown = currentSpell.getCooldown();
            } else if (currentSpell.getCooldown() > 0 && currentSpell.getCooldown() < lowestCooldown) {
                spell = currentSpell;
                lowestCooldown = currentSpell.getCooldown();
            }
        }

        return spell;
    }

    /**
     * @param firstSpell  can be spell or dot
     * @param secondSpell can be spell or dot
     * @return will return how much time firstSpell would be down if we cast secondSpell
     */
    //TODO should be private, public only for testing
    public int downTimeIfCastNext(Spell firstSpell, Spell secondSpell, int timeIntoFuture) {
        if (firstSpell instanceof Channeling && secondSpell instanceof DamageOverTime) {
            return ((DamageOverTime) secondSpell).getDuration() - timeIntoFuture - secondSpell.getCastTime();
        } else if (firstSpell instanceof Channeling || (firstSpell instanceof DamageOverTime && secondSpell.getCooldown() - timeIntoFuture > 0)) {
            return (secondSpell.getCooldown() - timeIntoFuture);
        } else if (firstSpell instanceof DamageOverTime) {
            return secondSpell.getTimeTakenFromCaster() - (((DamageOverTime) firstSpell).getDuration() - timeIntoFuture);
        } else if (secondSpell instanceof DamageOverTime && firstSpell.getCooldown() - timeIntoFuture == 0) {
            return ((DamageOverTime) secondSpell).getDuration() - timeIntoFuture - secondSpell.getCastTime();
        } else {
            return secondSpell.getTimeTakenFromCaster() - (firstSpell.getCooldown() - timeIntoFuture);
        }
    }

    /**
     * calculates how much damage a Spell would have done given a downtime for it and a timeLeft
     * @param spell the Spell
     * @param downTime time that the Spell would not be in use
     * @param timeLeft the time we have to work with
     * @return double how much damage that will be lost
     */
    private double damageSpellWouldHaveDone(Spell spell, int downTime, int timeLeft) {
        double result = 0;
        downTime = (spell.getTimeTakenFromCaster() < downTime) ? spell.getTimeTakenFromCaster() : downTime;

        if (spell instanceof Channeling) {
            //downTime = (((Channeling) spell).getChannelTime() < downTime) ? ((Channeling) spell).getChannelTime() : downTime;
            //result = (spell.getTotalDamage() / ((Channeling) spell).getChannelTime() * downTime);
            result = ((Channeling) spell).calculateChannelingDamage(downTime);
        } else if (spell instanceof DamageOverTime) {
            //int timeTheDotCouldBeUp = (downTime < timeLeft) ? downTime : timeLeft;
            //result = (spell.getTotalDamage() / ((DamageOverTime) spell).getMaxDuration()) * timeTheDotCouldBeUp;
            int timeTheDotCouldBeUp = (((DamageOverTime) spell).getMaxDuration() < timeLeft) ? ((DamageOverTime) spell).getMaxDuration() : timeLeft;
            result = (((DamageOverTime) spell).calculateDotDamage(timeLeft) / timeTheDotCouldBeUp) * downTime;
        } else {
            if (spell.getCastTime() <= timeLeft) {
                result = ((double) downTime / (spell.getCastTime())) * spell.getTotalDamage();
            }
        }
        result = (result < 0) ? 0 : result;

        return result;
    }
}
























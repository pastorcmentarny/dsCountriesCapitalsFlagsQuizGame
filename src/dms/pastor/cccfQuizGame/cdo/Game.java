package dms.pastor.cccfQuizGame.cdo;

import java.util.Calendar;
import java.util.List;
import java.util.Random;

import dms.pastor.cccfQuizGame.Config;
import dms.pastor.cccfQuizGame.cdo.enums.GameMode;
import dms.pastor.cccfQuizGame.cdo.enums.GameType;

import static dms.pastor.cccfQuizGame.Config.FREEZE_RANGE;
import static dms.pastor.cccfQuizGame.Config.MISTAKE_PENALTY_LIMIT_FOR_TIME_ATTACK;
import static dms.pastor.cccfQuizGame.Config.SECONDS;
import static dms.pastor.cccfQuizGame.Config.TIME_PER_ANSWER;
import static dms.pastor.cccfQuizGame.Config.UNFREEZE_BONUS;

/**
 * Author Dominik Symonowicz
 * Created 20/03/2013
 * WWW:	http://pastor.ovh.org
 * Github:	https://github.com/pastorcmentarny
 * Google Play:	https://play.google.com/store/apps/developer?id=Dominik+Symonowicz
 * LinkedIn: uk.linkedin.com/pub/dominik-symonowicz/5a/706/981/
 * <p>
 * Country representation for this game and dom learns chinese
 */
public final class Game {
    private static Game game = null;
    private List<Country> countries;
    private List<Country> levelList;
    private int correct = 0;
    private int mistake = 0;
    private int level = 0;
    private GameType askFor;
    private GameType answer;
    private GameMode gameMode;
    private long start;
    private long finish;
    private int mistakesPenalty = 1;
    private int health = 0;
    private int badAnswerRemoverLeft = 0;
    private int score = 0;
    private int wallHP = 0;
    private int originalWallHp = 0;
    private boolean frozen;    //TODO move from game to here
    private int unFreezeBonus;

    private Game(GameType question, GameType answer, GameMode gameMode) {
        setGameMode(gameMode);
        level = 1;
        correct = 0;
        mistake = 0;
        badAnswerRemoverLeft = 0;
        if (this.gameMode == GameMode.ADVENTURE) {
            mistakesPenalty = 1;
            health = 100;
            score = 0;
        } else {
            this.askFor = question;
            this.answer = answer;
            start = 0;
            finish = 0;
        }
    }

    public static synchronized Game getGameFor(GameType question, GameType answer, GameMode gameMode) {
        if (game == null) {
            game = new Game(question, answer, gameMode);
        }
        return game;
    }

    public static Game game() {
        return game;
    }

    public static Game createAdventureGame() {
        return getGameFor(null, null, GameMode.ADVENTURE);
    }

    //TODO replace this with better implementation
    private static int fibonacciPenalty(int number, int limit) {
        if (number <= 2) {
            return 1;
        }
        int fibonacci1 = 1;
        int fibonacci2 = 1;
        int fibonacci = 1;
        for (int i = 3; i <= number; i++) {
            fibonacci = fibonacci1 + fibonacci2;
            fibonacci1 = fibonacci2;
            fibonacci2 = fibonacci;
            if (fibonacci >= limit) {
                return limit;
            }
        }
        if (fibonacci < Config.MIN_PENALTY) {
            return Config.MIN_PENALTY;
        }
        return (fibonacci > limit) ? limit : fibonacci;
    }

    public static void kill() {
        game = null;
    }

    @Override
    public final Object clone() throws CloneNotSupportedException {
        throw new CloneNotSupportedException();
    }

    public void restartGame() {
        if (gameMode.equals(GameMode.ADVENTURE)) {
            level = 1;
            correct = 0;
            mistake = 0;
            start = 0;
            finish = 0;
            mistakesPenalty = 1;
            health = 100;
            score = 0;
            badAnswerRemoverLeft = 0;
        } else {
            level = 1;
            correct = 0;
            mistake = 0;
            start = 0;
            finish = 0;
        }
    }

    public List<Country> getCountries() {
        return countries;
    }

    public void setCountries(List<Country> countries) {
        this.countries = countries;
    }

    public List<Country> getLevelList() {
        return levelList;
    }

    public void setLevelList(List<Country> levelList) {
        this.levelList = levelList;
    }

    int getMistakesPenalty() {
        return mistakesPenalty;
    }

    public int getCorrect() {
        return correct;
    }

    public int getMistake() {
        return mistake;
    }

    public void addMistake() {
        mistake++;
    }

    public int getLevel() {
        return level;
    }

    public void addLevel() {
        level++;
    }

    public void addCorrect() {
        correct++;
    }

    public GameType getAskFor() {
        return askFor;
    }

    public void setAskFor(GameType gameType) {
        this.askFor = gameType;
    }

    public GameType getAnswer() {
        return answer;
    }

    public void setAnswer(GameType gameType) {
        this.answer = gameType;
    }

    public boolean isLastLevel() {
        return level >= levelList.size();
    }

    public int calculateScore() {
        switch (gameMode) {
            case PRACTICE:
                return calculatePracticeScore();
            case SAPER:
                return getLevel() - 1;
            case TIME_ATTACK:
                return calculateTimeAttackScore();
            case ADVENTURE:
                return getScore();
            case WALKTHROUGH:
                return calculateWalkthroughScore();
            default:
                return -1;
        }
    }

    //TODO move stopwatch to separate class
    public void start() {
        finish = 0;
        start = Calendar.getInstance().getTimeInMillis();
    }

    public long calcTotalTime() {
        return finish - start;
    }

    public long calcCurrentTime() {
        return Calendar.getInstance().getTimeInMillis() - start;
    }

    public void stop() {
        finish = Calendar.getInstance().getTimeInMillis();
    }

    public void resetTimer() {
        start = 0;
        finish = 0;
    }

    public long getTotalTime() {
        return finish - start;
    }

    public GameMode getGameMode() {
        return gameMode;
    }

    private void setGameMode(GameMode gameMode) {
        this.gameMode = gameMode;
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public boolean isAlive() {
        return (getHealth() > 0);
    }

    public void doPenalty() {
        addMistake();
        mistakesPenalty++;
        int dmg = fibonacciPenalty(mistakesPenalty, Config.MISTAKE_PENALTY_LIMIT_FOR_HEALTH);
        health -= getPositiveNumberScore(dmg);
    }

    public void newLevel() {
        if (health < 100) {
            health++;
        }
        if (health > 100) {
            health--;
        }
        if (mistakesPenalty > 1) {
            mistakesPenalty--;
        }
    }

    private int calculatePracticeScore() {
        int score;
        score = getCorrect() * 100 / getLevelList().size();
        score -= getMistake();
        return getPositiveNumberScore(score);
    }

    private int getPositiveNumberScore(int score) {
        return score > 0 ? score : 0;
    }

    private int calculateWalkthroughScore() {
        int score;
        score = getCountries().size();
        score += score * 4 - Math.round(getTotalTime() / SECONDS);
        score -= mistake;
        return getPositiveNumberScore(score);
    }

    private int calculateTimeAttackScore() {
        int score;
        score = 100;
        int penalty = (int) (Math.round(getTotalTime() / SECONDS) - Math.round(getLevelList().size() * TIME_PER_ANSWER));
        score -= penalty;
        score -= fibonacciPenalty(getMistake(), MISTAKE_PENALTY_LIMIT_FOR_TIME_ATTACK);
        return getPositiveNumberScore(score);
    }

    void reducePainKiller() {
        mistakesPenalty -= new Random().nextInt(4) - 1;
        if (mistakesPenalty < 0) {
            mistakesPenalty = 0;
        }
    }

    public int getScore() {
        return score;
    }

    public void addScore(int bonusPoint) {
        score += bonusPoint;
    }

    public int getBadAnswerRemoverLeft() {
        return badAnswerRemoverLeft;
    }

    public void setBadAnswerRemoverLeft(int badAnswerRemoverLeft) {
        this.badAnswerRemoverLeft = badAnswerRemoverLeft;
    }

    public int useBadAnswerRemover() {
        badAnswerRemoverLeft--;
        return getBadAnswerRemoverLeft();
    }

    public void unfreeze() {
        score += unFreezeBonus;
        originalWallHp = 0;
        frozen = false;
    }

    private void setUnFreezeBonus(int unFreezeBonus) {
        this.unFreezeBonus = unFreezeBonus * UNFREEZE_BONUS;
    }

    public void reduceWallHP() {
        wallHP--;
    }

    public boolean isWallHasHP() {
        return wallHP <= 0;
    }

    public int getWallHP() {
        return wallHP;
    }

    private void setWallHP(int wallHP) {
        this.wallHP = wallHP;
    }

    public boolean isFrozen() {
        return frozen;
    }

    public void setFrozen(boolean freeze) {
        this.frozen = freeze;
        if (frozen) {
            setWallHP(new Random().nextInt(FREEZE_RANGE) + 1);
            originalWallHp = wallHP;
            setUnFreezeBonus(originalWallHp);
        }
    }
}

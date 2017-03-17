package dms.pastor.cccfQuizGame.cdo;

import android.content.Context;

import java.util.Random;

import dms.pastor.cccfQuizGame.Config;

import static dms.pastor.cccfQuizGame.Utils.DomUtils.showToast;

/**
 * Author Dominik Symonowicz
 * Created 17/01/2013
 * WWW:	http://pastor.ovh.org
 * Github:	https://github.com/pastorcmentarny
 * Google Play:	https://play.google.com/store/apps/developer?id=Dominik+Symonowicz
 * LinkedIn: uk.linkedin.com/pub/dominik-symonowicz/5a/706/981/
 * <p>
 * It responses for handle all events that randomly happen during game
 */
public class Event {
    private final Game game = Game.getGameFor(null, null, null);

    public static void eventHappen(Context context, String message) {
        showToast(context, message);
    }

    public String castMinorPointsBonus() {
        int bonus = new Random().nextInt(game.getLevel() + 1);
        bonus += (game.getHealth() + new Random().nextInt(50)) + 1;
        game.addScore(bonus);
        return "every little bonus helps(" + bonus + ")!";
    }

    public String castPoison() {
        int poison = new Random().nextInt((game.getHealth() / 2) + 1);
        game.setHealth(game.getHealth() - poison);
        return "Poison caused " + poison + " damage.";
    }

    public String castJackpot() {
        int jackpot = 1000 + new Random().nextInt((game.getLevel() * 10) + 1) + new Random().nextInt((game.getLevel() * 10) + 1) + new Random().nextInt((game.getLevel() * 10) + 1);
        game.addScore(jackpot);
        return "Teleporting to next level (bonus:" + jackpot + ")";
    }

    public String castHeal() {
        int healBy = new Random().nextInt(Config.HEAL_HP_VALUE + 1);
        game.setHealth(game.getHealth() + healBy);
        return "Heal by " + healBy;
    }

    public String nonMistakeCombo(int combo) {
        int db25 = combo * combo * 2 / 5;
        int db10 = combo * (combo / 2) / 3;
        int comboPoints = combo + (combo + (db10 * 7)) + combo + (db25 * 9);
        game.addScore(comboPoints);
        return "No Mistake Combo (" + combo + "x) Bonus: " + comboPoints + "points.";
    }

    public String painKiller() {
        game.reducePainKiller();
        return "Painkiller applied. Current penalty: " + game.getMistakesPenalty() + " hp.";
    }

    public String doubleHP() {
        final int originalHp = game.getHealth();
        game.setHealth(originalHp * 2);
        return "Your HP was doubled (+" + originalHp + "hp).";
    }

    public String halfHP() {
        game.setHealth(game.getHealth() / 2);
        return "Your HP is halved (-" + (game.getHealth()) + "hp).";
    }
}

package dms.pastor.cccfQuizGame;


/**
 * Author Dominik Symonowicz
 * Created 07/01/2013 22:09
 * WWW:	http://pastor.ovh.org
 * Github:	https://github.com/pastorcmentarny
 * Google Play:	https://play.google.com/store/apps/developer?id=Dominik+Symonowicz
 * LinkedIn: uk.linkedin.com/pub/dominik-symonowicz/5a/706/981/
 * <p>
 * Settings with common constant values for application
 */
public final class Config {
    public static final int[] BONUS_POINTS = {25, 10, 3, 0, -1, -7};
    public static final int COUNTRIES = R.raw.countries;
    public static final long VIBRATE_ON_MISTAKE_TIME = 200;
    public static final int HALF_SECOND = 500;
    public static final int TIME_REDUCTION_PER_LEVEL = 20;
    public static final int REFRESH_RATE = 100;
    public static final int PENALTY_TIME_UNIT = 400;
    public static final int UNFREEZE_BONUS = 7;
    public static final String AD_INTERSTITIAL_UNIT = "ca-app-pub-1669938002445825/3647917900";
    public static final double TIME_PER_ANSWER = 2.5;
    public static final int GAME_LEVELS = 50;
    public static final int SECONDS = 1000;
    public static final int NO_PENALTY_TIME = 5;
    public static final int RANDOM_SIZE = 135;
    public static final int HEAL_HP_VALUE = 8;
    public static final int REMOVE_BAD_ANSWER_INIT_VALUE = 3;
    public static final int MISTAKE_PENALTY_LIMIT_FOR_HEALTH = 25;
    public static final int MISTAKE_PENALTY_LIMIT_FOR_TIME_ATTACK = 512;
    public static final int MIN_PENALTY = 5;
    public static final String DOMS = "DOMS";
    public static final int FREEZE_RANGE = 50;
    public static final String DEFAULT_NAME = "no name";

    private Config() {
    }
}

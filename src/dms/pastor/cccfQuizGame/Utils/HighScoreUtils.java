package dms.pastor.cccfQuizGame.Utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

/**
 * Author Dominik Symonowicz
 * Created: 14/02/2017
 * WWW:	http://pastor.ovh.org
 * Github:	https://github.com/pastorcmentarny
 * Google Play:	https://play.google.com/store/apps/developer?id=Dominik+Symonowicz
 * LinkedIn: uk.linkedin.com/pub/dominik-symonowicz/5a/706/981/
 * <p>
 * Contains all commons methods for UI manipulation
 */
public class HighScoreUtils {

    public static String getHighScores(Activity activity) {
        StringBuilder highScores = new StringBuilder(StringUtils.EMPTY_STRING);
        SharedPreferences sharedPreferences = activity.getSharedPreferences("highscore", Context.MODE_PRIVATE);
        highScores.append("ADVENTURE: ").append(sharedPreferences.getInt("adventure1", 0)).append(" points.\n");
        highScores.append("TIME ATTACK: ").append(DomUtils.getResultTimeAsString(sharedPreferences.getLong("timeAttack", 0))).append(" seconds.\n");
        highScores.append("SAPPER: ").append(sharedPreferences.getInt("saper", 0)).append(" level\n");
        highScores.append("WALKTHROUGH: ").append(sharedPreferences.getInt("walkthrough", 0)).append("points.\n");

        return highScores.toString();
    }
}

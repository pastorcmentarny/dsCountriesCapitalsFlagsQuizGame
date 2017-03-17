package dms.pastor.cccfQuizGame.cdo;

import java.util.Calendar;

/**
 * Author Dominik Symonowicz
 * Created 16/11/2012 08:32
 * WWW:	http://pastor.ovh.org
 * Github:	https://github.com/pastorcmentarny
 * Google Play:	https://play.google.com/store/apps/developer?id=Dominik+Symonowicz
 * LinkedIn: uk.linkedin.com/pub/dominik-symonowicz/5a/706/981/
 */
public class LevelTimer {
    private long start;

    public void start() {
        start = Calendar.getInstance().getTimeInMillis();
    }

    public int calcCurrentTimeInMilliSeconds() {
        return (int) (Calendar.getInstance().getTimeInMillis() - start);
    }

    public void resetTimer() {
        start = 0;
    }
}

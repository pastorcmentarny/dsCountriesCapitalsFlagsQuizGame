package dms.pastor.cccfQuizGame.Utils;

/**
 * Author Dominik Symonowicz
 * Created 14/02/2017
 * WWW:	http://pastor.ovh.org
 * Github:	https://github.com/pastorcmentarny
 * Google Play:	https://play.google.com/store/apps/developer?id=Dominik+Symonowicz
 * LinkedIn: uk.linkedin.com/pub/dominik-symonowicz/5a/706/981/
 * <p>
 * Utils for strings
 */
public class StringUtils {
    public static final String EMPTY_STRING = "";
    public static final String WHITESPACE = " ";

    public static boolean isStringEmpty(String string) {
        return (string != null && !string.equals(EMPTY_STRING));
    }
}

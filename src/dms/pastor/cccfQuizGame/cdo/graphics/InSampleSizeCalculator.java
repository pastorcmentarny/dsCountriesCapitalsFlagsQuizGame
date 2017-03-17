package dms.pastor.cccfQuizGame.cdo.graphics;

import android.graphics.BitmapFactory;

/**
 * Author Dominik Symonowicz
 * Created 14/02/2017
 * WWW:	http://pastor.ovh.org
 * Github:	https://github.com/pastorcmentarny
 * Google Play:	https://play.google.com/store/apps/developer?id=Dominik+Symonowicz
 * LinkedIn: uk.linkedin.com/pub/dominik-symonowicz/5a/706/981/
 */
public class InSampleSizeCalculator {
    public static final int DEFAULT_IN_SAMPLE_SIZE = 8;
    private static final int SAMPLED_BITMAP_WIDTH = 240;
    private static final int SAMPLED_BITMAP_HEIGHT = 160;

    private InSampleSizeCalculator() {
    }

    static int calculate(
            BitmapFactory.Options options) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > SAMPLED_BITMAP_HEIGHT || width > SAMPLED_BITMAP_WIDTH) {
            if (width > height) {
                inSampleSize = Math.round((float) height / (float) SAMPLED_BITMAP_HEIGHT);
            } else {
                inSampleSize = Math.round((float) width / (float) SAMPLED_BITMAP_WIDTH);
            }
        }
        return inSampleSize;
    }
}

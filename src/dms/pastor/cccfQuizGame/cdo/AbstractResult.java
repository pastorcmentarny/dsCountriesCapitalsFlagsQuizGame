package dms.pastor.cccfQuizGame.cdo;

import android.app.Activity;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.InterstitialAd;

import static android.view.View.VISIBLE;

/**
 * #author: Pastor cmentarny
 * #WWW: http://pastor.ovh.org
 * #Github: https://github.com/pastorcmentarny
 * #Google Play:
 * https://play.google.com/store/apps/developer?id=Dominik+Symonowicz
 * #LinkedIn: uk.linkedin.com/pub/dominik-symonowicz/5a/706/981/
 * #Email: email can be found on my website
 * <p/>
 * It displays Result from game.
 */
public abstract class AbstractResult extends Activity implements View.OnClickListener {

    private static final String TAG = "Result";
    private static final long COUNTDOWN_INTERVAL = 2000;
    private static final long MILLISECONDS_IN_FUTURE = 1000;
    private final InterstitialAd interstitial = null;
    protected Button backToMainMenu, restart;
    protected final AdListener adl = getAdListener();

    @Override
    protected final void onResume() {
        super.onResume();
        activateCountTimer();
    }

    private void activateCountTimer() {
        CountDownTimer countDownTimer = getCountDownTimer();
        countDownTimer.start();
    }

    @SuppressWarnings("ConstantConditions") // interstitial can be null
    private void displayInterstitial() {
        Log.w(TAG, "displaying interstitial");
        if (interstitial != null && interstitial.isLoaded()) {
            interstitial.show();
        }
    }

    private void setVisibilityForMenuItems() {
        backToMainMenu.setVisibility(VISIBLE);
        restart.setVisibility(VISIBLE);
    }

    @NonNull
    private CountDownTimer getCountDownTimer() {
        return new CountDownTimer(MILLISECONDS_IN_FUTURE, COUNTDOWN_INTERVAL) {

            public void onTick(long millisUntilFinished) {
                //NOT IN USE :)
            }

            public void onFinish() {
                backToMainMenu.setVisibility(VISIBLE);
                restart.setVisibility(VISIBLE);
            }
        };
    }

    @NonNull
    private AdListener getAdListener() {
        return new AdListener() {
            @Override
            public void onAdLoaded() {
                displayInterstitial();
                setVisibilityForMenuItems();
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                setVisibilityForMenuItems();
            }
        };
    }
}

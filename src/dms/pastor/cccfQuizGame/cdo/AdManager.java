package dms.pastor.cccfQuizGame.cdo;

import android.app.Activity;
import android.util.Log;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;

import dms.pastor.cccfQuizGame.Config;
import dms.pastor.cccfQuizGame.R;
import dms.pastor.cccfQuizGame.Utils.Utils;

/**
 * Author Dominik Symonowicz
 * Created 14/02/2017
 * WWW:	http://pastor.ovh.org
 * Github:	https://github.com/pastorcmentarny
 * Google Play:	https://play.google.com/store/apps/developer?id=Dominik+Symonowicz
 * LinkedIn: uk.linkedin.com/pub/dominik-symonowicz/5a/706/981/
 * <p>
 * Response for Ads used in this application
 */
public class AdManager {

    private static final String TAG = "Ad Manager";

    public static void loadAd(Activity activity) {
        AdView adView = activity.findViewById(R.id.adView);
        try {
            AdRequest adRequest = Utils.getAdRequest();
            adView.loadAd(adRequest);
        } catch (Exception e) {
            Log.d(activity.getString(R.string.admob_e), activity.getString(R.string.admob_error_message) + e.getMessage());
        }
    }

    public static void loadInterstitialAd(Activity activity, AdListener adListener) {
        try {
            InterstitialAd interstitial = new InterstitialAd(activity);
            interstitial.setAdUnitId(Config.AD_INTERSTITIAL_UNIT);
            interstitial.setAdListener(adListener);
            AdRequest adRequest = new AdRequest.Builder().build();
            interstitial.loadAd(adRequest);
        } catch (Exception e) {
            Log.w(TAG, "Problem with interstitial");
        }
    }

}

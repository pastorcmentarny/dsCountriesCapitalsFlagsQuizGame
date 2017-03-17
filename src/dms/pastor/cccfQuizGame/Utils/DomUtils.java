package dms.pastor.cccfQuizGame.Utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.util.Log;

import java.io.IOException;

import dms.pastor.cccfQuizGame.AppLauncher;
import dms.pastor.cccfQuizGame.Config;
import dms.pastor.cccfQuizGame.R;

import static android.widget.Toast.LENGTH_LONG;
import static android.widget.Toast.makeText;

/**
 * Author Dominik Symonowicz
 * Created: 07/01/2013 19:26
 * WWW:	http://pastor.ovh.org
 * Github:	https://github.com/pastorcmentarny
 * Google Play:	https://play.google.com/store/apps/developer?id=Dominik+Symonowicz
 * LinkedIn: uk.linkedin.com/pub/dominik-symonowicz/5a/706/981/
 * <p>
 * Contains all commons methods for UI manipulation
 */
public final class DomUtils {
    private static final String TAG = "DMS/CCF/DomUtils";

    private DomUtils() {
    }

    public static int getResultIn0to100Range(int result) {
        if (result > 100) {
            return 100;
        }
        if (result < 0) {
            return 0;
        }
        return result;
    }

    public static String getAppVersion(Context context) {
        try {
            return "v" + context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName +
                    "(" + context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionCode + ")";
        } catch (PackageManager.NameNotFoundException ntfe) {
            return context.getResources().getString(R.string.unknown);
        }
    }

    public static String getResultTimeAsString(long time) {
        int sec = (int) time / 1000;
        int dot = (int) time % 1000;
        return String.valueOf(sec) + "." + String.valueOf(dot) + "s.";
    }

    public static void showToast(Context context, String msg) {
        makeText(context, msg, LENGTH_LONG).show();
    }

    public static void displayError(Context context, String error) {
        makeText(context, context.getResources().getString(R.string.error) + error + context.getResources().getString(R.string.e_msg), LENGTH_LONG).show();
    }

    public static void playTestTune(Context context) {
        AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        MediaPlayer mediaPlayer = new MediaPlayer();
        if (audioManager == null) {
            Log.w(TAG, "No audio service");
            return;
        }

        int volume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mediaPlayer.setVolume(volume, volume);
        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, volume, AudioManager.FLAG_PLAY_SOUND);
        mediaPlayer.setLooping(false);

        try {
            mediaPlayer.setDataSource(context, Uri.parse("android.resource://dms.pastor.cccfQuizGame/" + R.raw.freezed));
            mediaPlayer.setLooping(false);
            mediaPlayer.prepare();
            mediaPlayer.start();
        } catch (IllegalStateException ise) {
            try {
                mediaPlayer.stop();
                Utils.displayErrorForISE("IllegalStateException", " preparing or play sound", ise.getMessage());
            } catch (IllegalStateException ise2) {
                Utils.displayErrorForISE("IllegalStateException", " stopping play sound in playTestTune(IllegalStateException)", ise2.getMessage());
            }
        } catch (IllegalArgumentException iae) {
            try {
                mediaPlayer.stop();
                Utils.displayErrorForISE("IllegalArgumentException", " preparing or play sound", iae.getMessage());
            } catch (IllegalStateException ise) {
                Utils.displayErrorForISE("IllegalStateException", " stopping play sound in playTestTune(IllegalArgumentException)", ise.getMessage());
            }
            Log.e(Config.DOMS, "IllegalArgumentException occurred. " + iae.getMessage());
        } catch (SecurityException se) {
            try {
                mediaPlayer.stop();
                Utils.displayErrorForISE("SecurityException", " preparing or play sound", se.getMessage());
            } catch (IllegalStateException ise) {
                Utils.displayErrorForISE("IllegalStateException", " stopping play sound in playTestTune(SecurityException)", ise.getMessage());
            }
        } catch (IOException ioe) {
            try {
                mediaPlayer.stop();
                Utils.displayErrorForISE("IOException", " preparing or play sound", ioe.getMessage());
            } catch (IllegalStateException ise) {
                Utils.displayErrorForISE("IllegalStateException", " stopping play sound in playTestTune(IOException)", ise.getMessage());
            }
        } catch (NullPointerException npe) {
            try {
                mediaPlayer.stop();
                Utils.displayErrorForISE("NullPointerException", " preparing or play sound", npe.getMessage());
            } catch (IllegalStateException ise) {
                Utils.displayErrorForISE("IllegalStateException", " stopping play sound in playTestTune(NullPointerException)", ise.getMessage());
            }
        }
    }

    public static void goToHome(Context context, Activity currentActivity) {
        Intent inMain = new Intent(currentActivity, AppLauncher.class);
        inMain.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(inMain);
    }

}

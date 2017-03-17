package dms.pastor.cccfQuizGame.cdo;

import android.app.Activity;
import android.widget.Toast;

import java.util.Random;

import dms.pastor.cccfQuizGame.R;
import dms.pastor.cccfQuizGame.Utils.DomUtils;


/**
 * Author Dominik Symonowicz
 * Created 07/01/2013 22:01
 * WWW:	http://pastor.ovh.org
 * Github:	https://github.com/pastorcmentarny
 * Google Play:	https://play.google.com/store/apps/developer?id=Dominik+Symonowicz
 * LinkedIn: uk.linkedin.com/pub/dominik-symonowicz/5a/706/981/
 * <p>
 * Country representation for this game and dom learns chinese
 */
public abstract class Quiz extends Activity {

    //performs specific task on each interval of time
    private final Runnable timerTicker = new Runnable() {
        public void run() {
            updateUI();
        }
    };
    private boolean wasAlreadyPressed;

    protected void timerMethod() {
        this.runOnUiThread(timerTicker);
    }

    protected abstract void updateUI();

    protected Country selectACountry(Country newCountry, Country[] selectedAlreadyCountry) {
        boolean stepA = true;
        while (stepA) {
            stepA = false;
            newCountry = Game.getGameFor(null, null, null).getCountries().get(new Random().nextInt((Game.getGameFor(null, null, null).getCountries().size())));
            for (Country alreadyUsedCountry : selectedAlreadyCountry) {
                if (newCountry == alreadyUsedCountry) {
                    stepA = true;
                }
            }
        }
        return newCountry;
    }

    @Override
    public void onBackPressed() {
        if (wasAlreadyPressed) {
            DomUtils.goToHome(this, Quiz.this);
            super.onBackPressed();
        } else {
            String text = getResources().getString(R.string.back_to_quit);
            Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
            wasAlreadyPressed = true;
        }
    }

}

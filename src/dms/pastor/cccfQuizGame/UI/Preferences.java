package dms.pastor.cccfQuizGame.UI;

import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.widget.Toast;

import dms.pastor.cccfQuizGame.R;

import static android.widget.Toast.LENGTH_SHORT;
import static dms.pastor.cccfQuizGame.Utils.Utils.resetHighScore;

/**
 * Author Dominik Symonowicz
 * Created: 24/11/12 00:36
 * WWW:	http://pastor.ovh.org
 * Github:	https://github.com/pastorcmentarny
 * Google Play:	https://play.google.com/store/apps/developer?id=Dominik+Symonowicz
 * LinkedIn: uk.linkedin.com/pub/dominik-symonowicz/5a/706/981/
 */
public class Preferences extends PreferenceActivity implements Preference.OnPreferenceClickListener {

    @SuppressWarnings("deprecation")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preference);
        Preference customPref = findPreference("reset");
        customPref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {

            public boolean onPreferenceClick(Preference preference) {
                resetHighScore(getApplicationContext());
                return true;
            }

        });
    }

    @Override
    public boolean onPreferenceClick(Preference preference) {
        Toast.makeText(this, preference.getTitle(), LENGTH_SHORT).show();
        return true;
    }

}

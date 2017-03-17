package dms.pastor.cccfQuizGame.UI;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import dms.pastor.cccfQuizGame.R;

import static dms.pastor.cccfQuizGame.Utils.StringUtils.EMPTY_STRING;
import static dms.pastor.cccfQuizGame.Utils.UIUtils.setTextColor;

/**
 * Author Dominik Symonowicz
 * WWW:	http://pastor.ovh.org
 * Github:	https://github.com/pastorcmentarny
 * Google Play:	https://play.google.com/store/apps/developer?id=Dominik+Symonowicz
 * LinkedIn: uk.linkedin.com/pub/dominik-symonowicz/5a/706/981/
 */
public class About extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about);
        TextView aboutContent = (TextView) findViewById(R.id.about_content);
        Bundle extras = getIntent().getExtras();

        if (extras != null) {
            String aboutType = extras.getString("TOPIC");
            setTextColor(aboutContent, R.color.about, this);
            switch (aboutType != null ? aboutType : EMPTY_STRING) {
                case "ME":
                    aboutContent.setText(getResources().getString(R.string.about_me_text));
                    break;
                case "PROGRAM":
                    aboutContent.setText(getResources().getString(R.string.about_program_text));
                    break;
                case "THANKS":
                    aboutContent.setText(getResources().getString(R.string.about_thanks_text));
                    break;
                case "EULA":
                    aboutContent.setText(getResources().getString(R.string.about_eula_text));
                    break;
                case "GA":
                    aboutContent.setText(getResources().getString(R.string.about_game_accuracy_text));
                    break;
                default:
                    setTextColor(aboutContent, R.color.error, this);
                    aboutContent.setText(getResources().getString(R.string.noAbout));
                    break;
            }
        } else {
            setTextColor(aboutContent, R.color.error, this);
            aboutContent.setText(getResources().getString(R.string.noAbout));
        }
    }
}

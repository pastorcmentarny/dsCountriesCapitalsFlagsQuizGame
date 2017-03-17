package dms.pastor.cccfQuizGame.games.Adventure;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.List;

import dms.pastor.cccfQuizGame.Config;
import dms.pastor.cccfQuizGame.R;
import dms.pastor.cccfQuizGame.cdo.AdManager;
import dms.pastor.cccfQuizGame.cdo.Country;
import dms.pastor.cccfQuizGame.cdo.Game;

import static dms.pastor.cccfQuizGame.Utils.Utils.readCountriesDataFromFile;
import static dms.pastor.cccfQuizGame.Utils.Utils.shuffle;
import static dms.pastor.cccfQuizGame.cdo.Game.createAdventureGame;

/**
 * Author Dominik Symonowicz
 * Created 01/04/2013 23:04
 * WWW:	http://pastor.ovh.org
 * Github:	https://github.com/pastorcmentarny
 * Google Play:	https://play.google.com/store/apps/developer?id=Dominik+Symonowicz
 * LinkedIn: uk.linkedin.com/pub/dominik-symonowicz/5a/706/981/
 */
public class AdventureIntro extends Activity implements View.OnClickListener {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.i(Config.DOMS, getString(R.string.log_arcade_intro));
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.game_intro);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(AdventureIntro.this);

        setupUIComponents();

        AdManager.loadAd(this);

        showGameIntroIfEnabled(prefs);
    }

    private void setupUIComponents() {
        EditText introAboutGame = (EditText) findViewById(R.id.intro_about_game);
        introAboutGame.setText(getResources().getString(R.string.adventure_intro));
        Button playGameButton = (Button) findViewById(R.id.play_game_button);
        playGameButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.play_game_button:
                startGame();
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
                break;
        }
    }

    private void startGame() {

        Game.kill();
        Game game = createAdventureGame();
        game.setCountries(readCountriesDataFromFile(this));
        game.setLevelList(shuffle(game.getCountries()));
        List<Country> countryListForGame = new ArrayList<>();
        for (int level = 0; level < Config.GAME_LEVELS; level++) {
            countryListForGame.add(game.getLevelList().get(level));
        }
        game.setLevelList(shuffle(countryListForGame));
        goToAdventureGameScreen();
    }

    private void goToAdventureGameScreen() {
        Intent intent;
        intent = new Intent(getApplicationContext(), AdventureGame.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    private void showGameIntroIfEnabled(SharedPreferences prefs) {
        if (!prefs.getBoolean("showIntro", true)) {
            startGame();
        }
    }

}

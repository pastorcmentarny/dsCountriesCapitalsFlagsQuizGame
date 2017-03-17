package dms.pastor.cccfQuizGame;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;

import dms.pastor.cccfQuizGame.UI.About;
import dms.pastor.cccfQuizGame.UI.CountryList;
import dms.pastor.cccfQuizGame.UI.Preferences;
import dms.pastor.cccfQuizGame.Utils.DomUtils;
import dms.pastor.cccfQuizGame.Utils.EEUtils;
import dms.pastor.cccfQuizGame.Utils.StringUtils;
import dms.pastor.cccfQuizGame.cdo.Country;
import dms.pastor.cccfQuizGame.cdo.Game;
import dms.pastor.cccfQuizGame.cdo.Listeners;
import dms.pastor.cccfQuizGame.cdo.enums.GameMode;
import dms.pastor.cccfQuizGame.games.Adventure.AdventureGame;
import dms.pastor.cccfQuizGame.games.Adventure.AdventureIntro;
import dms.pastor.cccfQuizGame.games.Quiz.CustomGameSetup;
import dms.pastor.cccfQuizGame.games.Quiz.QuizGame;

import static dms.pastor.cccfQuizGame.Utils.HighScoreUtils.getHighScores;
import static dms.pastor.cccfQuizGame.Utils.Utils.readCountriesDataFromFile;
import static dms.pastor.cccfQuizGame.Utils.Utils.shuffle;
import static dms.pastor.cccfQuizGame.cdo.AdManager.loadAd;
import static dms.pastor.cccfQuizGame.cdo.Game.getGameFor;
import static dms.pastor.cccfQuizGame.cdo.enums.GameMode.PRACTICE;
import static dms.pastor.cccfQuizGame.cdo.enums.GameType.CAPITAL;
import static dms.pastor.cccfQuizGame.cdo.enums.GameType.COUNTRY;
import static dms.pastor.cccfQuizGame.cdo.enums.GameType.FLAG;

/**
 * Author Dominik Symonowicz
 * WWW:	http://pastor.ovh.org
 * Github:	https://github.com/pastorcmentarny
 * Google Play:	https://play.google.com/store/apps/developer?id=Dominik+Symonowicz
 * LinkedIn: uk.linkedin.com/pub/dominik-symonowicz/5a/706/981/
 */
public class AppLauncher extends Activity implements View.OnClickListener {
    private static final String TAG = "AppLauncher";
    private AlertDialog dialog;
    private SharedPreferences settings;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.main);

        setupUIComponent();

        settings = PreferenceManager.getDefaultSharedPreferences(AppLauncher.this);

        loadAd(this);

        displayInputUsernameDialogIfRunFirstTime();
    }

    protected void onPause() {
        super.onPause();
        if (dialog != null) {
            dialog.dismiss();
        }
    }

    @Override
    public final void onClick(final View v) {
        switch (v.getId()) {
            case R.id.customGameButton:
                openQuickGameDialog();
                break;
            case R.id.quickGameButton:
                goToAdventureIntroScreen();
                break;
            case R.id.tutorialButton:
                displayTutorial();
                break;
            case R.id.aboutGameButton:
                openAboutDialog();
                break;
            case R.id.highScoreButton:
                DomUtils.showToast(getApplicationContext(), getHighScores(this));
                break;
            case R.id.countriesInfoButton:
                goToCountriesInfoScreen();
                break;
            case R.id.settingsButton:
                goToSettingsScreen();
                break;
            default:
                break;
        }
    }

    private void goToSettingsScreen() {
        Intent intent;
        intent = new Intent(getApplicationContext(), Preferences.class);
        startActivity(intent);
        overridePendingTransition(R.anim.push_up_in, R.anim.push_up_out);
    }

    private void goToCountriesInfoScreen() {
        Intent intent;
        intent = new Intent(getApplicationContext(), CountryList.class);
        startActivity(intent);
        overridePendingTransition(R.anim.push_up_in, R.anim.push_up_out);
    }

    private void goToAdventureIntroScreen() {
        startActivity(new Intent(getApplicationContext(), AdventureIntro.class));
    }

    private void openAboutDialog() {
        dialog = new AlertDialog.Builder(this)
                .setTitle(R.string.about)
                .setItems(R.array.runAbout, getSelectAboutTopicOnClickListener())
                .show();
    }

    @NonNull
    private OnClickListener getSelectAboutTopicOnClickListener() {
        return new OnClickListener() {
            public void onClick(final DialogInterface dialoginterface, final int i) {
                Intent ii = new Intent(getApplicationContext(), About.class);
                switch (i) {
                    case 0:
                        ii.putExtra("TOPIC", "ME");
                        startActivity(ii);
                        break;
                    case 1:
                        ii.putExtra("TOPIC", "PROGRAM");
                        startActivity(ii);
                        break;
                    case 2:
                        ii.putExtra("TOPIC", "THANKS");
                        startActivity(ii);
                        break;
                    case 3:
                        ii.putExtra("TOPIC", "EULA");
                        startActivity(ii);
                        break;
                    case 4:
                        ii.putExtra("TOPIC", "GA");
                        startActivity(ii);
                        break;
                    default:
                        Log.w(TAG, getString(R.string.missing_selection));
                }
            }
        };
    }

    private void displayTutorial() {
        dialog = new AlertDialog.Builder(this).setTitle(getResources().getString(R.string.tutorial_title))
                .setMessage(getResources().getString(R.string.tutorial_text))
                .setNeutralButton(getString(R.string.understand), Listeners.getDismissDialogOnClickUnderstandListener(dialog)).show();
    }

    private void openQuickGameDialog() {
        dialog = new AlertDialog.Builder(this)
                .setTitle(R.string.new_title)
                .setItems(R.array.gameType, getSelectQuickGameOnClickListener())
                .show();
    }

    @NonNull
    private OnClickListener getSelectQuickGameOnClickListener() {
        return new OnClickListener() {
            public void onClick(final DialogInterface dialoginterface, final int i) {
                Game.kill();
                Game game;
                switch (i) {
                    case 0:
                        Intent intent = new Intent(getApplicationContext(), CustomGameSetup.class);
                        startActivity(intent);
                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
                        return;
                    case 1:
                        game = getGameFor(COUNTRY, CAPITAL, PRACTICE);
                        break;
                    case 2:
                        game = getGameFor(CAPITAL, COUNTRY, PRACTICE);
                        break;
                    case 3:
                        game = getGameFor(FLAG, COUNTRY, PRACTICE);
                        break;
                    default:
                        Log.w(TAG, getString(R.string.missing_selection));
                        return;
                }
                createAGame(game);
            }
        };
    }

    private void usernameDialog() {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);

        alert.setTitle("Who are you?");
        alert.setMessage("Please insert your username.\n(If you don't want do this now,then you can do this later in 'Options')");

        final EditText input = new EditText(this);
        alert.setView(input);
        alert.setPositiveButton("OK", getInputUsernameOnClickListener(input));
        alert.setNegativeButton("CANCEL", getNoUsernameDialogOnClickListener());
        alert.show();
    }

    @NonNull
    private OnClickListener getNoUsernameDialogOnClickListener() {
        return new OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                DomUtils.showToast(getApplicationContext(), getString(R.string.anonymous));
            }
        };
    }

    @NonNull
    private OnClickListener getInputUsernameOnClickListener(final EditText input) {
        return new OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                String username = input.getText().toString();
                if (StringUtils.isStringEmpty(username)) {
                    SharedPreferences.Editor preferencesEditor = settings.edit();
                    preferencesEditor.putString("username", username);
                    preferencesEditor.apply();
                }
            }
        };
    }

    private void createAGame(Game game) {
        Intent intent;
        game.setCountries(readCountriesDataFromFile(this));
        game.setLevelList(shuffle(game.getCountries()));
        ArrayList<Country> tempArray = new ArrayList<>();
        for (int x = 0; x < Config.GAME_LEVELS; x++) {
            tempArray.add(game.getLevelList().get(x));
        }
        game.setLevelList(shuffle(tempArray));
        if (game.getGameMode() == GameMode.ADVENTURE) {
            intent = new Intent(getApplicationContext(), AdventureGame.class);
        } else {
            intent = new Intent(getApplicationContext(), QuizGame.class);
        }
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
    }


    private void displayInputUsernameDialogIfRunFirstTime() {
        if (settings.getBoolean("first", true)) {
            usernameDialog();
            SharedPreferences.Editor preferencesEditor = settings.edit();
            preferencesEditor.putBoolean("first", false);
            preferencesEditor.apply();
        }
    }

    private void setupUIComponent() {
        Button quickGameButton = (Button) findViewById(R.id.quickGameButton);
        quickGameButton.setOnClickListener(this);
        Button customGameButton = (Button) findViewById(R.id.customGameButton);
        customGameButton.setOnClickListener(this);
        TextView version = (TextView) findViewById(R.id.version);
        version.setText(DomUtils.getAppVersion(this));
        TextView tutorialButton = (Button) findViewById(R.id.tutorialButton);
        tutorialButton.setOnClickListener(this);
        TextView aboutButton = (Button) findViewById(R.id.aboutGameButton);
        aboutButton.setOnClickListener(this);
        TextView settingsButton = (Button) findViewById(R.id.settingsButton);
        settingsButton.setOnClickListener(this);
        Button highScoreButton = (Button) findViewById(R.id.highScoreButton);
        highScoreButton.setOnClickListener(this);
        Button countriesInfoButton = (Button) findViewById(R.id.countriesInfoButton);
        countriesInfoButton.setOnClickListener(this);

        TextView randomMessage = (TextView) findViewById(R.id.randomMessage);
        randomMessage.setText(EEUtils.getRandomTrivia());
    }
}

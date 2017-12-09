package dms.pastor.cccfQuizGame.games.Quiz;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import java.util.ArrayList;

import dms.pastor.cccfQuizGame.Config;
import dms.pastor.cccfQuizGame.R;
import dms.pastor.cccfQuizGame.cdo.AdManager;
import dms.pastor.cccfQuizGame.cdo.Country;
import dms.pastor.cccfQuizGame.cdo.Game;
import dms.pastor.cccfQuizGame.cdo.enums.GameMode;
import dms.pastor.cccfQuizGame.cdo.enums.GameType;

import static android.content.pm.ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
import static android.view.Window.FEATURE_NO_TITLE;
import static dms.pastor.cccfQuizGame.Utils.Utils.readCountriesDataFromFile;
import static dms.pastor.cccfQuizGame.Utils.Utils.saveSelectedCustomGame;
import static dms.pastor.cccfQuizGame.Utils.Utils.shuffle;
import static dms.pastor.cccfQuizGame.cdo.Game.getGameFor;
import static dms.pastor.cccfQuizGame.cdo.Listeners.getDoNothingListener;
import static dms.pastor.cccfQuizGame.cdo.enums.GameMode.TIME_ATTACK;
import static dms.pastor.cccfQuizGame.cdo.enums.GameType.valueOf;

/**
 * Author Dominik Symonowicz
 * Created: 10.01.2013 23:34
 * WWW:	http://pastor.ovh.org
 * Github:	https://github.com/pastorcmentarny
 * Google Play:	https://play.google.com/store/apps/developer?id=Dominik+Symonowicz
 * LinkedIn: uk.linkedin.com/pub/dominik-symonowicz/5a/706/981/
 */
public class CustomGameSetup extends Activity implements View.OnClickListener, AdapterView.OnItemSelectedListener {
    private static final String TAG = "CUSTOM GAME";
    private Button startGameButton;
    private Spinner gameModeSpinner;
    private Spinner spinnerQuestion;
    private Spinner spinnerAnswer;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(FEATURE_NO_TITLE);
        setRequestedOrientation(SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.custom_setup);

        setupUIComponents();

        AdManager.loadAd(this);

        createGameModes();

        configSpinners();
        setSpinners();
        updateUI();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.startGameButton:
                saveSelectedCustomGame(this,
                        spinnerQuestion.getSelectedItem().toString(),
                        spinnerAnswer.getSelectedItem().toString(),
                        gameModeSpinner.getSelectedItem().toString());

                Game.kill();
                Game game = getGameFor(valueOf(spinnerQuestion.getSelectedItem().toString()),
                        valueOf(spinnerAnswer.getSelectedItem().toString()),
                        GameMode.valueOf(gameModeSpinner.getSelectedItem().toString()));
                game.setCountries(readCountriesDataFromFile(this));
                game.setLevelList(shuffle(game.getCountries()));

                generateLevelsForTimeAttack(game);
                goToQuizGameScreen();
                break;
            case R.id.custom_setup_help_button:
                displayHelpForCustomGameDialog();
                break;
            default:
                Log.w(TAG, getString(R.string.missing_selection));
                break;
        }

    }

    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
        updateUI();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        updateUI();
    }

    private void createGameModes() {
        ArrayList<String> spinnerArray = getGameModesForCustomGame();
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item,
                spinnerArray);

        gameModeSpinner.setAdapter(spinnerArrayAdapter);
    }

    private void configSpinners() {
        spinnerQuestion = configSpinner(spinnerQuestion, GameType.REGION.toString());
        spinnerAnswer = configSpinner(spinnerAnswer, GameType.FLAG.toString());
    }

    private void setSpinners() {
        SharedPreferences settings = getSharedPreferences("settings", Context.MODE_PRIVATE);
        setSpinner(spinnerQuestion, settings.getString("cg_question", GameType.COUNTRY.toString()));
        setSpinner(spinnerAnswer, settings.getString("cg_answer", GameType.CAPITAL.toString()));
        setSpinner(gameModeSpinner, settings.getString("cg_gameMode", GameMode.SAPER.toString()));
    }

    @NonNull
    private ArrayList<String> getGameModesForCustomGame() {
        ArrayList<String> spinnerArray = new ArrayList<>();
        for (GameMode gm : GameMode.values()) {
            if (!(gm.equals(GameMode.PRACTICE) || gm.equals(GameMode.ADVENTURE))) {
                spinnerArray.add(gm.toString());
            }
        }
        return spinnerArray;
    }

    private void setupUIComponents() {
        Button helpButton = findViewById(R.id.custom_setup_help_button);
        startGameButton = findViewById(R.id.startGameButton);
        gameModeSpinner = findViewById(R.id.gameModeSpinner);
        spinnerAnswer = findViewById(R.id.spinner_answer);
        spinnerQuestion = findViewById(R.id.spinner_question);

        helpButton.setOnClickListener(this);
        startGameButton.setOnClickListener(this);

        gameModeSpinner.setOnItemSelectedListener(this);
        spinnerQuestion.setOnItemSelectedListener(this);
        spinnerAnswer.setOnItemSelectedListener(this);
    }

    private void generateLevelsForTimeAttack(Game game) {
        if (gameModeSpinner.getSelectedItem().toString().equalsIgnoreCase(TIME_ATTACK.toString())) {
            ArrayList<Country> tempArray = new ArrayList<>();
            for (int i = 0; i < Config.GAME_LEVELS; i++) {
                tempArray.add(game.getLevelList().get(i));
            }
            game.setLevelList(shuffle(tempArray));
        }
    }

    private void displayHelpForCustomGameDialog() {
        AlertDialog.Builder alertBox = new AlertDialog.Builder(this);
        alertBox.setTitle(getResources().getString(R.string.help_title));
        alertBox.setMessage(getResources().getString(R.string.help_custom_setup_text));
        alertBox.setNeutralButton(getString(R.string.ok), getDoNothingListener());
        alertBox.show();
    }

    private void goToQuizGameScreen() {
        Intent intent = new Intent(getApplicationContext(), QuizGame.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
    }

    @SuppressWarnings("unchecked") // It is always ArrayAdapter
    private void setSpinner(Spinner spinner, String value) {
        ArrayAdapter<String> spinnerAdapter = (ArrayAdapter<String>) spinner.getAdapter();
        Integer spinnerPosition = spinnerAdapter.getPosition(value);
        spinner.setSelection(spinnerPosition);
    }

    private void updateUI() {
        startGameButton.setEnabled(readyToPlay());
    }

    private Spinner configSpinner(Spinner spinner, String filter) {
        ArrayList<String> spinnerArray = new ArrayList<>();
        for (GameType gm : GameType.values()) {
            if (!filter.equalsIgnoreCase(gm.toString())) {
                spinnerArray.add(gm.toString());
            }

        }
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item,
                spinnerArray);
        spinner.setAdapter(spinnerArrayAdapter);
        return spinner;
    }

    private boolean readyToPlay() {
        return isSpinnerSelected(gameModeSpinner) && isSpinnerSelected(spinnerQuestion) && isSpinnerSelected(spinnerAnswer) && spinnerQuestion.getSelectedItemId() != spinnerAnswer.getSelectedItemId();

    }

    private boolean isSpinnerSelected(Spinner spinner) {
        return spinner != null && spinner.getSelectedItemId() != Spinner.INVALID_ROW_ID;
    }

}

package dms.pastor.cccfQuizGame.games.Adventure;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TableRow;
import android.widget.TextView;

import dms.pastor.cccfQuizGame.R;
import dms.pastor.cccfQuizGame.Utils.DomUtils;
import dms.pastor.cccfQuizGame.cdo.AbstractResult;
import dms.pastor.cccfQuizGame.cdo.AdManager;
import dms.pastor.cccfQuizGame.cdo.Game;
import dms.pastor.cccfQuizGame.cdo.enums.Grades;

import static dms.pastor.cccfQuizGame.Config.DEFAULT_NAME;
import static dms.pastor.cccfQuizGame.Utils.DomUtils.goToHome;
import static dms.pastor.cccfQuizGame.Utils.DomUtils.showToast;
import static dms.pastor.cccfQuizGame.Utils.UIUtils.newRecord;
import static dms.pastor.cccfQuizGame.Utils.Utils.resetHighScore;
import static java.lang.String.format;
import static java.util.Locale.ENGLISH;

/**
 * Author Dominik Symonowicz
 * Created: 26.11.12 19:06
 * WWW:	http://pastor.ovh.org
 * Github:	https://github.com/pastorcmentarny
 * Google Play:	https://play.google.com/store/apps/developer?id=Dominik+Symonowicz
 * LinkedIn: uk.linkedin.com/pub/dominik-symonowicz/5a/706/981/
 */
public class AdventureResult extends AbstractResult {

    private static final String TAG = "ADVENTURE RESULT";
    private static final int DEFAULT_SCORE = 0;
    private final Game game = Game.getGameFor(null, null, null);
    private SharedPreferences settings;
    private SharedPreferences preferences;
    private TextView resultGrade;
    private TextView resultScore;
    private TextView correctAnswersValue;
    private TextView mistakesValue;
    private TextView questionsValue;
    private TextView recordInfo;
    private TextView levelNumber;
    private TextView resultTime;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.result);
        AdManager.loadInterstitialAd(this, adl);

        settings = this.getSharedPreferences("highscore", Context.MODE_PRIVATE);
        preferences = PreferenceManager.getDefaultSharedPreferences(AdventureResult.this);
        setupUIComponents();

        try {
            setup();
        } catch (Exception e) {
            String msg = "Woops! " + e.getMessage();
            Log.e(TAG, msg);
            DomUtils.displayError(getApplicationContext(), msg);
            goToHome(getApplicationContext(), AdventureResult.this);
        }
    }

    /*
    * Fix  issue with problem that gradient does not looks smooth as should.
    * Source: http://crazygui.wordpress.com/2010/09/05/high-quality-radial-gradient-in-android/
    */
    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        Window window = getWindow();
        window.setFormat(PixelFormat.RGBA_8888);
    }

    private void setup() {
        int score = game.calculateScore();
        checkHighScore(score);
        resultScore.setText(format(ENGLISH, "%d points.", score));
        questionsValue.setText(String.valueOf(game.getLevelList().size()));
        levelNumber.setText(String.valueOf(game.getLevel()));
        resultTime.setText(DomUtils.getResultTimeAsString(game.calcTotalTime()));
        Grades.giveAGrade(this, resultGrade, score, game.getGameMode());
        correctAnswersValue.setText(String.valueOf(game.getCorrect()));
        mistakesValue.setText(String.valueOf(game.getMistake()));
    }

    private void checkHighScore(int score) {
        try {
            for (int place = 1; place <= 5; place++) {
                String hsPlace = "adventure" + place;
                int record = settings.getInt(hsPlace, DEFAULT_SCORE);
                if (score > record) {
                    saveHighScore(score, place, hsPlace);
                    if (place == 1) {
                        resultGrade.setText(R.string.new_hs);
                        newRecord(this, this, recordInfo);
                    } else {
                        showToast(this, "You are " + place + "place.");
                    }
                    break;
                }
            }
        } catch (Exception e) {
            resetHighScore(this);
        }
        recordInfo.setText(format(ENGLISH, "RECORD: %d by %s", settings.getInt("adventure1", DEFAULT_SCORE), settings.getString("adventureuser", DEFAULT_NAME)));
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.reset:
                game.restartGame();
                goToAdventureGameScreen();
                break;
            case R.id.backToMainMenu:
                goToHome(getApplicationContext(), AdventureResult.this);
                break;
            default:
                Log.w(TAG, "woops!");
                break;
        }
    }

    private void saveHighScore(int score, int place, String hsPlace) {
        SharedPreferences.Editor prefsEditor = settings.edit();
        int next;
        for (int x = place; x <= 4; x++) {
            next = settings.getInt("adventure" + x, DEFAULT_SCORE);
            prefsEditor.putInt("adventure" + (x + 1), next);
            prefsEditor.putString("adventureuser", preferences.getString("username", "no name"));
        }
        prefsEditor.putInt(hsPlace, score);
        prefsEditor.apply();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        goToHome(getApplicationContext(), AdventureResult.this);
    }

    private void goToAdventureGameScreen() {
        Intent intent = new Intent(this, AdventureGame.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
    }

    private void setupUIComponents() {
        resultGrade = (TextView) findViewById(R.id.result_grade);
        resultGrade.setVisibility(View.GONE);
        resultScore = (TextView) findViewById(R.id.result_score);
        levelNumber = (TextView) findViewById(R.id.levelNumber);
        correctAnswersValue = (TextView) findViewById(R.id.correct_answers_value);
        mistakesValue = (TextView) findViewById(R.id.mistakes_value);
        questionsValue = (TextView) findViewById(R.id.questions_value);
        resultTime = (TextView) findViewById(R.id.result_time);
        recordInfo = (TextView) findViewById(R.id.record);
        backToMainMenu = (Button) findViewById(R.id.backToMainMenu);
        backToMainMenu.setOnClickListener(this);
        backToMainMenu.setVisibility(View.INVISIBLE);

        restart = (Button) findViewById(R.id.reset);
        restart.setOnClickListener(this);
        restart.setVisibility(View.INVISIBLE);

        TableRow questionsRow = (TableRow) findViewById(R.id.questions_row);
        questionsRow.setVisibility(View.GONE);
    }

}

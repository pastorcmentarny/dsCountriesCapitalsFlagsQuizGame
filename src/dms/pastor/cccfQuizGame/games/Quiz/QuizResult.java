package dms.pastor.cccfQuizGame.games.Quiz;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import dms.pastor.cccfQuizGame.AppLauncher;
import dms.pastor.cccfQuizGame.Config;
import dms.pastor.cccfQuizGame.R;
import dms.pastor.cccfQuizGame.Utils.DomUtils;
import dms.pastor.cccfQuizGame.cdo.AbstractResult;
import dms.pastor.cccfQuizGame.cdo.AdManager;
import dms.pastor.cccfQuizGame.cdo.Game;
import dms.pastor.cccfQuizGame.cdo.enums.GameMode;

import static dms.pastor.cccfQuizGame.Utils.DomUtils.displayError;
import static dms.pastor.cccfQuizGame.Utils.DomUtils.getResultTimeAsString;
import static dms.pastor.cccfQuizGame.Utils.DomUtils.goToHome;
import static dms.pastor.cccfQuizGame.Utils.StringUtils.EMPTY_STRING;
import static dms.pastor.cccfQuizGame.Utils.UIUtils.newRecord;
import static dms.pastor.cccfQuizGame.cdo.enums.GameMode.PRACTICE;
import static dms.pastor.cccfQuizGame.cdo.enums.Grades.calcGrade;
import static dms.pastor.cccfQuizGame.cdo.enums.Grades.giveAGrade;
import static java.lang.Integer.MIN_VALUE;
import static java.lang.Long.MAX_VALUE;
import static java.lang.String.format;
import static java.lang.String.valueOf;
import static java.util.Locale.ENGLISH;


/**
 * Author Dominik Symonowicz
 * Created: 26.11.2012 19:06
 * WWW:	http://pastor.ovh.org
 * Github:	https://github.com/pastorcmentarny
 * Google Play:	https://play.google.com/store/apps/developer?id=Dominik+Symonowicz
 * LinkedIn: uk.linkedin.com/pub/dominik-symonowicz/5a/706/981/
 */
public class QuizResult extends AbstractResult {

    private static final String WALKTHROUGH = "walkthrough";
    private static final String WALKTHROUGH_USER = "walkthroughUser";
    private static final String TIME_ATTACK = "timeAttack";
    private static final String USERNAME = "username";
    private static final String DEFAULT_NAME = "no name";
    private static final String NEW_RECORD_TEXT = "NEW RECORD: %s by %s";
    private static final String RECORD_TEXT = "RECORD: %s by %s";
    private static final String NEW_HIGH_SCORE = "!NEW HIGH SCORE!";
    private static final String SAPER_KEY = "saper";
    private static final String SAPER_USER_KEY = "saperUser";
    private static final String PERCENTAGE_RESULT_FORMAT = "%d %%";
    private static final String TAG = "QUIZ RESULT";
    private static final String TIME_ATTACK_USER_KEY = "timeAttackUser";
    private Game game;

    private TextView resultGrade;
    private TextView resultScore;
    private TextView resultTime;
    private TextView correctAnswersValue;
    private TextView mistakesValue;
    private TextView questionsValue;
    private TextView recordInfo;
    private TextView levelNumber;
    private SharedPreferences preferences;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.result);
        game = Game.game();

        goToHomeScreenWhenGameIsNotInProgress();

        if (game.getGameMode() != GameMode.SAPER) {
            AdManager.loadInterstitialAd(this, adl);
        }

        setupUIComponents();

        setup();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.backToMainMenu:
                goBackToSetupScreen();
                break;
            case R.id.reset:
                goToQuizGameScreen();
                break;
            default:
                Log.w(TAG, "woops!");
                break;
        }
    }

    private void goBackToSetupScreen() {
        Intent intent;
        if (game.getGameMode().equals(PRACTICE)) {
            intent = new Intent(this, AppLauncher.class);
        } else {
            intent = new Intent(this, CustomGameSetup.class);
        }
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
    }

    private void goToQuizGameScreen() {
        Intent intent;
        game.restartGame();
        intent = new Intent(this, QuizGame.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
    }

    private void goToHomeScreenWhenGameIsNotInProgress() {
        if (game == null || game.getGameMode() == null) {
            Log.w(TAG, "No game in progress (Game or game mode is null");
            goToHome(this, QuizResult.this);
        }
    }

    private void setup() {
        int score;
        switch (game.getGameMode()) {
            case TIME_ATTACK:
                score = game.calculateScore();
                setupResultForTimeAttack(score);
                break;
            case PRACTICE:
                setupResultForPractice();
                break;
            case SAPER:
                setupResultForSaper();
                break;
            case WALKTHROUGH:
                setupResultForWalkThrough();
            default:
                Log.e(TAG, format(getString(R.string.error_unsupported_game_mode_requested), game.getGameMode()));
                break;
        }

        correctAnswersValue.setText(String.valueOf(game.getCorrect()));
        mistakesValue.setText(String.valueOf(game.getMistake()));
    }

    private void setupResultForWalkThrough() {
        int score = game.calculateScore();
        int grade = calcGrade(game.getCorrect(), game.getCountries().size());
        giveAGrade(this, resultGrade, grade, game.getGameMode());
        resultScore.setText(String.valueOf(score));
        resultTime.setText(getResultTimeAsString(game.getTotalTime()));
        levelNumber.setText(getLastLevelAsString());
        questionsValue.setText(String.valueOf(game.getCountries().size()));
        checkHighScore(score);
    }

    private void setupResultForSaper() {
        giveAGrade(this, resultGrade, game.calculateScore(), game.getGameMode());
        TableRow questionsRow = (TableRow) findViewById(R.id.questions_row);
        questionsRow.setVisibility(View.GONE);
        resultScore.setText(getLastLevelAsString());
        resultTime.setText(getResultTimeAsString(game.getTotalTime()));
        levelNumber.setText(getLastLevelAsString());
        checkHighScore(0L);
    }

    private void setupResultForPractice() {
        int score = game.calculateScore();
        giveAGrade(this, resultGrade, score, game.getGameMode());
        score = DomUtils.getResultIn0to100Range(score);
        resultScore.setText(format(ENGLISH, PERCENTAGE_RESULT_FORMAT, score));
        questionsValue.setText(String.valueOf(game.getLevelList().size()));
        resultTime.setText(getResultTimeAsString(game.getTotalTime()));
    }

    private void setupResultForTimeAttack(int score) {
        long time = game.getTotalTime();
        giveAGrade(this, resultGrade, score, game.getGameMode());
        checkHighScore(time);
        score = DomUtils.getResultIn0to100Range(score);
        resultScore.setText(format(ENGLISH, PERCENTAGE_RESULT_FORMAT, score));
        questionsValue.setText(String.valueOf(game.getLevelList().size()));
        resultTime.setText(getResultTimeAsString(time));
    }

    //TODO why there are 2 check high score methods ???
    private void checkHighScore(long score) {
        SharedPreferences settings = this.getSharedPreferences("highscore", Context.MODE_PRIVATE);
        try {
            Long newResult = score;
            switch (game.getGameMode()) {
                case TIME_ATTACK:
                    displayHighScoreForTimeAttack(score, settings, newResult);
                    break;
                case SAPER:
                    displayHighScoreForSaper(settings);
                    break;
                case PRACTICE:
                    recordInfo.setText(EMPTY_STRING);
                    break;
                default:
                    break;
            }
        } catch (Exception e) {
            Log.w(TAG, "Unable to check score for game mode: " + game.getGameMode() + " because " + e.getCause() + " was thrown.", e);
        }
    }

    //TODO why there are 2 check high score methods ???
    private void checkHighScore(int score) {
        SharedPreferences settings = this.getSharedPreferences("highscore", Context.MODE_PRIVATE);
        try {
            switch (game.getGameMode()) {
                case WALKTHROUGH:
                    displayHighScoreForWalkthrough(score, settings);
                    break;
                default:
                    displayError(this, "Problem with game mode for result");
                    break;
            }
        } catch (Exception e) {
            Log.w(TAG, "Unable to check score for game mode: " + game.getGameMode() + " because " + e.getCause() + " was thrown.", e);
        }
    }

    private void displayHighScoreForTimeAttack(long score, SharedPreferences settings, Long newResult) {
        Long record = settings.getLong(TIME_ATTACK, MAX_VALUE);
        if (newResult < record) {
            saveTimeAttackResult(settings, newResult);
            displayHighestScore();
            recordInfo.setText(format(NEW_RECORD_TEXT, getResultTimeAsString(score), settings.getString(TIME_ATTACK_USER_KEY, Config.DEFAULT_NAME)));
        } else {
            recordInfo.setText(format(RECORD_TEXT, getResultTimeAsString(settings.getLong(TIME_ATTACK, MAX_VALUE)), settings.getString(TIME_ATTACK_USER_KEY, Config.DEFAULT_NAME)));
        }
    }

    private void displayHighScoreForSaper(SharedPreferences settings) {
        int saperRecord = (settings.getInt(SAPER_KEY, MIN_VALUE + 1) - 1);
        int lvl = getLastLevel();
        if (lvl > saperRecord) {
            saveResult(settings, lvl, SAPER_KEY, SAPER_USER_KEY);
            displayHighestScore();
            recordInfo.setText(format(ENGLISH, NEW_RECORD_TEXT, valueOf(lvl), settings.getString(SAPER_USER_KEY, Config.DEFAULT_NAME)));
        } else {
            recordInfo.setText(format(ENGLISH, RECORD_TEXT, valueOf(settings.getInt(SAPER_KEY, MIN_VALUE)), settings.getString(SAPER_USER_KEY, Config.DEFAULT_NAME)));
        }
    }

    private void displayHighScoreForWalkthrough(int score, SharedPreferences settings) {
        int walkRecord = settings.getInt(WALKTHROUGH, MIN_VALUE + 1) - 1;

        if (score > walkRecord) {
            saveResult(settings, score, WALKTHROUGH, WALKTHROUGH_USER);
            displayHighestScore();
            recordInfo.setText(format(ENGLISH, NEW_RECORD_TEXT, score, settings.getString(WALKTHROUGH_USER, Config.DEFAULT_NAME)));
        } else {
            recordInfo.setText(format(ENGLISH, RECORD_TEXT, settings.getInt(WALKTHROUGH, 0), settings.getString(WALKTHROUGH_USER, Config.DEFAULT_NAME)));
        }
    }

    private void displayHighestScore() {
        Toast.makeText(this, NEW_HIGH_SCORE, Toast.LENGTH_LONG).show();
        newRecord(this, this, recordInfo);
    }

    private void saveResult(SharedPreferences settings, int lvl, String saperKey, String saperUserKey) {
        SharedPreferences.Editor prefsEditor = settings.edit();
        prefsEditor.putInt(saperKey, lvl);
        prefsEditor.putString(saperUserKey, preferences.getString(USERNAME, DEFAULT_NAME));
        prefsEditor.apply();
    }

    private void saveTimeAttackResult(SharedPreferences settings, Long newResult) {
        SharedPreferences.Editor prefsEditor = settings.edit();
        prefsEditor.putLong(TIME_ATTACK, newResult);
        prefsEditor.putString(TIME_ATTACK_USER_KEY, preferences.getString(USERNAME, DEFAULT_NAME));
        prefsEditor.apply();
    }

    private void setupUIComponents() {
        preferences = PreferenceManager.getDefaultSharedPreferences(QuizResult.this);
        resultGrade = (TextView) findViewById(R.id.result_grade);
        resultScore = (TextView) findViewById(R.id.result_score);
        resultTime = (TextView) findViewById(R.id.result_time);
        correctAnswersValue = (TextView) findViewById(R.id.correct_answers_value);
        mistakesValue = (TextView) findViewById(R.id.mistakes_value);
        questionsValue = (TextView) findViewById(R.id.questions_value);
        recordInfo = (TextView) findViewById(R.id.record);
        levelNumber = (TextView) findViewById(R.id.levelNumber);

        backToMainMenu = (Button) findViewById(R.id.backToMainMenu);
        backToMainMenu.setOnClickListener(this);
        backToMainMenu.setVisibility(View.INVISIBLE);

        restart = (Button) findViewById(R.id.reset);
        restart.setOnClickListener(this);
        restart.setVisibility(View.INVISIBLE);
    }

    private int getLastLevel() {
        return game.getLevel() - 1;
    }

    private String getLastLevelAsString() {
        return valueOf(getLastLevel());
    }

}

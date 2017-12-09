package dms.pastor.cccfQuizGame.games.Quiz;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import dms.pastor.cccfQuizGame.Config;
import dms.pastor.cccfQuizGame.R;
import dms.pastor.cccfQuizGame.Utils.DomUtils;
import dms.pastor.cccfQuizGame.Utils.EEUtils;
import dms.pastor.cccfQuizGame.Utils.UIUtils;
import dms.pastor.cccfQuizGame.Utils.Utils;
import dms.pastor.cccfQuizGame.cdo.Country;
import dms.pastor.cccfQuizGame.cdo.Game;
import dms.pastor.cccfQuizGame.cdo.Quiz;
import dms.pastor.cccfQuizGame.cdo.enums.GameType;
import dms.pastor.cccfQuizGame.cdo.enums.Region;

import static dms.pastor.cccfQuizGame.Utils.StringUtils.EMPTY_STRING;
import static dms.pastor.cccfQuizGame.Utils.UIUtils.setTextColor;
import static dms.pastor.cccfQuizGame.Utils.Utils.shuffle;
import static dms.pastor.cccfQuizGame.cdo.AdManager.loadAd;
import static dms.pastor.cccfQuizGame.cdo.Country.noCountry;
import static dms.pastor.cccfQuizGame.cdo.enums.GameMode.SAPER;
import static dms.pastor.cccfQuizGame.cdo.enums.GameType.REGION;
import static dms.pastor.cccfQuizGame.cdo.graphics.InSampleSizeCalculator.DEFAULT_IN_SAMPLE_SIZE;
import static dms.pastor.cccfQuizGame.cdo.graphics.SampledBitmapFromResourceDecoder.decodeToBitmap;
import static java.lang.String.format;
import static java.lang.String.valueOf;
import static java.util.Locale.ENGLISH;

/**
 * Author Dominik Symonowicz
 * Created: 14.11.2012 23:19
 * WWW:	http://pastor.ovh.org
 * Github:	https://github.com/pastorcmentarny
 * Google Play:	https://play.google.com/store/apps/developer?id=Dominik+Symonowicz
 * LinkedIn: uk.linkedin.com/pub/dominik-symonowicz/5a/706/981/
 */
public class QuizGame extends Quiz implements View.OnClickListener {
    private static final String TAG = "Quiz Game";
    private static final String OF = " of: ";
    private static final String WHAT_IS = "What is ";
    private SharedPreferences prefs;
    private Vibrator vibrator;
    private Game game;

    private Country answerCountry;
    private Country wrongCountry1;
    private Country wrongCountry2;
    private Country wrongCountry3;

    private Button answer1Button;
    private Button answer2Button;
    private Button answer3Button;
    private Button answer4Button;
    private Button answer5Button;
    private Button answer6Button;
    private Button removeBadAnswerButton;

    private TextView questionTitle;
    private TextView questionText;
    private TextView currentLevel;
    private TextView score;
    private TextView timeElapsed;
    private TextView mistakesValue;

    private List<Country> countries;
    private ProgressBar levelProgressBar;
    private boolean qFLAGType;

    private boolean woops = false;

    private TextView correctValue;
    private TextView levelStatus;
    private Timer myTimer;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        game = Game.game();
        goToHomeScreenIfNoGameInProgress();
        setLevelLayout();

        prefs = PreferenceManager.getDefaultSharedPreferences(QuizGame.this);
        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        setupUIComponents();
        loadAd(this);
        setup();
        updateUI();

        game.resetTimer();
        game.start();

        myTimer = new Timer();
        myTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                timerMethod();
            }

        }, 0, Config.REFRESH_RATE);
    }

    @Override
    public void onPause() {
        super.onPause();
        if (vibrator != null) {
            vibrator.cancel();
        }
        if (myTimer != null) {
            myTimer.cancel();
        }
    }

    @Override
    public void onClick(View view) {
        final Animation castSpell = AnimationUtils.loadAnimation(this, R.anim.cast_spell);
        switch (view.getId()) {
            case R.id.answer1:
                checkAnswer(answer1Button);
                break;
            case R.id.answer2:
                checkAnswer(answer2Button);
                break;
            case R.id.answer3:
                checkAnswer(answer3Button);
                break;
            case R.id.answer4:
                checkAnswer(answer4Button);
                break;
            case R.id.answer5:
                checkAnswer(answer5Button);
                break;
            case R.id.answer6:
                checkAnswer(answer6Button);
                break;
            case R.id.spell_remove_wrong_button:
                view.startAnimation(castSpell);
                castSpellHalf();
                break;
        }
    }

    protected void updateUI() {
        score.setText(valueOf(game.getScore()));
        timeElapsed.setText(Utils.getResultTimeAsString(game.calcCurrentTime()));
        correctValue.setText(valueOf(game.getCorrect()));
        mistakesValue.setText(valueOf(game.getMistake()));
        setupButtonEnabledForRemoveBadAnswerButton();
        setProgressBar();
    }

    private void setupUIComponents() {
        setupUIForQuestionType();
        currentLevel = findViewById(R.id.current_lvl);
        correctValue = findViewById(R.id.correct_value);
        mistakesValue = findViewById(R.id.mistakes_value);
        score = findViewById(R.id.current_score);
        timeElapsed = findViewById(R.id.time_elapsed_value);
        levelStatus = findViewById(R.id.levelStatus);
        levelProgressBar = findViewById(R.id.level_progressbar);
        answer1Button = findViewById(R.id.answer1);
        answer2Button = findViewById(R.id.answer2);
        answer3Button = findViewById(R.id.answer3);
        answer4Button = findViewById(R.id.answer4);
        answer5Button = findViewById(R.id.answer5);
        answer6Button = findViewById(R.id.answer6);
        TableRow continentRow = findViewById(R.id.continent_row);
        if (game.getAnswer().equals(REGION)) {
            continentRow.setVisibility(View.VISIBLE);
        }
        removeBadAnswerButton = findViewById(R.id.spell_remove_wrong_button);

        answer1Button.setOnClickListener(this);
        answer2Button.setOnClickListener(this);
        answer3Button.setOnClickListener(this);
        answer4Button.setOnClickListener(this);
        answer5Button.setOnClickListener(this);
        answer6Button.setOnClickListener(this);
        removeBadAnswerButton.setOnClickListener(this);
    }

    private void setupUIForQuestionType() {
        questionTitle = findViewById(R.id.question_title);
        if (qFLAGType) {
            //noinspection UnusedAssignment
            ImageView flagImage = this.findViewById(R.id.imageView);
        } else {
            questionText = findViewById(R.id.questionText);
        }
    }

    private void setLevelLayout() {
        qFLAGType = game.getAskFor().equals(GameType.FLAG);

        if (qFLAGType) {
            setContentView(R.layout.ilevel);
        } else {
            setContentView(R.layout.level);
        }
    }

    private void goToHomeScreenIfNoGameInProgress() {
        if (game == null) {
            Log.w(TAG, "No game in progress");
            DomUtils.goToHome(this, QuizGame.this);
        }
    }

    @NonNull
    private String generateQuestion() {
        String question;
        switch (game.getAnswer()) {
            case REGION:
                question = getQuestion("On which ");
                break;
            case FLAG:
                question = getQuestion("This is  ");
                break;
            case COUNTRY:
                question = getQuestion(WHAT_IS);
                break;
            case CAPITAL:
                question = getQuestion(WHAT_IS);
                break;
            default:
                question = "Woops! Author mess up something.";
        }
        return question;
    }

    private void setQuestionText(String question) {
        questionTitle.setText(question);

        if (qFLAGType) {
            ImageView flagImage;
            flagImage = this.findViewById(R.id.imageView);
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = DEFAULT_IN_SAMPLE_SIZE;
            Bitmap bitmap = decodeToBitmap(this.getResources(), this.getResources().getIdentifier(answerCountry.getFlagId(), "drawable", getPackageName()));
            flagImage.setImageBitmap(bitmap);
        } else {
            questionText.setText(getData(answerCountry, game.getAskFor()));
        }
    }

    @NonNull
    private String getQuestion(String prefix) {
        return prefix + game.getAnswer().toString() + QuizGame.OF;
    }

    private String getData(Country country, GameType gameType) {
        switch (gameType) {
            case CAPITAL:
                return country.getCapital();
            case COUNTRY:
                return country.getCountry();
            case REGION:
                return country.getRegions().get(0).toString();
            default:
                return null;
        }
    }

    private void setButton(Button answerButton, int buttonNo, GameType answer) {
        switch (answer) {
            case CAPITAL:
                answerButton.setText(countries.get(buttonNo).getCapital());
                break;
            case COUNTRY:
                answerButton.setText(countries.get(buttonNo).getCountry());
                break;
            case REGION:
                answerButton.setText(countries.get(buttonNo).getRegions().get(0).toString());
                break;
        }
    }

    private void setup() {
        woops = false;
        countries = new ArrayList<>();

        setAnswerCountry();
        setPossibleAnswers();
        setQuestionText(generateQuestion());
        shuffle(countries);
        setButtons();
        resetToDefaultButtonUI();
        setProgressBar();
        levelStatus.setText(EMPTY_STRING);
    }

    private void setPossibleAnswers() {
        if (game.getAnswer().equals(REGION)) {
            List<Region> regionList;
            Country country;
            for (Region region : Region.values()) {
                regionList = new ArrayList<>();
                regionList.add(region);
                country = noCountry();
                country.setReqion(regionList);
                countries.add(country);
            }
        } else {
            wrongCountry1 = selectACountry(wrongCountry1, new Country[]{answerCountry});
            wrongCountry2 = selectACountry(wrongCountry2, new Country[]{answerCountry, wrongCountry1});
            wrongCountry3 = selectACountry(wrongCountry3, new Country[]{answerCountry, wrongCountry1, wrongCountry2});
            countries.add(answerCountry);
            countries.add(wrongCountry1);
            countries.add(wrongCountry2);
            countries.add(wrongCountry3);
        }
    }


    private void setAnswerCountry() {
        if (game.getGameMode() == SAPER) {
            answerCountry = game.getCountries().get(new Random().nextInt(game.getCountries().size() - 1));
        } else {
            do {
                answerCountry = game.getLevelList().get(game.getLevel() - 1);
            } while (answerCountry == null);
        }
    }

    private void setProgressBar() {
        if (game.getGameMode() != SAPER) {
            currentLevel.setText(format(ENGLISH, "%d/%d", game.getLevel(), game.getLevelList().size()));
            levelProgressBar.setProgress(game.getLevel());
            levelProgressBar.setMax(game.getLevelList().size());
        } else {
            currentLevel.setText(valueOf(game.getLevel()));
            levelProgressBar.setVisibility(View.GONE);
        }
    }

    private void resetToDefaultButtonUI() {
        answer1Button.setBackgroundResource(android.R.drawable.btn_default);
        answer2Button.setBackgroundResource(android.R.drawable.btn_default);
        answer3Button.setBackgroundResource(android.R.drawable.btn_default);
        answer4Button.setBackgroundResource(android.R.drawable.btn_default);
        setTextColor(answer1Button, android.R.color.black, this);
        setTextColor(answer2Button, android.R.color.black, this);
        setTextColor(answer3Button, android.R.color.black, this);
        setTextColor(answer4Button, android.R.color.black, this);
        if (game.getAnswer().equals(REGION)) {
            setButton(answer5Button, 4, game.getAnswer());
            setButton(answer6Button, 5, game.getAnswer());
            setTextColor(answer5Button, android.R.color.black, this);
            setTextColor(answer6Button, android.R.color.black, this);
            answer5Button.setBackgroundResource(android.R.drawable.btn_default);
            answer6Button.setBackgroundResource(android.R.drawable.btn_default);

        }
    }

    private void setButtons() {
        setButton(answer1Button, 0, game.getAnswer());
        setButton(answer2Button, 1, game.getAnswer());
        setButton(answer3Button, 2, game.getAnswer());
        setButton(answer4Button, 3, game.getAnswer());
    }

    private void castSpellHalf() {
        if (game.getBadAnswerRemoverLeft() > 0) {
            ArrayList<Button> buttons = new ArrayList<>();
            buttons.add(answer1Button);
            buttons.add(answer2Button);
            buttons.add(answer3Button);
            buttons.add(answer4Button);
            if (game.getAnswer().equals(REGION)) {
                buttons.add(answer5Button);
                buttons.add(answer6Button);
            }
            buttons = UIUtils.shuffleButtons(buttons);
            for (int i = 0; i < buttons.size(); i++) {
                if (disableWrongButton(buttons.get(i))) {
                    i = buttons.size();
                }
            }
        }
        updateUI();
    }

    private boolean disableWrongButton(Button button) {
        if (!button.getText().toString().equals(answerCountry.getCountry())) {
            if (button.isEnabled()) {
                game.setBadAnswerRemoverLeft(game.useBadAnswerRemover());
                UIUtils.setRemoved(this, this, button);
                return true;
            }
        }
        return false;
    }

    private void checkAnswer(Button button) {
        if (isCorrectAnswer(button.getText().toString())) {
            toNextLevel();

        } else {
            if (vibrator != null && prefs.getBoolean("vibrate", false)) {
                vibrator.vibrate(Config.VIBRATE_ON_MISTAKE_TIME);
            }
            if (prefs.getBoolean("sound", false)) {
                DomUtils.playTestTune(this);
            }
            UIUtils.setIncorrect(this, this, button);
            woops = true;
            game.addMistake();
            updateUI();
            if (game.getGameMode() == SAPER) {
                dead();
            }
            levelStatus.setText(EEUtils.getMaybeComment());
        }
    }

    private boolean isCorrectAnswer(String answer) {
        if (game.getAnswer() == REGION) {
            for (Region region : answerCountry.getRegions()) {
                boolean eq = region.toString().equalsIgnoreCase(answer);
                if (eq) {
                    return true;
                }
            }
            return false;
        } else {
            return answer.equalsIgnoreCase(getData(answerCountry, game.getAnswer()));
        }
    }

    private void toNextLevel() {
        if (!woops) {
            game.addCorrect();
        }

        if (game.getGameMode() != SAPER) {
            if (game.isLastLevel()) {
                dead();
            } else {
                goToNextLevel();
            }
        } else {
            goToNextLevel();
        }
    }

    private void goToNextLevel() {
        game.addLevel();
        setupNextLevel();
    }

    private void dead() {
        game.stop();
        goToResultScreen();
    }

    private void goToResultScreen() {
        Intent ii;
        ii = new Intent(this, QuizResult.class);
        ii.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(ii);
    }

    private void setupButtonEnabledForRemoveBadAnswerButton() {
        removeBadAnswerButton.setEnabled(game.getBadAnswerRemoverLeft() > 0);
        if (removeBadAnswerButton.isEnabled()) {
            UIUtils.setBackground(removeBadAnswerButton, Utils.getDrawable(this, R.drawable.spell_button));
        } else {
            UIUtils.setBackground(removeBadAnswerButton, Utils.getDrawable(this, R.drawable.spell_disabled_button));
        }
    }

    private void setupNextLevel() {
        setEnabled(false);
        setup();
        setEnabled(true);
    }

    private void setEnabled(boolean enableGUI) {
        answer1Button.setEnabled(enableGUI);
        answer2Button.setEnabled(enableGUI);
        answer3Button.setEnabled(enableGUI);
        answer4Button.setEnabled(enableGUI);
        if (answer5Button != null && answer6Button != null) {
            answer5Button.setEnabled(enableGUI);
            answer6Button.setEnabled(enableGUI);
        }
    }

}

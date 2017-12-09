package dms.pastor.cccfQuizGame.games.Adventure;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

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
import dms.pastor.cccfQuizGame.cdo.Event;
import dms.pastor.cccfQuizGame.cdo.Game;
import dms.pastor.cccfQuizGame.cdo.LevelTimer;
import dms.pastor.cccfQuizGame.cdo.Quiz;
import dms.pastor.cccfQuizGame.cdo.enums.GameType;
import dms.pastor.cccfQuizGame.cdo.enums.Region;

import static android.R.color.black;
import static android.R.drawable.btn_default;
import static android.content.Intent.FLAG_ACTIVITY_CLEAR_TOP;
import static android.graphics.Color.BLACK;
import static android.graphics.Color.WHITE;
import static android.preference.PreferenceManager.getDefaultSharedPreferences;
import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static android.view.animation.AnimationUtils.loadAnimation;
import static dms.pastor.cccfQuizGame.Config.HALF_SECOND;
import static dms.pastor.cccfQuizGame.Config.RANDOM_SIZE;
import static dms.pastor.cccfQuizGame.Utils.StringUtils.EMPTY_STRING;
import static dms.pastor.cccfQuizGame.Utils.UIUtils.setBackground;
import static dms.pastor.cccfQuizGame.Utils.UIUtils.setFrozen;
import static dms.pastor.cccfQuizGame.Utils.UIUtils.setIncorrect;
import static dms.pastor.cccfQuizGame.Utils.UIUtils.setInvisibleButton;
import static dms.pastor.cccfQuizGame.Utils.UIUtils.setRemoved;
import static dms.pastor.cccfQuizGame.Utils.UIUtils.setTextColor;
import static dms.pastor.cccfQuizGame.Utils.UIUtils.setUnTapButton;
import static dms.pastor.cccfQuizGame.Utils.UIUtils.shuffleButtons;
import static dms.pastor.cccfQuizGame.Utils.Utils.shuffle;
import static dms.pastor.cccfQuizGame.cdo.AdManager.loadAd;
import static dms.pastor.cccfQuizGame.cdo.Event.eventHappen;
import static dms.pastor.cccfQuizGame.cdo.enums.GameType.CAPITAL;
import static dms.pastor.cccfQuizGame.cdo.enums.GameType.COUNTRY;
import static dms.pastor.cccfQuizGame.cdo.enums.GameType.FLAG;
import static dms.pastor.cccfQuizGame.cdo.enums.GameType.REGION;
import static dms.pastor.cccfQuizGame.cdo.graphics.InSampleSizeCalculator.DEFAULT_IN_SAMPLE_SIZE;
import static dms.pastor.cccfQuizGame.cdo.graphics.SampledBitmapFromResourceDecoder.decodeToBitmap;
import static java.lang.String.format;
import static java.lang.String.valueOf;
import static java.util.Locale.ENGLISH;

/**
 * Author Dominik Symonowicz
 * Created 14/11/2012 23:19
 * WWW:	http://pastor.ovh.org
 * Github:	https://github.com/pastorcmentarny
 * Google Play:	https://play.google.com/store/apps/developer?id=Dominik+Symonowicz
 * LinkedIn: uk.linkedin.com/pub/dominik-symonowicz/5a/706/981/
 */
public class AdventureGame extends Quiz implements View.OnClickListener {
    private static final String TAG = "Adventure Game";
    private static final int NO_DELAY = 0;
    private final LevelTimer lvlTimer = new LevelTimer();
    private SharedPreferences prefs;
    private Vibrator vibrator;
    private Game game;
    private Country answerCountry;
    private Country wrongCountry1;
    private Country wrongCountry2;
    private Country wrongCountry3;
    private LinearLayout adventureBackground;
    private Button answer1Button;
    private Button answer2Button;
    private Button answer3Button;
    private Button answer4Button;
    private Button answer5Button;
    private Button answer6Button;
    private Button removeBadAnswerButton;
    private Button tap2unfreeze;
    private TextView questionTitle;
    private TextView questionText;
    private TextView currentLevel;
    private TextView score;
    private TextView bonusScore;
    private TextView timeElapsed;
    private TextView mistakesValue;
    private TextView current_life;
    private TableRow continentRow;
    private List<Country> countries;

    private Event event;
    private ImageView flagImage;
    private boolean woops = false;
    private int combo = 0;
    private int mistakes;
    private int rmBadAns = Config.REMOVE_BAD_ANSWER_INIT_VALUE;

    private TextView correctValue;
    private TextView levelStatus;
    private Timer myTimer;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        event = new Event();
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.adventure);

        prefs = getDefaultSharedPreferences(AdventureGame.this);
        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        setupUIComponents();
        loadAd(this);

        getupGame();

        myTimer = new Timer();
        myTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                timerMethod();
            }
        }, NO_DELAY, Config.REFRESH_RATE);
    }

    private void getupGame() {
        game = Game.game();
        goBackToMainMenuIfGameIsNull();
        game.resetTimer();
        game.start();
        setupNextLevel();
    }

    private void setQuestion() {
        String q;
        switch (game.getAnswer()) {
            case REGION:
                q = "On which " + game.getAnswer().toString() + " is: ";
                break;
            case FLAG:
                q = "This is  " + game.getAnswer().toString() + " of: ";
                break;
            case COUNTRY:
                q = "What is " + game.getAnswer().toString() + " of: ";
                break;
            case CAPITAL:
                q = "What is " + game.getAnswer().toString() + " of: ";
                break;
            default:
                q = "Woops! Author mess up something.";
        }
        questionTitle.setText(q);
        setupQuestionComponent();
    }

    private void setupQuestionComponent() {
        if (game.getAskFor().equals(FLAG)) {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = DEFAULT_IN_SAMPLE_SIZE;
            Bitmap bitmap = decodeToBitmap(this.getResources(),
                    this.getResources().getIdentifier(answerCountry.getFlagId(),
                            "drawable",
                            getPackageName()));
            flagImage.setImageBitmap(bitmap);
        } else {
            questionText.setText(getData(answerCountry, game.getAskFor()));
        }
    }

    private String getData(Country country, GameType gameType) {
        switch (gameType) {
            case CAPITAL:
                return country.getCapital();
            case COUNTRY:
                return country.getCountry();
            default:
                Log.w(TAG, "Unsupported gameType for " + gameType);
                return EMPTY_STRING;
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

    private void setupQuestion() {
        switch (new Random().nextInt(4)) {
            case 0:
                game.setAskFor(COUNTRY);
                game.setAnswer(CAPITAL);
                break;
            case 1:
                game.setAskFor(CAPITAL);
                game.setAnswer(COUNTRY);
                break;
            case 2:
                game.setAskFor(FLAG);
                game.setAnswer(COUNTRY);
                break;
            case 3:
                game.setAskFor(COUNTRY);
                game.setAnswer(REGION);
                break;
            default:
                dead();
                break;
        }
    }

    private void setup() {
        woops = false;
        mistakes = 0;
        game.setFrozen(false);
        countries = new ArrayList<>();
        answerCountry = getRandomlySelectedCountry();
        if (game.getAnswer().equals(REGION)) {
            List<Region> regionList;
            Country c;
            for (Region region : Region.values()) {
                regionList = new ArrayList<>();
                regionList.add(region);
                c = new Country(0, null, null, null, regionList, null, EMPTY_STRING);
                countries.add(c);
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

        setQuestion();
        shuffle(countries);
        setButtons();
    }

    private Country getRandomlySelectedCountry() {
        return game.getCountries().get(new Random().nextInt(game.getCountries().size() - 1));
    }

    private void setButtons() {
        setButton(answer1Button, 0, game.getAnswer());
        setButton(answer2Button, 1, game.getAnswer());
        setButton(answer3Button, 2, game.getAnswer());
        setButton(answer4Button, 3, game.getAnswer());

        answer1Button.setBackgroundResource(btn_default);
        answer2Button.setBackgroundResource(btn_default);
        answer3Button.setBackgroundResource(btn_default);
        answer4Button.setBackgroundResource(btn_default);
        setTextColor(answer1Button, black, this);
        setTextColor(answer2Button, black, this);
        setTextColor(answer3Button, black, this);
        setTextColor(answer4Button, black, this);

        if (game.getAnswer().equals(REGION)) {
            setButton(answer5Button, 4, game.getAnswer());
            setButton(answer6Button, 5, game.getAnswer());
            setTextColor(answer5Button, black, this);
            setTextColor(answer6Button, black, this);
            answer5Button.setBackgroundResource(btn_default);
            answer6Button.setBackgroundResource(btn_default);
        }
        currentLevel.setText(valueOf(game.getLevel()));
        adventureBackground.setBackgroundColor(BLACK);
    }

    @Override
    public void onClick(View view) {
        final Animation castSpell = loadAnimation(this, R.anim.cast_spell);

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
                removeBadAnswer();
                break;
            case R.id.tap2unfreeze:
                unFreezeAction();
                break;
            default:
                Log.w(TAG, "Whoops. No onClick implementation for:" + view.getId());
        }
    }

    private void unFreezeAction() {
        game.reduceWallHP();
        if (game.isWallHasHP()) {
            unFreeze();
        } else {
            levelStatus.setText(format("You need tap button %s times to unfreeze buttons.", game.getWallHP()));
            setTextColor(levelStatus, R.color.bad_news, this);
        }
        updatePlayer();
    }

    private void removeBadAnswer() {
        rmBadAns--;
        ArrayList<Button> buttons = new ArrayList<>();
        buttons.add(answer1Button);
        buttons.add(answer2Button);
        buttons.add(answer3Button);
        buttons.add(answer4Button);
        if (game.getAnswer().equals(REGION)) {
            if (answer5Button != null || answer6Button != null) {
                buttons.add(answer5Button);
                buttons.add(answer6Button);
            }
        }
        buttons = shuffleButtons(buttons);
        for (int i = 0; i < buttons.size(); i++) {
            if (disableWrongButton(buttons.get(i))) {
                i = buttons.size();//cause a loop break .This need to be improved!!!!
            }
        }
        updatePlayer();
    }

    private boolean disableWrongButton(Button button) {
        switch (game.getAnswer()) {
            case COUNTRY:
                if (!button.getText().toString().equals(answerCountry.getCountry())) {
                    if (button.isEnabled()) {
                        game.setBadAnswerRemoverLeft(game.useBadAnswerRemover());
                        setRemoved(this, this, button);
                        return true;
                    }
                }
                return false;
            case CAPITAL:
                if (!button.getText().toString().equals(answerCountry.getCapital())) {
                    if (button.isEnabled()) {
                        game.setBadAnswerRemoverLeft(game.useBadAnswerRemover());
                        setRemoved(this, this, button);
                        return true;
                    }
                }
                return false;
            case REGION:
                for (Region region : answerCountry.getRegions()) {
                    if (!region.toString().equalsIgnoreCase(button.getText().toString())) {
                        if (button.isEnabled()) {
                            game.setBadAnswerRemoverLeft(game.useBadAnswerRemover());
                            setRemoved(this, this, button);
                            return true;
                        }
                    }
                }
                return false;
            default:
                Log.w(TAG, "Unsupported answer type for disable wrong button");
                return false;
        }
    }

    private void checkAnswer(Button button) {

        if (isCorrectAnswer(button.getText().toString())) {
            toNextLevel();
        } else {
            vibrateIfEnabled();
            setIncorrect(this, this, button);
            woops = true;
            mistakes++;
            game.doPenalty();
            levelStatus.setText(EEUtils.getMaybeComment());
        }
        updatePlayer();
    }

    private void vibrateIfEnabled() {
        if (vibrator != null && prefs.getBoolean("vibrate", false)) {
            vibrator.vibrate(Config.VIBRATE_ON_MISTAKE_TIME);
        }
    }

    private boolean isCorrectAnswer(String answer) {
        switch (game.getAnswer()) {
            case CAPITAL:
            case COUNTRY:
                return answer.equalsIgnoreCase(getData(answerCountry, game.getAnswer()));
            case REGION:
                for (Region region : answerCountry.getRegions()) {
                    boolean eq = region.toString().equalsIgnoreCase(answer);
                    if (eq) {
                        return true;
                    }
                }
                break;
            default:
                Log.w(TAG, "Unable to check is answer correct for Unsupported answer type.");
                break;
        }
        return false;
    }

    private void toNextLevel() {
        if (!woops) {
            game.addCorrect();
        }
        checkForRedeemComboBonus();

        int healthPenalty = lvlTimer.calcCurrentTimeInMilliSeconds();
        checkIfPenaltyForSlowAnswerNeedBeApplied(healthPenalty);
        reduceHealthByOneIfOverHundred();

        game.addScore(Config.BONUS_POINTS[mistakes]);
        lvlTimer.resetTimer();
        game.addLevel();
        setupNextLevel();
    }

    private void reduceHealthByOneIfOverHundred() {
        //max health is 100,if current health is higher it will be reduce by 1 per turn
        if (game.getHealth() > 100) {
            game.setHealth(game.getHealth() - 1);
        }
    }

    private void checkIfPenaltyForSlowAnswerNeedBeApplied(int healthPenalty) {
        if (healthPenalty > calcNoTimePenalty()) {
            int dmg = Math.round((healthPenalty - calcNoTimePenalty()) / Config.PENALTY_TIME_UNIT);
            game.setHealth(game.getHealth() - dmg);
        } else if (healthPenalty < 1) {
            game.addScore(3);
        }
    }

    private int calcNoTimePenalty() {
        int noPenaltyTime = (Config.NO_PENALTY_TIME * Config.SECONDS) - (game.getLevel() * Config.TIME_REDUCTION_PER_LEVEL);
        return noPenaltyTime > HALF_SECOND ? noPenaltyTime : HALF_SECOND;
    }

    private void checkHealthPenalty() {
        int healthPenalty = lvlTimer.calcCurrentTimeInMilliSeconds();
        if (healthPenalty > calcNoTimePenalty()) {
            int dmg = Math.round((healthPenalty - calcNoTimePenalty()) / 400);
            current_life.setText(format(ENGLISH, "%d (- %d )", game.getHealth(), dmg));
        } else {
            current_life.setText(valueOf(game.getHealth()));
        }
    }

    private void dead() {
        game.stop();
        Intent ii = new Intent(this, AdventureResult.class);
        ii.setFlags(FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(ii);
    }

    protected void updateUI() {
        checkHealthPenalty();
        score.setText(valueOf(game.getScore()));
        bonusScore.setText(EMPTY_STRING);
        timeElapsed.setText(Utils.getResultTimeAsString(game.calcCurrentTime()));
        correctValue.setText(valueOf(game.getCorrect()));
        mistakesValue.setText(valueOf(game.getMistake()));
        currentLevel.setText(valueOf(game.getLevel()));
        if (game.isFrozen()) {
            removeBadAnswerButton.setEnabled(false);
        } else {
            removeBadAnswerButton.setEnabled(rmBadAns > 0);
        }

        if (removeBadAnswerButton.isEnabled()) {
            setBackground(removeBadAnswerButton, Utils.getDrawable(this, R.drawable.spell_button));
            removeBadAnswerButton.setText(format(ENGLISH, "%s(%d)", getString(R.string.spell_remove_wrong_answer), rmBadAns));
        } else {
            setBackground(removeBadAnswerButton, Utils.getDrawable(this, R.drawable.spell_disabled_button));
            removeBadAnswerButton.setText(getString(R.string.spell_remove_wrong_answer));
        }
    }

    private void updatePlayer() {
        if (game.isAlive()) {
            updateUI();
        } else {
            dead();
        }
    }

    private void setupUI() {
        if (game.getAnswer().equals(REGION)) {
            continentRow.setVisibility(VISIBLE);
        } else {
            continentRow.setVisibility(GONE);
        }

        if (game.getAskFor().equals(FLAG)) {
            flagImage.setVisibility(VISIBLE);
            questionText.setVisibility(GONE);
        } else {
            flagImage.setVisibility(GONE);
            questionText.setVisibility(VISIBLE);
        }
    }

    private void setupNextLevel() {
        setEnabled(false);
        game.newLevel();
        setupQuestion();
        setup();
        setupUI();
        setEnabled(true);
        generateEvent();
        lvlTimer.start();
    }

    private void generateEvent() {
        if (game.getLevel() % 3 == 0) {
            event(new Random().nextInt(RANDOM_SIZE));
        }
    }

    private void checkForRedeemComboBonus() {
        if (woops) {
            if (combo >= 5) {
                eventHappen(this, event.nonMistakeCombo(combo));
            }
            combo = 0;
        } else {
            combo++;
        }
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

    private void event(int random) {
        switch (random) {
            case 11:
            case 22:
            case 33:
            case 44:
            case 55:
            case 66:
            case 99:
                if (game.getHealth() < 100) {
                    eventHappen(this, event.castHeal());
                    updatePlayer();
                }
                break;
            case 13:
                eventHappen(this, event.castJackpot());
                toNextLevel();
                break;
            case 5:
            case 8:
            case 14:
            case 21:
            case 34:
                eventHappen(this, event.castMinorPointsBonus());
                updatePlayer();
                break;
            case 7:
            case 77:
            case 35:
            case 42:
                eventHappen(this, event.castPoison());
                updatePlayer();
                break;
            case 85:
            case 88:
            case 89:
                eventHappen(this, addRemoveWrongAnswer());
                break;
            case 45:
            case 47:
            case 54:
                eventHappen(this, event.painKiller());
                break;
            case 71:
            case 72:
            case 73:
                EEUtils.displayBlindToast(this, this);
                levelStatus.setText(R.string.event_blind);
                blind();
                break;
            case 74:
            case 76:
            case 78:
                freeze();
                break;
            case 81:
            case 91:
            case 101:
                EEUtils.displayHalfHalfToast(this, this);
                castHalf();
                break;
            case 111:
            case 112:
            case 113:
                eventHappen(this, event.doubleHP());
                updatePlayer();
                break;
            case 114:
                eventHappen(this, event.halfHP());
                updatePlayer();
                break;
            default:
                Log.d(TAG, "nothing happen");
        }
    }

    private void castHalf() {
        ArrayList<Button> buttons = new ArrayList<>();
        buttons.add(answer1Button);
        buttons.add(answer2Button);
        buttons.add(answer3Button);
        buttons.add(answer4Button);
        if (game.getAnswer().equals(REGION)) {
            buttons.add(answer5Button);
            buttons.add(answer6Button);
        }
        ArrayList<Button> temp = new ArrayList<>();
        for (Button button : buttons) {
            if (isCorrectAnswer(button.getText().toString())) {
                temp.add(button);
            }
        }
        buttons.removeAll(temp);
        buttons = shuffleButtons(buttons);
        try {
            buttons.remove(0);
            for (Button button : buttons) {
                setRemoved(this, this, button);
            }
        } catch (IndexOutOfBoundsException ioobe) {
            Log.w(TAG, getString(R.string.e_ioobe) + ioobe.getMessage());
        }
        updatePlayer();
    }

    private void blind() {
        setInvisibleButton(this, answer1Button);
        setInvisibleButton(this, answer2Button);
        setInvisibleButton(this, answer3Button);
        setInvisibleButton(this, answer4Button);
        setInvisibleButton(this, answer5Button);
        setInvisibleButton(this, answer6Button);
    }

    private void freeze() {
        if (prefs.getBoolean("sound", false)) {
            DomUtils.playTestTune(this);
        }
        game.setFrozen(true);
        setEnabled(false);
        setFrozenToAllButtons();
        adventureBackground.setBackgroundColor(UIUtils.getColor(this, R.color.freeze_background));
        tap2unfreeze.setVisibility(VISIBLE);
        removeBadAnswerButton.setEnabled(false);
    }

    private void setFrozenToAllButtons() {
        setFrozenButton(answer1Button);
        setFrozenButton(answer2Button);
        setFrozenButton(answer3Button);
        setFrozenButton(answer4Button);
        setFrozenButton(answer5Button);
        setFrozenButton(answer6Button);
        setFrozenButton(removeBadAnswerButton);
    }


    private void setFrozenButton(Button button) {
        setFrozen(this, this, button);
        setUnTapButton(this, tap2unfreeze);
        setTextColor(levelStatus, R.color.bad_news, this);
    }

    private void unFreeze() {
        setEnabled(true);
        removeBadAnswerButton.setEnabled(true);
        tap2unfreeze.setVisibility(GONE);
        setBackground(removeBadAnswerButton, Utils.getDrawable(this, R.drawable.spell_button));
        game.unfreeze();
        removeBadAnswerButton.setText(getResources().getString(R.string.spell_remove_wrong_answer));
        removeBadAnswerButton.setTextColor(WHITE);
        levelStatus.setText(R.string.button_defrosted);
        levelStatus.setTextColor(UIUtils.getColor(this, R.color.status));
        setButtons();
    }

    private String addRemoveWrongAnswer() {
        int add2removeBadAnswer = new Random().nextInt(2) + 1;
        rmBadAns += add2removeBadAnswer;
        updateUI();
        return "add remove bad answer " + add2removeBadAnswer + "time(s)";
    }

    private void setupUIComponents() {
        flagImage = this.findViewById(R.id.imageView);

        bonusScore = findViewById(R.id.bonus_score);
        correctValue = findViewById(R.id.correct_value);
        currentLevel = findViewById(R.id.current_lvl);
        current_life = findViewById(R.id.current_life);
        mistakesValue = findViewById(R.id.mistakes_value);
        questionTitle = findViewById(R.id.question_title);
        questionText = findViewById(R.id.questionText);
        score = findViewById(R.id.current_score);
        timeElapsed = findViewById(R.id.time_elapsed_value);
        levelStatus = findViewById(R.id.levelStatus);

        flagImage = this.findViewById(R.id.imageView);

        answer1Button = findViewById(R.id.answer1);
        answer2Button = findViewById(R.id.answer2);
        answer3Button = findViewById(R.id.answer3);
        answer4Button = findViewById(R.id.answer4);
        answer5Button = findViewById(R.id.answer5);
        answer6Button = findViewById(R.id.answer6);
        continentRow = findViewById(R.id.continent_row);

        removeBadAnswerButton = findViewById(R.id.spell_remove_wrong_button);

        tap2unfreeze = findViewById(R.id.tap2unfreeze);
        tap2unfreeze.setOnClickListener(this);

        adventureBackground = findViewById(R.id.adventure_background);

        answer1Button.setOnClickListener(this);
        answer2Button.setOnClickListener(this);
        answer3Button.setOnClickListener(this);
        answer4Button.setOnClickListener(this);
        answer5Button.setOnClickListener(this);
        answer6Button.setOnClickListener(this);
        removeBadAnswerButton.setOnClickListener(this);
    }

    private void goBackToMainMenuIfGameIsNull() {
        if (game == null) {
            Toast.makeText(this, "No game in progress.Backing to main menu", Toast.LENGTH_SHORT).show();
            DomUtils.goToHome(this, AdventureGame.this);
        }
    }

}
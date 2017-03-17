package dms.pastor.cccfQuizGame.cdo.enums;

import android.content.Context;
import android.util.Log;
import android.widget.TextView;

import dms.pastor.cccfQuizGame.R;

import static dms.pastor.cccfQuizGame.Utils.UIUtils.setTextAppearanceWithText;
import static java.lang.Integer.MIN_VALUE;

/**
 * Author Dominik Symonowicz
 * Created 28/11/2012 08:05
 * WWW:	http://pastor.ovh.org
 * Github:	https://github.com/pastorcmentarny
 * Google Play:	https://play.google.com/store/apps/developer?id=Dominik+Symonowicz
 * LinkedIn: uk.linkedin.com/pub/dominik-symonowicz/5a/706/981/
 * <p>
 * It keep all data related to give grade as result
 */
public enum Grades {
    PERFECT(100),
    AWESOME(88),
    GREAT(75),
    GOOD(63),
    POOR(51),
    FAIL(0),
    EPIC_FAIL(MIN_VALUE);

    private static final String TAG = "Grades";
    private final int percentage;

    Grades(int grade) {
        percentage = grade;
    }

    private static Grades getGradeForScore(int score) {
        if (score >= PERFECT.percentage) {
            return PERFECT;
        }

        if (score >= GREAT.percentage) {
            return GREAT;
        }

        if (score >= GOOD.percentage) {
            return GOOD;
        }

        if (score >= POOR.percentage) {
            return POOR;
        }
        if (score >= FAIL.percentage) {
            return FAIL;
        }
        return EPIC_FAIL;
    }

    public static void giveAGrade(Context context, TextView view, int score, GameMode mode) {
        switch (mode) {
            case SAPER:
            case WALKTHROUGH:
            case PRACTICE:
            case TIME_ATTACK:
                switch (getGradeForScore(score)) {
                    case PERFECT:
                        setTextAppearanceWithText(context, view, R.style.grade_perfect, R.string.perfect);
                        break;
                    case AWESOME:
                        setTextAppearanceWithText(context, view, R.style.grade_awesome, R.string.awesome);
                        break;
                    case GREAT:
                        setTextAppearanceWithText(context, view, R.style.grade_great, R.string.great);
                        break;
                    case GOOD:
                        setTextAppearanceWithText(context, view, R.style.grade_good, R.string.good);
                        break;
                    case POOR:
                        setTextAppearanceWithText(context, view, R.style.grade_poor, R.string.poor);
                        break;
                    case FAIL:
                        setTextAppearanceWithText(context, view, R.style.grade_fail, R.string.fail);
                        break;
                    default:
                        setTextAppearanceWithText(context, view, R.style.grade_epic_fail, R.string.epic_fail);
                        break;
                }
            default:
                Log.w(TAG, "Unknown grade for mode:" + mode + " with score" + score);
                break;
        }
    }

    public static int calcGrade(int correct, int questions) {
        return (Math.round(correct / questions)) * 100;
    }

}

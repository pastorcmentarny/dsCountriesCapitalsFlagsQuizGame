package dms.pastor.cccfQuizGame.Utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

import dms.pastor.cccfQuizGame.R;

import static android.os.Build.VERSION.SDK_INT;

/**
 * Author Dominik Symonowicz
 * Created: 03/12/2012
 * WWW:	http://pastor.ovh.org
 * Github:	https://github.com/pastorcmentarny
 * Google Play:	https://play.google.com/store/apps/developer?id=Dominik+Symonowicz
 * LinkedIn: uk.linkedin.com/pub/dominik-symonowicz/5a/706/981/
 * <p>
 * Contains all commons methods for UI manipulation
 */
public final class UIUtils {
    private UIUtils() {
    }

    public static void setIncorrect(Activity activity, Context context, Button button) {
        Animation animWrong = AnimationUtils.loadAnimation(context, R.anim.remove_bad_answer);
        button.startAnimation(animWrong);
        button.setText(context.getResources().getString(R.string.incorrect));
        button.setTextColor(getColor(activity, R.color.error));
        button.setBackgroundColor(Color.BLACK);
        button.setEnabled(false);
    }

    public static void setFrozen(Context context, Activity activity, Button button) {
        button.setText(context.getString(R.string.frozen));
        button.setTextColor(getColor(activity, R.color.freeze_text));
        button.setBackgroundColor(getColor(activity, R.color.freeze_text_background));
        button.setEnabled(false);
    }

    public static void setRemoved(Context context, Activity activity, Button button) {
        Animation animWrong = AnimationUtils.loadAnimation(context, R.anim.remove_bad_answer);
        button.startAnimation(animWrong);
        button.setText(context.getResources().getString(R.string.removed));
        button.setTextColor(getColor(activity, R.color.removed_button));
        button.setBackgroundColor(Color.BLACK);
        button.setEnabled(false);
    }

    public static void newRecord(Context context, Activity activity, TextView text) {
        Animation animWrong = AnimationUtils.loadAnimation(context, R.anim.new_record);
        text.startAnimation(animWrong);
        text.setText(context.getResources().getString(R.string.new_record));
        setTextColor(text, R.color.good_news, activity);
        text.setTypeface(null, Typeface.BOLD);
        setBackgroundColor(context, text, R.color.error);
        text.setEnabled(false);
    }

    public static ArrayList<Button> shuffleButtons(ArrayList<Button> buttons) {
        int size = buttons.size();

        for (int i = 0; i < size; i++) {
            int r = i + (int) (Math.random() * (size - i));
            Button swap = buttons.get(r);
            buttons.set(r, buttons.get(i));
            buttons.set(i, swap);
        }
        return buttons;
    }

    @SuppressWarnings("deprecation")
    public static void setTextColor(TextView view, int color, Activity activity) {
        if (SDK_INT >= Build.VERSION_CODES.M) {
            view.setTextColor(activity.getResources().getColor(color, activity.getTheme()));
        } else {
            view.setTextColor(activity.getResources().getColor(color));
        }
    }

    @SuppressWarnings("deprecation")
    public static int getColor(Activity activity, int color) {
        if (SDK_INT >= Build.VERSION_CODES.M) {
            return activity.getResources().getColor(color, activity.getTheme());
        } else {
            return activity.getResources().getColor(color);
        }
    }


    @SuppressWarnings("deprecation")
    private static void setTextColor(TextView view, Context context) {
        if (SDK_INT >= Build.VERSION_CODES.M) {
            view.setTextColor(context.getResources().getColor(R.color.transparent, context.getTheme()));
        } else {
            view.setTextColor(context.getResources().getColor(R.color.transparent));
        }
    }

    @SuppressWarnings("deprecation")
    private static void setBackgroundColor(Context context, TextView view, int color) {
        if (SDK_INT >= Build.VERSION_CODES.M) {
            view.setBackgroundColor(context.getResources().getColor(color, context.getTheme()));
        } else {
            view.setBackgroundColor(context.getResources().getColor(color));
        }
    }

    @SuppressWarnings("deprecation")
    private static void setBackgroundColor(TextView view, Context context) {
        setBackgroundColor(context, view, R.color.transparent);
    }

    @SuppressWarnings("deprecation")
    public static void setBackground(Button button, Drawable background) {
        if (Build.VERSION.SDK_INT >= 16) {
            button.setBackground(background);
        } else {
            button.setBackgroundDrawable(background);
        }
    }

    public static void setUnTapButton(Context context, Button button) {
        Animation animWrong = AnimationUtils.loadAnimation(context, R.anim.untap_anim);
        button.startAnimation(animWrong);
        button.setEnabled(true);
        button.setText(R.string.tap_to_unfreeze);
        UIUtils.setBackground(button, Utils.getDrawable(context, R.drawable.unfreeze_button));
    }

    @SuppressWarnings("deprecation")
    public static void setTextAppearanceWithText(Context context, TextView view, int resId, int textId) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            view.setTextAppearance(resId);
        } else {
            view.setTextAppearance(context, resId);
        }
        view.setText(context.getString(textId));
    }

    public static void setInvisibleButton(Context context, Button button) {
        setTextColor(button, context);
        setBackgroundColor(button, context);
    }

}

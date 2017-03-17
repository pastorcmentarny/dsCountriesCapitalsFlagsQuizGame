package dms.pastor.cccfQuizGame.cdo;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.support.annotation.NonNull;
import android.view.View;

/**
 * Author Dominik Symonowicz
 * Created 16/02/2017 08:32
 * WWW:	http://pastor.ovh.org
 * Github:	https://github.com/pastorcmentarny
 * Google Play:	https://play.google.com/store/apps/developer?id=Dominik+Symonowicz
 * LinkedIn: uk.linkedin.com/pub/dominik-symonowicz/5a/706/981/
 */
public class Listeners {

    @NonNull
    public static OnClickListener getDoNothingListener() {
        return new OnClickListener() {
            public void onClick(final DialogInterface di, final int arg) {
            }
        };
    }

    @NonNull
    public static OnClickListener getDismissDialogOnClickUnderstandListener(final AlertDialog dialog) {
        return new OnClickListener() {
            public void onClick(final DialogInterface di, final int arg) {
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        };
    }

    @NonNull
    public static View.OnClickListener getDismissDialogOnClick(final Dialog countryDialog) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                countryDialog.dismiss();
            }
        };
    }

}

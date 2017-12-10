package dms.pastor.cccfQuizGame.Utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.Log;

import com.google.android.gms.ads.AdRequest;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import dms.pastor.cccfQuizGame.Config;
import dms.pastor.cccfQuizGame.R;
import dms.pastor.cccfQuizGame.cdo.Country;
import dms.pastor.cccfQuizGame.cdo.enums.Region;

import static android.content.Context.MODE_PRIVATE;
import static java.util.Collections.emptyList;


/**
 * Author Dominik Symonowicz
 * Created: 08/01/2013
 * WWW:	http://pastor.ovh.org
 * Github:	https://github.com/pastorcmentarny
 * Google Play:	https://play.google.com/store/apps/developer?id=Dominik+Symonowicz
 * LinkedIn: uk.linkedin.com/pub/dominik-symonowicz/5a/706/981/
 */
public final class Utils {
    private static final String TAG = "Utils";

    private Utils() {
    }

    public static List<Country> readCountriesDataFromFile(Context context) {
        Log.i(TAG, "Loading countries to dictionary from file");
        ArrayList<Country> countriesList = new ArrayList<>();
        InputStream inputStream;
        try {
            inputStream = context.getResources().openRawResource(Config.COUNTRIES);
        } catch (Resources.NotFoundException e) {
            Log.e(TAG, context.getString(R.string.problem) + e.getMessage());
            return emptyList();
        }

        DataInputStream in = new DataInputStream(inputStream);
        BufferedReader br = null;
        InputStreamReader isr = null;
        String strLine;
        String[] data;
        Country country;
        int nr = 0;
        try {
            isr = new InputStreamReader(in);
            br = new BufferedReader(isr);
            while ((strLine = br.readLine()) != null) {
                nr++;
                data = strLine.split(";;");
                ArrayList<Region> regions;
                String[] regionsS;
                try {
                    regionsS = data[5].split("::");
                    regions = new ArrayList<>(regionsS.length);
                    for (String region : regionsS) {
                        regions.add(Region.valueOf(region));

                    }
                    country = new Country(Integer.parseInt(data[1]), data[2], data[3], data[4], regions, data[6], data[7]);
                    countriesList.add(country);
                } catch (ArrayIndexOutOfBoundsException aioobe) {
                    Log.w(TAG, "No category for country in line: " + nr + ".Reason: " + aioobe.getMessage());
                    return emptyList();
                }
            }
        } catch (NumberFormatException nfe) {
            Log.w(TAG, "Problem with reading file. (Line: " + nr + ")\n(NumberFormatException): " + nfe.getMessage());
            return emptyList();
        } catch (IOException e) {
            Log.w(TAG, "Problem with reading file. (Line: " + nr + ")\n(IOException):" + e.getMessage());
            return emptyList();
        } catch (ArrayIndexOutOfBoundsException aioobe) {
            Log.w(TAG, "Dictionary data is corrupted. (Line: " + nr + ")\n(ArrayIndexOutOfBoundsException)" + aioobe.getMessage());
            return emptyList();
        } finally {
            if (isr != null) {
                try {
                    isr.close();
                } catch (IOException e) {
                    Log.w(TAG, "Problem with closing stream for read Dictionary From File.\nError message:" + e.getMessage());
                }
            }
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    Log.w(TAG, "Problem with closing stream for read Dictionary From File.\nError message:" + e.getMessage());
                }
            }
        }

        try {
            in.close();
        } catch (IOException e) {
            Log.w(TAG, "Problem with \"closes this stream\"  file.\n" + e.getMessage());
            return emptyList();
        }
        return countriesList;
    }

    public static List<Country> shuffle(List<Country> countries) {
        int number = countries.size();
        for (int i = 0; i < number; i++) {
            int r = i + (int) (Math.random() * (number - i));
            Country swap = countries.get(r);
            countries.set(r, countries.get(i));
            countries.set(i, swap);
        }
        return countries;
    }

    public static void resetHighScore(Context context) {
        SharedPreferences settings = context.getSharedPreferences("highscore", MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = settings.edit();
        prefsEditor.clear();
        prefsEditor.apply();
    }

    static void displayErrorForISE(String what, String where, String errorMessage) {
        String msg = what + " occurred, while " + where + "\nMsg:" + errorMessage;
        Log.w(Config.DOMS, msg);
    }

    public static void saveSelectedCustomGame(Context context, String question, String answer, String gameMode) {
        SharedPreferences settings = context.getSharedPreferences("settings", MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = settings.edit();
        prefsEditor.putString("cg_question", question);
        prefsEditor.putString("cg_answer", answer);
        prefsEditor.putString("cg_gameMode", gameMode);
        prefsEditor.apply();
    }

    public static String getResultTimeAsString(long time) {
        int sec = (int) time / 1000;
        int dot = (int) time % 1000;
        return String.valueOf(sec) + "." + String.valueOf(dot) + "s.";
    }

    public static AdRequest getAdRequest() {
        return new AdRequest.Builder()
                .addTestDevice("598E91746DAA4C010BE3D61C4C4E454E")
                .build();
    }

    @SuppressWarnings("deprecation")
    public static Drawable getDrawable(Context context, int id) {
        if (Build.VERSION.SDK_INT >= 22) {
            return context.getDrawable(id);
        } else {
            return context.getResources().getDrawable(id);
        }

    }

    public static boolean isNull(Object object) {
        return object == null;
    }
}

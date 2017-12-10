package dms.pastor.cccfQuizGame.UI;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ListActivity;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory.Options;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import dms.pastor.cccfQuizGame.R;
import dms.pastor.cccfQuizGame.Utils.Utils;
import dms.pastor.cccfQuizGame.cdo.Country;
import dms.pastor.cccfQuizGame.cdo.enums.Region;
import dms.pastor.cccfQuizGame.cdo.graphics.SampledBitmapFromResourceDecoder;

import static dms.pastor.cccfQuizGame.Utils.StringUtils.EMPTY_STRING;
import static dms.pastor.cccfQuizGame.Utils.StringUtils.WHITESPACE;
import static dms.pastor.cccfQuizGame.cdo.Listeners.getDismissDialogOnClick;
import static dms.pastor.cccfQuizGame.cdo.Listeners.getDoNothingListener;
import static dms.pastor.cccfQuizGame.cdo.graphics.InSampleSizeCalculator.DEFAULT_IN_SAMPLE_SIZE;

/**
 * Author Dominik Symonowicz
 * Created: 18/01/13 11:24
 * WWW:	http://pastor.ovh.org
 * Github:	https://github.com/pastorcmentarny
 * Google Play:	https://play.google.com/store/apps/developer?id=Dominik+Symonowicz
 * LinkedIn: uk.linkedin.com/pub/dominik-symonowicz/5a/706/981/
 * <p/>
 * Displays list of countries and all useful information about them
 */
public class CountryList extends ListActivity {
    private static final String TAG = "Vocabulary List";
    private static final String DEFAULT_TYPE = "drawable";

    private List<Country> allCountries;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        allCountries = Utils.readCountriesDataFromFile(this);
        String[] countriesList = generateWordList();
        setListAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, countriesList));
        getListView().setTextFilterEnabled(true);
    }

    @Override
    protected void onListItemClick(ListView listView, View view, int position, long id) {
        super.onListItemClick(listView, view, position, id);
        showInformationAboutCountry(position);
    }

    private void showInformationAboutCountry(int position) {
        Country country = getCountryFromAllCountries(position);
        if (country != null) {
            displayInformationAboutCountryDialog(getInformationAboutCountry(country), country);
        } else {
            Log.e(TAG, "unable to get country ");
            displayUnableToGetCountryDialog(position);
        }
    }

    private String getInformationAboutCountry(Country country) {
        return String.format("Capital: %s\nRegion(s): %s\nNotes: %s\n\n",
                country.getCapital(),
                getRegionsThatCountryBelongsTo(country.getRegions()),
                country.getNotes());
    }

    private void displayInformationAboutCountryDialog(String countryInformation, Country country) {
        final Dialog countryDialog = new Dialog(this);
        countryDialog.setContentView(R.layout.countrydialog);
        countryDialog.setTitle(getResources().getString(R.string.country_list_title));

        setImage(country, countryDialog);

        TextView countryName = countryDialog.findViewById(R.id.countryName);
        countryName.setText(country.getCountry());

        TextView message = countryDialog.findViewById(R.id.countryDialogMessage);
        message.setText(countryInformation);

        Button dialogButton = countryDialog.findViewById(R.id.buttonOK);
        dialogButton.setOnClickListener(getDismissDialogOnClick(countryDialog));
        countryDialog.show();
    }

    private void setImage(Country country, Dialog countryDialog) {
        ImageView flagImage = countryDialog.findViewById(R.id.image);
        Options options = new Options();
        options.inSampleSize = DEFAULT_IN_SAMPLE_SIZE;

        Bitmap bitmap = SampledBitmapFromResourceDecoder.decodeToBitmap(this.getResources(),
                this.getResources().getIdentifier(country.getFlagId(), DEFAULT_TYPE, getPackageName()));
        flagImage.setImageBitmap(bitmap);
        flagImage.setImageResource(getResources().getIdentifier(country.getFlagId(), DEFAULT_TYPE, getPackageName()));
    }

    private void displayUnableToGetCountryDialog(int position) {
        AlertDialog.Builder alertBox = new AlertDialog.Builder(this);
        alertBox.setTitle(getString(R.string.error));
        alertBox.setMessage(getString(R.string.error_dictionary_problem) + position);
        alertBox.setNeutralButton(getResources().getString(R.string.awesome), getDoNothingListener());
        alertBox.show();
    }

    private Country getCountryFromAllCountries(int id) {
        id++;
        Log.i(TAG, ("DictSize: " + allCountries.size() + "Look for:" + id));
        for (Country country : allCountries) {
            if (country.getId() == id) {
                return country;
            }
        }
        return null;
    }

    private String[] generateWordList() {
        Log.i(TAG, "Generating countries list");
        ArrayList<String> countries = new ArrayList<>();
        for (Country country : allCountries) {
            countries.add(country.toString());
        }
        return countries.toArray(new String[countries.size()]);
    }

    private String getRegionsThatCountryBelongsTo(List<Region> regions) {
        StringBuilder regionList = new StringBuilder(EMPTY_STRING);
        for (Region region : regions) {
            regionList.append(region.toString()).append(WHITESPACE);
        }
        return regionList.toString();
    }
}
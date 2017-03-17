package dms.pastor.cccfQuizGame.cdo;

import android.support.annotation.NonNull;

import java.util.Collections;
import java.util.List;

import dms.pastor.cccfQuizGame.cdo.enums.Region;

import static dms.pastor.cccfQuizGame.Utils.StringUtils.EMPTY_STRING;
import static java.lang.String.format;

/**
 * Author Dominik Symonowicz
 * Created 07/01/2013
 * WWW:	http://pastor.ovh.org
 * Github:	https://github.com/pastorcmentarny
 * Google Play:	https://play.google.com/store/apps/developer?id=Dominik+Symonowicz
 * LinkedIn: uk.linkedin.com/pub/dominik-symonowicz/5a/706/981/
 * <p>
 * Country representation for this game and dom learns chinese
 */
public class Country {
    private int id;
    private String countryName;
    private String country;
    private String capital;
    private List<Region> region;
    private String flagId;
    private String notes;

    public Country(int id, String countryName, String country, String capital, List<Region> region, String flagId, String notes) {
        setId(id);
        setCountryName(countryName);
        setCountry(country);
        setCapital(capital);
        setRegion(region);
        setFlagId(flagId);
        setNotes(notes);
    }

    @NonNull
    public static Country noCountry() {
        return new Country(0, null, null, null, Collections.<Region>emptyList(), null, EMPTY_STRING);
    }

    public String getNotes() {
        return notes != null ? notes : EMPTY_STRING;
    }

    private void setNotes(String notes) {
        this.notes = notes;
    }

    public int getId() {
        return id;
    }

    private void setId(int id) {
        this.id = id;
    }

    private String getCountryName() {
        return countryName;
    }

    private void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public String getCountry() {
        return country;
    }

    private void setCountry(String country) {
        this.country = country;
    }

    public String getCapital() {
        return capital;
    }

    private void setCapital(String capital) {
        this.capital = capital;
    }

    public List<Region> getRegions() {
        return region;
    }

    private void setRegion(List<Region> region) {
        this.region = region;
    }

    public String getFlagId() {
        return flagId;
    }

    private void setFlagId(String flagId) {
        this.flagId = flagId;
    }

    @Override
    public String toString() {
        return format("------------\nCountry: '%s' {Capital: '%s'}\n-°ﾟº｡°ﾟº°｡°-\n", getCountryName(), getCapital());
    }

    public void setReqion(List<Region> region) {
        this.region = region;
    }
}

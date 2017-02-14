package com.vorogushinigor.cityfavorite.model;

/**
 * Created by viv on 12.02.2017.
 */

public class StructureCountryCity {

    public final static int TAG_COUNTRY = 1;
    public final static int TAG_CITY = 2;

    private Country country;
    private City city;
    private int tag;
    private boolean countryOpen;

    public StructureCountryCity(Country country, int tag, boolean countryOpen) {
        this.country = country;
        this.tag = tag;
        this.countryOpen=countryOpen;
    }

    public StructureCountryCity(City city, int tag) {
        this.city = city;
        this.tag = tag;
    }

    public int getTag() {
        return tag;
    }

    public City getCity() {
        return city;
    }

    public Country getCountry() {
        return country;
    }

    public boolean isCountryOpen() {
        return countryOpen;
    }

    public void setCountryOpen(boolean countryOpen) {
        this.countryOpen = countryOpen;
    }
}

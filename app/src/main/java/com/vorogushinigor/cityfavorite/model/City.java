
package com.vorogushinigor.cityfavorite.model;

import java.util.List;

import com.google.gson.Gson;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class City {

    @SerializedName("Id")
    @Expose
    private Integer id;
    @SerializedName("CountryId")
    @Expose
    private Integer countryId;
    @SerializedName("Name")
    @Expose
    private String name;
    @SerializedName("ImageLink")
    @Expose
    private Object imageLink;

    private boolean favorites;
    private boolean selected;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getCountryId() {
        return countryId;
    }

    public void setCountryId(Integer countryId) {
        this.countryId = countryId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Object getImageLink() {
        return imageLink;
    }

    public void setImageLink(Object imageLink) {
        this.imageLink = imageLink;
    }

    public boolean isFavorites() {
        return favorites;
    }

    public void setFavorites(boolean favorites) {
        this.favorites = favorites;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}

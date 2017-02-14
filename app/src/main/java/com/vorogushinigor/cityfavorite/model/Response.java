
package com.vorogushinigor.cityfavorite.model;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Response {

    @SerializedName("Timestamp")
    @Expose
    private String timestamp;
    @SerializedName("Error")
    @Expose
    private Object error;
    @SerializedName("Result")
    @Expose
    private List<Country> country = null;

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public Object getError() {
        return error;
    }

    public void setError(Object error) {
        this.error = error;
    }

    public List<Country> getCountry() {
        return country;
    }

    public void setCountry(List<Country> country) {
        this.country = country;
    }

}

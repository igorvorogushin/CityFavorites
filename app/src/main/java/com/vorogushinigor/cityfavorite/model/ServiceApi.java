package com.vorogushinigor.cityfavorite.model;


import retrofit2.http.GET;
import rx.Observable;

public interface ServiceApi {
    @GET("api/countries")
    Observable<Response> getCountry();

}

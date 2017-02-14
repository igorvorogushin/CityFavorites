package com.vorogushinigor.cityfavorite.viewmodel;

import android.content.Context;
import android.content.SharedPreferences;
import android.databinding.BaseObservable;
import android.preference.PreferenceManager;
import android.util.Log;

import com.google.gson.Gson;
import com.vorogushinigor.cityfavorite.R;
import com.vorogushinigor.cityfavorite.model.Response;
import com.vorogushinigor.cityfavorite.model.ServiceApi;
import com.vorogushinigor.cityfavorite.model.StructureCountryCity;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


public class ViewModelMain extends BaseObservable implements ViewModel {

    private static final String TAG_LOG = ViewModelMain.class.getName();
    private static final String URL = "https://atw-backend.azurewebsites.net/";

    private static ViewModelMain viewModel;
    private static Context context;


    private Response response;
    private List<StructureCountryCity> listCity;
    private List<StructureCountryCity> listCityFavorite;

    private ServiceApi serviceApi;
    private Observable<Response> observable;
    private Subscription subscription;
    private Subscriber<Response> subscriber;
    private boolean loading = false;
    private CallBack callBack;

    public interface CallBack {

        void updateCity(List<StructureCountryCity> list);

        void updateCityFavorite(List<StructureCountryCity> list);

        void message(String message);
    }


    public static ViewModelMain getInstance(Context context) {
        if (viewModel == null)
            viewModel = new ViewModelMain();
        ViewModelMain.context = context;
        return viewModel;
    }

    private ViewModelMain() {

    }


    public void init() {
        loadResponse();
        if (response != null) {
            createStructureCountryCity();
            createStructureCity();
            if (callBack != null) {
                callBack.updateCity(listCity);
                callBack.updateCityFavorite(listCityFavorite);
            }
        }
        if (listCity == null && !loading)
            start();
    }

    private void createStructureCountryCity() {
        listCity = new ArrayList<>();
        for (int i = 0; i < response.getCountry().size(); i++) {
            listCity.add(new StructureCountryCity(response.getCountry().get(i), StructureCountryCity.TAG_COUNTRY, false));
            //for open all list
         /*   if (response.getCountry().get(i).getCities() != null)
                for (int j = 0; j < response.getCountry().get(i).getCities().size(); j++) {
                    listCity.add(new StructureCountryCity(response.getCountry().get(i).getCities().get(j), StructureCountryCity.TAG_CITY));
                }*/
        }
    }

    private void createStructureCity() {
        listCityFavorite = new ArrayList<>();
        for (int i = 0; i < response.getCountry().size(); i++)
            if (response.getCountry().get(i).getCities() != null)
                for (int j = 0; j < response.getCountry().get(i).getCities().size(); j++) {
                    listCityFavorite.add(new StructureCountryCity(response.getCountry().get(i).getCities().get(j), StructureCountryCity.TAG_CITY));
                }

    }

    private void start() {
        createRetrofit();
        createSubscriber();
        createObservable();
    }

    public void setCallBack(CallBack callBack) {
        this.callBack = callBack;
    }

    private OkHttpClient getOkHttpClient() {
        return new OkHttpClient.Builder()
                .readTimeout(10, TimeUnit.SECONDS)
                .connectTimeout(5, TimeUnit.SECONDS)
                .build();
    }

    private void createRetrofit() {
        serviceApi = new Retrofit.Builder()
                .baseUrl(URL).client(getOkHttpClient())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build().create(ServiceApi.class);
    }

    private void createSubscriber() {
        subscriber = new Subscriber<Response>() {
            @Override
            public void onCompleted() {
                Log.v(TAG_LOG, "onCompleted");
                loading = false;
                if (callBack != null) callBack.message(context.getString(R.string.update));

            }

            @Override
            public void onStart() {
                super.onStart();
                Log.v(TAG_LOG, "onStart");
                loading = true;
            }

            @Override
            public void onError(Throwable e) {
                Log.v(TAG_LOG, "onError " + e.getMessage());
                loading = false;
                if (callBack != null)
                    callBack.message(context.getString(R.string.error) + ": " + e.getMessage());

            }

            @Override
            public void onNext(Response _response) {
                Log.v(TAG_LOG, "onNext");
                response = _response;
                createStructureCountryCity();
                createStructureCity();
                if (callBack != null) {
                    callBack.updateCity(listCity);
                    callBack.updateCityFavorite(listCityFavorite);
                }
                saveResponse();
            }
        };
    }

    private void createObservable() {
        observable = serviceApi.getCountry();
        subscription = observable.observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread()).subscribe(subscriber);
    }

    public void saveResponse() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor ed = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(response);
        ed.putString(Response.class.getName(), json);
        ed.commit();
    }

    private void loadResponse() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        Gson gson = new Gson();
        String json = sharedPreferences.getString(Response.class.getName(), null);
        response = gson.fromJson(json, Response.class);
    }

    @Override
    public void onDestroy() {
        callBack = null;
        context = null;
    }


}
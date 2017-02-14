package com.vorogushinigor.cityfavorite.view.activity;

import android.databinding.DataBindingUtil;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.vorogushinigor.cityfavorite.R;
import com.vorogushinigor.cityfavorite.databinding.ActivityMainBinding;
import com.vorogushinigor.cityfavorite.model.StructureCountryCity;
import com.vorogushinigor.cityfavorite.view.adapter.TabsAdapter;
import com.vorogushinigor.cityfavorite.view.fragment.CityFragment;
import com.vorogushinigor.cityfavorite.view.fragment.FavoriteFragment;
import com.vorogushinigor.cityfavorite.view.fragment.MainFragment;
import com.vorogushinigor.cityfavorite.view.fragment.SettingFragment;
import com.vorogushinigor.cityfavorite.viewmodel.ViewModelMain;

import java.util.List;

public class MainActivity extends AppCompatActivity implements ViewModelMain.CallBack, MainFragment.CallBack {

    private ActivityMainBinding binding;
    private ViewModelMain viewModelMain;
    private MainFragment mainFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme_NoActionBar);
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        setSupportActionBar(binding.toolbar);

        viewModelMain = ViewModelMain.getInstance(getApplicationContext());
        viewModelMain.setCallBack(this);
        startFragment();

        if (savedInstanceState == null) {
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                public void run() {
                    viewModelMain.init();
                }
            }, 1);
        }

    }

    private void startFragment() {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        mainFragment = (MainFragment) getSupportFragmentManager().findFragmentByTag(MainFragment.class.getName());
        if (mainFragment == null)
            mainFragment = new MainFragment();
        mainFragment.setRetainInstance(true);
        mainFragment.setCallBack(this);

        fragmentTransaction.replace(R.id.frame, mainFragment, MainFragment.class.getName());
        fragmentTransaction.commit();
    }

    @Override
    protected void onDestroy() {
        viewModelMain.onDestroy();
        super.onDestroy();
    }

    @Override
    public void updateCity(List<StructureCountryCity> list) {
        mainFragment.updateCity(list);

    }

    @Override
    public void updateCityFavorite(List<StructureCountryCity> list) {
        mainFragment.updateCityFavorite(list);
    }

    @Override
    public void message(String message) {
        Snackbar snackbar = Snackbar.make(binding.coordinator, message, Snackbar.LENGTH_SHORT);
        snackbar.show();
    }

    @Override
    public void initPageView(ViewPager viewPager) {
        binding.tab.setupWithViewPager(viewPager);
    }

    @Override
    public void saveResponse() {
        viewModelMain.saveResponse();
    }
}

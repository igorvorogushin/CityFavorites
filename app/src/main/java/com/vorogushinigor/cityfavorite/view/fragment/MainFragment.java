package com.vorogushinigor.cityfavorite.view.fragment;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.vorogushinigor.cityfavorite.R;
import com.vorogushinigor.cityfavorite.databinding.FragmentMainBinding;
import com.vorogushinigor.cityfavorite.model.StructureCountryCity;
import com.vorogushinigor.cityfavorite.view.adapter.TabsAdapter;

import java.util.List;


public class MainFragment extends Fragment {
    private FavoriteFragment favoriteFragment;
    private CityFragment cityFragment;
    private SettingFragment settingFragment;

    private CallBack callBack;

    public interface CallBack {
        void initPageView(ViewPager viewPager);

        void saveResponse();
    }

    public void setCallBack(CallBack callBack) {
        this.callBack = callBack;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        FragmentMainBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_main, container, false);
        View view = binding.getRoot();

        if (cityFragment == null)
            cityFragment = new CityFragment();
        cityFragment.setRetainInstance(true);

        if (favoriteFragment == null)
            favoriteFragment = new FavoriteFragment();
        favoriteFragment.setRetainInstance(true);

        if (settingFragment == null)
            settingFragment = new SettingFragment();
        settingFragment.setRetainInstance(true);

        cityFragment.setCallBack(new CityFragment.CallBack() {
            @Override
            public void updateFavorite() {
                if (callBack != null) callBack.saveResponse();
                favoriteFragment.notifyAdapter();
            }
        });

        favoriteFragment.setCallBack(new FavoriteFragment.CallBack() {
            @Override
            public void updateCity() {
                if (callBack != null) callBack.saveResponse();
                cityFragment.notifyAdapter();

            }
        });


        TabsAdapter tabsAdapter = new TabsAdapter(getChildFragmentManager());
        tabsAdapter.addFrag(cityFragment, getString(R.string.tab_city));
        tabsAdapter.addFrag(favoriteFragment, getString(R.string.tab_favorite));
        tabsAdapter.addFrag(settingFragment, getString(R.string.tab_setting));
        binding.viewpager.setAdapter(tabsAdapter);

        if (callBack != null) callBack.initPageView(binding.viewpager);

        return view;
    }


    public void updateCity(List<StructureCountryCity> list) {
        if (cityFragment != null) cityFragment.setCity(list);

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    public void updateCityFavorite(List<StructureCountryCity> list) {
        if (favoriteFragment != null) favoriteFragment.setCity(list);
    }


}

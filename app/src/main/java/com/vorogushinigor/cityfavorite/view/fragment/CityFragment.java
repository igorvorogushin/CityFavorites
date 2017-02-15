package com.vorogushinigor.cityfavorite.view.fragment;


import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.vorogushinigor.cityfavorite.R;
import com.vorogushinigor.cityfavorite.databinding.FragmentCityBinding;
import com.vorogushinigor.cityfavorite.model.StructureCountryCity;
import com.vorogushinigor.cityfavorite.view.adapter.CountryAdapter;

import java.util.List;


public class CityFragment extends Fragment implements CountryAdapter.CallBack {

    public interface CallBack {
        void updateFavorite();
    }

    private CallBack callBack;
    private FragmentCityBinding binding;
    private boolean visible;
    private CountryAdapter countryAdapter;
    private List<StructureCountryCity> list;

    public void setCallBack(CallBack callBack) {
        this.callBack = callBack;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_city, container, false);
        View view = binding.getRoot();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        binding.recyclerview.setLayoutManager(linearLayoutManager);

        return view;
    }

    public void setCity(List<StructureCountryCity> list) {
        this.list = list;
        countryAdapter = new CountryAdapter(list);
        countryAdapter.setCallBack(this);
        if (visible)
            binding.recyclerview.setAdapter(countryAdapter);
    }

    @Override
    public void onPause() {
        visible = false;
        binding.recyclerview.setAdapter(null);
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        visible = true;
        if (countryAdapter != null) {
            countryAdapter.setCallBack(this);
            binding.recyclerview.setAdapter(countryAdapter);
        }

    }

    public void notifyAdapter() {
        if (countryAdapter != null) countryAdapter.notifyDataSetChanged();
    }

    @Override
    public void onDestroyView() {
        binding = null;
        super.onDestroyView();
    }


    @Override
    public void update() {
        if (callBack != null) callBack.updateFavorite();
    }
}

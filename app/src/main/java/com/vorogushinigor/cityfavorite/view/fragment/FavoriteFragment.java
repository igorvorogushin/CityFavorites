package com.vorogushinigor.cityfavorite.view.fragment;


import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.vorogushinigor.cityfavorite.R;
import com.vorogushinigor.cityfavorite.databinding.FragmentFavoriteBinding;
import com.vorogushinigor.cityfavorite.model.StructureCountryCity;
import com.vorogushinigor.cityfavorite.view.adapter.FavoriteAdapter;

import java.util.List;


public class FavoriteFragment extends Fragment implements FavoriteAdapter.CallBack {

    public interface CallBack {
        void updateCity();
    }

    private CallBack callBack;
    private FragmentFavoriteBinding binding;
    private boolean visible;
    private List<StructureCountryCity> list;
    private FavoriteAdapter favoriteAdapter;

    public void setCallBack(CallBack callBack) {
        this.callBack = callBack;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_favorite, container, false);
        View view = binding.getRoot();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        binding.recyclerview.setLayoutManager(linearLayoutManager);
        return view;
    }


    public void setCity(List<StructureCountryCity> list) {
        this.list = list;
        favoriteAdapter = new FavoriteAdapter(list);
        favoriteAdapter.setCallBack(this);
        if (visible)
            binding.recyclerview.setAdapter(favoriteAdapter);

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
        if (favoriteAdapter != null && list != null) {
            favoriteAdapter.setCallBack(this);
            binding.recyclerview.setAdapter(favoriteAdapter);
        }
    }

    public void notifyAdapter() {
        if (list != null) setCity(list);
    }

    @Override
    public void onDestroyView() {
        binding = null;
        super.onDestroyView();
    }

    @Override
    public void update() {
        if (callBack != null) callBack.updateCity();
    }
}

package com.vorogushinigor.cityfavorite.view.adapter;


import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.vorogushinigor.cityfavorite.databinding.AdapterCityFavoritesBinding;
import com.vorogushinigor.cityfavorite.model.StructureCountryCity;

import java.util.ArrayList;
import java.util.List;


public class FavoriteAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private CallBack callBack;

    public interface CallBack {
        void update();
    }

    public void setCallBack(CallBack callBack) {
        this.callBack = callBack;
    }

    private List<StructureCountryCity> list;
    private List<StructureCountryCity> listTemp;


    class ViewHolder extends RecyclerView.ViewHolder {
        private AdapterCityFavoritesBinding binding;

        ViewHolder(View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);
        }
    }

    public FavoriteAdapter(List<StructureCountryCity> list) {
        this.list = list;
        listTemp = new ArrayList<>(list);

        for (int i = 0; i < listTemp.size(); i++)
            if (!listTemp.get(i).getCity().isFavorites()) {
                listTemp.remove(i);
                i--;
            }
    }

    private void delete(int position) {
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, list.size());
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return new ViewHolder(AdapterCityFavoritesBinding.inflate(inflater, parent, false).getRoot());

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof ViewHolder) {
            ((ViewHolder) holder).binding.setModel(listTemp.get(position).getCity());


            ((ViewHolder) holder).binding.btRemove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listTemp.get(position).getCity().setFavorites(false);
                    listTemp.remove(position);
                    delete(position);
                    if (callBack != null) callBack.update();
                }
            });

        }
    }

    @Override
    public int getItemCount() {
        return listTemp.size();
    }


}

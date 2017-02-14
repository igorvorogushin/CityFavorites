package com.vorogushinigor.cityfavorite.view.adapter;


import android.databinding.BindingAdapter;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.vorogushinigor.cityfavorite.databinding.AdapterCityBinding;
import com.vorogushinigor.cityfavorite.databinding.AdapterCountryBinding;
import com.vorogushinigor.cityfavorite.model.City;
import com.vorogushinigor.cityfavorite.model.Country;
import com.vorogushinigor.cityfavorite.model.StructureCountryCity;

import java.util.ArrayList;
import java.util.List;


public class CountryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private CallBack callBack;
    private int lastSelected = -1;

    public interface CallBack {
        void update();
    }


    public void setCallBack(CallBack callBack) {
        this.callBack = callBack;
    }

    private List<StructureCountryCity> list;

    @Override
    public int getItemViewType(int position) {
        return list.get(position).getTag();
    }

    class ViewHolderCountry extends RecyclerView.ViewHolder {
        private AdapterCountryBinding binding;

        ViewHolderCountry(View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);
        }
    }


    class ViewHolderCity extends RecyclerView.ViewHolder {
        private AdapterCityBinding binding;

        ViewHolderCity(View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);
        }
    }


    public CountryAdapter(List<StructureCountryCity> list) {
        this.list = new ArrayList<>(list);
    }

    private void add(Country country, int position) {
        for (int i = 0; i < country.getCities().size(); i++) {
            position++;
            list.add(position, new StructureCountryCity(country.getCities().get(i), StructureCountryCity.TAG_CITY));
            notifyItemInserted(position);
        }
        notifyItemRangeChanged(position, list.size());


    }

    private void delete(int position) {
        for (int i = position; i < list.size(); i++) {
            if (list.get(i).getTag() == StructureCountryCity.TAG_CITY) {
                list.remove(position);
                notifyItemRemoved(position);
                i--;
            } else {
                break;
            }
        }
        notifyItemRangeChanged(position, list.size());
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        if (viewType == StructureCountryCity.TAG_COUNTRY)
            return new ViewHolderCountry(AdapterCountryBinding.inflate(inflater, parent, false).getRoot());
        else
            return new ViewHolderCity(AdapterCityBinding.inflate(inflater, parent, false).getRoot());
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof ViewHolderCountry) {

            final Country country = list.get(position).getCountry();
            ((ViewHolderCountry) holder).binding.setModel(country);
            ((ViewHolderCountry) holder).binding.liner.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (list.get(position).isCountryOpen()) {
                        delete(position + 1);

                    } else {
                        add(country, position);
                    }

                    list.get(position).setCountryOpen(!list.get(position).isCountryOpen());


                }
            });
        } else {
            if (list.get(position).getCity().isSelected()) lastSelected = position;

            final City city = list.get(position).getCity();
            ((ViewHolderCity) holder).binding.setModel(city);
            ((ViewHolderCity) holder).binding.btFavorites.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    city.setFavorites(!city.isFavorites());
                    notifyItemChanged(position);
                    if (callBack != null) callBack.update();
                }
            });
            ((ViewHolderCity) holder).binding.liner.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (lastSelected != -1) {
                        list.get(lastSelected).getCity().setSelected(false);
                        notifyItemChanged(lastSelected);
                    }
                    city.setSelected(!city.isSelected());
                    notifyItemChanged(position);
                    if (callBack != null) callBack.update();
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    /*
        @BindingAdapter("bind:imageUrl")
        public static void loadImage(ImageView imageView, String path) {
            Picasso.with(imageView.getContext())
                    .load(path)
                    .error(R.drawable.ic_error_outline_black_48dp)
                    .placeholder(R.drawable.progress_animation)
                    .into(imageView);
        }
    */
    @BindingAdapter("bind:text")
    public static void text(TextView textView, String text) {
        textView.setText(text);
    }


}

package com.app.blitz.countriesapp.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.blitz.countriesapp.R;
import com.app.blitz.countriesapp.model.CountryModel;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

import static com.app.blitz.countriesapp.view.Util.getProgressDrawable;
import static com.app.blitz.countriesapp.view.Util.loadImage;


public class AdapterCountryList extends RecyclerView.Adapter<AdapterCountryList.CountryViewHolder> {

    private List<CountryModel> countryModels;
    private Context context;

    public AdapterCountryList(List<CountryModel> countryModels, Context context) {
        this.countryModels = countryModels;
        this.context = context;
    }

    public void updateCountries(List<CountryModel> newCountries){
        countryModels.clear();
        countryModels.addAll(newCountries);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CountryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_country, parent, false);
        return new CountryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CountryViewHolder holder, int position) {
        holder.bind(countryModels.get(position));
    }

    @Override
    public int getItemCount() {
        return countryModels.size();
    }

    class CountryViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.image_country)
        CircleImageView imageViewCountry;

        @BindView(R.id.txt_name_country)
        TextView textViewNameCountry;

        @BindView(R.id.txt_capital_country)
        TextView textViewCapitalCountry;

        public CountryViewHolder(@NonNull View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);
        }

        void bind(CountryModel countryModel) {
            textViewNameCountry.setText(countryModel.getCountryName());
            textViewCapitalCountry.setText(countryModel.getCapital());
            loadImage(imageViewCountry, countryModel.getFlag(), getProgressDrawable(imageViewCountry.getContext()));
        }
    }
}

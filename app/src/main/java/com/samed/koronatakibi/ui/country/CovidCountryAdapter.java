package com.samed.koronatakibi.ui.country;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.samed.koronatakibi.R;

import java.util.ArrayList;
import java.util.List;

public class CovidCountryAdapter extends RecyclerView.Adapter<CovidCountryAdapter.ViewHolder> implements Filterable {

    List<CovidCountry> covidCountries;
    static List<CovidCountry> covidCountriesFiltered;
    private Context context;

    boolean isDark=false;

    public CovidCountryAdapter(List<CovidCountry> covidCountries, Context context) {
        this.covidCountries = covidCountries;
        this.context = context;
        covidCountriesFiltered=covidCountries;

    }

    public CovidCountryAdapter(List<CovidCountry> covidCountries, Context context, boolean isDark){
        this.covidCountries=covidCountries;
        this.context=context;

        this.isDark=isDark;
        covidCountriesFiltered=covidCountries;
    }

    @NonNull
    @Override
    public CovidCountryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_covid_country,parent,false);


        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CovidCountryAdapter.ViewHolder holder, int position) {
        //CovidCountry covidCountry=covidCountriesFiltered.get(position);


        holder.container.setAnimation(AnimationUtils.loadAnimation(context,R.anim.fade_transition_anim));
        holder.container.setAnimation(AnimationUtils.loadAnimation(context,R.anim.fade_scale_anim));


        holder.tvTotalCases.setText(covidCountriesFiltered.get(position).getmCases());
        holder.tvCountryName.setText(covidCountriesFiltered.get(position).getmCovidCountry());

        //Glide
        Glide.with(context)
                .load(covidCountriesFiltered.get(position).getmFlags())
                .apply(new RequestOptions().override(240,160))
                .into(holder.imgCountryFlag);

    }

    @Override
    public int getItemCount() {
        return covidCountriesFiltered.size();
    }

    /*@Override
    public Filter getFilter() {
        return covidCountriesFilter;
    }*/

   /* private Filter covidCountriesFilter=new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<CovidCountry> filteredCovidCountry=new ArrayList<>();

            if(constraint==null || constraint.length()==0){
                filteredCovidCountry.addAll(covidCountriesFiltered);
            }else{
                String filterPattern=constraint.toString().toLowerCase().trim();
                for(CovidCountry itemCovidCountry:covidCountriesFiltered){
                    if(itemCovidCountry.getmCovidCountry().toLowerCase().contains(filterPattern)){
                        filteredCovidCountry.add(itemCovidCountry);
                    }
                }
            }

            FilterResults results=new FilterResults();
            results.values=filteredCovidCountry;
            return  results;

        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            covidCountries.clear();
            covidCountries.addAll((List)results.values);
            notifyDataSetChanged();
        }
    };*/

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String Key=constraint.toString();
                if(Key.isEmpty()){
                    covidCountriesFiltered=covidCountries;
                }else{
                    ArrayList<CovidCountry> lstFiltered=new ArrayList<>();
                    for(CovidCountry row:covidCountries){
                        if(row.getmCovidCountry().toLowerCase().contains(Key.toLowerCase())){
                            lstFiltered.add(row);
                        }
                    }
                    covidCountriesFiltered=lstFiltered;
                }

                FilterResults filterResults=new FilterResults();
                filterResults.values=covidCountriesFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                covidCountriesFiltered=(ArrayList<CovidCountry>) results.values;
                notifyDataSetChanged();
            }
        };
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView tvTotalCases,tvCountryName;
        ImageView imgCountryFlag;
        RelativeLayout container;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTotalCases=itemView.findViewById(R.id.tvTotalCases);
            tvCountryName=itemView.findViewById(R.id.tvCountryName);
            imgCountryFlag=itemView.findViewById(R.id.imgCountryFlag);

            container=itemView.findViewById(R.id.item_list_country_container);

            if(isDark)
                setDarkTheme();
        }

        private void setDarkTheme(){
            container.setBackgroundResource(R.drawable.line_divider_dark);
        }
    }



}

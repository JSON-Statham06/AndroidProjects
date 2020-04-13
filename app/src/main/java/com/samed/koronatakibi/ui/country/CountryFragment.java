package com.samed.koronatakibi.ui.country;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.samed.koronatakibi.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class CountryFragment extends Fragment {

    CovidCountryAdapter covidCountryAdapter;
    RecyclerView rvCovidCountry;
    ProgressBar progressBar;

    FloatingActionButton floatingActionButton;
    boolean isDark=false;
    RelativeLayout rootLayout;
    BottomNavigationView navView;

    private static final String TAG=CountryFragment.class.getSimpleName();
    List<CovidCountry> covidCountries;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
;
        View root = inflater.inflate(R.layout.fragment_country, container, false);

        covidCountries=new ArrayList<>();

        setHasOptionsMenu(true);

        rvCovidCountry=root.findViewById(R.id.rvCovicCountry);
        progressBar=root.findViewById(R.id.progress_circular_country);

        floatingActionButton=root.findViewById(R.id.colormode_switcher);
        rootLayout=root.findViewById(R.id.rootLayout);
        navView=root.findViewById(R.id.navigation_view);

        rvCovidCountry.setLayoutManager(new LinearLayoutManager(getActivity()));

        DividerItemDecoration dividerItemDecoration=new DividerItemDecoration(rvCovidCountry.getContext(),DividerItemDecoration.VERTICAL);
        dividerItemDecoration.setDrawable(ContextCompat.getDrawable(getContext(),R.drawable.line_divider));
        rvCovidCountry.addItemDecoration(dividerItemDecoration);


        getDataFromServer();
        return root;
    }



    private void showRecyclerView(){

        covidCountryAdapter=new CovidCountryAdapter(covidCountries,getActivity());
        rvCovidCountry.setAdapter(covidCountryAdapter);

        floatingActionButton.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {
                isDark=!isDark;
                if(isDark){
                    rootLayout.setBackgroundColor(getResources().getColor(R.color.card_bg_color));
                    floatingActionButton.setBackgroundTintList(ContextCompat.getColorStateList(getActivity(),R.color.card_bg_color));
                }else{
                    rootLayout.setBackgroundColor(getResources().getColor(R.color.card_bg_color));
                    floatingActionButton.setBackgroundTintList(ContextCompat.getColorStateList(getActivity(),R.color.card_bg_dark_color));
                }
                covidCountryAdapter=new CovidCountryAdapter(covidCountries,getActivity(),isDark);
                rvCovidCountry.setAdapter(covidCountryAdapter);
            }
        });

        ItemClickSupport.addTo(rvCovidCountry).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                showSelectedCovidCountry(CovidCountryAdapter.covidCountriesFiltered.get(position));
            }
        });
    }

    private void showSelectedCovidCountry(CovidCountry covidCountry){
        Intent covidCountryDetail=new Intent(getActivity(),CovidCountryDetail.class);
        covidCountryDetail.putExtra("EXTRA COVID",covidCountry);
        startActivity(covidCountryDetail);
    }

    private void getDataFromServer() {
        String url="https://corona.lmao.ninja/countries";

        StringRequest stringRequest=new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressBar.setVisibility(View.GONE);
                if (response != null) {
                    Log.e(TAG, "onResponse:" + response);
                    try {
                        JSONArray jsonArray = new JSONArray(response);
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject data = jsonArray.getJSONObject(i);
                            JSONObject countryInfo=data.getJSONObject("countryInfo");

                            covidCountries.add(new CovidCountry(data.getString("country"), data.getString("cases"),
                                data.getString("todayCases"),data.getString("deaths")
                                    ,data.getString("todayDeaths"),data.getString("recovered")
                                    ,data.getString("active"),data.getString("critical")
                                    ,countryInfo.getString("flag")

                            ));

                        }

                        showRecyclerView();


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressBar.setVisibility(View.GONE);
                Log.e(TAG,"onResponse: "+error);
            }
        });

        Volley.newRequestQueue(getActivity()).add(stringRequest);

    }


    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.search_menu,menu);
        MenuItem searchItem=menu.findItem(R.id.action_search);
        SearchView searchView=new SearchView(getActivity());
        searchView.setQueryHint("Search...");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if(covidCountryAdapter != null){
                    covidCountryAdapter.getFilter().filter(newText);
                }
                return  true;
            }
        });

        searchItem.setActionView(searchView);

        super.onCreateOptionsMenu(menu, inflater);
    }
}

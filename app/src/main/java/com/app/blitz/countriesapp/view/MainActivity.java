package com.app.blitz.countriesapp.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.app.blitz.countriesapp.R;
import com.app.blitz.countriesapp.viewmodel.ListViewModel;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.countries_recyclerView)
    RecyclerView recyclerView;

    @BindView(R.id.empty_list_textView)
    TextView emptylist;

    @BindView(R.id.loading_view)
    ProgressBar progressBar;

    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout refreshLayout;

    private ListViewModel viewModel;
    private AdapterCountryList adapterCountryList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        viewModel = ViewModelProviders.of(this).get(ListViewModel.class);
        viewModel.refresh();

        adapterCountryList = new AdapterCountryList(new ArrayList<>(), this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapterCountryList);

        refreshLayout.setOnRefreshListener(() -> {
            viewModel.refresh();
            refreshLayout.setRefreshing(false);
        });

        observerViewModel();
    }

    private void observerViewModel() {
        viewModel.getCountries().observe(this, countryModels -> {
            if(countryModels != null) {
                recyclerView.setVisibility(View.VISIBLE);
                adapterCountryList.updateCountries(countryModels);
            }
        });
        viewModel.getCountryLoadError().observe(this, isError -> {
            if(isError!=null){
                emptylist.setVisibility(isError ? View.VISIBLE : View.GONE);
            }
        });
        viewModel.getLoading().observe(this, isLoading -> {
            if(isLoading!=null){
                progressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE);
                if(isLoading){
                    emptylist.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.GONE);
                }
            }
        });
    }


}

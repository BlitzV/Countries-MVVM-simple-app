package com.app.blitz.countriesapp.dependencyInjection;

import com.app.blitz.countriesapp.model.CountriesService;
import com.app.blitz.countriesapp.viewmodel.ListViewModel;

import dagger.Component;

@Component(modules = {ApiModule.class})
public interface ApiComponent {

    void inject(CountriesService service);

    void inject(ListViewModel inject);
}

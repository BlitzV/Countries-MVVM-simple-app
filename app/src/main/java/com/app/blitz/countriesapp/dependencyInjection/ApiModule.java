package com.app.blitz.countriesapp.dependencyInjection;

import com.app.blitz.countriesapp.model.CountriesApi;
import com.app.blitz.countriesapp.model.CountriesService;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.app.blitz.countriesapp.BuildConfig.BASE_URL;

@Module
public class ApiModule {

    @Provides
    public CountriesApi provideCountriesApp(){
        return new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()
                .create(CountriesApi.class);
    }

    @Provides
    public CountriesService providesCountriesService(){
        return CountriesService.getInstance();
    }
}

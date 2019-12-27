package com.app.blitz.countriesapp.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.app.blitz.countriesapp.dependencyInjection.DaggerApiComponent;
import com.app.blitz.countriesapp.model.CountriesService;
import com.app.blitz.countriesapp.model.CountryModel;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

public class ListViewModel extends ViewModel {

    private MutableLiveData<List<CountryModel>> countries = new MutableLiveData<>();
    private MutableLiveData<Boolean> countryLoadError = new MutableLiveData<>();
    private MutableLiveData<Boolean> loading = new MutableLiveData<>();

    @Inject
    public CountriesService countriesService;

    private CompositeDisposable disposable = new CompositeDisposable();

    public ListViewModel(){
        super();
        DaggerApiComponent.create().inject(this);
    }

    public void refresh(){
        fetchCountries();
    }

    public MutableLiveData<List<CountryModel>> getCountries() {
        if(countries==null){
            countries = new MutableLiveData<>();
        }
        return countries;
    }

    public MutableLiveData<Boolean> getCountryLoadError() {
        if(countryLoadError==null){
            countryLoadError = new MutableLiveData<>();
        }
        return countryLoadError;
    }

    public MutableLiveData<Boolean> getLoading() {
        if(loading==null){
            loading= new MutableLiveData<>();
        }
        return loading;
    }

    private void fetchCountries(){

        loading.setValue(true);

        disposable.add(countriesService.getCountries()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<List<CountryModel>>() {
                    @Override
                    public void onSuccess(List<CountryModel> countryModels) {
                        countries.setValue(countryModels);
                        countryLoadError.setValue(false);
                        loading.setValue(false);
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.getStackTrace();
                        countryLoadError.setValue(true);
                        loading.setValue(false);
                    }
                }));
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        disposable.clear();
    }
}

package com.app.blitz.countriesapp;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;

import com.app.blitz.countriesapp.model.CountriesService;
import com.app.blitz.countriesapp.model.CountryModel;
import com.app.blitz.countriesapp.viewmodel.ListViewModel;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import io.reactivex.Scheduler;
import io.reactivex.Single;
import io.reactivex.android.plugins.RxAndroidPlugins;
import io.reactivex.internal.schedulers.ExecutorScheduler;
import io.reactivex.plugins.RxJavaPlugins;

public class ListViewModelTest {

    @Rule
    public InstantTaskExecutorRule rule = new InstantTaskExecutorRule();

    @Mock
    CountriesService countriesService;

    @InjectMocks
    ListViewModel listViewModel = new ListViewModel();

    private Single<List<CountryModel>> testSingle;
   // private Observable<List<CountryModel>> testObservable;

    @Before
    public void setup(){
        MockitoAnnotations.initMocks(this);
    }

    @Before
    public void setupRxSchedulers() {
        Scheduler inmediate = new Scheduler() {
            @Override
            public Worker createWorker() {
                return new ExecutorScheduler.ExecutorWorker(runnable -> {
                    runnable.run();
                },true);
            }
        };

        RxJavaPlugins.setInitNewThreadSchedulerHandler(scheduler -> inmediate);
        RxAndroidPlugins.setInitMainThreadSchedulerHandler(scheduler -> inmediate);
    }

    @Test
    public void getCountriesSuccess(){
        CountryModel countryModel = new CountryModel("name test", "Capital test", "flag test");
        ArrayList<CountryModel> countryModelArrayList = new ArrayList<>();
        countryModelArrayList.add(countryModel);

        testSingle = Single.just(countryModelArrayList);
       // testObservable = Observable.just(countryModelArrayList);

        Mockito.when(countriesService.getCountries()).thenReturn(testSingle);

        listViewModel.refresh();

        Assert.assertEquals(1, Objects.requireNonNull(listViewModel.getCountries().getValue()).size());
        Assert.assertEquals(false, listViewModel.getCountryLoadError().getValue());
        Assert.assertEquals(false, listViewModel.getLoading().getValue());
    }

    @Test
    public void getCountriesFailure(){

        testSingle = Single.error(new Throwable());

        Mockito.when(countriesService.getCountries()).thenReturn(testSingle);

        listViewModel.refresh();

        Assert.assertEquals(true, listViewModel.getCountryLoadError().getValue());
        Assert.assertEquals(false, listViewModel.getLoading().getValue());
    }


}

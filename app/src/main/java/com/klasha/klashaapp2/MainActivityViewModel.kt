package com.klasha.klashaapp2

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainActivityViewModel : ViewModel() {
    val countries: MutableLiveData<ArrayList<String>> = MutableLiveData<ArrayList<String>>()
    val currencies: MutableLiveData<ArrayList<String>> = MutableLiveData<ArrayList<String>>()
    val amount: MutableLiveData<Double> = MutableLiveData(500.0)
    val country: MutableLiveData<Int> = MutableLiveData(0)
    val sourceCurrency: MutableLiveData<Int> = MutableLiveData(0)
    val name: MutableLiveData<String> = MutableLiveData("Yemi Desola")
    val email: MutableLiveData<String> = MutableLiveData("ola@klasha.com")
    val phone: MutableLiveData<String> = MutableLiveData("080344006699")
}
package com.klasha.klashaapp2.ui.mobilemoney

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MobileMoneyViewModel : ViewModel() {

    val voucher: MutableLiveData<String> = MutableLiveData("")
    val network: MutableLiveData<String> = MutableLiveData("")
}
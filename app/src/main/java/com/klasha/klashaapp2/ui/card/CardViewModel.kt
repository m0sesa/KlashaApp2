package com.klasha.klashaapp2.ui.card

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class CardViewModel : ViewModel() {
    val cardNumber: MutableLiveData<String> = MutableLiveData("5531886652142950")
    val cardMonth: MutableLiveData<String> = MutableLiveData("10")
    val cardYear: MutableLiveData<String> = MutableLiveData("31")
    val cardCvv: MutableLiveData<String> = MutableLiveData("470")
}
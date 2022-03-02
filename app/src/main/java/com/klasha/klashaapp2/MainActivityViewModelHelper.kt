package com.klasha.klashaapp2

import android.R
import android.app.Activity
import android.content.Context
import android.text.Editable
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.LifecycleOwner
import com.klasha.android.KlashaSDK
import com.klasha.android.model.Country
import com.klasha.android.model.Currency
import com.klasha.klashaapp2.databinding.*

object MainActivityViewModelHelper {

    fun setupPaymentViews(mainActivityViewModel: MainActivityViewModel, context: Context, viewLifecycleOwner: LifecycleOwner, binding: Any){
        var layoutBinding: LayoutPaymentBinding?
        when (binding){
            is FragmentBankBinding -> {
                layoutBinding = binding.paymentLayout
            }
            is FragmentCardBinding -> {
                layoutBinding = binding.paymentLayout
            }
            is FragmentMobileMoneyBinding -> {
                layoutBinding = binding.paymentLayout
            }
            is FragmentMpesaBinding -> {
                layoutBinding = binding.paymentLayout
            }
            else ->{
                layoutBinding = (binding as FragmentKlashaWalletBinding).paymentLayout
            }
        }

        mainActivityViewModel.currencies.observe(viewLifecycleOwner){
            if (!it.isNullOrEmpty()){
                layoutBinding.sourceCurrency.adapter = ArrayAdapter(context, R.layout.simple_list_item_1, it)
            }
        }

        mainActivityViewModel.countries.observe(viewLifecycleOwner){
            if (!it.isNullOrEmpty()){
                layoutBinding.country.adapter = ArrayAdapter(context, R.layout.simple_list_item_1, it)
            }
        }

        layoutBinding.sourceCurrency.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                mainActivityViewModel.sourceCurrency.postValue(p2)
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {}
        }

        layoutBinding.country.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                mainActivityViewModel.country.postValue(p2)
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {}
        }

        layoutBinding.amount.doOnTextChanged { text, _, _, _ ->
            if (text.toString() != mainActivityViewModel.amount.value.toString()){
                mainActivityViewModel.amount.postValue(text.toString().toDouble())
            }
            mainActivityViewModel.amount.removeObservers(viewLifecycleOwner)
        }

        layoutBinding.email.doOnTextChanged { text, _, _, _ ->
            if (text.toString() != mainActivityViewModel.amount.value.toString()){
                mainActivityViewModel.email.postValue(text.toString())
            }
            mainActivityViewModel.email.removeObservers(viewLifecycleOwner)
        }

        layoutBinding.name.doOnTextChanged { text, _, _, _ ->
            if (text.toString() != mainActivityViewModel.amount.value.toString()){
                mainActivityViewModel.name.postValue(text.toString())
            }
            mainActivityViewModel.name.removeObservers(viewLifecycleOwner)
        }

        layoutBinding.phone.doOnTextChanged { text, _, _, _ ->
            if (text.toString() != mainActivityViewModel.amount.value.toString()){
                mainActivityViewModel.phone.postValue(text.toString())
            }
            mainActivityViewModel.phone.removeObservers(viewLifecycleOwner)
        }

        mainActivityViewModel.sourceCurrency.observe(viewLifecycleOwner){
            layoutBinding.sourceCurrency.setSelection(it)
        }

        mainActivityViewModel.country.observe(viewLifecycleOwner){
            layoutBinding.country.setSelection(it)
        }

        mainActivityViewModel.amount.observe(viewLifecycleOwner){
            layoutBinding.amount.text = Editable.Factory().newEditable(it.toString())
        }

        mainActivityViewModel.email.observe(viewLifecycleOwner){
            layoutBinding.email.text = Editable.Factory().newEditable(it.toString())
        }

        mainActivityViewModel.name.observe(viewLifecycleOwner){
            layoutBinding.name.text = Editable.Factory().newEditable(it.toString())
        }

        mainActivityViewModel.phone.observe(viewLifecycleOwner){
            layoutBinding.phone.text = Editable.Factory().newEditable(it.toString())
        }

    }

    fun initializeKlashaSdk(activity: Activity, mainActivityViewModel: MainActivityViewModel){
        val sourceCurrency = mainActivityViewModel.currencies.value?.get(mainActivityViewModel.sourceCurrency.value!!)
        val country = mainActivityViewModel.countries.value?.get(mainActivityViewModel.country.value!!)
        val ctr = country!!.replace(" ", "_").uppercase()
        KlashaSDK.initialize(activity, BuildConfig.KLASHA_AUTH_TOKEN, Country.valueOf(ctr), Currency.valueOf(sourceCurrency!!.uppercase()))
    }
}
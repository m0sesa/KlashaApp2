package com.klasha.klashaapp2.ui.mobilemoney

import android.R
import android.app.Activity
import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import com.klasha.android.KlashaSDK
import com.klasha.android.model.Charge
import com.klasha.android.model.MobileMoney
import com.klasha.android.model.Network
import com.klasha.klashaapp2.MainActivityViewModel
import com.klasha.klashaapp2.MainActivityViewModelHelper
import com.klasha.klashaapp2.databinding.FragmentMobileMoneyBinding
import com.klasha.klashaapp2.databinding.FragmentMpesaBinding

class MobileMoneyFragment : Fragment() {

    private lateinit var mobileMoneyViewModel: MobileMoneyViewModel
    private lateinit var mainActivityViewModel: MainActivityViewModel
    private var _binding: FragmentMobileMoneyBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mobileMoneyViewModel =
            ViewModelProvider(this).get(MobileMoneyViewModel::class.java)
        mainActivityViewModel =
            ViewModelProvider(requireActivity()).get(MainActivityViewModel::class.java)

        _binding = FragmentMobileMoneyBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val networks = arrayListOf("MTN")

        binding.voucher.doOnTextChanged { text, _, _, _ ->
            if (text.toString() != mobileMoneyViewModel.voucher.value.toString()){
                mobileMoneyViewModel.voucher.postValue(text.toString())
            }
            mobileMoneyViewModel.voucher.removeObservers(viewLifecycleOwner)
        }

        binding.network.adapter = ArrayAdapter(requireContext(), R.layout.simple_list_item_1, networks)
        binding.network.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                mobileMoneyViewModel.network.postValue(networks[p2])
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {}
        }

        mobileMoneyViewModel.voucher.observe(viewLifecycleOwner){
            binding.voucher.text = Editable.Factory().newEditable(it.toString())
        }

        MainActivityViewModelHelper.setupPaymentViews(mainActivityViewModel, requireContext(), viewLifecycleOwner, binding)

        binding.pay.setOnClickListener {
            val amount = mainActivityViewModel.amount.value!!

            val email = mainActivityViewModel.email.value!!
            val name = mainActivityViewModel.name.value!!
            val phone = mainActivityViewModel.phone.value!!

            val network = mobileMoneyViewModel.network.value!!
            val voucher = mobileMoneyViewModel.voucher.value!!

            val mobileMoney = MobileMoney(voucher, Network.valueOf(network))
            val charge = Charge(amount, email, name, null, mobileMoney, phone)

            MainActivityViewModelHelper.initializeKlashaSdk(requireActivity(), mainActivityViewModel)

            binding.progressBar.visibility = View.VISIBLE

            KlashaSDK.mobileMoney(charge, object : KlashaSDK.TransactionCallback{
                override fun error(ctx: Activity, message: String) {
                    ctx.runOnUiThread {
                        binding.progressBar.visibility = View.GONE
                        Snackbar.make(binding.root,"Transaction Failed $message", Snackbar.LENGTH_LONG).show()
                    }
                }

                override fun success(ctx: Activity, transactionReference: String) {
                    ctx.runOnUiThread {
                        binding.progressBar.visibility = View.GONE
                        Snackbar.make(binding.root,"Transaction Successful $transactionReference", Snackbar.LENGTH_LONG).show()
                    }
                }

                override fun transactionInitiated(transactionReference: String) {
                    Snackbar.make(binding.root,"Transaction Initiated $transactionReference", Snackbar.LENGTH_LONG).show()
                }
            })
        }

        return root
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
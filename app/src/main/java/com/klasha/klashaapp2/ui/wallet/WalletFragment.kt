package com.klasha.klashaapp2.ui.wallet

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
import android.widget.TextView
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import com.klasha.android.KlashaSDK
import com.klasha.android.model.BankTransferResp
import com.klasha.android.model.Charge
import com.klasha.klashaapp2.MainActivityViewModel
import com.klasha.klashaapp2.MainActivityViewModelHelper
import com.klasha.klashaapp2.databinding.FragmentKlashaWalletBinding

class WalletFragment : Fragment() {

    private lateinit var walletViewModel: WalletViewModel
    private lateinit var mainActivityViewModel: MainActivityViewModel
    private var _binding: FragmentKlashaWalletBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        walletViewModel =
            ViewModelProvider(this).get(WalletViewModel::class.java)
        mainActivityViewModel =
            ViewModelProvider(requireActivity()).get(MainActivityViewModel::class.java)

        _binding = FragmentKlashaWalletBinding.inflate(inflater, container, false)
        val root: View = binding.root

        MainActivityViewModelHelper.setupPaymentViews(mainActivityViewModel, requireContext(), viewLifecycleOwner, binding)

        binding.pay.setOnClickListener {
            val amount = mainActivityViewModel.amount.value!!

            val email = mainActivityViewModel.email.value!!
            val name = mainActivityViewModel.name.value!!
            val phone = mainActivityViewModel.phone.value!!

            val charge = Charge(amount, email, name, null, null, phone)

            MainActivityViewModelHelper.initializeKlashaSdk(requireActivity(), mainActivityViewModel)

            binding.progressBar.visibility = View.VISIBLE

            KlashaSDK.wallet(charge, object : KlashaSDK.TransactionCallback{
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
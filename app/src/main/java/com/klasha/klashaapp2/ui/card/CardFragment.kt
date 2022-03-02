package com.klasha.klashaapp2.ui.card

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
import android.widget.EditText
import android.widget.TextView
import androidx.core.widget.addTextChangedListener
import androidx.core.widget.doAfterTextChanged
import androidx.core.widget.doBeforeTextChanged
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import com.klasha.android.KlashaSDK
import com.klasha.android.model.Card
import com.klasha.android.model.Charge
import com.klasha.android.model.Country
import com.klasha.android.model.Currency
import com.klasha.klashaapp2.BuildConfig
import com.klasha.klashaapp2.MainActivityViewModel
import com.klasha.klashaapp2.MainActivityViewModelHelper
import com.klasha.klashaapp2.databinding.FragmentCardBinding

class CardFragment : Fragment() {

    private lateinit var cardViewModel: CardViewModel
    private lateinit var mainActivityViewModel: MainActivityViewModel

    private var _binding: FragmentCardBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        cardViewModel =
            ViewModelProvider(this).get(CardViewModel::class.java)
        mainActivityViewModel =
            ViewModelProvider(requireActivity()).get(MainActivityViewModel::class.java)

        _binding = FragmentCardBinding.inflate(inflater, container, false)
        val root: View = binding.root

        MainActivityViewModelHelper.setupPaymentViews(mainActivityViewModel, requireContext(), viewLifecycleOwner, binding)

        cardViewModel.cardNumber.observe(viewLifecycleOwner){
            binding.cardNumber.text = Editable.Factory().newEditable(it.toString())
        }
        cardViewModel.cardCvv.observe(viewLifecycleOwner){
            binding.cardCvv.text = Editable.Factory().newEditable(it.toString())
        }
        cardViewModel.cardMonth.observe(viewLifecycleOwner){
            binding.tvCardMonth.text = Editable.Factory().newEditable(it.toString())
        }
        cardViewModel.cardYear.observe(viewLifecycleOwner){
            binding.tvCardYear.text = Editable.Factory().newEditable(it.toString())
        }

        binding.pay.setOnClickListener {
            val cardNumber = cardViewModel.cardNumber.value!!
            val cardCvv = cardViewModel.cardCvv.value!!
            val cardMonth = cardViewModel.cardMonth.value!!
            val cardYear = cardViewModel.cardYear.value!!

            val amount = mainActivityViewModel.amount.value!!

            val email = mainActivityViewModel.email.value!!
            val name = mainActivityViewModel.name.value!!
            val phone = mainActivityViewModel.phone.value!!

            val card = Card(cardNumber, cardMonth.toInt(), cardYear.toInt(), cardCvv.toInt())
            val charge = Charge(amount.toDouble(), email, name, card, null, phone)

            MainActivityViewModelHelper.initializeKlashaSdk(requireActivity(), mainActivityViewModel)

            binding.progressBar.visibility = View.VISIBLE

            KlashaSDK.chargeCard(charge, object : KlashaSDK.TransactionCallback{
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
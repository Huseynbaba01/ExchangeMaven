package com.codinghuseyn.exchangemaven.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.codinghuseyn.exchangemaven.R
import com.codinghuseyn.exchangemaven.databinding.FragmentExchangeBinding
import com.codinghuseyn.exchangemaven.presentation.viewmodel.ExchangeViewModel

class ExchangeFragment : Fragment() {
    private var _exchangeBinding: FragmentExchangeBinding? = null
    private val viewModel by activityViewModels<ExchangeViewModel>()
    private val binding
        get() = _exchangeBinding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _exchangeBinding = FragmentExchangeBinding.inflate(inflater, container, false)

        viewModel.quotesLiveData.observe(viewLifecycleOwner) {
            updateAutoCompleteData(binding.baseCurrency, it)
            updateAutoCompleteData(binding.secondaryCurrency, it)
        }

        viewModel.getListsQuotes()

        binding.baseCurrency.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) binding.baseCurrency.showDropDown()
        }
        binding.secondaryCurrency.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                binding.secondaryCurrency.showDropDown()
            }
        }
        binding.baseCurrency.doOnTextChanged { _, _, _, _ ->
            binding.baseCurrencyLayout.error = null
        }
        binding.secondaryCurrency.doOnTextChanged { _, _, _, _ ->
            binding.secondaryCurrencyLayout.error = null
        }

        binding.btnConvert.setOnClickListener {

            if(hasError()) return@setOnClickListener

            val from = binding.baseCurrency.text.toString()
            val to = binding.secondaryCurrency.text.toString()
            viewModel.exchange(from, to).observe(viewLifecycleOwner) {
                binding.outputLayout.editText!!.setText(it.toString())
            }
        }

        binding.btnWarning.setOnClickListener {
            findNavController().navigate(R.id.action_exchangeFragment_to_warnFragment)
        }

        return binding.root
    }

    private fun hasError(): Boolean {
        var hasError = false
        if (binding.baseCurrency.text.isNullOrEmpty()) {
            hasError = true
            binding.baseCurrencyLayout.error = getString(R.string.fill_error)
        }

        if(viewModel.quotesLiveData.value?.contains(binding.baseCurrency.text.toString()) != true){
            hasError = true
            binding.baseCurrencyLayout.error = getString(R.string.wrong_symbol)
        }

        if (binding.secondaryCurrency.text.isNullOrEmpty()) {
            hasError = true
            binding.secondaryCurrencyLayout.error = getString(R.string.fill_error)
        }

        if(viewModel.quotesLiveData.value?.contains(binding.secondaryCurrency.text.toString()) != true){
            hasError = true
            binding.secondaryCurrencyLayout.error = getString(R.string.wrong_symbol)
        }

        return hasError
    }

    @Suppress("UNCHECKED_CAST")
    private fun updateAutoCompleteData(
        autoCompleteTextView: AutoCompleteTextView,
        newItems: List<String>
    ) {
        ((autoCompleteTextView.adapter ?: ArrayAdapter<String>(
            requireContext(),
            android.R.layout.simple_dropdown_item_1line
        )) as ArrayAdapter<String>).let {
            it.clear()
            it.addAll(newItems)
            autoCompleteTextView.setAdapter(it)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _exchangeBinding = null
    }
}
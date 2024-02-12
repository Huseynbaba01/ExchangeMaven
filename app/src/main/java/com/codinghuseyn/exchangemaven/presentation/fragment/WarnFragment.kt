package com.codinghuseyn.exchangemaven.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.work.Constraints
import androidx.work.Data
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.codinghuseyn.exchangemaven.R
import com.codinghuseyn.exchangemaven.databinding.FragmentWarnBinding
import com.codinghuseyn.exchangemaven.presentation.viewmodel.ExchangeViewModel
import com.codinghuseyn.exchangemaven.presentation.workmanager.UpdateWorker
import com.codinghuseyn.exchangemaven.utils.Constants
import java.util.concurrent.TimeUnit

class WarnFragment : Fragment() {
    private val viewModel by activityViewModels<ExchangeViewModel>()
    private var _fragmentWarnBinding: FragmentWarnBinding? = null
    private val binding
        get() = _fragmentWarnBinding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _fragmentWarnBinding = FragmentWarnBinding.inflate(inflater, container, false)

        updateAutoCompleteData(binding.baseCurrency, viewModel.quotesLiveData.value!!)
        updateAutoCompleteData(binding.secondaryCurrency, viewModel.quotesLiveData.value!!)

        binding.btnConverting.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }

        binding.btnWarn.setOnClickListener {
            if(hasError()) return@setOnClickListener
            setUpWarning()
        }

        return binding.root
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


    private fun setUpWarning() {
        val data = Data.Builder()
        data.putString("from", binding.baseCurrency.text.toString())
        data.putString("to", binding.secondaryCurrency.text.toString())
        data.putString("min", binding.minimumLayout.editText!!.text.toString())
        data.putString("max", binding.maximumLayout.editText!!.text.toString())
        data.putString("quantity", binding.quantityLayout.editText!!.text.toString())

        val workRequest = PeriodicWorkRequestBuilder<UpdateWorker>(15, TimeUnit.MINUTES)
            .addTag(Constants.PERIODIC_WARNING)
            .setConstraints(Constraints(requiredNetworkType = NetworkType.CONNECTED))
            .setInputData(data.build())
            .build()

        WorkManager.getInstance(requireContext()).enqueueUniquePeriodicWork(Constants.PERIODIC_WARNING,
            ExistingPeriodicWorkPolicy.UPDATE, workRequest)
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
        if (binding.minimumLayout.editText?.text.isNullOrEmpty()) {
            hasError = true
            binding.minimumLayout.error = getString(R.string.fill_error)
        }
        if (binding.maximumLayout.editText?.text.isNullOrEmpty()) {
            hasError = true
            binding.maximumLayout.error = getString(R.string.fill_error)
        }
        if (binding.quantityLayout.editText?.text.isNullOrEmpty()) {
            hasError = true
            binding.quantityLayout.error = getString(R.string.fill_error)
        }
        return hasError
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _fragmentWarnBinding = null
    }
}
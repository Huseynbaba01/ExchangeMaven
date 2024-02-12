package com.codinghuseyn.exchangemaven.presentation.fragment

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.codinghuseyn.exchangemaven.databinding.FragmentExchangeBinding
import com.codinghuseyn.exchangemaven.presentation.viewmodel.ExchangeViewModel

class ExchangeFragment : Fragment() {
    private var _exchangeBinding: FragmentExchangeBinding? = null
    private val viewModel by activityViewModels<ExchangeViewModel>()
    private val exchangeBinding
        get() = _exchangeBinding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _exchangeBinding = FragmentExchangeBinding.inflate(inflater, container, false)


        viewModel.getListsQuotes().observe(viewLifecycleOwner) {
            Toast.makeText(requireContext(), it.size.toString(), Toast.LENGTH_SHORT).show()
        }

        return exchangeBinding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _exchangeBinding = null
    }
}
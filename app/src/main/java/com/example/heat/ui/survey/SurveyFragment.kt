package com.example.heat.ui.survey

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.example.heat.databinding.FragmentSurvayBinding
import com.example.heat.ui.base.ScopedFragment
import com.example.heat.ui.itemRecyclerView.SurvayPagerAdapter
import kotlinx.coroutines.launch
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.instance


class SurveyFragment : ScopedFragment(), KodeinAware {

    override val kodein by closestKodein()
    private val viewModelFactory:SurveyViewModelFactory by instance()

    private lateinit var viewModel:SurveyViewModel

    private var _binding: FragmentSurvayBinding? = null
    private val binding
        get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View{
        _binding = FragmentSurvayBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this, viewModelFactory).get(SurveyViewModel::class.java)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bindUI()
    }

    private fun bindUI() = launch{
        binding.apply {
            val myAdapter = SurvayPagerAdapter(requireActivity().supportFragmentManager)

            viewPager.adapter = myAdapter
        }
    }
}
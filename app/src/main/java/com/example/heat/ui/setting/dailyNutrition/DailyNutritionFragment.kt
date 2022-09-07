package com.example.heat.ui.setting.dailyNutrition

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.heat.R
import com.example.heat.data.datamodel.user.UserPreferences
import com.example.heat.databinding.FragmentDailyNutritionBinding
import com.example.heat.databinding.FragmentDiseaseBinding
import com.example.heat.ui.base.ScopedFragment
import com.example.heat.ui.setting.activeLevel.ActiveLevelFragmentArgs
import com.example.heat.ui.setting.disease.DiseaseFragmentDirections
import com.example.heat.ui.setting.disease.DiseaseViewModel
import com.example.heat.ui.setting.disease.DiseaseViewModelFactory
import com.example.heat.util.UiUtils
import com.example.heat.util.UiUtils.Companion.getColor
import com.example.heat.util.enumerian.ActiveLevel
import com.example.heat.util.enumerian.ComeFrom
import com.example.heat.util.enumerian.Disease
import com.example.heat.util.enumerian.Gender
import com.example.heat.util.exhaustive
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.PercentFormatter
import kotlinx.coroutines.launch
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.factory
import kotlin.math.ceil
import kotlin.math.roundToInt

class DailyNutritionFragment : ScopedFragment(), KodeinAware {
    override val kodein by closestKodein()
    private val viewModelFactoryInstanceFactory: ((UserPreferences?) -> DailyNutritionViewModelFactory) by factory()

    private lateinit var viewModel: DailyNutritionViewModel

    private var _binding: FragmentDailyNutritionBinding? = null
    private val binding
        get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDailyNutritionBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val safeArgs = arguments?.let { ActiveLevelFragmentArgs.fromBundle(it) }
        val userPref = safeArgs?.userPreferences
        val comeFrom = safeArgs?.comeFrom

        viewModel = ViewModelProvider(
            this,
            viewModelFactoryInstanceFactory(userPref)
        )[DailyNutritionViewModel::class.java]
        var isFromProfile = false

        if (comeFrom == ComeFrom.PROFILE) isFromProfile = true

        bindUI(isFromProfile, userPref)
    }


    private fun bindUI(isFromProfile: Boolean, userPreference: UserPreferences?) = launch {
        if (userPreference == null) {
            return@launch
        }

        calculateUserNeeds(userPreference)

        if (isFromProfile) {
            binding.getStarted.visibility = View.INVISIBLE
            binding.backArrow.visibility = View.VISIBLE
        } else {
            binding.getStarted.visibility = View.VISIBLE
            binding.backArrow.visibility = View.INVISIBLE
        }

        binding.apply {
            if (isFromProfile) {
                backArrow.setOnClickListener {
                    saveData(binding, userPreference, it)
                }
            } else {
                getStarted.setOnClickListener {
                    saveData(binding, userPreference, it)
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.transactionEvent.collect { event ->
                when (event) {
                    is DailyNutritionViewModel.DailyNutritionTransactionsEvents.NavigateToHomeScreen -> {
                        Log.e("USER_PREF_PERSONAL_DATA", userPreference.toString())
                        when (isFromProfile) {
                            true -> {
                                requireActivity().onBackPressed()
                            }
                            false -> {
                                /*val action =
                                    DiseaseFragmentDirections.actionDiseaseFragmentToHomeFragment()
                                findNavController().navigate(action)*/
                            }
                        }
                    }
                    else -> {
                        UiUtils.showSimpleSnackBar(
                            binding.root,
                            getString(R.string.complete_all_parts)
                        )
                    }
                }
            }
        }.exhaustive
    }

    @SuppressLint("SetTextI18n")
    private fun calculateUserNeeds(userPreference: UserPreferences) {
        //BMI
        val bmi =
            userPreference.weight / ((userPreference.height / 100) * (userPreference.height / 100))
        val bmr = when (userPreference.gender) {
            Gender.MALE -> {
                ceil(66.5 + (13.8 * userPreference.weight) + (5.0 * userPreference.height) - (6.8 * userPreference.age))
            }
            Gender.FEMALE -> {
                ceil(655.1 + (9.6 * userPreference.weight) + (1.9 * userPreference.height) - (4.7 * userPreference.age))
            }
        }
        val calorie = when (userPreference.activeLevel) {
            ActiveLevel.SEDENTARY -> ceil(bmr * 1.2)
            ActiveLevel.LIGHTLY -> ceil(bmr * 1.375)
            ActiveLevel.MODERATELY -> ceil(bmr * 1.55)
            ActiveLevel.VERY -> ceil(bmr * 1.725)
            ActiveLevel.EXTRA -> ceil(bmr * 1.9)
            else -> ceil(bmr * 1.55)
        }

        val minProtein = userPreference.weight * 0.8
        val maxProtein = userPreference.weight * 1.0

        val fat = (calorie * 0.3) / 9

        val carbo = (calorie * 0.6) / 4

        binding.apply {
            calories.text = "${calorie.roundToInt().toString()} kcal"
            carbos.text = "${carbo.roundToInt().toString()} gr"
            fats.text = "${fat.roundToInt().toString()} gr"
            proteins.text =
                "${minProtein.roundToInt().toString()} - ${maxProtein.roundToInt().toString()} gr"
        }
        initPieChart(calorie, fat, minProtein, maxProtein, carbo)
    }

    private fun initPieChart(
        calorie: Double,
        fat: Double,
        minProtein: Double,
        maxProtein: Double,
        carbo: Double
    ) {
        binding.apply {
            pieChartDailyNutrition.apply {
                setUsePercentValues(true)
                description.text = ""
                //hollow pie chart
                isDrawHoleEnabled = true
                setTouchEnabled(true)
                setDrawEntryLabels(false)
                //adding padding
                setExtraOffsets(20f, 0f, 20f, 20f)
                setUsePercentValues(true)
                isRotationEnabled = true
                setDrawEntryLabels(false)
                legend.orientation = Legend.LegendOrientation.VERTICAL
                legend.isWordWrapEnabled = true

                setUsePercentValues(true)
                val dataEntries = ArrayList<PieEntry>()
                dataEntries.add(PieEntry( ((minProtein + maxProtein) / 2).toFloat(), "Protein"))
                dataEntries.add(PieEntry(carbo.toFloat(), "Carbo"))
                dataEntries.add(PieEntry(fat.toFloat(), "Fat"))

                val colors: ArrayList<Int> = ArrayList()
                colors.add(getColor(requireContext(), R.color.blue))
                colors.add(getColor(requireContext(), R.color.red))
                colors.add(getColor(requireContext(), R.color.yellow))

                val dataSet = PieDataSet(dataEntries, "")
                val _data = PieData(dataSet)

                // In Percentage
                _data.setValueFormatter(PercentFormatter())
                dataSet.sliceSpace = 3f
                dataSet.colors = colors
                data = _data
                data.setValueTextSize(15f)
                setExtraOffsets(5f, 10f, 5f, 5f)
                animateY(1400, Easing.EaseInOutQuad)

                //create hole in center
                holeRadius = 58f
                transparentCircleRadius = 61f
                isDrawHoleEnabled = true
                setHoleColor(Color.WHITE)


                //add text in center
                setDrawCenterText(true);
                centerText = "Daily Nutrition"

                invalidate()
            }
        }
    }

    private fun saveData(
        binding: FragmentDailyNutritionBinding,
        userPreference: UserPreferences,
        itemView: View
    ) = launch {
        binding.apply {
            if (itemView == getStarted || itemView == backArrow)
                viewModel.onGetStartedClicked(userPreference)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}
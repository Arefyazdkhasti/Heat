package com.example.heat.ui.itemRecyclerView

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.example.heat.ui.setting.dietType.DietTypeFragment
import com.example.heat.ui.setting.abstractGoal.AbstractGoalFragment
import com.example.heat.ui.setting.activeLevel.ActiveLevelFragment
import com.example.heat.ui.setting.disease.DiseaseFragment
import com.example.heat.ui.setting.ingredientAllergy.IngredientAllergyFragment
import com.example.heat.ui.setting.personalData.PersonalDataFragment


class SurvayPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {
    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> PersonalDataFragment()
            1 -> ActiveLevelFragment()
            2 -> AbstractGoalFragment()
            3 -> DietTypeFragment()
            4 -> IngredientAllergyFragment()
            5 -> DiseaseFragment()
            else -> PersonalDataFragment()
        }
    }

    override fun getCount(): Int {
        return 6
    }

    /*override fun getPageTitle(position: Int): CharSequence? {
        return if (position == 0) {
            "Fragment1"
        } else {
            "Fragment2"
        }
    }*/
}
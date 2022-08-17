package com.example.heat.ui.itemRecyclerView

import android.util.Log
import com.bumptech.glide.Glide
import com.example.heat.R
import com.example.heat.data.datamodel.recipeDetail.Step
import com.example.heat.data.datamodel.recipeList.RecipeListItem
import com.example.heat.databinding.InstructionItemBinding
import com.example.heat.databinding.RecipeItemBinding
import com.example.heat.util.UiUtils
import com.example.heat.util.UiUtils.Companion.setImageWithGlideWithView
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item

class InstructionItemRecyclerView(val instruction: Step) : Item<GroupieViewHolder>() {


    override fun getLayout(): Int = R.layout.instruction_item
    override fun bind(viewHolder: GroupieViewHolder, position: Int) {

        val binding = InstructionItemBinding.bind(viewHolder.itemView)

        Log.e("PASHM", instruction.toString())

        binding.apply {
            instructionNumber.text = instruction.number.toInt().toString()
            instructionStep.text = instruction.step
        }

    }
}
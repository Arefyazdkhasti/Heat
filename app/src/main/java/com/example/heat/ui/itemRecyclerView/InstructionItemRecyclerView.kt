package com.example.heat.ui.itemRecyclerView

import android.util.Log
import com.bumptech.glide.Glide
import com.example.heat.R
import com.example.heat.data.datamodel.food.foodDetail.InstructionStep
import com.example.heat.databinding.InstructionItemBinding
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item

class InstructionItemRecyclerView(val instruction: InstructionStep) : Item<GroupieViewHolder>() {


    override fun getLayout(): Int = R.layout.instruction_item
    override fun bind(viewHolder: GroupieViewHolder, position: Int) {

        val binding = InstructionItemBinding.bind(viewHolder.itemView)


        binding.apply {
            instructionNumber.text = instruction.number.toInt().toString()
            instructionStep.text = instruction.step
        }

    }
}
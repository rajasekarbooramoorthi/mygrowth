package com.raj.mygrowth

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.raj.mygrowth.ColorUtilities.colorsMulti
import com.raj.mygrowth.ColorUtilities.colorsMultiText
import com.raj.mygrowth.databinding.ItemDietHeaderBinding
import com.raj.mygrowth.databinding.ItemDietMealBinding
import com.raj.mygrowth.databinding.ItemDietRuleBinding
import com.raj.mygrowth.domain.DietItem
import com.raj.mygrowth.domain.DietResponse
import com.raj.mygrowth.uiState.DietListItem
import kotlin.math.abs

class DietAdapter(val context: Context) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val items = mutableListOf<DietListItem>()

    companion object {
        private const val TYPE_HEADER = 0
        private const val TYPE_MEAL = 1
        private const val TYPE_RULE = 2
    }

    fun submitData(response: DietResponse) {
        items.clear()
        items.add(DietListItem.Header("Daily Target"))
        items.add(
            DietListItem.Rule("Calories: ${response.dailyTarget.calories}")
        )
        items.add(
            DietListItem.Rule("Protein: ${response.dailyTarget.protein}")
        )
        items.add(
            DietListItem.Rule("Meal: ${response.dailyTarget.mealFrequency}")
        )
        items.add(DietListItem.Header("Diet Plan"))

        response.dietPlan.forEach {
            items.add(DietListItem.Meal(it))
        }
        items.add(DietListItem.Header("Rules"))
        response.rules.forEach {
            items.add(DietListItem.Rule(it))
        }
        notifyDataSetChanged()
    }

    override fun getItemViewType(position: Int): Int {
        return when (items[position]) {
            is DietListItem.Header -> TYPE_HEADER
            is DietListItem.Meal -> TYPE_MEAL
            is DietListItem.Rule -> TYPE_RULE
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        val inflater = LayoutInflater.from(parent.context)

        return when (viewType) {

            TYPE_HEADER -> {
                val binding = ItemDietHeaderBinding.inflate(inflater, parent, false)
                HeaderVH(binding)
            }

            TYPE_MEAL -> {
                val binding = ItemDietMealBinding.inflate(inflater, parent, false)
                MealVH(binding, context)
            }

            else -> {
                val binding = ItemDietRuleBinding.inflate(inflater, parent, false)
                RuleVH(binding, context)
            }

        }
    }

    override fun getItemCount() = items.size


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (val item = items[position]) {
            is DietListItem.Header -> (holder as HeaderVH).bind(item)
            is DietListItem.Meal -> (holder as MealVH).bind(item.item, position)
            is DietListItem.Rule -> (holder as RuleVH).bind(item.text, position)
        }
    }

    class HeaderVH(private val binding: ItemDietHeaderBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: DietListItem.Header) {
            binding.title = item.title
        }
    }

    class MealVH(private val binding: ItemDietMealBinding, val context: Context) :
        RecyclerView.ViewHolder(binding.root) {


        fun bind(item: DietItem, position: Int) {
            binding.tvTime.text = item.time
            binding.tvLabel.text = item.label
            binding.tvFood.text = item.foods.joinToString(", ")
            binding.tvNotes.text = item.note
            val index = abs(position.hashCode()) % colorsMulti.size
            val colorRes = colorsMulti[index]
            binding.cardView.setCardBackgroundColor(ContextCompat.getColor(context, colorRes))
        }
    }

    class RuleVH(private val binding: ItemDietRuleBinding, val context: Context) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(rule: String, position: Int) {
            binding.rule = rule
            val index = abs((position).hashCode()) % colorsMultiText.size
            val colorRes = colorsMultiText[index]
            binding.tvName.setTextColor(ContextCompat.getColor(context, colorRes))
            binding.tvName.text = "• $rule"
        }
    }
}
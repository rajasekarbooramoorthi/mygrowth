package com.raj.mygrowth.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.google.mlkit.common.model.DownloadConditions
import com.google.mlkit.nl.translate.TranslateLanguage
import com.google.mlkit.nl.translate.Translation
import com.google.mlkit.nl.translate.Translator
import com.google.mlkit.nl.translate.TranslatorOptions
import com.raj.mygrowth.ColorUtilities.colorsMulti
import com.raj.mygrowth.ColorUtilities.colorsMultiText
import com.raj.mygrowth.databinding.AdapterQuitZillaMotivationBinding
import com.raj.mygrowth.domain.ResponseQuitZillaMotivationItem
import kotlin.math.abs

class QuitZillaMotivationAdapter(
    private val list: List<ResponseQuitZillaMotivationItem>,
    private val context: Context,
) : RecyclerView.Adapter<QuitZillaMotivationAdapter.ViewHolder>() {

    private val options = TranslatorOptions.Builder()
        .setSourceLanguage(TranslateLanguage.ENGLISH)
        .setTargetLanguage(TranslateLanguage.TAMIL)
        .build()

    private val translator: Translator = Translation.getClient(options)

    init {
        val conditions = DownloadConditions.Builder()
            .requireWifi()
            .build()

        translator.downloadModelIfNeeded(conditions)
    }

    class ViewHolder(val binding: AdapterQuitZillaMotivationBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = AdapterQuitZillaMotivationBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun getItemCount() = list.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val item = list[position]

        val formattedDescription =
            item.description.replaceFirstChar { it.uppercase() }

        holder.binding.tvId.text = item.id
        holder.binding.tvTitle.text = item.title
        holder.binding.tvDescription.text = formattedDescription

        holder.binding.tvTranslated.text = formattedDescription

        translator.translate(formattedDescription)
            .addOnSuccessListener { translatedText ->
                if (holder.bindingAdapterPosition == position) {
                    holder.binding.tvTranslated.text = translatedText
                }
            }
            .addOnFailureListener {
                holder.binding.tvTranslated.text = formattedDescription
            }

        val index = abs(position) % colorsMulti.size
        val colorRes = colorsMulti[index]

        val indexText = abs(position) % colorsMultiText.size
        val colorText = colorsMultiText[indexText]

        holder.binding.viewLine.setBackgroundColor(
            ContextCompat.getColor(context, colorText)
        )

        holder.binding.cardView.setCardBackgroundColor(
            ContextCompat.getColor(context, colorRes)
        )
    }

    fun release() {
        translator.close()
    }
}
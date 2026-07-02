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
import com.raj.mygrowth.databinding.AdapterQuitZillaQuoteBinding
import com.raj.mygrowth.domain.QuitZillaQuoteItem
import kotlin.math.abs

class QuitZillaQuoteAdapter(
    private val list: List<QuitZillaQuoteItem>,
    private val context: Context,
) : RecyclerView.Adapter<QuitZillaQuoteAdapter.ViewHolder>() {

    class ViewHolder(val binding: AdapterQuitZillaQuoteBinding) :
        RecyclerView.ViewHolder(binding.root)

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


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = AdapterQuitZillaQuoteBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = list[position]

        val formattedTitle = item.Quote.replaceFirstChar { it.uppercase() }

        val formattedDescription =
            item.Quote.replaceFirstChar { it.uppercase() }
        holder.binding.tvId.text = (position + 1).toString()
        holder.binding.tvTitle.text = item.Author
        holder.binding.tvDescription.text = formattedTitle


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


        val index = abs(position.hashCode()) % colorsMulti.size
        val colorRes = colorsMulti[index]

        val indexs = abs(position.hashCode()) % colorsMultiText.size
        val colorRess = colorsMultiText[indexs]

        holder.binding.viewLine.setBackgroundColor(ContextCompat.getColor(context, colorRess))
        holder.binding.cardView.setCardBackgroundColor(ContextCompat.getColor(context, colorRes))
    }
}
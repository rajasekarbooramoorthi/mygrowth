package com.raj.mygrowth

import android.content.Context
import android.net.Uri
import android.util.Log
import android.widget.Toast
import com.raj.mygrowth.domain.ConceptModel
import java.io.BufferedReader
import java.io.InputStreamReader
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class Utilities {
    private fun readCsvFile(uri: Uri, context: Context) {
        try {
            val inputStream = context.contentResolver.openInputStream(uri)
            val reader = BufferedReader(InputStreamReader(inputStream))

            val list = ArrayList<ConceptModel>()
            var isHeader = true

            reader.forEachLine { rawLine ->
                val line = rawLine.trim()
                if (line.isEmpty()) return@forEachLine   // skip blank rows

                if (isHeader) {
                    isHeader = false
                    return@forEachLine
                }

                // Split CSV safely, including text containing commas inside quotes
                val columns = mutableListOf<String>()
                var current = StringBuilder()
                var insideQuotes = false

                for (char in line) {
                    when (char) {
                        '"' -> insideQuotes = !insideQuotes
                        ',' -> {
                            if (insideQuotes) {
                                current.append(char)
                            } else {
                                columns.add(current.toString())
                                current = StringBuilder()
                            }
                        }

                        else -> current.append(char)
                    }
                }
                columns.add(current.toString()) // last value

                if (columns.size < 2) return@forEachLine

                val id = columns[0].trim()
                val name = columns[1].trim()
                val links = columns.drop(2).map { it.trim() }.filter { it.isNotEmpty() }

                list.add(ConceptModel(conceptId = id, conceptName = name, links = links))
            }

            reader.close()
            Log.d("CSV_FINAL", list.toString())

            // Send to API

        } catch (e: Exception) {
            Toast.makeText(context, "Error reading CSV: ${e.message}", Toast.LENGTH_LONG)
                .show()
            Log.e("CSV_ERROR", e.toString())
        }
    }

    fun getCurrentDate() {
        val currentDate = Date()
        val formatter = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault())
        val formattedDate = formatter.format(currentDate)
        println(formattedDate)
    }
}
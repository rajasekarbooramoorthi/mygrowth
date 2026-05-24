package com.raj.mygrowth.adapter


import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.raj.mygrowth.FragmentReport
import com.raj.mygrowth.FragmentMotivation
import com.raj.mygrowth.FragmentRGUpdate
import com.raj.mygrowth.FragmentQuats
import com.raj.mygrowth.FragmentBenifits

class ViewPagerAdapter(activity: AppCompatActivity) : FragmentStateAdapter(activity) {

    private val fragments by lazy {
        listOf(
            FragmentReport(),
            FragmentRGUpdate(),
            FragmentMotivation(),
            FragmentBenifits(),
            FragmentQuats()
        )
    }

    val tabTitles = arrayOf(
        "Report",
        "Track",
        "Motive",
        "Benefits",
        "Quotes"
    )
    var currentPosition = 0
    override fun getItemCount(): Int = fragments.size

    override fun createFragment(position: Int): Fragment {
        currentPosition = position
        return fragments.getOrElse(position) { FragmentBenifits() }
    }

    fun fetCurrentPosition() = currentPosition
}
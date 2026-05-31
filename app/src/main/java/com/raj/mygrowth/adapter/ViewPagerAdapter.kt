package com.raj.mygrowth.adapter


import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.raj.mygrowth.FragmentBenefits
import com.raj.mygrowth.FragmentQuitZillaReport
import com.raj.mygrowth.FragmentMotivation
import com.raj.mygrowth.FragmentRGUpdate
import com.raj.mygrowth.FragmentQuotes

class ViewPagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

    private val fragments = listOf(
        FragmentQuitZillaReport(),
        FragmentRGUpdate(),
        FragmentMotivation(),
        FragmentBenefits(),
        FragmentQuotes()
    )

    val tabTitles = arrayOf(
        "Report",
        "Track",
        "Motive",
        "Benefits",
        "Quotes"
    )

    override fun getItemCount(): Int = fragments.size

    override fun createFragment(position: Int): Fragment {
        return fragments[position]
    }
}
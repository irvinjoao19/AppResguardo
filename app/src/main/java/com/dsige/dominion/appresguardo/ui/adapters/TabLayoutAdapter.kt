package com.dsige.dominion.appresguardo.ui.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.dsige.dominion.appresguardo.ui.fragments.*

abstract class TabLayoutAdapter {

    class TabLayoutForm(
        fm: FragmentManager,
        var numberOfTabs: Int, var otId: Int, var usuarioId: Int, var empresaId: Int
    ) :
        FragmentStatePagerAdapter(fm, numberOfTabs) {

        override fun getItem(position: Int): Fragment {
            return when (position) {
                0 -> GeneralFragment.newInstance(otId, usuarioId,empresaId)
                1 -> FirmFragment.newInstance(otId, 1)
                2 -> FirmFragment.newInstance(otId, 2)
                3 -> IncidenciaFragment.newInstance(otId,usuarioId)
                else -> Fragment()
            }
        }

        override fun getCount(): Int {
            return numberOfTabs
        }
    }

}
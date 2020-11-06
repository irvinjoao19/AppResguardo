package com.dsige.dominion.appresguardo.ui.activities

import android.os.Bundle
import com.dsige.dominion.appresguardo.R
import com.dsige.dominion.appresguardo.helper.Util
import com.dsige.dominion.appresguardo.ui.adapters.TabLayoutAdapter
import com.google.android.material.tabs.TabLayout
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.android.synthetic.main.activity_form.*

class FormActivity : DaggerAppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_form)
        val b = intent.extras
        if (b != null) {
            bind(
                b.getInt("otId"), b.getInt("usuarioId"), b.getInt("empresaId")
            )
        }
    }

    private fun bind(otId: Int, u: Int, e: Int) {
        setSupportActionBar(toolbar)
        supportActionBar!!.title = "Registro Parte Diario"
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener {
            finish()
        }
        tabLayout.addTab(tabLayout.newTab().setText(R.string.tab1))
        tabLayout.addTab(tabLayout.newTab().setText(R.string.tab2))
        tabLayout.addTab(tabLayout.newTab().setText(R.string.tab3))
        tabLayout.addTab(tabLayout.newTab().setText(R.string.tab4))

        val tabLayoutAdapter =
            TabLayoutAdapter.TabLayoutForm(
                supportFragmentManager, tabLayout.tabCount, otId, u, e
            )
        viewPager.adapter = tabLayoutAdapter
        viewPager.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tabLayout))
        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                viewPager.currentItem = tab.position
                Util.hideKeyboard(this@FormActivity)
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {}

            override fun onTabReselected(tab: TabLayout.Tab) {}
        })
    }
}

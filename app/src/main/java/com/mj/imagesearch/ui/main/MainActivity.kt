package com.mj.imagesearch.ui.main

import android.graphics.Typeface
import androidx.activity.viewModels
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.mj.imagesearch.R
import com.mj.imagesearch.base.BaseActivity
import com.mj.imagesearch.databinding.ActivityMainBinding
import com.mj.imagesearch.ui.main.favorite.FavoriteFragment
import com.mj.imagesearch.ui.main.search.SearchFragment
import com.mj.imagesearch.util.setTypeface
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding, MainViewModel>() {

    override val layoutResourceId: Int
        get() = R.layout.activity_main

    override val viewModel: MainViewModel by viewModels()

    override fun initOnCreate() {

        with(binding) {

            val adapter = Pager(this@MainActivity)
                .also { viewPager.adapter = it }

            tabs.getTabAt(tabs.selectedTabPosition)?.setTypeface(null, Typeface.BOLD)
            tabs.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
                override fun onTabUnselected(tab: TabLayout.Tab) {
                    tab.setTypeface(null, Typeface.NORMAL)
                }

                override fun onTabSelected(tab: TabLayout.Tab) {
                    tab.setTypeface(null, Typeface.BOLD)
                }

                override fun onTabReselected(tab: TabLayout.Tab) = Unit
            })

            TabLayoutMediator(tabs, viewPager, adapter).attach()
        }
    }

    class Pager(owner: FragmentActivity) :
        FragmentStateAdapter(owner), TabLayoutMediator.TabConfigurationStrategy {

        enum class Page { SEARCH, FAVORITE }

        override fun getItemCount(): Int = Page.values().size

        override fun createFragment(position: Int): Fragment = when (Page.values()[position]) {
            Page.SEARCH -> SearchFragment()
            Page.FAVORITE -> FavoriteFragment()
        }

        override fun onConfigureTab(tab: TabLayout.Tab, position: Int) {
            tab.text = when (Page.values()[position]) {
                Page.SEARCH -> "이미지 검색"
                Page.FAVORITE -> "즐겨 찾기"
            }
        }
    }
}
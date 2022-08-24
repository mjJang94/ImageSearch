package com.mj.imagesearch.ui.main

import androidx.activity.viewModels
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.mj.imagesearch.R
import com.mj.imagesearch.base.BaseActivity
import com.mj.imagesearch.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding, CommonSearchViewModel>() {

    override val layoutResourceId: Int
        get() = R.layout.activity_main

    override val viewModel: CommonSearchViewModel by viewModels()

    override fun initOnCreate() {

        val sectionsPagerAdapter = SectionsPagerAdapter(this)
        val viewPager: ViewPager2 = binding.viewPager
        viewPager.adapter = sectionsPagerAdapter
        val tabs: TabLayout = binding.tabs
        TabLayoutMediator(tabs, viewPager) { tab, position ->
            tab.text = if (position == 0) {
                "이미지 검색"
            } else {
                "즐겨 찾기"
            }
        }.attach()
    }

    inner class SectionsPagerAdapter(fragmentActivity: FragmentActivity) :
        FragmentStateAdapter(fragmentActivity) {

        override fun createFragment(position: Int): Fragment = if (position == 0) {
            ImageSearchFragment()
        } else {
            FavoriteFragment()
        }

        override fun getItemCount(): Int {
            return 2
        }
    }
}
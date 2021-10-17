package com.suonk.whatsapp_clone.ui.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.suonk.whatsapp_clone.ui.fragments.main_pages.CallsFragment
import com.suonk.whatsapp_clone.ui.fragments.main_pages.OnlineChatFragment
import com.suonk.whatsapp_clone.ui.fragments.main_pages.StatusFragment

class MainViewPagerAdapter(fragmentActivity: FragmentActivity) :
    FragmentStateAdapter(fragmentActivity) {

    override fun getItemCount() = 3

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> {
                OnlineChatFragment()
            }
            1 -> {
                StatusFragment()
            }
            2 -> {
                CallsFragment()
            }
            else -> {
                OnlineChatFragment()
            }
        }
    }
}
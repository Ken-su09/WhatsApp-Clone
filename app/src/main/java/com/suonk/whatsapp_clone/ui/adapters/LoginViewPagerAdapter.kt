package com.suonk.whatsapp_clone.ui.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.suonk.whatsapp_clone.ui.fragments.login.LoginFragment
import com.suonk.whatsapp_clone.ui.fragments.login.SignUpFragment

class LoginViewPagerAdapter(fragmentActivity: FragmentActivity) :
    FragmentStateAdapter(fragmentActivity) {

    override fun getItemCount() = 2

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> {
                LoginFragment()
            }
            1 -> {
                SignUpFragment()
            }
            else -> {
                LoginFragment()
            }
        }
    }
}
package com.suonk.whatsapp_clone.navigation

import androidx.fragment.app.FragmentActivity
import com.suonk.whatsapp_clone.R
import com.suonk.whatsapp_clone.ui.fragments.SplashScreenFragment
import com.suonk.whatsapp_clone.ui.fragments.login.MainLoginFragment
import javax.inject.Inject

class Navigator @Inject constructor(var activity: FragmentActivity?) {

    fun showSplashScreen() {
        activity?.supportFragmentManager
            ?.beginTransaction()
            ?.replace(R.id.fragment_container_view, SplashScreenFragment())
            ?.commit()
    }

    fun showMainLoginFragment() {
        activity?.supportFragmentManager
            ?.beginTransaction()
            ?.replace(R.id.fragment_container_view, MainLoginFragment())
            ?.commit()
    }
}
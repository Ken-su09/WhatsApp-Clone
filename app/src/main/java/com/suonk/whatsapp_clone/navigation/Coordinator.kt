package com.suonk.whatsapp_clone.navigation

import javax.inject.Inject

class Coordinator @Inject constructor(var navigator: Navigator) {

    fun showSplashScreen() {
        navigator.showSplashScreen()
    }

    fun showMainLoginFragment() {
        navigator.showMainLoginFragment()
    }
}
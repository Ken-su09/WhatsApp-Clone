package com.suonk.whatsapp_clone.ui.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.suonk.whatsapp_clone.databinding.FragmentSplashScreenBinding

@SuppressLint("CustomSplashScreen")
class SplashScreenFragment : Fragment() {

    private var binding: FragmentSplashScreenBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSplashScreenBinding.inflate(inflater, container, false)
        initializeUI()
        return binding?.root
    }

    //region ============================================== UI ==============================================

    private fun initializeUI() {
    }

    //endregion

    //region =========================================== LifeCycle ==========================================

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    //endregion
}
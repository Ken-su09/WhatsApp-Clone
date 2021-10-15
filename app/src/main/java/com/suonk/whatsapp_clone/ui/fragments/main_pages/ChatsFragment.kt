package com.suonk.whatsapp_clone.ui.fragments.main_pages

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.suonk.whatsapp_clone.R
import com.suonk.whatsapp_clone.databinding.FragmentChatsBinding
import com.suonk.whatsapp_clone.databinding.FragmentSplashScreenBinding

class ChatsFragment : Fragment() {

    private var binding: FragmentChatsBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentChatsBinding.inflate(inflater, container, false)
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
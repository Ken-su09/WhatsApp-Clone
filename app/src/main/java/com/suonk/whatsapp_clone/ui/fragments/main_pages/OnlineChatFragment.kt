package com.suonk.whatsapp_clone.ui.fragments.main_pages

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.suonk.whatsapp_clone.databinding.FragmentOnlineChatBinding

class OnlineChatFragment : Fragment() {

    //region ========================================== Val or Var ==========================================

    private var binding: FragmentOnlineChatBinding? = null

    //endregion

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentOnlineChatBinding.inflate(inflater, container, false)
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
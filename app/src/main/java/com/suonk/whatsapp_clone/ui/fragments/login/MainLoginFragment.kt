package com.suonk.whatsapp_clone.ui.fragments.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayoutMediator
import com.suonk.whatsapp_clone.R
import com.suonk.whatsapp_clone.databinding.FragmentMainLoginBinding
import com.suonk.whatsapp_clone.ui.activity.MainActivity
import com.suonk.whatsapp_clone.ui.adapters.LoginViewPagerAdapter

class MainLoginFragment : Fragment() {

    private var binding: FragmentMainLoginBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMainLoginBinding.inflate(inflater, container, false)
        initializeUI()
        return binding?.root
    }

    //region ============================================== UI ==============================================

    private fun initializeUI() {
        setupViewPager()
    }

    private fun setupViewPager() {
        binding?.loginViewPager?.adapter = LoginViewPagerAdapter(activity as MainActivity)

        if (binding?.loginTabLayout != null && binding?.loginViewPager != null) {
            TabLayoutMediator(
                binding?.loginTabLayout!!,
                binding?.loginViewPager!!
            ) { tab, position ->
                when (position) {
                    0 -> {
                        tab.icon = ResourcesCompat.getDrawable(resources, R.drawable.ic_login, null)
                    }
                    1 -> {
                        tab.icon =
                            ResourcesCompat.getDrawable(resources, R.drawable.ic_sign_up, null)
                    }
                }
            }.attach()
        }
    }

    //endregion

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}
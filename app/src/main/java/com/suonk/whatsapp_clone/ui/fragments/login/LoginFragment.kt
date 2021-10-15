package com.suonk.whatsapp_clone.ui.fragments.login

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import com.google.android.material.tabs.TabLayoutMediator
import com.suonk.whatsapp_clone.R
import com.suonk.whatsapp_clone.databinding.FragmentLoginBinding
import com.suonk.whatsapp_clone.databinding.FragmentMainLoginBinding
import com.suonk.whatsapp_clone.ui.activity.MainActivity
import com.suonk.whatsapp_clone.ui.adapters.LoginViewPagerAdapter

class LoginFragment : Fragment() {

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
                        tab.text = "Login"
//                        tab.icon = ResourcesCompat.getDrawable(resources, R.drawable.ic_notes, null)
                    }
                    1 -> {
                        tab.text = "Sign Up"
//                        tab.icon = ResourcesCompat.getDrawable(
//                            resources,
//                            R.drawable.ic_star_selected,
//                            null
//                        )
                    }
                }
            }.attach()
        }
    }

    //endregion

    //region =========================================== LifeCycle ==========================================

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    //endregion
}
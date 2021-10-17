package com.suonk.whatsapp_clone.ui.fragments.main_pages

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.ktx.Firebase
import com.suonk.whatsapp_clone.R
import com.suonk.whatsapp_clone.databinding.FragmentMainBinding
import com.suonk.whatsapp_clone.ui.activity.MainActivity
import com.suonk.whatsapp_clone.ui.adapters.MainViewPagerAdapter
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainFragment : Fragment() {

    //region ========================================== Val or Var ==========================================

    private var binding: FragmentMainBinding? = null
    private lateinit var cA: MainActivity

    @Inject
    lateinit var auth: FirebaseAuth

    //endregion

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMainBinding.inflate(inflater, container, false)
        initializeUI()
        return binding?.root
    }

    //region ============================================== UI ==============================================

    private fun initializeUI() {
        cA = activity as MainActivity
        initToolbar()
        setupViewPager()
    }

    private fun initToolbar() {
        getMessagesFilterText()

        binding?.toolbar?.inflateMenu(R.menu.main_toolbar_menu)
        binding?.toolbar?.overflowIcon = ResourcesCompat.getDrawable(
            (activity as MainActivity).resources,
            R.drawable.ic_three_dots,
            null
        )
        binding?.toolbar?.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.toolbar_search -> {
                    true
                }
                R.id.toolbar_logout -> {
                    alertDialogLogout()
                    true
                }
                R.id.toolbar_new_group -> {
                    menuItem.isChecked = true
                    true
                }
                R.id.toolbar_devices_connected -> {
                    menuItem.isChecked = true
                    true
                }
                R.id.toolbar_settings -> {
                    menuItem.isChecked = true
                    true
                }
                else -> {
                    super.onOptionsItemSelected(menuItem)
                    true
                }
            }
        }
    }

    private fun alertDialogLogout() {
        MaterialAlertDialogBuilder(cA, R.style.AlertDialogTheme)
            .setTitle(getString(R.string.alert_dialog_logout_button))
            .setPositiveButton(getString(R.string.alert_dialog_logout_button)) { _, _ ->
                auth.signOut()
                cA.showMainLoginFragment()
            }
            .setNegativeButton(getString(R.string.alert_dialog_negative_button_text)) { dialogInterface, _ ->
                dialogInterface.cancel()
                dialogInterface.dismiss()
            }
            .show()
    }

    private fun getMessagesFilterText() {

    }

    private fun setupViewPager() {
        binding?.mainViewPager?.adapter = MainViewPagerAdapter(activity as MainActivity)

        if (binding?.mainTabLayout != null && binding?.mainTabLayout != null) {
            TabLayoutMediator(
                binding?.mainTabLayout!!,
                binding?.mainViewPager!!
            ) { tab, position ->
                when (position) {
                    0 -> {
                        tab.text = "Chats"
                    }
                    1 -> {
                        tab.icon =
                            ResourcesCompat.getDrawable(resources, R.drawable.ic_sign_up, null)
                    }
                    2 -> {
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
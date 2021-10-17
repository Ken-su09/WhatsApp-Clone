package com.suonk.whatsapp_clone.ui.fragments.main_pages

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.suonk.whatsapp_clone.databinding.FragmentOnlineChatBinding
import com.suonk.whatsapp_clone.ui.activity.MainActivity
import com.suonk.whatsapp_clone.ui.adapters.ContactAdapter
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class OnlineChatFragment : Fragment() {

    //region ========================================== Val or Var ==========================================

    private var binding: FragmentOnlineChatBinding? = null
    private lateinit var contactAdapter: ContactAdapter

    private lateinit var cA: MainActivity

    @Inject
    lateinit var auth: FirebaseAuth

    @Inject
    lateinit var firestore: FirebaseFirestore

    @Inject
    lateinit var reference: DatabaseReference

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
        cA = activity as MainActivity
        contactAdapter = ContactAdapter(cA)

        initRecyclerView()
    }

    private fun initRecyclerView() {
        binding?.messagesRecyclerView?.apply {
            this.adapter = contactAdapter
            getContactsListFromFirebase()
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(cA)
        }
    }

    //endregion

    //region ==================================== Get Data from Firebase ====================================

    private fun getContactsListFromFirebase() {
        auth.currentUser
        reference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

            }

            override fun onCancelled(error: DatabaseError) {
            }

        })
    }

    //endregion

    //region =========================================== LifeCycle ==========================================

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    //endregion
}
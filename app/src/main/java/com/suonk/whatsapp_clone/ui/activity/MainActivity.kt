package com.suonk.whatsapp_clone.ui.activity

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.suonk.whatsapp_clone.R
import com.suonk.whatsapp_clone.databinding.ActivityMainBinding
import com.suonk.whatsapp_clone.navigation.Coordinator
import com.suonk.whatsapp_clone.navigation.Navigator
import dagger.hilt.android.AndroidEntryPoint
import de.hdodenhof.circleimageview.CircleImageView
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    //region ========================================== Val or Var ==========================================

    companion object {
        const val REQUEST_CODE = 100
    }

    private var binding: ActivityMainBinding? = null
    private lateinit var circleImageView: CircleImageView

    @Inject
    lateinit var navigator: Navigator
    private lateinit var coordinator: Coordinator

    //endregion

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        navigator.activity = this
        coordinator = Coordinator(navigator)
        coordinator.showSplashScreen()
    }

    fun openGalleryForImage(civ: CircleImageView) {
        circleImageView = civ
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE) {
            circleImageView.setImageURI(data?.data)
        }
    }
}
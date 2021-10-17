package com.suonk.whatsapp_clone.ui.fragments.login

import android.content.Context
import android.content.SharedPreferences
import android.graphics.drawable.AnimationDrawable
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.content.res.AppCompatResources
import androidx.appcompat.widget.AppCompatEditText
import androidx.appcompat.widget.AppCompatImageView
import com.google.firebase.auth.FirebaseAuth
import com.suonk.whatsapp_clone.R
import com.suonk.whatsapp_clone.databinding.FragmentLoginBinding
import com.suonk.whatsapp_clone.ui.activity.MainActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import kotlinx.coroutines.tasks.await
import java.lang.Exception
import java.util.regex.Pattern
import javax.inject.Inject

@AndroidEntryPoint
class LoginFragment : Fragment() {

    //region ========================================== Val or Var ==========================================

    private var binding: FragmentLoginBinding? = null
    private lateinit var cA: MainActivity

    @Inject
    lateinit var auth: FirebaseAuth
    private var passwordVisible = false

    private lateinit var sharedPrefCheckBox: SharedPreferences
    private lateinit var sharedPrefEmail: SharedPreferences
    private lateinit var sharedPrefPassword: SharedPreferences
    private var fieldsRemembered = false

    //endregion

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        initializeUI()
        return binding?.root
    }

    //region ============================================== UI ==============================================

    private fun initializeUI() {
        cA = activity as MainActivity
        isConnected()
        initSharedPreferences()
        signUpEmailTextListener()
        loginPasswordChangeVisibilityClick()
        loginButtonClick()
        checkboxClick()
        rememberFillFields()
    }

    //endregion

    //region ============================================ Clicks ============================================

    private fun loginButtonClick() {
        binding?.logInButton?.setOnClickListener {
            if (checkAll()) {
                loginUser()
            }
        }
    }

    private fun loginPasswordChangeVisibilityClick() {
        binding?.apply {
            loginPasswordGoToVisible.setOnClickListener {
                passwordChangeVisibility(loginPasswordGoToVisible, loginPassword)
            }
        }
    }

    private fun passwordChangeVisibility(iV: AppCompatImageView, password: AppCompatEditText) {
        val invisible = AppCompatResources.getDrawable(cA, R.drawable.password_invisible_animation)
        val visible = AppCompatResources.getDrawable(cA, R.drawable.password_visible_animation)

        CoroutineScope(Dispatchers.Main).launch {
            val frameAnimation = iV.drawable as AnimationDrawable
            frameAnimation.start()
            delay(525)
            frameAnimation.stop()
            if (passwordVisible) {
                iV.setImageDrawable(visible)
                password.transformationMethod = PasswordTransformationMethod.getInstance()
                passwordVisible = false
            } else {
                iV.setImageDrawable(invisible)
                password.transformationMethod = HideReturnsTransformationMethod.getInstance()
                passwordVisible = true
            }
        }
    }

    private fun checkboxClick() {
        binding?.loginCheckboxRemember?.setOnCheckedChangeListener { _, isChecked ->
            checkboxChangeSharedPreferences(isChecked)
        }
    }

    //endregion

    //region =========================================== Listeners ==========================================

    private fun signUpEmailTextListener() {
        binding?.loginEmail?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(p0: Editable?) {
                checkEmailConstantly()
            }
        })
    }

    //endregion

    //region ============================================ Checks ============================================

    private fun checkIfEmptyFields(): Boolean {
        return binding?.loginEmail?.text?.isNotEmpty()!! ||
                binding?.loginPassword?.text?.isNotEmpty()!!
    }

    private fun checkEmailValidationLogin(): Boolean {
        val emailPattern = Pattern.compile("[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+")
        val userEmail = binding?.loginEmail?.text?.toString()

        return userEmail?.trim()?.matches(emailPattern.toRegex())!!
    }

    private fun checkEmailConstantly() {
        val emailPattern = Pattern.compile("[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+")
        binding?.apply {
            if (loginEmail.text.toString().trim().matches(emailPattern.toRegex())) {
                loginEmailValidation.visibility = View.VISIBLE
                loginEmailValidation.setImageDrawable(
                    AppCompatResources.getDrawable(
                        cA,
                        R.drawable.ic_check_email
                    )
                )
            } else {
                if (loginEmail.text.toString().isEmpty()) {
                    loginEmailValidation.visibility = View.INVISIBLE
                } else {
                    loginEmailValidation.visibility = View.VISIBLE
                    loginEmailValidation.setImageDrawable(
                        AppCompatResources.getDrawable(
                            cA,
                            R.drawable.ic_check_email_cross
                        )
                    )
                }
            }
        }
    }

    private fun checkAll(): Boolean {
        return if (checkIfEmptyFields()) {
            if (checkEmailValidationLogin()) {
                true
            } else {
                toastWarningMessage(getString(R.string.email_not_valid_msg))
                false
            }
        } else {
            toastWarningMessage(getString(R.string.fields_empty_msg))
            false
        }
    }

    //endregion

    //region ========================================= User Firebase ========================================

    private fun loginUser() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                binding?.apply {
                    auth.signInWithEmailAndPassword(
                        loginEmail.text.toString(),
                        loginPassword.text.toString()
                    ).await()

                    withContext(Dispatchers.Main) {
                        sharedPreferencesRememberIsChecked()
                        toastWarningMessage("Log In")
                        cA.showMainFragment()
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    toastWarningMessage(e.message!!)
                }
            }
        }
    }

    //endregion

    //region ======================================= SharedPreferences ======================================

    private fun initSharedPreferences() {
        sharedPrefCheckBox = cA.getSharedPreferences("remember_checkbox", Context.MODE_PRIVATE)
        fieldsRemembered = sharedPrefCheckBox.getBoolean("remember_checkbox", false)
        binding?.loginCheckboxRemember?.isChecked = fieldsRemembered
    }

    private fun checkboxChangeSharedPreferences(isChecked: Boolean) {
        val editorCheckBox: SharedPreferences.Editor = sharedPrefCheckBox.edit()
        editorCheckBox.putBoolean("remember_checkbox", isChecked)
        editorCheckBox.apply()
    }

    private fun sharedPreferencesRememberIsChecked() {
        if (binding?.loginCheckboxRemember?.isChecked!!) {
            sharedPrefEmail = cA.getSharedPreferences("remember_email", Context.MODE_PRIVATE)
            val editorEmail: SharedPreferences.Editor = sharedPrefEmail.edit()
            editorEmail.putString("remember_email", binding?.loginEmail?.text.toString())
            editorEmail.apply()

            sharedPrefPassword = cA.getSharedPreferences("remember_password", Context.MODE_PRIVATE)
            val editorPassword: SharedPreferences.Editor = sharedPrefPassword.edit()
            editorPassword.putString("remember_password", binding?.loginPassword?.text.toString())
            editorPassword.apply()
        }
    }

    private fun rememberFillFields() {
        val sharedPrefEmail = cA.getSharedPreferences("remember_email", Context.MODE_PRIVATE)
        val sharedPrefPassword = cA.getSharedPreferences("remember_password", Context.MODE_PRIVATE)

        binding?.apply {
            if (binding?.loginCheckboxRemember?.isChecked!!) {
                loginEmail.setText(sharedPrefEmail.getString("remember_email", "")!!)
                loginPassword.setText(sharedPrefPassword.getString("remember_password", ""))
            }
        }
    }

    //endregion

    private fun isConnected() {
        if (auth.currentUser != null) {
            Log.i("isConnected", "${auth.currentUser}")
            cA.showMainFragment()
        }
    }

    private fun toastWarningMessage(msg: String) {
        Log.i("toastWarningMessage", msg)
        Toast.makeText(cA, msg, Toast.LENGTH_LONG).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}
package com.suonk.whatsapp_clone.ui.fragments.login

import android.graphics.drawable.AnimationDrawable
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.content.res.AppCompatResources
import com.suonk.whatsapp_clone.R
import com.suonk.whatsapp_clone.databinding.FragmentLoginBinding
import com.suonk.whatsapp_clone.databinding.FragmentSignUpBinding
import com.suonk.whatsapp_clone.ui.activity.MainActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.regex.Pattern

class SignUpFragment : Fragment() {

    //region ========================================== Val or Var ==========================================

    private var binding: FragmentSignUpBinding? = null
    private lateinit var contextActivity: MainActivity

    //endregion

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSignUpBinding.inflate(inflater, container, false)
        initializeUI()
        return binding?.root
    }

    //region ============================================== UI ==============================================

    private fun initializeUI() {
        contextActivity = activity as MainActivity

        signUpButtonClick()
        passwordGoToVisibleClick()
        passwordGoToInvisibleClick()

        signUpEmailTextListener()
    }

    //endregion

    //region =========================================== Listeners ==========================================

    private fun signUpEmailTextListener() {
        binding?.signUpEmail?.addTextChangedListener(object : TextWatcher {
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

    //region ============================================ Clicks ============================================

    private fun signUpButtonClick() {
        if (checkAll()) {

        }
    }

    private fun passwordGoToVisibleClick() {
        binding?.apply {
            loginPasswordGoToVisible.setOnClickListener {
                val frameAnimation = loginPasswordGoToVisible.drawable as AnimationDrawable
                frameAnimation.start()

                CoroutineScope(Dispatchers.Main).launch {
                    delay(525)
                    frameAnimation.stop()
                    loginPasswordGoToVisible.visibility = View.GONE
                    loginPasswordGoToInvisible.visibility = View.VISIBLE
                    signUpPassword.transformationMethod =
                        HideReturnsTransformationMethod.getInstance()
                }
            }
        }
    }

    private fun passwordGoToInvisibleClick() {
        binding?.apply {
            loginPasswordGoToInvisible.setOnClickListener {
                val frameAnimation = loginPasswordGoToInvisible.drawable as AnimationDrawable
                frameAnimation.start()

                CoroutineScope(Dispatchers.Main).launch {
                    delay(525)
                    frameAnimation.stop()
                    loginPasswordGoToVisible.visibility = View.VISIBLE
                    loginPasswordGoToInvisible.visibility = View.GONE
                    signUpPassword.transformationMethod = PasswordTransformationMethod.getInstance()
                }
            }
        }
    }

    //endregion

    //region ============================================ Checks ============================================

    private fun checkIfEmptyFields(): Boolean {
        return binding?.signUpUsername?.text?.isNotEmpty()!! ||
                binding?.signUpPhoneNumber?.text?.isNotEmpty()!! ||
                binding?.signUpEmail?.text?.isNotEmpty()!! ||
                binding?.signUpPassword?.text?.isNotEmpty()!! ||
                binding?.signUpConfirmPassword?.text?.isNotEmpty()!!
    }

    private fun checkEmailValidationSignUp(): Boolean {
        val emailPattern = Pattern.compile("[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+")
        val userEmail = binding?.signUpEmail?.text?.toString()

        return userEmail?.trim()?.matches(emailPattern.toRegex())!!
    }

    private fun checkEmailConstantly() {
        val emailPattern = Pattern.compile("[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+")

        binding?.apply {
            if (signUpEmail.text.toString().trim().matches(emailPattern.toRegex())) {
                loginEmailValidation.visibility = View.VISIBLE
                loginEmailValidation.setImageDrawable(
                    AppCompatResources.getDrawable(
                        contextActivity,
                        R.drawable.ic_check_email
                    )
                )
            } else {
                if (signUpEmail.text.toString().isEmpty()) {
                    loginEmailValidation.visibility = View.INVISIBLE
                } else {
                    loginEmailValidation.visibility = View.VISIBLE
                    loginEmailValidation.setImageDrawable(
                        AppCompatResources.getDrawable(
                            contextActivity,
                            R.drawable.ic_check_email_cross
                        )
                    )
                }
            }
        }
    }

    private fun checkIfPasswordMoreThan6Chars(): Boolean {
        return binding?.signUpPassword?.text?.length!! < 6
    }

    private fun checkIfPasswordAndConfirmedPassword(): Boolean {
        return binding?.signUpPassword?.text?.toString()
            ?.equals(binding?.signUpConfirmPassword?.text?.toString())!!
    }

    private fun checkAll(): Boolean {
        if (checkIfEmptyFields()) {
            if (checkEmailValidationSignUp()) {
                return if (checkIfPasswordMoreThan6Chars()) {
                    if (checkIfPasswordAndConfirmedPassword()) {
                        true
                    } else {
                        toastWarningMessage(getString(R.string.check_password_and_confirmed))
                        false
                    }
                } else {
                    toastWarningMessage(getString(R.string.password_not_enough_char))
                    false
                }
            } else {
                toastWarningMessage(getString(R.string.email_not_valid_msg))
                return false
            }
        } else {
            toastWarningMessage(getString(R.string.fields_empty_msg))
            return false
        }
    }

    //endregion

    private fun toastWarningMessage(msg: String) {
        Toast.makeText(contextActivity, msg, Toast.LENGTH_LONG).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}
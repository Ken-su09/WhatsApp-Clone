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
import androidx.appcompat.widget.AppCompatEditText
import androidx.appcompat.widget.AppCompatImageView
import com.suonk.whatsapp_clone.R
import com.suonk.whatsapp_clone.databinding.FragmentSignUpBinding
import com.suonk.whatsapp_clone.ui.activity.MainActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import java.lang.Exception
import java.util.regex.Pattern
import android.graphics.Bitmap
import android.graphics.Canvas

import android.graphics.drawable.BitmapDrawable

import android.graphics.drawable.Drawable
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.*
import java.io.ByteArrayOutputStream
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@AndroidEntryPoint
class SignUpFragment : Fragment() {

    //region ========================================== Val or Var ==========================================

    private var binding: FragmentSignUpBinding? = null
    private lateinit var cA: MainActivity

    @Inject
    lateinit var auth: FirebaseAuth

    private lateinit var callbacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks
    private lateinit var myCredential: PhoneAuthCredential
    private var passwordVisible = false

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
        cA = activity as MainActivity

        addUserImageClick()
        signUpButtonClick()
        signUpPasswordClick()
        confirmPasswordClick()
        signUpEmailTextListener()
        verifyCodeClick()
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
        binding?.signUpButton?.setOnClickListener {
            if (checkAll()) {
                registerNewUser()
            }
        }
    }

    private fun signUpPasswordClick() {
        binding?.apply {
            signUpPasswordGoToVisible.setOnClickListener {
                passwordChangeVisibility(signUpPasswordGoToVisible, signUpPassword)
            }
        }
    }

    private fun confirmPasswordClick() {
        binding?.apply {
            confirmPasswordGoToVisible.setOnClickListener {
                passwordChangeVisibility(confirmPasswordGoToVisible, signUpConfirmPassword)
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

    private fun addUserImageClick() {
        binding?.signUpUserImg?.setOnClickListener {
            cA.openGalleryForImage(binding?.signUpUserImg!!)
        }
    }

    private fun verifyCodeClick() {
        binding?.apply {
            verifyPhoneNumberButton.setOnClickListener {
                val code = verifyPhoneNumberEditText.text.toString()
                if (code.isEmpty() || code.length < 6) {
                    toastWarningMessage("Wrong code")
                } else {
                    getCallbacksFromPhoneAuthProvider(verifyPhoneNumberEditText.text.toString())
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
                        cA,
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
                            cA,
                            R.drawable.ic_check_email_cross
                        )
                    )
                }
            }
        }
    }

    private fun checkIfPasswordMoreThan6Chars(): Boolean {
        return binding?.signUpPassword?.text?.length!! > 6
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

    //region ========================================= User Firebase ========================================

    private fun registerNewUser() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                binding?.apply {
                    withContext(Dispatchers.Main) {
                        registerUserLayout.visibility = View.GONE
                        verifyPhoneNumberLayout.visibility = View.VISIBLE
                    }
                    auth.createUserWithEmailAndPassword(
                        signUpEmail.text.toString(),
                        signUpPassword.text.toString()
                    )
                    val phoneNumber = signUpPhoneNumber.text.toString()
                    verifyPhoneNumber(convertNumberToWhatsappNumber(phoneNumber))

                    if (auth.currentUser != null) {
                        updateUserData(auth.currentUser!!)
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    toastWarningMessage(e.message!!)
                }
            }
        }
    }

    private fun updateUserData(currentUser: FirebaseUser) {
        binding?.apply {
            currentUser.let { user ->
                val username = signUpUsername.text.toString()
                val photoURI = getImageUri(drawableToBitmap(signUpUserImg.drawable)!!)
                val profileUpdates = UserProfileChangeRequest.Builder()
                    .setDisplayName(username)
                    .setPhotoUri(photoURI)
                    .build()

                user.updatePhoneNumber(myCredential)
                user.updateProfile(profileUpdates)
            }
        }
    }

    //endregion

    //region ===================================== Verify Phone Number ======================================

    private fun verifyPhoneNumber(phoneNumber: String) {
        val options = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber(phoneNumber)
            .setTimeout(60L, TimeUnit.SECONDS)
            .setActivity(cA)
            .setCallbacks(callbacks)
            .build()

        PhoneAuthProvider.verifyPhoneNumber(options)
    }

    private fun getCallbacksFromPhoneAuthProvider(codeByUser: String) {
        callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                myCredential = credential
            }

            override fun onVerificationFailed(e: FirebaseException) {
                toastWarningMessage(e.message!!)
            }

            override fun onCodeSent(
                verificationId: String,
                token: PhoneAuthProvider.ForceResendingToken
            ) {
                myCredential = PhoneAuthProvider.getCredential(verificationId, codeByUser)
            }
        }
    }

    //endregion

    private fun drawableToBitmap(drawable: Drawable): Bitmap? {
        if (drawable is BitmapDrawable) {
            if (drawable.bitmap != null) {
                return drawable.bitmap
            }
        }
        val bitmap: Bitmap? = if (drawable.intrinsicWidth <= 0 || drawable.intrinsicHeight <= 0) {
            Bitmap.createBitmap(
                1,
                1,
                Bitmap.Config.ARGB_8888
            ) // Single color bitmap will be created of 1x1 pixel
        } else {
            Bitmap.createBitmap(
                drawable.intrinsicWidth,
                drawable.intrinsicHeight,
                Bitmap.Config.ARGB_8888
            )
        }
        val canvas = Canvas(bitmap!!)
        drawable.setBounds(0, 0, canvas.width, canvas.height)
        drawable.draw(canvas)
        return bitmap
    }

    private fun getImageUri(inImage: Bitmap): Uri? {
        val bytes = ByteArrayOutputStream()
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
        val path = MediaStore.Images.Media.insertImage(
            cA.contentResolver,
            inImage,
            "Title",
            null
        )
        return Uri.parse(path)
    }

    private fun convertNumberToWhatsappNumber(phoneNumber: String): String {
        if (phoneNumber[0] == '0') {
            return "+33$phoneNumber"
        }
        return phoneNumber
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
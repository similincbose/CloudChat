package dev.similin.cloudchat.ui.login

import android.os.CountDownTimer
import android.text.format.DateUtils
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.PhoneAuthProvider
import dev.similin.cloudchat.repository.LoginRepository

class LoginViewModel(private val repo: LoginRepository) : ViewModel() {
    var phoneNumber: String? = null
    var uid: String? = null
    val clicked = MutableLiveData<Boolean>()
    var time_in_milli_seconds = MutableLiveData<Long>()
    var verificationInProgress = false
    var storedVerificationId: String? = null
    var _resendToken: PhoneAuthProvider.ForceResendingToken? = null
    val currentTime = Transformations.map(time_in_milli_seconds) { time ->
        DateUtils.formatElapsedTime(time)
    }
    fun saveCountryCode(countryCode: String) = repo.saveCountryCode(countryCode)

    fun getCountryCode(): String? = repo.getCountryCode()

    fun saveUserID(userId: String) = repo.saveUserID(userId)

    fun getUserID(): String? = repo.getUserID()

    fun saveUserPhoneNumber(phone: String) = repo.saveUserPhoneNumber(phone)

    fun onLoginButtonClicked() {
        clicked.value = true
    }

    fun startTimer(seconds: Long) {
        var countDownTimer: CountDownTimer = object : CountDownTimer(seconds, ONE_SECOND) {
            override fun onTick(p0: Long) {
                time_in_milli_seconds.value = p0 / ONE_SECOND
            }

            override fun onFinish() {
                time_in_milli_seconds.value = DONE
            }
        }
        countDownTimer.start()
    }

    fun doneClick(){
        clicked.value=false
    }

    companion object {
        private const val ONE_SECOND = 1000L
        private const val DONE = 0L
    }
}
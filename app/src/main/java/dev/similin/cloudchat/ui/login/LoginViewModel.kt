package dev.similin.cloudchat.ui.login

import android.os.CountDownTimer
import android.text.format.DateUtils
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import dev.similin.cloudchat.network.Resource
import dev.similin.cloudchat.repository.LoginRepository
import timber.log.Timber

class LoginViewModel(private val repo: LoginRepository) : ViewModel() {
    var phoneNumber: String? = null
    var username: String? = null
    var about: String? = null
    var imageUrl: String? = null
    var uid: String? = null
    val clicked = MutableLiveData<Boolean>()
    var timeInMilliSeconds = MutableLiveData<Long>()
    var verificationInProgress = false
    var storedVerificationId: String? = null
    var resendingToken: PhoneAuthProvider.ForceResendingToken? = null
    val currentTime = Transformations.map(timeInMilliSeconds) { time ->
        DateUtils.formatElapsedTime(time)
    }
    val reference = Firebase.database.reference

    fun saveCountryCode(countryCode: String) = repo.saveCountryCode(countryCode)

    fun getCountryCode(): String? = repo.getCountryCode()

    fun saveUserID(userId: String) = repo.saveUserID(userId)

    fun saveUserPhoneNumber(phone: String) = repo.saveUserPhoneNumber(phone)

    fun onLoginButtonClicked() {
        clicked.value = true
    }

    fun startTimer(seconds: Long) {
        var countDownTimer: CountDownTimer = object : CountDownTimer(seconds, ONE_SECOND) {
            override fun onTick(p0: Long) {
                timeInMilliSeconds.value = p0 / ONE_SECOND
            }

            override fun onFinish() {
                timeInMilliSeconds.value = DONE
            }
        }
        countDownTimer.start()
    }

    fun fetchUsers() = liveData {
        emit(Resource.loading())
        try {
            val response = repo.fetchUsers()
            emit(Resource.success(response))
        } catch (e: Exception) {
            Timber.e(e)
            emit(Resource.error(e.message ?: "Error Occurred"))
        }
    }

    fun writeNewUser() {
        phoneNumber?.let {
            reference.child(it).child("username").setValue(username)
            reference.child(it).child("phoneNumber").setValue(phoneNumber)
            reference.child(it).child("about").setValue(about)
            reference.child(it).child("imageUrl").setValue(imageUrl)
        }
    }

    companion object {
        private const val ONE_SECOND = 1000L
        private const val DONE = 0L
    }
}
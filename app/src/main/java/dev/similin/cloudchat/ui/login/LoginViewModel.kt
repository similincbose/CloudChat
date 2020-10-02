package dev.similin.cloudchat.ui.login

import android.os.CountDownTimer
import android.text.format.DateUtils
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import dev.similin.cloudchat.model.UserResponseApi
import dev.similin.cloudchat.network.Resource
import dev.similin.cloudchat.repository.LoginRepository
import timber.log.Timber


class LoginViewModel @ViewModelInject constructor(private val repo: LoginRepository) : ViewModel() {
    var phoneNumber: String? = null
    var username: String? = ""
    var about: String? = ""
    var imageUrl: String? = "https://cdn.onlinewebfonts.com/svg/img_568657.png"
    var uid: String? = null
    val clicked = MutableLiveData<Boolean>()
    val timeInMilliSeconds = MutableLiveData<Long>()
    var found: Boolean? = false
    var verificationInProgress = false
    var storedVerificationId: String? = null
    var flag: Boolean? = false
    var resendingToken: PhoneAuthProvider.ForceResendingToken? = null
    val currentTime = Transformations.map(timeInMilliSeconds) { time ->
        DateUtils.formatElapsedTime(time)
    }
    private val reference = Firebase.database.reference

    fun saveCountryCode(countryCode: String) = repo.saveCountryCode(countryCode)

    fun getCountryCode(): String? = repo.getCountryCode()

    fun saveUserID(userId: String) = repo.saveUserID(userId)

    fun saveUserPhoneNumber(phone: String) = repo.saveUserPhoneNumber(phone)

    init {
        val user = UserResponseApi.Users("", "", "")
        reference.child("users").child("users").child("dummy").setValue(user)
    }

    fun onLoginButtonClicked() {
        clicked.value = true
    }

    fun startTimer(seconds: Long) {
        val countDownTimer: CountDownTimer = object : CountDownTimer(seconds, ONE_SECOND) {
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
            emit(Resource.error(e.message ?: "Error Occurred", null))
        }
    }

    fun writeNewUser(): Boolean? {
        phoneNumber?.let {
            val user = UserResponseApi.Users(username, about, imageUrl)
            reference.child("users").child("users").child("+" + getCountryCode() + it)
                .setValue(user)
            flag = true
        }
        return flag
    }

    fun checkUser(data: UserResponseApi.UserResponse) {
        val users: Map<String, UserResponseApi.Users>? = data.users
        if (users != null) {
            for (user in users) {
                if (("+${getCountryCode()}${phoneNumber}") == user.key) {
                    found = true
                    return
                }
            }
        }
    }

    companion object {
        private const val ONE_SECOND = 1000L
        private const val DONE = 0L
    }
}
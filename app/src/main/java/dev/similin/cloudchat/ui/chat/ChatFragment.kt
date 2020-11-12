package dev.similin.cloudchat.ui.chat

import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.google.firebase.FirebaseError
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.AndroidEntryPoint
import dev.similin.cloudchat.R
import dev.similin.cloudchat.databinding.FragmentChatBinding
import dev.similin.cloudchat.ui.main.MainActivity
import timber.log.Timber


@AndroidEntryPoint
class ChatFragment : Fragment() {
    private lateinit var binding: FragmentChatBinding
    private val viewModel by viewModels<ChatViewModel>()
    private lateinit var chatUserReference: DatabaseReference
    private lateinit var chatWithReference: DatabaseReference


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentChatBinding.inflate(layoutInflater)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val args by navArgs<ChatFragmentArgs>()
        (activity as MainActivity).supportActionBar?.title = args.name
        viewModel.chatWithUser = args.number
        viewModel.chatWithUserName = args.name
        chatUserReference =
            Firebase.database.reference.child("message")
                .child(
                    (viewModel.getUserPhone()?.replace(
                        "+91",
                        ""
                    )) + "_" + (viewModel.chatWithUser?.replace("+91", ""))
                )

        chatWithReference =
            Firebase.database.reference.child("message")
                .child(
                    viewModel.chatWithUser?.replace("+91", "") + "_" + viewModel.getUserPhone()
                        ?.replace(
                            "+91",
                            ""
                        )
                )
        showChats()
        binding.sendButton.setOnClickListener {
            addChats()
        }
    }

    private fun addChats() {
        if (!viewModel.chatMessage.equals("")) {
            val map: MutableMap<String, String> = HashMap()
            map["message"] = viewModel.chatMessage.toString()
            map["user"] = viewModel.chatWithUser.toString()
            chatUserReference.push().setValue(map)
            chatWithReference.push().setValue(map)
            binding.messageArea.setText("")
        }

    }

    private fun showChats() {
        chatUserReference.addChildEventListener(object : ChildEventListener {
            override fun onChildAdded(dataSnapshot: DataSnapshot, s: String?) {
                val map: Map<String, String> = dataSnapshot.value as Map<String, String>
                val message = map["message"].toString()
                val userName = map["user"].toString()
                Timber.d("user $userName and usrphone ${viewModel.getUserPhone()} ")
                if (userName.replace("+91", "") == viewModel.getUserPhone()) {
                    addMessageBox(message, 1)
                } else {
                    addMessageBox(message, 2)
                }
            }

            override fun onChildChanged(dataSnapshot: DataSnapshot, s: String?) {}
            override fun onChildRemoved(dataSnapshot: DataSnapshot) {}
            override fun onChildMoved(dataSnapshot: DataSnapshot, s: String?) {}
            override fun onCancelled(error: DatabaseError) {
            }

            fun onCancelled(firebaseError: FirebaseError?) {}
        })
    }


    private fun addMessageBox(message: String?, type: Int) {
        val textView = TextView(requireContext())
        textView.text = message
        textView.textSize = 14f
        val lp = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        lp.setMargins(0, 0, 0, 10)
        textView.layoutParams = lp
        // lp.weight=1.0f;
        if (type == 1) {
            lp.gravity = Gravity.RIGHT
            textView.setBackgroundResource(R.drawable.round_out)
        } else {
            lp.gravity = Gravity.LEFT
            textView.setBackgroundResource(R.drawable.round_in)
        }
        // textView.setLayoutParams(lp);
        binding.chatLayout.addView(textView)
        binding.scrollV.fullScroll(View.FOCUS_DOWN)
    }
}
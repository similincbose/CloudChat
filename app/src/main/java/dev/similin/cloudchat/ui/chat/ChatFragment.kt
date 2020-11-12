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
import dagger.hilt.android.AndroidEntryPoint
import dev.similin.cloudchat.R
import dev.similin.cloudchat.databinding.FragmentChatBinding
import dev.similin.cloudchat.ui.main.MainActivity
import timber.log.Timber

@AndroidEntryPoint
class ChatFragment : Fragment() {
    private lateinit var binding: FragmentChatBinding
    private val viewModel by viewModels<ChatViewModel>()
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
        showChats()
        binding.sendButton.setOnClickListener {
            addChats()
        }
    }

    private fun addChats() {
        viewModel.addChatReference()
        binding.messageArea.setText("")
        viewModel.mapAddChat.observe(viewLifecycleOwner) {
            it.let { map ->
                val message = map?.get("message")?.toString()
                when (val userName = map?.get("user")?.toString()) {
                    viewModel.getUserPhone() -> addMessageBox(message, 1)
                    viewModel.chatWithUser -> addMessageBox(
                        message,
                        2
                    )
                    else -> Timber.e(userName.toString())
                }
            }
        }
    }

    private fun showChats() {
        viewModel.getChatReference()
        viewModel.mapChat.observe(viewLifecycleOwner) {
            it.let { map ->
                Timber.e(map.toString())
                val message = map?.get("message")
                when (val userName = map?.get("user")) {
                    viewModel.getUserPhone() -> addMessageBox(message, 1)
                    viewModel.chatWithUser -> addMessageBox(
                        message,
                        2
                    )
                    else -> Timber.e(userName.toString())
                }
            }
        }
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
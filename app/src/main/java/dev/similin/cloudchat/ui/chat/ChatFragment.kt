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
import dev.similin.cloudchat.databinding.FragmentChatBinding
import dev.similin.cloudchat.ui.main.MainActivity

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
        binding.sendButton.setOnClickListener {
            showChats()
        }
    }

    private fun showChats() {
        viewModel.addChatReference().observe(viewLifecycleOwner) {
            it.let { map ->
                val message = map?.get("message")?.toString()
                val userName = map?.get("user")?.toString()
                if (userName.equals(viewModel.getUserPhone())) {
                    addMessageBox("You:-\n$message", 1);
                } else {
                    addMessageBox(viewModel.chatWithUserName + ":-\n" + message, 2);
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
            /*lp.background=R.drawable.round_in*/
        } else {
            lp.gravity = Gravity.LEFT
            /*textView.setBackgroundResource(R.drawable.round_out)*/
        }
        // textView.setLayoutParams(lp);
        binding.chatLayout.addView(textView)
        binding.scrollV.fullScroll(View.FOCUS_DOWN)
    }
}
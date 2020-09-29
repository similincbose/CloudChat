package dev.similin.cloudchat.ui.chat

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import dev.similin.cloudchat.databinding.FragmentChatBinding
import dev.similin.cloudchat.ui.main.MainActivity

class ChatFragment : Fragment() {
    private lateinit var binding: FragmentChatBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentChatBinding.inflate(layoutInflater)
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val args by navArgs<ChatFragmentArgs>()
        (activity as MainActivity).supportActionBar?.title = args.name
        Toast.makeText(requireContext(), "${args.name} and ${args.number}", Toast.LENGTH_SHORT)
            .show()
    }
}
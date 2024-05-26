package com.example.shoppingapp.presentation.start_screen

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.shoppingapp.R
import com.example.shoppingapp.databinding.FragmentStartScreenBinding

class StartScreenFragment : Fragment() {

    private var _binding: FragmentStartScreenBinding? = null

    private val binding: FragmentStartScreenBinding
        get() = _binding ?: throw Exception("FragmentStartScreenBinding == null")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentStartScreenBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnMoveSignUp.setOnClickListener {
            launchSignUpFragment()
        }

        binding.btnMoveSignIn.setOnClickListener {
            launchSignInFragment()
        }
    }

    private fun launchSignUpFragment(){
        findNavController().navigate(StartScreenFragmentDirections.actionStartScreenFragmentToSignUpFragment())
    }

    private fun launchSignInFragment(){
        findNavController().navigate(StartScreenFragmentDirections.actionStartScreenFragmentToSignInFragment())
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}
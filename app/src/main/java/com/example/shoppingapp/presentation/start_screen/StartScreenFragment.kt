package com.example.shoppingapp.presentation.start_screen

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.example.shoppingapp.databinding.FragmentStartScreenBinding
import com.example.shoppingapp.presentation.activity.AppActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class StartScreenFragment : Fragment() {

    private var _binding: FragmentStartScreenBinding? = null

    private val binding: FragmentStartScreenBinding
        get() = _binding ?: throw Exception("FragmentStartScreenBinding == null")

    private val viewModel by viewModels<StartScreenViewModel>()

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

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED){
                viewModel.navState.collect{
                    when(it){
                        StartScreenViewModel.AUTHED -> {
                            val intent = Intent(activity, AppActivity::class.java)
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                            startActivity(intent)
                        }
                    }
                }
            }
        }

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
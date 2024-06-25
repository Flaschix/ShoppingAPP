package com.example.shoppingapp.presentation.sign_up

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.example.shoppingapp.databinding.FragmentSignUpBinding
import com.example.shoppingapp.domain.entity.User
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SignUpFragment : Fragment() {

    private var _binding: FragmentSignUpBinding? = null
    private val binding: FragmentSignUpBinding
        get() = _binding ?: throw Exception("FragmentSignUpBinding == null")

    private val viewModel by viewModels<SignUpViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSignUpBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnSignUp.setOnClickListener {
            val name = binding.edName.text.toString().trim()
            val surname = binding.edSurname.text.toString().trim()
            val email = binding.edEmail.text.toString().trim()
            val password = binding.edPassword.text.toString()

            val user = User(
                name,
                surname,
                email,
                password
            )

            viewModel.signUpUser(user)
        }

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED){
                viewModel.signUpState.collect{
                    when(it){
                        is SignUpState.Initial -> binding.prbSignUp.visibility = View.GONE
                        is SignUpState.Loading -> {
                            binding.btnSignUp.visibility = View.GONE
                            binding.prbSignUp.visibility = View.VISIBLE
                        }
                        is SignUpState.Success -> {
                            launchSignInFragment()
                        }
                        is SignUpState.Error -> {
                            Log.d("TEST", "onViewCreated: ${it.error}")
                            binding.btnSignUp.visibility = View.VISIBLE
                            binding.prbSignUp.visibility = View.GONE
                        }
                    }
                }
            }
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED){
                viewModel.validateState.collect{
                    if (it.name is SignUpValidateState.Error){
                        binding.edName.apply {
                            requestFocus()
                            error = it.name.msg
                        }
                    }

                    if (it.surname is SignUpValidateState.Error){
                        binding.edSurname.apply {
                            requestFocus()
                            error = it.surname.msg
                        }
                    }

                    if (it.email is SignUpValidateState.Error){
                        binding.edEmail.apply {
                            requestFocus()
                            error = it.email.msg
                        }
                    }

                    if (it.password is SignUpValidateState.Error){
                        binding.edPassword.apply {
                            requestFocus()
                            error = it.password.msg
                        }
                    }
                }
            }
        }
    }

    fun launchSignInFragment(){
        findNavController().navigate(SignUpFragmentDirections.actionSignUpFragmentToSignInFragment())
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}
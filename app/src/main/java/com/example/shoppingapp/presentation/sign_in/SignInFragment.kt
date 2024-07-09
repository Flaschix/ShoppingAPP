package com.example.shoppingapp.presentation.sign_in

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.shoppingapp.presentation.activity.AppActivity
import com.example.shoppingapp.databinding.FragmentSignInBinding
import com.example.shoppingapp.presentation.sign_in.dialog.ResetPasswordState
import com.example.shoppingapp.presentation.sign_in.dialog.setupBottomSheetDialog
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class SignInFragment : Fragment() {

    private var _binding: FragmentSignInBinding? = null
    private val binding: FragmentSignInBinding
        get() = _binding ?: throw Exception("FragmentSignInBinding === null")

    private val viewModel by viewModels<SignInViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSignInBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            btnSignIn.setOnClickListener {
                val email: String = edSignInEmail.text.toString().trim()
                val password: String = edSignInPassword.text.toString()
                viewModel.signIn(email, password)
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED){
                viewModel.signInState.collect{
                    when(it){
                        is SignInState.Initial -> binding.prbSignIn.visibility = View.GONE
                        is SignInState.Loading -> {
                            binding.btnSignIn.visibility = View.GONE
                            binding.prbSignIn.visibility = View.VISIBLE
                        }

                        is SignInState.Error -> {
                            Toast.makeText(context, "${it.error}", Toast.LENGTH_SHORT).show()
                            binding.btnSignIn.visibility = View.VISIBLE
                            binding.prbSignIn.visibility = View.GONE
                        }
                        is SignInState.Success -> {
                            val intent = Intent(activity, AppActivity::class.java)
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                            startActivity(intent)
                        }
                    }
                }
            }
        }

        binding.tvForgotPassword.setOnClickListener {
            setupBottomSheetDialog { email ->
                viewModel.resetPassword(email)
            }
        }

        viewLifecycleOwner.lifecycleScope.launch{
            repeatOnLifecycle(Lifecycle.State.STARTED){
                viewModel.resetPassword.collect{
                    when(it){
                        is ResetPasswordState.Loading -> {

                        }

                        is ResetPasswordState.Error -> {
                            Snackbar.make(requireView(), "Error ${it.error}", Snackbar.LENGTH_SHORT).show()
                        }
                        is ResetPasswordState.Success -> {
                            Snackbar.make(requireView(), "Resent link was sent to your email", Snackbar.LENGTH_SHORT).show()
                        }

                        else -> {}
                    }
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED){

                viewModel.validateState.collect{
                    if (it.email is SignInValidateState.Error) {
                        binding.edSignInEmail.apply {
                            requestFocus()
                            error = it.email.msg
                        }
                    }
                    if (it.password is SignInValidateState.Error){
                        binding.edSignInPassword.apply {
                            requestFocus()
                            error = it.password.msg
                        }
                    }
                }

            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
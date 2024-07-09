package com.example.shoppingapp.presentation.profile

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.shoppingapp.R
import com.example.shoppingapp.databinding.FragmentProfileBinding
import com.example.shoppingapp.domain.entity.User
import com.example.shoppingapp.presentation.activity.AuthActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ProfileFragment: Fragment(R.layout.fragment_profile) {

    private var _binding: FragmentProfileBinding? = null

    private val binding: FragmentProfileBinding
        get() = _binding ?: throw Exception("FragmentProfileBinding === null")

    private val viewModel by viewModels<ProfileViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED){
                viewModel.userState.collect{
                    when(it){
                        is UserState.Error -> {
                            Toast.makeText(requireContext(), it.msg, Toast.LENGTH_SHORT).show()
                        }
                        UserState.Initial -> {}
                        UserState.Loading -> {
                            loadingStateView()
                        }
                        is UserState.Success -> {
                            successStateView()
                            setUpView(it.data)
                        }
                    }
                }
            }
        }


    }

    private fun successStateView() {
        binding.apply {
//            profileContainer.visibility = View.VISIBLE
            progressbarSettings.visibility = View.GONE
        }
    }

    private fun loadingStateView() {
        binding.apply {
//            profileContainer.visibility = View.GONE
            progressbarSettings.visibility = View.VISIBLE
        }
    }

    private fun setUpView(data: User){
        binding.apply {
            tvUserName.text = data.name
            tvUserEmail.text = data.mail

            btnLogOut.setOnClickListener {
                viewModel.logOut()

                val intent = Intent(requireActivity(), AuthActivity::class.java)
                startActivity(intent)
                requireActivity().finish()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}
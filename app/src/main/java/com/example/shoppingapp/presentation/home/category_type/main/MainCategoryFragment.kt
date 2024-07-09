package com.example.shoppingapp.presentation.home.category_type.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.shoppingapp.R
import com.example.shoppingapp.databinding.FragmentMainCategoryBinding
import com.example.shoppingapp.presentation.home.HomeFragmentDirections
import com.example.shoppingapp.presentation.home.category_type.RVState
import com.example.shoppingapp.presentation.home.category_type.adapter.BestCaseAdapter
import com.example.shoppingapp.presentation.home.category_type.adapter.BestProductAdapter
import com.example.shoppingapp.presentation.home.category_type.adapter.SpecialProductAdapter
import com.example.shoppingapp.util.showBottomNavigationView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainCategoryFragment: Fragment(R.layout.fragment_main_category) {

    private var _binding: FragmentMainCategoryBinding? = null

    private val binding: FragmentMainCategoryBinding
        get() = _binding ?: throw Exception("FragmentMainCategoryBinding === null")

    private val specialProductAdapter: SpecialProductAdapter by lazy {
        SpecialProductAdapter()
    }

    private val bestCaseAdapter: BestCaseAdapter by lazy {
        BestCaseAdapter()
    }

    private val bestProductAdapter: BestProductAdapter by lazy {
        BestProductAdapter()
    }

    private val viewModel by viewModels<MainCategoryViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainCategoryBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpSPRV()
        setUpBCRV()
        setUpBPRV()

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED){
                viewModel.screenState.collect{
                    when(it){
                        is RVState.Error -> {}
                        RVState.Initial -> {}
                        RVState.Loading -> {
                            showProgress()
                        }
                        is RVState.Success -> {
                            specialProductAdapter.submitList(it.data)
                            bestCaseAdapter.submitList(it.data)
                            bestProductAdapter.submitList(it.data)
                            hideProgress()
                        }
                    }
                }
            }
        }


    }

    private fun setUpSPRV(){
        binding.rvSpecialProducts.apply {
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            adapter = specialProductAdapter
        }

        specialProductAdapter.onProductClickListener = {
            findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToShopItemFragment(it))
        }

        bestCaseAdapter.onProductClickListener = {
            findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToShopItemFragment(it))
        }

        bestProductAdapter.onProductClickListener = {
            findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToShopItemFragment(it))
        }
    }

    private fun setUpBCRV(){
        binding.rvBestDealsProducts.apply {
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            adapter = bestCaseAdapter
        }
    }

    private fun setUpBPRV(){
        binding.rvBestProducts.apply {
            layoutManager = GridLayoutManager(requireContext(), 2, GridLayoutManager.VERTICAL, false)
            adapter = bestProductAdapter
        }
    }

    private fun showProgress() {
        binding.mainCategoryProgressbar.visibility = View.VISIBLE
        binding.bestProductsProgressbar.visibility = View.VISIBLE
    }

    private fun hideProgress() {
        binding.mainCategoryProgressbar.visibility = View.GONE
        binding.bestProductsProgressbar.visibility = View.GONE
    }

    override fun onResume() {
        super.onResume()
        showBottomNavigationView()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
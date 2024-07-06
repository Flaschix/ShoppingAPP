package com.example.shoppingapp.presentation.home.category_type.base

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
import com.example.shoppingapp.databinding.FragmentBaseCategoryBinding
import com.example.shoppingapp.domain.usecase.GetListProductByCategoryUseCase
import com.example.shoppingapp.presentation.home.HomeFragmentDirections
import com.example.shoppingapp.presentation.home.category_type.RVState
import com.example.shoppingapp.presentation.home.category_type.adapter.BestProductAdapter
import com.example.shoppingapp.util.showBottomNavigationView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

open class BaseCategoryFragment: Fragment(R.layout.fragment_base_category) {

    private var _binding: FragmentBaseCategoryBinding? = null
    private val binding: FragmentBaseCategoryBinding
        get() = _binding ?: throw Exception("${this.javaClass} === null")

    open val offerAdapter: BestProductAdapter by lazy {
        BestProductAdapter()
    }

    open val bestProductAdapter: BestProductAdapter by lazy {
        BestProductAdapter()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBaseCategoryBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpBPRV()
        setUpOfferRV()

        offerAdapter.onProductClickListener = {
            findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToShopItemFragment(it))
        }

        bestProductAdapter.onProductClickListener = {
            findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToShopItemFragment(it))
        }
    }

    open fun setUpBPRV(){
        binding.rvBestProducts.apply {
            layoutManager = GridLayoutManager(
                requireContext(),
                3,
                GridLayoutManager.VERTICAL,
                false
            )
            adapter = bestProductAdapter
        }
    }

    open fun setUpOfferRV(){
        binding.rvOfferProducts.apply {
            layoutManager = LinearLayoutManager(
                requireContext(),
                LinearLayoutManager.HORIZONTAL,
                false
            )
            adapter = offerAdapter
        }
    }

    open fun showProgress() {
        binding.offerProductsProgressBar.visibility = View.VISIBLE
        binding.bestProductsProgressBar.visibility = View.VISIBLE
    }

    open fun hideProgress() {
        binding.offerProductsProgressBar.visibility = View.GONE
        binding.bestProductsProgressBar.visibility = View.GONE
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
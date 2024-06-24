package com.example.shoppingapp.presentation.home.category_type

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.shoppingapp.R
import com.example.shoppingapp.databinding.FragmentMainCategoryBinding
import com.example.shoppingapp.presentation.home.special_product.SpecialProductAdapter

class MainCategoryFragment: Fragment(R.layout.fragment_main_category) {

    private var _binding: FragmentMainCategoryBinding? = null

    private val binding: FragmentMainCategoryBinding
        get() = _binding ?: throw Exception("FragmentMainCategoryBinding === null")

    private val productAdapter: SpecialProductAdapter by lazy {
        SpecialProductAdapter()
    }

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

    }

    private fun setUpRV(){
        binding.rvSpecialProducts.apply {
            adapter = productAdapter
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
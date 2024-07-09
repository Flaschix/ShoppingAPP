package com.example.shoppingapp.presentation.shop_item

import android.graphics.Paint
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
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.shoppingapp.R
import com.example.shoppingapp.databinding.FragmentShopItemBinding
import com.example.shoppingapp.util.hideBottomNavigationView
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ShopItemFragment : Fragment(R.layout.fragment_shop_item) {
    private var _binding: FragmentShopItemBinding? = null

    private val binding: FragmentShopItemBinding
        get() = _binding ?: throw Exception("FragmentShopItemBinding === null")

    private val vpAdapter: ShopItemVPAdapter by lazy {
        ShopItemVPAdapter()
    }

    private val infoAdapter: ShopItemInfoAdapter by lazy {
        ShopItemInfoAdapter()
    }

    private val args by navArgs<ShopItemFragmentArgs>()

    private val viewModel by viewModels<ShopItemViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        hideBottomNavigationView()
        _binding = FragmentShopItemBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val product = args.product

        setUpVP()
        setUpRVInfo()

        vpAdapter.submitList(product.images)
        infoAdapter.submitList(product.info)

        binding.imageClose.setOnClickListener {
            findNavController().navigateUp()
        }



        binding.apply {
            tvTitle.text = product.title
            tvProductDescription.text = product.description
            tvProductPrice.text = getString(R.string.price, product.price.priceWithDiscount)
            tvOldProductPrice.text = getString(R.string.old_price, product.price.price)
            tvOldProductPrice.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
            tvProductDiscount.text = getString(R.string.discount, product.price.discount.toString())
            tvAvailable.text = getString(R.string.available_to_order, product.available.toString())
            tvIngredients.text = product.ingredients
            tvSubtitle.text = product.subtitle
            if(product.info.isEmpty()) binding.tvProductInfo.visibility = View.INVISIBLE
        }

        binding.buttonAddToCart.setOnClickListener {
            viewModel.addProductToBasket(product)
        }

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED){
                viewModel.addToBasketState.collect{
                    when(it){
                        is AddProductState.Error -> {
                            Toast.makeText(requireContext(), "Error: ${it.msg}", Toast.LENGTH_SHORT).show()
                        }
                        AddProductState.Initial -> {
                            binding.prbAddShopItem.visibility = View.GONE
                        }
                        AddProductState.Loading -> {
                            binding.prbAddShopItem.visibility = View.VISIBLE
                            binding.buttonAddToCart.visibility = View.GONE
                        }
                        AddProductState.Success -> {
                            binding.prbAddShopItem.visibility = View.GONE
                            binding.buttonAddToCart.visibility = View.VISIBLE
                        }
                    }
                }
            }
        }
    }

    private fun setUpVP(){
        binding.apply {
            viewPagerProductImages.adapter = vpAdapter
            TabLayoutMediator(intoTabLayout, viewPagerProductImages) { tab, pos -> }.attach()
        }
    }

    private fun setUpRVInfo(){
        binding.rvInfo.apply {
            adapter = infoAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
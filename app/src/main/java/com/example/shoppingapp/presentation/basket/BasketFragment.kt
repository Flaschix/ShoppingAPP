package com.example.shoppingapp.presentation.basket

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.shoppingapp.R
import com.example.shoppingapp.databinding.FragmentBasketBinding
import com.example.shoppingapp.util.showBottomNavigationView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class BasketFragment: Fragment(R.layout.fragment_basket) {

    private var _binding: FragmentBasketBinding? = null

    private val binding: FragmentBasketBinding
        get() = _binding ?: throw Exception("FragmentBasketBinding === null")

    private val viewModel by activityViewModels<BasketViewModel>()

    private val basketAdapter by lazy {
        BasketAdapter()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        showBottomNavigationView()
        _binding = FragmentBasketBinding.inflate(layoutInflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpRV()

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED){
                viewModel.basketState.collect{
                    when(it){
                        is BasketState.Error -> {
                            binding.prbBasket.visibility = View.INVISIBLE
                            Toast.makeText(requireContext(), "Error", Toast.LENGTH_SHORT).show()
                        }
                        BasketState.Initial -> {}
                        BasketState.Loading -> {
                            binding.prbBasket.visibility = View.VISIBLE
                        }
                        is BasketState.Success -> {
                            binding.prbBasket.visibility = View.INVISIBLE
                            if (it.data.isEmpty()){
                                hideBuyElements()
                            } else {
                                showBuyElements()
                                basketAdapter.submitList(it.data)
                            }
                        }
                    }
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch{
            repeatOnLifecycle(Lifecycle.State.STARTED){
                viewModel.productsPrice.collectLatest {
                    it?.let {
                        binding.tvTotalPrice.text = getString(R.string.amount_price, it.toString())
                    }
                }
            }
        }
    }

    private fun hideBuyElements(){
        binding.apply {
            layoutCartEmpty.visibility = View.VISIBLE
            rvBasket.visibility = View.INVISIBLE
            totalBoxContainer.visibility = View.INVISIBLE
            buttonCheckout.visibility = View.INVISIBLE
        }
    }

    private fun showBuyElements(){
        binding.apply {
            layoutCartEmpty.visibility = View.INVISIBLE
            rvBasket.visibility = View.VISIBLE
            totalBoxContainer.visibility = View.VISIBLE
            buttonCheckout.visibility = View.VISIBLE
        }
    }

    private fun setUpRV(){
        binding.rvBasket.apply {
            layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
            adapter = basketAdapter
        }

        basketAdapter.onProductClickListener = {
            findNavController().navigate(BasketFragmentDirections.actionBasketFragmentToShopItemFragment(it))
        }

        basketAdapter.deleteClickListener = {
            viewModel.deleteProductFromBasket(it)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
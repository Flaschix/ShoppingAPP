package com.example.shoppingapp.presentation.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.shoppingapp.R
import com.example.shoppingapp.databinding.FragmentHomeBinding
import com.example.shoppingapp.presentation.home.category_type.base.BodyFragment
import com.example.shoppingapp.presentation.home.category_type.base.FaceFragment
import com.example.shoppingapp.presentation.home.category_type.base.MaskFragment
import com.example.shoppingapp.presentation.home.category_type.base.InRoadFragment
import com.example.shoppingapp.presentation.home.category_type.base.SuntanFragment
import com.example.shoppingapp.presentation.home.category_type.main.MainCategoryFragment
import com.google.android.material.tabs.TabLayoutMediator

class HomeFragment: Fragment(R.layout.fragment_home) {

    private var _binding: FragmentHomeBinding? = null
    private val binding: FragmentHomeBinding
        get() = _binding ?: throw Exception("FragmentHomeBinding === null")

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val categoriesFragment = arrayListOf<Fragment>(
            MainCategoryFragment(),
            MaskFragment(),
            SuntanFragment(),
            FaceFragment(),
            BodyFragment(),
            InRoadFragment()
        )

        binding.viewpagerHome.isUserInputEnabled = false

        val viewPagerAdapter = HomeAdapter(categoriesFragment, childFragmentManager, lifecycle)

        binding.viewpagerHome.adapter = viewPagerAdapter

        TabLayoutMediator(binding.tabLayout, binding.viewpagerHome) { tab, pos ->
            when(pos){
                0 -> tab.text = "Главная"
                1 -> tab.text = "Тело"
                2 -> tab.text = "Загар"
                3 -> tab.text = "Лицо"
                4 -> tab.text = "Маски"
                5 -> tab.text = "В дорогу"
            }
        }.attach()

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
package com.example.shoppingapp.presentation.home.category_type

import androidx.fragment.app.Fragment
import com.example.shoppingapp.R

sealed class BaseCategoryFragment: Fragment(R.layout.fragment_base_category) {

    class Cupboard: BaseCategoryFragment()

    class Table: BaseCategoryFragment()

    class Chair: BaseCategoryFragment()

    class Bad: BaseCategoryFragment()

    class Illumination: BaseCategoryFragment()
}
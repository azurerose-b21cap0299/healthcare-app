package com.ilham.azurerosehealthmanagerapps.search

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.ilham.azurerosehealthmanagerapps.R
import com.ilham.azurerosehealthmanagerapps.core.data.Resource
import com.ilham.azurerosehealthmanagerapps.databinding.FragmentSearchBinding
import kotlinx.coroutines.flow.observeOn
import org.koin.android.viewmodel.compat.ScopeCompat.viewModel
import org.koin.android.viewmodel.ext.android.viewModel

class SearchFragment : Fragment() {
    private val searchViewModel: SearchViewModel by viewModel()
    private var _fragmentSearchBinding: FragmentSearchBinding? = null
    private val binding get() = _fragmentSearchBinding

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _fragmentSearchBinding = FragmentSearchBinding.inflate(layoutInflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (activity != null) {
            val foodAdapter = FoodAdapter()
            foodAdapter.notifyDataSetChanged()
            binding?.rvUser?.layoutManager = LinearLayoutManager(context)
            binding?.rvUser?.adapter = foodAdapter
            binding?.rvUser?.setHasFixedSize(true)
            binding?.rvUser?.visibility = View.VISIBLE

            binding?.btnSearch?.setOnClickListener {
                val foodName = binding?.editQuery?.text.toString()
                if (foodName.isEmpty()) return@setOnClickListener
                searchViewModel.searchFood(foodName).observe(viewLifecycleOwner, { food ->
                    if (food != null) {
                        when(food) {
                            is Resource.Loading -> binding?.progressBar?.visibility = View.GONE
                            is Resource.Success -> {
                                binding?.progressBar?.visibility = View.GONE
                                Toast.makeText(context, "bisa", Toast.LENGTH_SHORT).show()
                                foodAdapter.setFood(food.data)
                                foodAdapter.notifyDataSetChanged()
                            }
                            is Resource.Error -> {
                                binding?.progressBar?.visibility = View.GONE
                                Toast.makeText(context, "Terjadi kesalahan", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                })
               /* with(binding?.rvUser) {
                    this?.layoutManager = LinearLayoutManager(context)
                    this?.setHasFixedSize(true)
                    this?.adapter = foodAdapter
                }

                */
            }
        }

    }
    override fun onDestroy() {
        super.onDestroy()
        _fragmentSearchBinding = null
    }
/*
    private fun showRv(name: String) {

        searchViewModel.searchFood(name).observe(viewLifecycleOwner, { food ->
            if (food != null) {
                when(food) {
                    is Resource.Loading -> binding?.progressBar?.visibility = View.GONE
                    is Resource.Success -> {
                        binding?.progressBar?.visibility = View.GONE
                        foodAdapter.setFood(food.data)
                        foodAdapter.notifyDataSetChanged()
                    }
                    is Resource.Error -> {
                        binding?.progressBar?.visibility = View.GONE
                        Toast.makeText(context, "Terjadi kesalahan", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        })
        with(binding?.rvUser) {
            this?.layoutManager = LinearLayoutManager(context)
            this?.setHasFixedSize(true)
            this?.adapter = foodAdapter
        }
    }

 */

}
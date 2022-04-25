package com.example.mvvmdogs.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mvvmdogs.databinding.FragmentListBinding
import com.example.mvvmdogs.viewmodel.ListViewModel

class ListFragment : Fragment() {
    //vars for view model and dogsListAdapter to instantiate the interface
    private lateinit var viewModel: ListViewModel
    private val dogsListAdapter = DogsListAdapter(arrayListOf())


    private lateinit var binding: FragmentListBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
       binding = FragmentListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //instantiating ViewModel
        // we use viewModelP to prevent loosing state of the data when the screen is rotated or when app is run in background, without needing to handle the state management
        viewModel = ViewModelProvider(this).get(ListViewModel::class.java)
        viewModel.refresh()//generates dog objects and a dogList and update the variables in the ListFragment

        //retrieving that data we refreshed
        binding.dogsList.apply {
            layoutManager= LinearLayoutManager(context)//can also be a GridLayoutManager
            adapter = dogsListAdapter
        }
        //to observe the ViewModel using the values declared in the ListViewModel
        observeViewModel()
    }

    //
    private fun observeViewModel() {
        viewModel.dogs.observe(viewLifecycleOwner, Observer {dogs ->
            dogs?.let {
                binding.dogsList.visibility = View.VISIBLE
                dogsListAdapter.updateDogList(dogs)
            }
        })
        viewModel.dogsLoadError.observe(viewLifecycleOwner, Observer { isError ->
            isError?.let {
                binding.listError.visibility = if(it) View.VISIBLE else View.GONE
            }
        })

        viewModel.loading.observe(viewLifecycleOwner, Observer { isLoading  ->
           isLoading?.let {
               binding.loadingView.visibility = if (it) View.VISIBLE else View.GONE
               if(it) {//i.e, if isLoading
                 binding.listError.visibility = View.GONE
                 binding.dogsList.visibility = View.GONE
               }
           }
        })
    }
}
package com.example.mvvmdogs.view

import android.os.Bundle
import android.view.*
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mvvmdogs.R
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
        //setHasOptionsMenu(true)
        // Inflate the layout for this fragment
        binding = FragmentListBinding.inflate(inflater, container, false)
        return binding.root
    }

    /*override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //instantiating ViewModel
        viewModel = ViewModelProvider(this).get(ListViewModel::class.java)
        viewModel.refresh()//generates dog objects and a dogList and update the variables in the ListFragment

        //retrieving that data we refreshed
        binding.dogsList.apply {
            layoutManager= LinearLayoutManager(context)//can also be a GridLayoutManager
            adapter = dogsListAdapter
        }

        //functionality to refresh the details
        binding.refreshLayout.setOnRefreshListener {
            binding.dogsList.visibility = View.GONE
            binding.listError.visibility = View.GONE
            binding.loadingView.visibility = View.VISIBLE
            viewModel.refreshBypassCache()//to retrieve data from Remote API when we swipeRefresh
            binding.refreshLayout.isRefreshing = false
        }

        //to observe the ViewModel using the values declared in the ListViewModel
        observeViewModel()
    }*/
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        // The usage of an interface lets you inject your own implementation
        val menuHost: MenuHost = requireActivity()

        // Add menu items without using the Fragment Menu APIs
        // Note how we can tie the MenuProvider to the viewLifecycleOwner
        // and an optional Lifecycle.State (here, RESUMED) to indicate when
        // the menu should be visible
        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                // Add menu items here
                menuInflater.inflate(R.menu.list_menu, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                // Handle the menu selection
                return when (menuItem.itemId) {
                    R.id.actionSettings -> {
                        view.let { Navigation.findNavController(it).navigate(ListFragmentDirections.actionSettings()) }
                        true
                    }
                    else -> false
                }
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)

        super.onViewCreated(view, savedInstanceState)

        //instantiating ViewModel
        viewModel = ViewModelProvider(this).get(ListViewModel::class.java)
        viewModel.refresh()//generates dog objects and a dogList and update the variables in the ListFragment

        //retrieving that data we refreshed
        binding.dogsList.apply {
            layoutManager= LinearLayoutManager(context)//can also be a GridLayoutManager
            adapter = dogsListAdapter
        }

        //functionality to refresh the details
        binding.refreshLayout.setOnRefreshListener {
            binding.dogsList.visibility = View.GONE
            binding.listError.visibility = View.GONE
            binding.loadingView.visibility = View.VISIBLE
            viewModel.refreshBypassCache()//to retrieve data from Remote API when we swipeRefresh
            binding.refreshLayout.isRefreshing = false
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

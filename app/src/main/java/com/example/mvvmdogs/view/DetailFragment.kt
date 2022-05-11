package com.example.mvvmdogs.view

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.*
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.palette.graphics.Palette
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.example.mvvmdogs.R
import com.example.mvvmdogs.databinding.FragmentDetailBinding
import com.example.mvvmdogs.model.DogPalette
import com.example.mvvmdogs.viewmodel.DetailViewModel

class DetailFragment : Fragment() {

    private lateinit var viewModel: DetailViewModel

    private var dogUuid = 0

    private lateinit var binding: FragmentDetailBinding

    //allows us to know whether the process to send sms has been started or not?
    private var sendSmsStarted = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        //setHasOptionsMenu(true) //deprecated
        // Inflate the layout for this fragment
        binding = FragmentDetailBinding.inflate(inflater, container, false)
        //binding = DataBindingUtil.inflate(inflater, R.layout.fragment_detail, container, false)//can be either way
        return binding.root
    }

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
                menuInflater.inflate(R.menu.detail_menu, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                // Handle the menu selection
                return when (menuItem.itemId) {
                    R.id.action_send_sms -> {
                        sendSmsStarted = true//start the process of sending sms
                        //call a method on activity to ask for the permission(SMS) .The fragment cant do it
                        (activity as MainActivity).checkSmsPermission()
                        true
                    }
                    R.id.action_share -> {
                        //view.let { Navigation.findNavController(it).navigate(ListFragmentDirections.actionSettings()) }
                        true
                    }
                    else -> onMenuItemSelected(menuItem)
                }
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)

        super.onViewCreated(view, savedInstanceState)

        //retrieving the argument we passed in list-fragment to detail-fragment if its not null
        arguments?.let {
            dogUuid = DetailFragmentArgs.fromBundle(it).dogUuid//we already have a uuid here unlike DOgsListAdapter
        }

        //instantiating DetailViewModel
        viewModel = ViewModelProvider(this).get(DetailViewModel::class.java)
        viewModel.fetch(dogUuid)

        observeViewModel()
    }

    private fun observeViewModel() {
        viewModel.dogLiveData.observe(viewLifecycleOwner, Observer { dog ->
            dog?.let {
                binding.dog = dog

                it.imageUrl?.let {
                    setupBackgroundColor(it)
                }
            }
        })
    }

    private fun setupBackgroundColor(url: String) {
        // we use glide to load an image and use it to add some functionality using it
        Glide.with(this)
            .asBitmap()
            .load(url)
            .into(object: CustomTarget<Bitmap>(){

                override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                  Palette.from(resource)
                      .generate { palette ->
                        val intColor = palette?.mutedSwatch?.rgb ?: 0//extract the preferred color
                        val myPalette =  DogPalette(intColor)
                          binding.palette = myPalette
                      }
                }

                override fun onLoadCleared(placeholder: Drawable?) {                }

            })//custom object used to access the image
    }

    //method that gets called when the activity(MainActivity) finishes the checkSmsPermission() and gets the result(true or false)
    fun onPermissionResult(permissionGranted: Boolean) {

    }
}
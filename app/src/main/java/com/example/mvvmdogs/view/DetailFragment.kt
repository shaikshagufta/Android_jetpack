package com.example.mvvmdogs.view

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat.checkSelfPermission
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.databinding.DataBindingUtil
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
import com.example.mvvmdogs.databinding.SendSmsDialogBinding
import com.example.mvvmdogs.model.DogBreed
import com.example.mvvmdogs.model.DogPalette
import com.example.mvvmdogs.model.SmsInfo
import com.example.mvvmdogs.viewmodel.DetailViewModel

private const val PERMISSION_SEND_SMS_REQUEST = 1

class DetailFragment : Fragment() {

    private lateinit var viewModel: DetailViewModel

    private var dogUuid = 0

    private lateinit var binding: FragmentDetailBinding

    //private lateinit var dialogBinding: SendSmsDialogBinding

    //allows us to know whether the process to send sms has been started or not?
    private var sendSmsStarted = false

    //to maintain the info about the dog we are working with
    private var currentDog: DogBreed? = null

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
                        /*(activity as MainActivity).*/checkSmsPermission()
                        true
                    }
                    R.id.action_share -> {
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
            currentDog = dog//to maintain the info about the dog we are working ith
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
            .into(object : CustomTarget<Bitmap>() {

                override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                  Palette.from(resource)
                      .generate { palette ->
                        val intColor = palette?.mutedSwatch?.rgb ?: 0//extract the preferred color
                        val myPalette =  DogPalette(intColor)
                          binding.palette = myPalette
                      }
                }

                override fun onLoadCleared(placeholder: Drawable?) {}

            })//custom object used to access the image
    }

    /*You don't need to go back to the activity to request permissions anymore.
     This introduces a dependency from the Fragment to the Activity and limits re-usability of the fragment.
     You can simply call all the permission-related functions straight from the fragment.*/
    private fun checkSmsPermission() {
        if(checkSelfPermission(requireContext(), Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
            if(shouldShowRequestPermissionRationale(Manifest.permission.SEND_SMS)) {
                AlertDialog.Builder(requireContext())
                    .setTitle("Send SMS permission")
                    .setMessage("This app requires access to send an SMS")
                    .setPositiveButton("Ask me") { dialog, which ->
                        requestSmsPermission()
                    }
                    .setNegativeButton("No") { dialog, which ->
                        onPermissionResult(false)
                    }
                    .show()
            } else {
                requestSmsPermission()
            }
        } else {
            onPermissionResult(true)
        }
    }

    private fun requestSmsPermission() {
        requestPermissions(arrayOf(Manifest.permission.SEND_SMS), PERMISSION_SEND_SMS_REQUEST)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when(requestCode) {
            PERMISSION_SEND_SMS_REQUEST -> {
                if(grantResults.isNotEmpty() && grantResults.first() == PackageManager.PERMISSION_GRANTED) {
                    onPermissionResult(true)
                } else {
                    onPermissionResult(false)
                }
            }
            else -> super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }
    }

    //method that gets called when the activity(MainActivity) finishes the checkSmsPermission() and gets the result(true or false)
    fun onPermissionResult(permissionGranted: Boolean) {
        if (sendSmsStarted && permissionGranted) {
            context?.let {
                val smsInfo = SmsInfo(
                    "",
                    "${currentDog?.dogBreed} bred for ${currentDog?.bredFor}",
                    currentDog?.imageUrl
                )

                //dialogBinding = SendSmsDialogBinding.inflate(LayoutInflater.from(it), null, false)
                val dialogBinding = DataBindingUtil.inflate<SendSmsDialogBinding>(
                    LayoutInflater.from(it),
                    R.layout.send_sms_dialog,
                    null,
                    false
                )

                AlertDialog.Builder(it)
                    .setView(dialogBinding.root)
                    .setPositiveButton("Send SMS") { dialog, which ->
                        if (!dialogBinding.smsDestination.text.isNullOrBlank()) {
                            smsInfo.to = dialogBinding.smsDestination.text.toString()
                            sendSms(smsInfo)
                        }
                    }
                    .setNegativeButton("Cancel") { dialog ,which -> }
                    .show()
                dialogBinding.smsInfo = smsInfo
            }
        }
    }

    private fun sendSms(smsInfo: SmsInfo) {

    }
}
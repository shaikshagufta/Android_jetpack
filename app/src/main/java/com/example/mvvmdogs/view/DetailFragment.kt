package com.example.mvvmdogs.view

import android.Manifest
import android.app.PendingIntent
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.telephony.SmsManager
import android.view.*
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat.checkSelfPermission
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
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
import com.example.mvvmdogs.util.PERMISSION_SEND_SMS
import com.example.mvvmdogs.viewmodel.DetailViewModel


class DetailFragment : Fragment() {

    private lateinit var viewModel: DetailViewModel

    private var dogUuid = 0

    private lateinit var binding: FragmentDetailBinding

    //allows us to know whether the process to send sms has been started or not?
    private var sendSmsStarted = false

    //to maintain the info about the dog we are working with
    private var currentDog: DogBreed? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        setHasOptionsMenu(true)
        // Inflate the layout for this fragment
        //binding = FragmentDetailBinding.inflate(inflater, container, false)
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_detail, container, false)//can be either way
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //retrieving the argument we passed in list-fragment to detail-fragment if its not null
        arguments?.let {
            dogUuid = DetailFragmentArgs.fromBundle(it).dogUuid//we already have a uuid here unlike DOgsListAdapter
        }

        //instantiating DetailViewModel
        viewModel = ViewModelProvider(this)[DetailViewModel::class.java]
        viewModel.fetch(dogUuid)

        observeViewModel()
    }

    private fun observeViewModel() {
        viewModel.dogLiveData.observe(viewLifecycleOwner) { dog ->
            currentDog = dog//to maintain the info about the dog we are working ith
            dog?.let { it ->
                binding.dog = dog

                it.imageUrl?.let {
                    setupBackgroundColor(it)
                }
            }
        }
    }

    private fun setupBackgroundColor(url: String) {
        // we use glide to load an image and use it to add some functionality using it
        Glide.with(this)
            .asBitmap()
            .load(url)
            .into(object : CustomTarget<Bitmap>() {
                override fun onLoadCleared(placeholder: Drawable?) {}
                override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                  Palette.from(resource)
                      .generate { palette ->
                        val intColor = palette?.mutedSwatch?.rgb ?: 0//extract the preferred color
                        val myPalette =  DogPalette(intColor)
                          binding.palette = myPalette
                      }
                }
            })//custom object used to access the image
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.detail_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_send_sms -> {
                //2
                sendSmsStarted = true
                /*(activity as MainActivity).*/checkSmsPermission()
            }
            R.id.action_share -> {
                val intent = Intent(Intent.ACTION_SEND)
                intent.type = "text/plain"
                intent.putExtra(Intent.EXTRA_SUBJECT, "Check out this dog breed")
                intent.putExtra(
                    Intent.EXTRA_TEXT,
                    "${currentDog?.dogBreed} bred for ${currentDog?.bredFor}"
                )
                intent.putExtra(
                    Intent.EXTRA_STREAM,
                    currentDog?.imageUrl
                )//the recipient app may take or disregard it.
                startActivity(
                    Intent.createChooser(
                        intent,
                        "Share with"
                    )
                )//to allow the apps that can handle the intent to be able to take this info
                //chooser is the endUser, we pop up a dialog that lets the user choose which app should handle this sharing functionality
            }
        }
        return super.onOptionsItemSelected(item)
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
                    .setPositiveButton("Ask me") { _, _ ->
                        requestSmsPermission()
                    }
                    .setNegativeButton("No") { _, _ ->
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
        requestPermissions(arrayOf(Manifest.permission.SEND_SMS),
            //request code that we defined in Util class
            PERMISSION_SEND_SMS
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when(requestCode) {
            PERMISSION_SEND_SMS -> {
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
    private fun onPermissionResult(permissionGranted: Boolean) {
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
                    .setPositiveButton("Send SMS") { _, _ ->
                        if (!dialogBinding.smsDestination.text.isNullOrBlank()) {
                            smsInfo.to = dialogBinding.smsDestination.text.toString()
                            sendSms(smsInfo)
                        }
                    }
                    .setNegativeButton("Cancel") { _, _ -> }
                    .show()
                dialogBinding.smsInfo = smsInfo
            }
        }
    }

    private fun sendSms(smsInfo: SmsInfo) {
        val intent = Intent(context, MainActivity::class.java)
        val pi = PendingIntent.getActivity(context, 0, intent,0)
        val smsManager = context?.getSystemService(SmsManager::class.java)
        smsManager?.sendTextMessage(smsInfo.to, null, smsInfo.text, pi, null)
    }
}
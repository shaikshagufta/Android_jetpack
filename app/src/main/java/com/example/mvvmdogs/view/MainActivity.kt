package com.example.mvvmdogs.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.example.mvvmdogs.R

class MainActivity : AppCompatActivity() {

    //1.to add a back button on the tool bar
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //2.to add a back button on the tool bar
        //navController = Navigation.findNavController(this, R.id.fragmentContainerView)
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment  // fragmentContainerView is the name of my fragment.
        navController = navHostFragment.navController
        NavigationUI.setupActionBarWithNavController(this, navController)
    }

    //3.to add a back button on the tool bar
    override fun onSupportNavigateUp(): Boolean {
        return NavigationUI.navigateUp(navController, null)
    }

    /*//You don't need to go back to the activity to request permissions anymore.
    // This introduces a dependency from the Fragment to the Activity and limits reusability of the fragment.
    // You can simply call all the permission-related functions straight from the fragment.
    fun checkSmsPermission() {
        //check if we don't have the permission
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.SEND_SMS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            //check if we need to show the permission rationale(why we need the rationale) or if we can simply request the permission
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    this,
                    Manifest.permission.SEND_SMS
                )
            ) {
                //show the permission rationale
                AlertDialog.Builder(this)
                    .setTitle("Send SMS permission")
                    .setMessage("App requires access to send an SMS")
                    .setPositiveButton("Ask me") { _, _ ->
                        requestSmsPermission()
                    }
                    .setNegativeButton("No") { _, _ ->
                        notifyDetailFragment(false)//no permission
                    }
                    .show()
            } else {
                //don't need to show the rationale
                requestSmsPermission()
            }
        } else {//i.e, if we have permission
            notifyDetailFragment(true)
        }
    }

    private fun requestSmsPermission() {
        ActivityCompat.requestPermissions(
            this, arrayOf(Manifest.permission.SEND_SMS),
            //request code that we defined in Util class
            PERMISSION_SEND_SMS
        )
    }

    //system will take whatever result from the permission and give a call-back in this function
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            PERMISSION_SEND_SMS -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    notifyDetailFragment(true)
                } else {
                    notifyDetailFragment(false)
                }
            }
            else -> super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }
    }

    private fun notifyDetailFragment(permissionGranted: Boolean) {
        val navHost = supportFragmentManager.findFragmentById(R.id.fragmentContainerView)
        navHost?.let { navFragment ->
            navFragment.childFragmentManager.primaryNavigationFragment?.let { fragment ->
                val activeFragment: Fragment? =
                    fragment.childFragmentManager.primaryNavigationFragment
                if (activeFragment is DetailFragment) {
                    activeFragment.onPermissionResult(permissionGranted)
                }
            }
        }
    }*/
}
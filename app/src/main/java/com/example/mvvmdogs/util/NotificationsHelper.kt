package com.example.mvvmdogs.util

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.mvvmdogs.R
import com.example.mvvmdogs.view.MainActivity

class NotificationsHelper(val context: Context) {

    private val CHANNEL_ID = "Dogs Channel id"
    private val NOTIFICATION_ID = 123

     fun createNotification() {
         createNotificationChannel()
        //basically starts the mainActivity
         val intent = Intent(context, MainActivity::class.java).apply {
             flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
             //when the user clicks on the notification its going to open main activity(create a new task)
            // or clear the existing task if there exits one
         }
         //manages what happens when the user clicks the icon; run the 'intent'-which basically starts the mainActivity
        val pendingIntent = PendingIntent.getActivity(context, 0,intent,0)

        val icon = BitmapFactory.decodeResource(context.resources, R.drawable.dog)

         val notification = NotificationCompat.Builder(context, CHANNEL_ID)
             .setSmallIcon(R.drawable.dog_icon)
             .setLargeIcon(icon)
             .setContentTitle("Dogs retrieved")
             .setContentText("This notification has some content")
             .setStyle(
                 NotificationCompat.BigPictureStyle()
                     .bigPicture(icon)
                     .bigLargeIcon(null)
             )
             .setContentIntent(pendingIntent)
             .setPriority(NotificationCompat.PRIORITY_DEFAULT)
             .build()

         //to show the notification
         NotificationManagerCompat.from(context).notify(NOTIFICATION_ID, notification)
     }

    //create a channel for the notification
    private fun createNotificationChannel() {
        //If build version is later than Oreo
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val name = CHANNEL_ID
            val descriptionText = "Channel description"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel =  NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }
            //registering the channel
            val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)//create a channel
        }
    }
}
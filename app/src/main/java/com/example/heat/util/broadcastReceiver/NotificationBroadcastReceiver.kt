package com.example.heat.util.broadcastReceiver

import android.app.NotificationManager
import android.content.Context

import android.content.BroadcastReceiver
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.example.heat.R

const val breakfastNotificationID = 1
const val lunchNotificationID = 2
const val dinnerNotificationID = 3
const val snackNotificationID = 4
const val otherNotificationID = 5

const val channelID = "channel1"
const val titleExtra = "titleExtra"
const val messageExtra = "messageExtra"


class NotificationBroadcastReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent)
    {
        val notification = NotificationCompat.Builder(context, channelID)
            .setSmallIcon(R.drawable.icon)
            .setContentTitle(intent.getStringExtra(titleExtra))
            .setContentText(intent.getStringExtra(messageExtra))
            .build()

        val  manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.notify(breakfastNotificationID, notification)
        manager.notify(lunchNotificationID, notification)
        manager.notify(dinnerNotificationID, notification)
        manager.notify(snackNotificationID, notification)
    }

}

